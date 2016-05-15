package test.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.brainchase.common.CsvFileReader;
import com.brainchase.common.PropertiesUtils;
import com.brainchase.common.SoftAssert;
import com.brainchase.common.TestNGReportAppender;
import com.brainchase.items.User;
import com.brainchase.pages.Login;

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
		users = CsvFileReader.readCsvFile((new File(".")).getCanonicalPath() + "\\users.csv", "student");
		return User.getUsers(users, PropertiesUtils.getInt("students_number"));
	}
	
	@Test(groups = { "demo" }, dataProvider = "demoProvider")
	public void student(User user) throws Exception {
		login(user);
	}

//	@Test(groups = { "demo" }, dependsOnMethods = "student")
//	public void teacher() throws Exception {
//		User teacher = CsvFileReader.readCsvFile((new File(".")).getCanonicalPath() + "\\users.csv", "teacher").get(0);
//		login(teacher);
//	}
//	
//	@Test(groups = { "demo" }, dependsOnMethods = "teacher")
//	public void supervisor() throws Exception {
//		User supervisor = CsvFileReader.readCsvFile((new File(".")).getCanonicalPath() + "\\users.csv", "supervisor").get(0);
//		login(supervisor);
//	}
	
	/**
	 * This is a login method
	 * 
	 * @throws Exception
	 */
	public void login(User user) throws Exception {
		logger.info("Started demo test for");
		driver.get().get(PropertiesUtils.get("url"));

		logger.info("Open login page, fill username and password and click login");
		Login loginPage = new Login(driver.get());
		loginPage.login(user);

		logger.removeAllAppenders();
		m_assert.assertAll();
	}
	
}
