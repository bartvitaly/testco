package com.brainchase.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * This class describes a supervisor's dashboard page of the web site and page
 * elements
 * 
 * @author vbartashchuk@testco.com
 * 
 */
public class DashboardSupervisorPage extends DashboardTeacherPage {
	final static Logger logger = Logger.getLogger(DashboardSupervisorPage.class);

	/**
	 * This is constructor that sets a web driver for the page object
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public DashboardSupervisorPage(WebDriver driver) throws InterruptedException {
		super(driver);
		logger.info("Opened Supervisor's Dashboard page");
	}

}