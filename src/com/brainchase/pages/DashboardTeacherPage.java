package com.brainchase.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;
import com.brainchase.items.Challenge;
import com.brainchase.items.Transaction;
import com.brainchase.items.User;

/**
 * This class describes a teacher's dashboard page of the web site and page
 * elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardTeacherPage extends Menu {
	final static Logger logger = Logger.getLogger(DashboardTeacherPage.class);

	public HashMap<String, HashMap<String, String>> nonWritingAssignments = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String, String>> writingAssignments = new HashMap<String, HashMap<String, String>>();
	public HashMap<String, HashMap<String, String>> allAssignments = new HashMap<String, HashMap<String, String>>();

	// Grade controls
	static By gradeIt = By.cssSelector("[href*='grading']");
	static By studentName = By.cssSelector(".small-8 .small-12 tbody td:nth-child(1)");
	static By challengeType = By.cssSelector(".small-8 .small-12 tbody td:nth-child(2)");

	// Miscellaneous and writing challenges info
	static By miscellaneousInfo = By.cssSelector("[id=bc-caching-teacher-assignment-form] p:nth-of-type(1)");
	static By writingInfo = By.cssSelector("[id=bc-caching-teacher-assignment-form] p:nth-of-type(2)");

	// Retrieve assignment buttons
	static By getNewAssignment = By.cssSelector("[id=get-assignment]");
	static By getNewWritingAssignment = By.cssSelector("[id=get-writing-assignment]");

	// Tables with assignments
	static By nonWriting = By.cssSelector(".small-12:nth-child(2) tbody tr");
	static By writing = By.cssSelector(".small-12:nth-child(3) tbody tr");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardTeacherPage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Teacher's Dashboard page");

		// Getting all the assignments
		nonWritingAssignments = putAssignmentsInList(getElements(nonWriting));
		writingAssignments    = putAssignmentsInList(getElements(writing));

		allAssignments.putAll(nonWritingAssignments);
		allAssignments.putAll(writingAssignments);
	}

	/**
	 * This method is to grade student's assignments
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * @throws InterruptedException
	 * 
	 */
	public void grade(User user, int maxAssignmentsToGrade) throws InterruptedException {
		for (int i = 0; i < maxAssignmentsToGrade; i++) {
			if (!getNewAssignmentToGrade()) {
				break;
			}

			// Get student name and challenge type before grading
			String name = getText(studentName);
			name = name.substring(0, name.indexOf(" "));
			String type = getText(challengeType).toLowerCase();

			// Grade a challenge and store transactionId
			click(gradeIt);
			GradingPage gradingPage = new GradingPage(driver);
			gradingPage.grade(true, "commentsText");
			checkAttribute(alert, "text", "Your comments have been saved!", true);

			// Add transactionId from grading page and finalise return array
			user.getTransactions().get(type).put(name, gradingPage.transactionId);
		}
	}

	/**
	 * This method is to get an assignment and make other checks
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * @throws InterruptedException
	 * 
	 */
	private Boolean getNewAssignmentToGrade() {
		if (!present(gradeIt)) {
			click(getNewAssignment);
			if (!present(gradeIt)) {
				click(getNewWritingAssignment);
				if (!present(gradeIt)) {
					logger.info("No assignments to grade.");
					return false;
				} else {
					String challengeTypeToGrade = getAttribute(challengeType, "text");
					if (!challengeTypeToGrade.equalsIgnoreCase("writing")) {
						logger.error(
								"A non writing assignment is shown after clicking Get New Writing Assignment to Review.");
					}
				}
			} else {
				if (getAttribute(challengeType, "text").equalsIgnoreCase("writing")) {
					logger.error("A writing assignment is shown after clicking Get New Assignment to Review.");
				}
			}
		}
		return true;
	}

	/**
	 * This method gets all the student's assignments from page
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * 
	 */
	private HashMap<String, HashMap<String, String>> putAssignmentsInList(ArrayList<WebElement> assignments) {
		HashMap<String, HashMap<String, String>> listAssignments = Transaction.getTransactionsHashMap();
		
		HashMap<String, String> students = new HashMap<>();
		String studentName, challengeName, date;

		for (int i = 0; i < assignments.size(); i++) {
			ArrayList<String> entry = new ArrayList<String>();

			studentName = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(1)")));
			challengeName = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(2)"))).toLowerCase();
			date = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(3)")));

			// Preserve the order
			entry.add(challengeName.toLowerCase());
			entry.add(studentName.toLowerCase());
			entry.add(date);

			students.put(studentName, date);
			listAssignments.put(challengeName, students);
		}

		return listAssignments;
	}

	/**
	 * This method is to check that all students' assignments are on the
	 * dashboard
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * 
	 */
	public void checkAssignments(HashMap<String, HashMap<String, String>> assignments,
			HashMap<String, HashMap<String, String>> transactions) {

		String nonWritingAssignments = Integer.toString(assignments.get("art").size() + assignments.get("engineering").size());
		String writingAssignments = Integer.toString(assignments.get("writing").size());
		
		checkAttribute(miscellaneousInfo, "text",
				"Miscellaneous challenges remaining to be graded: " + nonWritingAssignments,
				true);
		checkAttribute(writingInfo, "text",
				"Writing challenges remaining to be graded: " + writingAssignments, true);

		// Iterate over challenges' types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactions.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();
				if (!assignments.get(types.getKey()).containsKey(students.getKey())) {
					logger.error("An assignment for student: '" + students.getKey() + "' and challenge '"
							+ types.getKey() + "' is not found in transactions list: \r\n\""
							+ Transaction.transactionsToString(transactions));
				}
			}
		}
	}

}