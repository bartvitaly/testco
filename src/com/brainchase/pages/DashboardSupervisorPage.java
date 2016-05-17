package com.brainchase.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This class describes a supervisor's dashboard page of the web site and page
 * elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardSupervisorPage extends Menu {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(DashboardSupervisorPage.class);

	HashMap<String, ArrayList<String>> nonWritingAssignments = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> writingAssignments = new HashMap<String, ArrayList<String>>();

	static By gradeIt = By.cssSelector("[href*='grading']");
	static By getNewAssignment = By.cssSelector("#get-assignment");
	static By getNewWritingAssignment = By.cssSelector("#get-writing-assignment");

	static By nonWriting = By.cssSelector(".small-12:nth-child(2) tbody tr");
	static By writing = By.cssSelector(".small-12:nth-child(3) tbody tr");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardSupervisorPage(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method stores all the student's assignments
	 * 
	 */
	public void getAssignments() {
		nonWritingAssignments = putAssignmentsInList(getElements(nonWriting));
		writingAssignments = putAssignmentsInList(getElements(writing));
	}

	/**
	 * This method gets all the student's assignments from page
	 * 
	 * @param assignments
	 * @return ArrayList<ArrayList<String>>
	 * 
	 */
	public HashMap<String, ArrayList<String>> putAssignmentsInList(ArrayList<WebElement> assignments) {
		HashMap<String, ArrayList<String>> listAssignments = new HashMap<String, ArrayList<String>>();
		ArrayList<String> entry = new ArrayList<>();
		String student_name;
		
		for (int i = 0; i < assignments.size(); i++) {
			student_name = getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(1)")));
			
			entry.add(getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(2)"))));
			entry.add(getText(assignments.get(i).findElement(By.cssSelector("td:nth-child(3)"))));
			listAssignments.put(student_name, entry);
		}
		
		return listAssignments;
	}

}