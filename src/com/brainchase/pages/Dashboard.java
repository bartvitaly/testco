package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.User;

/**
 * This class describes a login page of the web site and page elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class Dashboard extends WebDriverCommon {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(Dashboard.class);

	By introductory = By.cssSelector("[id=Introductory1]");
	By videoClose = By.cssSelector(".fancybox-close");
	
	
	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public Dashboard(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method logs in a user
	 * 
	 * @param User
	 */
	public void login(User user) {
	}

}
