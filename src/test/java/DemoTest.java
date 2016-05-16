package test.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.brainchase.common.CsvFileReader;
import com.brainchase.common.PropertiesUtils;
import com.brainchase.common.SoftAssert;
import com.brainchase.common.TestNGReportAppender;
import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.Challenge;
import com.brainchase.items.Student;
import com.brainchase.items.User;
import com.brainchase.pages.DashboardPage;
import com.brainchase.pages.LoginPage;
import com.thoughtworks.selenium.webdriven.commands.Click;

/**
 * This class contains a test demo
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class DemoTest extends Initialize {

	private Logger logger = Logger.getLogger(this.getClass());
	private SoftAssert m_assert;

	@BeforeMethod(groups = { "demo" })
	public void setUp() throws Exception {
		TestNGReportAppender appender = new TestNGReportAppender();
		m_assert = appender.getAssert();
		BasicConfigurator.configure(appender);
		logger.info("Started demo test for");
		driver.get().get(PropertiesUtils.get("url"));
	}

	/**
	 * This is data provider for running the same test with different data
	 * 
	 * @return
	 * @throws IOException 
	 */
	@DataProvider(parallel = true, name = "demoProvider")
	public static User[][] user() throws IOException {
		ArrayList<User> users = new ArrayList<User>();
		users = CsvFileReader.readUsersFile((new File(".")).getCanonicalPath() + "\\users.csv", "student");
		return User.getUsers(users, PropertiesUtils.getInt("students_number"));
	}
	
	@Test(groups = { "demo" }, dataProvider = "demoProvider")
	public void student(User user) throws Exception {
		Student student = new Student(user.name, user.password, user.type);
		
		logger.info("Open login page, fill username and password and click login");
		DashboardPage dashboardPage = login(user);
		
		logger.info("A student submits challenges");
		dashboardPage.submitChallenges(student);
		
		logger.info("A student logs out");
		dashboardPage.logout();
	}

	@Test(groups = { "demo" }, dependsOnMethods = "student")
	public void teacher() throws Exception {
		User teacher = CsvFileReader.readUsersFile((new File(".")).getCanonicalPath() + "\\users.csv", "teacher").get(0);
		login(teacher);
	}
	
	@Test(groups = { "demo" }, dependsOnMethods = "teacher")
	public void supervisor() throws Exception {
		User supervisor = CsvFileReader.readUsersFile((new File(".")).getCanonicalPath() + "\\users.csv", "supervisor").get(0);
		login(supervisor);
	}
	
	@AfterMethod(groups = { "demo" })
	public void tear() {
		WebDriverCommon.takeScreenshot(driver.get());
		driver.get().close();
		driver.get().quit();
		logger.removeAllAppenders();
		m_assert.assertAll();
	}
	
	public DashboardPage login(User user) throws InterruptedException {
		logger.info("Open login page, fill username and password and click login");
		LoginPage loginPage = new LoginPage(driver.get());
		DashboardPage dashboardPage = loginPage.login(user);
		return dashboardPage;
	}
	
}
