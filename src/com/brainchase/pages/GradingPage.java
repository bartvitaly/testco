package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.User;

/**
 * This class describes a grading page of the web site and page elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class GradingPage extends Menu {
	final static Logger logger = Logger.getLogger(GradingPage.class);
	String transactionId;

	private static By transactionIdElement = By.cssSelector("[name='transaction_id']");

	private static By accepted = By.cssSelector("[id=edit-accepted-1]");
	private static By needsWork = By.cssSelector("[id=edit-accepted-0]");
	private static By comments = By.cssSelector("[id=edit-comments]");
	private static By submit = By.cssSelector("[id=comments-submit-save]");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public GradingPage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Grading page");
		setTransactionId();
	}

	/**
	 * This method grades an assignment
	 * 
	 * @param User
	 * @return
	 * @throws InterruptedException
	 */
	public void grade(Boolean accept, String commentsText) throws InterruptedException {
		if (accept) {
			click(accepted);
		} else {
			click(needsWork);
		}

		type(comments, commentsText);
		setTransactionId();
		click(submit);

		checkAttribute(alert, "text", "Your comments have been saved!", true);
	}

	/**
	 * This method is to set transactionID
	 * 
	 * @return
	 */
	void setTransactionId() {
		if (transactionId == null || transactionId.equals("")) {
			transactionId = getAttribute(transactionIdElement, "value");
		}
		if (transactionId == null) {
			transactionId = "";
		}
	}

}
