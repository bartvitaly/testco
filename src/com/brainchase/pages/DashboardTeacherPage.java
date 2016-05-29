package com.brainchase.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;
import com.brainchase.common.PropertiesUtils;
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

	public HashMap<String, HashMap<String, String>> nonWritingAssignments = Transaction.getAllTransactionsHashMap();
	public HashMap<String, HashMap<String, String>> writingAssignments = Transaction.getAllTransactionsHashMap();
	public HashMap<String, HashMap<String, String>> allAssignments = Transaction.getAllTransactionsHashMap();

	// Grade controls
	static By gradeIt = By.cssSelector("[href*='grading']");
	static By studentName = By.cssSelector(".small-8 .small-12 tbody td:nth-child(1)");
	static By challengeType = By.cssSelector(".small-8 .small-12 tbody td:nth-child(2)");
	static By date = By.cssSelector(".small-8 .small-12 tbody td:nth-child(3)");

	// Miscellaneous and writing challenges info
	static By miscellaneousInfo = By.cssSelector("[id=bc-caching-teacher-assignment-form] p:nth-of-type(1)");
	static By writingInfo = By.cssSelector("[id=bc-caching-teacher-assignment-form] p:nth-of-type(2)");

	// Retrieve assignment buttons
	static By getNewAssignment = By.cssSelector("[id=get-assignment]");
	static By getNewWritingAssignment = By.cssSelector("[id=get-writing-assignment]");

	// Tables with assignments
	static By nonWriting = By.cssSelector(".small-12:nth-child(2) tbody tr");
	static By writing = By.cssSelector(".small-12:nth-child(3) tbody tr");

	// Grading page
	private static By accepted = By.cssSelector("[id=edit-accepted-1]");

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
		if (present(nonWriting)) {
			nonWritingAssignments = putAssignmentsInList(getElements(nonWriting));
		}
		nonWritingAssignments.remove("writing");

		if (present(writing)) {
			writingAssignments = putAssignmentsInList(getElements(writing));
		}
		writingAssignments.remove("art");
		writingAssignments.remove("engineering");

		allAssignments.putAll(nonWritingAssignments);
		allAssignments.putAll(writingAssignments);
	}

	/**
	 * This method is to grade student's assignments
	 * 
	 * @param user
	 * @param maxAssignmentsToGrade
	 * @throws InterruptedException
	 * 
	 */
	public void grade(User user, int maxAssignmentsToGrade) throws InterruptedException {
		for (int i = 0; i < maxAssignmentsToGrade; i++) {
			if (!present(gradeIt)) {
				break;
			}

			// Get student name and challenge type before grading
			String name = getText(studentName);
			name = name.substring(0, name.indexOf(" "));
			String type = getText(challengeType).toLowerCase();

			// Grade a challenge and store transactionId
			int j = 0;
			while (!present(accepted) && j < 10) {
				click(gradeIt);
			}

			GradingPage gradingPage = new GradingPage(driver);
			gradingPage.grade(true, "commentsText");

			// Add transactionId from grading page and finalise return array
			if (user.getTransactions().containsKey(type)) {
				user.getTransactions().get(type).put(name, gradingPage.transactionId);
			}
		}
	}

	/**
	 * This method is to grade student's assignments without saving them
	 * 
	 * @param assignments
	 * @throws InterruptedException
	 * 
	 */
	public void grade(int maxAssignmentsToGrade) throws InterruptedException {
		for (int i = 0; i < maxAssignmentsToGrade; i++) {
			if (!getNewAssignmentToGrade()) {
				break;
			}

			// Grade a challenge and store transactionId
			int j = 0;
			while (!present(accepted) && j < 10) {
				click(gradeIt);
			}

			GradingPage gradingPage = new GradingPage(driver);
			gradingPage.grade(true, "commentsText");
		}
	}

	/**
	 * This method is to grade an assignment
	 * 
	 * @param user
	 * @param transactions
	 * @throws InterruptedException
	 * 
	 */
	public void grade(User user, ConcurrentHashMap<String, HashMap<String, String>> transactions)
			throws InterruptedException {
		int i = 0;
		int maxIterations = PropertiesUtils.getInt("grade_number");
		while (getNewAssignmentToGrade() && i < maxIterations) {
			String challengeTypeToGrade = getAttribute(challengeType, "text").toLowerCase();
			String studentToGrade = getText(studentName);
			studentToGrade = studentToGrade.substring(0, studentToGrade.indexOf(" "));

			Boolean transactionRemoved = Transaction.removeTransaction(transactions, challengeTypeToGrade,
					studentToGrade);

			if (transactionRemoved) {
				grade(user, 1);
			}
			i++;
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
	private Boolean getNewAssignmentToGrade() throws InterruptedException {
		super.driver.navigate().refresh();
		if (present(gradeIt)) {
			return true;
		}
		if (getAttribute(getNewAssignment, "class").contains("disabled")
				&& getAttribute(getNewWritingAssignment, "class").contains("disabled")) {
			return false;
		}

		if (!getAttribute(getNewAssignment, "class").contains("disabled")) {
			click(getNewAssignment);
			String challengeTypeToGrade = getAttribute(challengeType, "text");
			if (challengeTypeToGrade.equalsIgnoreCase("writing")) {
				logger.error("A writing assignment is shown after clicking Get New Assignment to Review.");
			}
			if (present(gradeIt)) {
				return true;
			}
		}

		if (!getAttribute(getNewWritingAssignment, "class").contains("disabled")) {
			click(getNewWritingAssignment);
			String challengeTypeToGrade = getAttribute(challengeType, "text");
			if (!challengeTypeToGrade.equalsIgnoreCase("writing")) {
				logger.error("A non writing assignment is shown after clicking Get New Writing Assignment to Review.");
			}
			if (present(gradeIt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method gets all the student's assignments from page
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * 
	 */
	private HashMap<String, HashMap<String, String>> putAssignmentsInList(ArrayList<WebElement> assignments) {
		HashMap<String, HashMap<String, String>> listAssignments = Transaction.getAllTransactionsHashMap();
		String studentName, challengeName, date;

		for (int i = 0; i < assignments.size(); i++) {
			studentName = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(1)")));
			challengeName = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(2)"))).toLowerCase();
			date = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(3)")));

			if (listAssignments.containsKey(challengeName)) {
				listAssignments.get(challengeName).put(studentName.toLowerCase(), date);
			}

		}

		return listAssignments;
	}

	/**
	 * This method is to check that all students' assignments are on the
	 * dashboard
	 * 
	 * @param transactions
	 * @return HashMap<String, HashMap<String, String>> transactions
	 * 
	 */
	public void getAssignmentToGrade(HashMap<String, HashMap<String, String>> transactions) {
		if (present(challengeType)) {
			String type = getText(challengeType).toLowerCase();
			String name = getText(studentName);
			name = name.substring(0, name.indexOf(" "));
			String dateDue = getText(date);

			transactions.get(type).put(name, dateDue);
		}
	}

	/**
	 * This method is to check that all students' assignments are on the
	 * dashboard
	 * 
	 * @param transactions
	 * @return HashMap<String, HashMap<String, String>> transactions
	 * 
	 */
	public void checkAssignments(HashMap<String, HashMap<String, String>> transactions) {
		// add an assignment that is ready for grading
		getAssignmentToGrade(allAssignments);
		Transaction.removeEmpty(allAssignments);

		// check all the assignments on the page
		// Iterate over challenges in student HashMap
		int nonWritingCount = 0;
		int writingCount = 0;

		for (int i = 0; i < CsvFileReader.CHALLENGES_ACTUAL.length; i++) {
			String challengeType = CsvFileReader.CHALLENGES_ACTUAL[i];
			if (challengeType != "writing") {
				if (nonWritingAssignments.containsKey(challengeType)) {
					if (!nonWritingAssignments.get(challengeType).isEmpty()) {
						nonWritingCount = nonWritingCount + nonWritingAssignments.get(challengeType).size();
					}
				}
			} else {
				if (writingAssignments.containsKey(challengeType)) {
					if (!writingAssignments.get(challengeType).isEmpty()) {
						writingCount = writingCount + writingAssignments.get(challengeType).size();
					}
				}
			}
		}

		String nonWriting = Integer.toString(nonWritingCount);
		String writing = Integer.toString(writingCount);

		checkAttribute(miscellaneousInfo, "text", "Miscellaneous challenges remaining to be graded: " + nonWriting,
				true);
		checkAttribute(writingInfo, "text", "Writing challenges remaining to be graded: " + writing, true);

		if (nonWritingCount + writingCount == 0) {
			return;
		}

		// Iterate over challenges' types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactions.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();
				if (allAssignments.get(types.getKey()) == null
						|| !allAssignments.get(types.getKey()).containsKey(students.getKey())) {
					logger.error(System.lineSeparator() + "An assignment for student: '" + students.getKey()
							+ "' and challenge '" + types.getKey() + "' is not found in assignments list: "
							+ System.lineSeparator()
							+ Transaction.transactionsToString(Transaction.transactionsToArrayList(allAssignments)));
				}
			}
		}
	}

}