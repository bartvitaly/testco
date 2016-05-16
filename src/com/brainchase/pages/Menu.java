package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.User;

/**
 * This class describes a menu of the web site
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class Menu extends WebDriverCommon {

	private final WebDriver driver;
	final static Logger logger = Logger.getLogger(Menu.class);

	By logout = By.cssSelector("[href*='logout']");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public Menu(WebDriver driver) throws InterruptedException {
		super(driver);
		this.driver = driver;
	}

	/**
	 * This method logs in a user
	 * 
	 * @return LoginPage 
	 * @throws InterruptedException 
	 */
	public LoginPage logout() throws InterruptedException {
		click(logout);
		return new LoginPage(driver);
	}

}
