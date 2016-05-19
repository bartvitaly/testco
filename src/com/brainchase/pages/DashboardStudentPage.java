package com.brainchase.pages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.brainchase.common.Common;
import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.Challenge;
import com.brainchase.items.Student;
import com.brainchase.items.User;

/**
 * This class describes a student's dashboard page of the web site and page
 * elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardStudentPage extends Menu {
	final static Logger logger = Logger.getLogger(DashboardStudentPage.class);

	static String CHALLENGE_SUBMITTED = "challenge-status-2";
	static String CHALLENGE_GRADED = "challenge-status-1";
	static String CHALLENGE_ACTIVE = "challenge-status-0";

	static By welcomePopup = By.cssSelector("[style*='visible'] .joyride-close-tip");

	private static By introductory = By.cssSelector("[id=Introductory1]");
	private static By videoClose = By.cssSelector(".fancybox-close");
	private static By weekContainer = By.cssSelector(".week-container");

	private static By engineeringChallenge = By.cssSelector("[style*='engineering'] span");
	private static By mathChallenge = By.cssSelector("[style*='math'] span");
	private static By readingChallenge = By.cssSelector("[style*='reading'] span");
	private static By writingChallenge = By.cssSelector("[style*='writing'] span");
	private static By artChallenge = By.cssSelector("[style*='art'] span");
	private static By bonusChallenge = By.cssSelector("[style*='bonus'] span");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardStudentPage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Student's Dashboard page");

		if (present(welcomePopup)) {
			click(welcomePopup);
		}
	}

	/**
	 * This method opens a challenge
	 * 
	 * @param challengeType
	 * @return Challenge
	 * @throws InterruptedException
	 */
	public ChallengePage openChallenge(String challengeType) throws InterruptedException {
		// If Challenges are not enabled click introductory video and close it
		// then
		String challengeStatus = getAttribute(engineeringChallenge, "class");
		if (challengeStatus.equals(CHALLENGE_ACTIVE)) {
			click(introductory);
			click(videoClose);
			super.driver.navigate().refresh();
		}

		// Open a challenge
		click(getChallengeElement(challengeType));

		return new ChallengePage(driver);
	}

	/**
	 * This method opens and submits challenges dedicated to a student
	 * 
	 * @param student
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void submitChallenges(Student student) throws InterruptedException, IOException {
		for (Map.Entry<String, HashMap<String, String>> transactions : student.getTransactions().entrySet()) {
			String challengeType = transactions.getKey();
			String challengeStatus = getAttribute(getChallengeElement(challengeType), "class");
			if (challengeStatus.equals(CHALLENGE_SUBMITTED) || challengeStatus.equals(CHALLENGE_GRADED)) {
//				student.getTransactions().get(challengeType).put(student.name, "");
			} else {
				ChallengePage challengePage = openChallenge(challengeType);
				challengePage.submitChallenge(student, challengeType);
			}
		}
	}

	/**
	 * This method returns a By locator for a challenge type
	 * 
	 * @param type
	 */
	static By getChallengeElement(String type) {
		switch (type) {
		case "art":
			return artChallenge;
		case "bonus":
			return bonusChallenge;
		case "engineering":
			return engineeringChallenge;
		case "math":
			return mathChallenge;
		case "reading":
			return readingChallenge;
		case "writing":
			return writingChallenge;
		default:
			logger.fatal("The type passed '" + type
					+ "' is not in scope of: art, bonus, engineering, math, reading, writing");
		}
		return null;
	}

}