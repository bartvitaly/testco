package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.items.Challenge;

/**
 * This class describes a challenge page of the web site and the page elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class ChallengePage extends Menu {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(ChallengePage.class);

	By closeModal = By.cssSelector(".close-reveal-modal");
	
	By externalLink = By.cssSelector("[id=edit-external-url]");
	By response = By.cssSelector("[id=edit-student-input]");
	By answer = By.cssSelector("[id=edit-answer]");
	
	By submit = By.cssSelector("[id=submit-final]");
	
	By backToDashboard = By.cssSelector(".button[href*=dashboard]");
	
	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public ChallengePage(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
		
//		Close modal dialog
		if (present(closeModal)) {
			click(closeModal);
		}
	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException 
	 */
	public DashboardPage submitChallenge(Challenge challenge) throws InterruptedException {
		if (!present(backToDashboard)) {
			if (present(externalLink)) {
				type(externalLink, challenge.externalLink);
			}
			if (present(response)) {
				type(response, challenge.response);
			}
			if (present(answer)) {
				type(answer, challenge.answer);
			}
			click(submit);
		}
		else {
			click(backToDashboard);
		}
		return new DashboardPage(driver);
	}

}
