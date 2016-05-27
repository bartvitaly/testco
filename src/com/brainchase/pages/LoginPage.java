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
public class LoginPage extends Menu {
	final static Logger logger = Logger.getLogger(LoginPage.class);

	private static By username = By.cssSelector("[id=edit-name]");
	private static By password = By.cssSelector("[id=edit-pass]");
	private static By submit = By.cssSelector("[id=edit-submit]");

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public LoginPage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Login page.");

		if (present(logout)) {
			click(logout);
		}
	}

	/**
	 * This method logs in a user
	 * 
	 * @param User
	 * @return Page Object
	 * @throws InterruptedException
	 */
	public Object login(User user) throws InterruptedException {
		logger.info("Fill username '" + user.name + "' and password '" + user.password + "' and click login");
		int i = 0;
		while (!present(logout) && i < 10) {
			type(username, user.login);
			type(password, user.password);
			clickEnter(password);
		}

		switch (user.type) {
		case "teacher":
			return new DashboardTeacherPage(driver);
		case "supervisor":
			return new DashboardSupervisorPage(driver);
		}
		return new DashboardStudentPage(driver);
	}

}
