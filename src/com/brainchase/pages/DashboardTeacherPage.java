package com.brainchase.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class describes a teacher's dashboard page of the web site and page
 * elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardTeacherPage extends Menu {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(DashboardTeacherPage.class);

	HashMap<String, ArrayList<String>> nonWritingAssignments = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> writingAssignments = new HashMap<String, ArrayList<String>>();

//	Grade controls
	static By gradeIt = By.cssSelector("[href*='grading']");
	static By studentName = By.cssSelector(".small-8 .small-12 tbody td:nth-child(1)");
	static By challengeType = By.cssSelector(".small-8 .small-12 tbody td:nth-child(2)");

	
//	Retrieve assignment buttons
	static By getNewAssignment = By.cssSelector("[id=get-assignment]");
	static By getNewWritingAssignment = By.cssSelector("[id=get-writing-assignment]");

//	Tables with assignments
	static By nonWriting = By.cssSelector(".small-12:nth-child(2) tbody tr");
	static By writing = By.cssSelector(".small-12:nth-child(3) tbody tr");

//	Assignment graded message
	private static By alert    = By.cssSelector(".alert-box");
	
	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardTeacherPage(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
		
		nonWritingAssignments = putAssignmentsInList(getElements(nonWriting));
		writingAssignments = putAssignmentsInList(getElements(writing));
	}

	

	/**
	 * This method is to grade student's assignments
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * @throws InterruptedException 
	 * 
	 */
	public ArrayList<ArrayList<String>> grade(int maxAssignmentsToGrade) throws InterruptedException {
		ArrayList<ArrayList<String>> transactions = new ArrayList<>();
		ArrayList<String> transaction = new ArrayList<>();
		
		for (int i = 0; i < maxAssignmentsToGrade; i++) {			
			if (!present(gradeIt)) {
				click(getNewAssignment);
				if (!present(gradeIt)) {
					logger.info("No assignments to grade.");
					break;
				}
			}
			
//			Get student name and challenge type before grading
			String name = getText(studentName);
			transaction.add(name.substring(0, name.indexOf(" ")));
			transaction.add(getText(challengeType));
			
//			Grade a challenge and store transactionId
			click(gradeIt);
			GradingPage gradingPage = new GradingPage(driver);
			gradingPage.grade(true, "commentsText");
			checkAttribute(alert, "text", "Your comments have been saved!", true);
			
//			Add transactionId from grading page and finalise return array
			transaction.add(gradingPage.transactionId);
			transactions.add(transaction);
		}
		return transactions;
	}
	
	
	/**
	 * This method gets all the student's assignments from page
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * 
	 */
	private HashMap<String, ArrayList<String>> putAssignmentsInList(ArrayList<WebElement> assignments) {
		HashMap<String, ArrayList<String>> listAssignments = new HashMap<String, ArrayList<String>>();
		String student_name;
		
		for (int i = 0; i < assignments.size(); i++) {
			ArrayList<String> entry = new ArrayList<>();
			student_name = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(1)")));
			
			entry.add(getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(2)"))));
			entry.add(getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(3)"))));
			listAssignments.put(student_name, entry);
		}
		
		return listAssignments;
	}

}