package com.brainchase.pages;

import java.io.File;
import java.io.IOException;

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
 * This class describes a dashboard page of the web site and page elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardPage extends Menu {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(DashboardPage.class);
	static String CHALLENGE_SUBMITTED = "challenge-status-2";

	static By welcomePopup = By.cssSelector("[style*='visible'] .joyride-close-tip");

	By introductory = By.cssSelector("[id=Introductory1]");
	By videoClose = By.cssSelector(".fancybox-close");
	By weekContainer = By.cssSelector(".week-container");

	static By engineeringChallenge = By.cssSelector("[style*='engineering']");
	static By mathChallenge = By.cssSelector("[style*='math']");
	static By readingChallenge = By.cssSelector("[style*='reading']");
	static By writingChallenge = By.cssSelector("[style*='writing']");
	static By artChallenge = By.cssSelector("[style*='art']");
	static By bonusChallenge = By.cssSelector("[style*='bonus']");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardPage(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;

		if (present(welcomePopup)) {
			click(welcomePopup);
		}

	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException
	 */
	public ChallengePage openChallenge(Challenge challenge) throws InterruptedException {
		// If Challenges are not enabled click introductory video and close it
		// then
		if (getAttribute(engineeringChallenge, "style").contains("block")) {
			click(introductory);
			click(videoClose);
			driver.navigate().refresh();
		}

		// Open a challenge
		click(getChallengeElement(challenge.type));

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
		for (int i = 0; i < student.challenges.size(); i++) {
			if (getAttribute(getChallengeElement(student.challenges.get(i).type), "class")
					.equals(CHALLENGE_SUBMITTED)) {
				student.challengesProgress.add(
						student.name + "," + student.challenges.get(i).type + ",the challenge is already submitted");
			} else {
				ChallengePage challengePage = openChallenge(student.challenges.get(i));
				challengePage.submitChallenge(student, i);
				student.challengesProgress.add(student.name + "," + student.challenges.get(i).type + ","
						+ student.challenges.get(i).transactionId);
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
