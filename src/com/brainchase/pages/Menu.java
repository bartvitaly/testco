package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.brainchase.common.WebDriverCommon;

/**
 * This class describes a menu of the web site
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class Menu extends WebDriverCommon {

	protected final WebDriver driver;
	final static Logger logger = Logger.getLogger(Menu.class);

	static By logout = By.cssSelector("[href*='logout']");
	static By welcomeText = By.cssSelector(".welcome-text");

	// Alert box
	static By alert = By.cssSelector(".alert-box");

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
	 * This method logs out a user
	 * 
	 * @return LoginPage
	 * @throws InterruptedException
	 */
	public LoginPage logout() throws InterruptedException {
		click(logout);
		if (present(logout)) {
			click(logout);
		}
		return new LoginPage(driver);
	}

}
