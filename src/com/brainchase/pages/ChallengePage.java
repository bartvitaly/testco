package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.items.Challenge;
import com.brainchase.items.Student;

/**
 * This class describes a challenge page of the web site and the page elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class ChallengePage extends Menu {
	final static Logger logger = Logger.getLogger(ChallengePage.class);

	private static By closeModal = By.cssSelector(".close-reveal-modal");

	private static By transactionIdElement = By.cssSelector("[name='transaction_id']");
	private static By externalLink = By.cssSelector("[id=edit-external-url]");
	private static By response = By.cssSelector("[id=edit-student-input]");
	private static By answer = By.cssSelector("[id=edit-answer]");

	private static By submit = By.cssSelector("[id=submit-final");
	private static By backToDashboard = By.cssSelector(".button[href*=dashboard]");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public ChallengePage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Challenge page.");

		// Close modal dialog
		if (present(closeModal)) {
			logger.info("Closing modal dialog.");
			click(closeModal);
		}
	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException
	 */
	public void submitChallenge(Student student, String challengeType) throws InterruptedException {
		logger.info("Submitting challenge.");

		// Get transaction_id, if it is not exist then it will be assumed that a
		// challenge is already submitted
		Challenge challenge = student.challenges.get(challengeType);

		if (!present(transactionIdElement)) {
			logger.error("TransactionId is not present on the challenge page for student + " + student.name
					+ " and challenge " + challengeType);
		} else {
			// Adding transaction_id to a Student object
			student.getTransactions().get(challengeType).put(student.name, getAttribute(transactionIdElement, "value"));

			// Complete a challenge
			fillChallengeFields(challenge, challengeType);
			click(submit);
		}

		// Returning back to dashboard
		click(backToDashboard);
	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException
	 */
	public void fillChallengeFields(Challenge challenge, String challengeType) throws InterruptedException {
		switch (challengeType) {
		case "art":
			type(externalLink, challenge.externalLink);
			type(response, challenge.answer);
			break;
		case "engineering":
			type(externalLink, challenge.externalLink);
			type(response, challenge.answer);
			break;
		case "writing":
			type(response, challenge.response);
			break;
		}
	}

}
