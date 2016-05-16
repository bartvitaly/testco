package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

	By introductory = By.cssSelector("[id=Introductory1]");
	By videoClose = By.cssSelector(".fancybox-close");
	By weekContainer = By.cssSelector(".week-container");
	By engineeringChallenge = By.cssSelector("[style*='engineering']");
	By mathChallenge = By.cssSelector("[style*='math']");
	By readingChallenge = By.cssSelector("[style*='reading']");
	By writingChallenge = By.cssSelector("[style*='writing']");
	By artChallenge = By.cssSelector("[style*='art']");
	By bonusChallenge = By.cssSelector("[style*='bonus']");
	
	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardPage(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException 
	 */
	public ChallengePage openChallenge(Challenge challenge) throws InterruptedException {
		switch (challenge.type) {
	        case "art":
	        	click(artChallenge);
	        	break;
	        case "bonus":
	        	click(bonusChallenge);
	        	break;
	        case "engineering":
	        	click(engineeringChallenge);
	        	break;
	        case "math":
	        	click(mathChallenge);
	        	break;
	        case "reading":
	        	click(readingChallenge);
	        	break;
	        case "writing":
	        	click(writingChallenge);
	        	break;
		}
		return new ChallengePage(driver);
	}

	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException 
	 */
	public void submitChallenge(Challenge challenge) throws InterruptedException {
		ChallengePage challengePage = openChallenge(challenge);
		challengePage.submitChallenge(challenge);
	}
	
	/**
	 * This method logs in a user
	 * 
	 * @param challenge
	 * @throws InterruptedException 
	 */
	public void submitChallenges(Student student) throws InterruptedException {
		for (int i = 0; i < student.challenges.size(); i++) {
			submitChallenge(student.challenges.get(i));
		}
	}
	
}
