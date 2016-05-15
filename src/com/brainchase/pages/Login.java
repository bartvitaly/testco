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
public class Login extends WebDriverCommon {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(Login.class);

	By username = By.cssSelector("[id=edit-name]");
	By password = By.cssSelector("[id=edit-pass]");
	By logIn = By.cssSelector("[id=edit-submit]");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public Login(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method logs in a user
	 * 
	 * @param User
	 * @return 
	 * @throws InterruptedException 
	 */
	public Dashboard login(User user) throws InterruptedException {
		type(username, user.name);
		type(password, user.password);
		click(logIn);
		return new Dashboard(driver);
	}

}
