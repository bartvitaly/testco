package test.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;
import com.brainchase.common.PropertiesUtils;
import com.brainchase.common.SoftAssert;
import com.brainchase.common.TestNGReportAppender;
import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.Challenge;
import com.brainchase.items.Student;
import com.brainchase.items.Transaction;
import com.brainchase.items.User;
import com.brainchase.pages.DashboardStudentPage;
import com.brainchase.pages.DashboardTeacherPage;
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

	static ArrayList<User> users = new ArrayList<User>();
	static ArrayList<Student> students = new ArrayList<Student>();
	static ArrayList<User> allUsers = new ArrayList<User>();

	@BeforeMethod(groups = { "demo" })
	public void setUp() throws Exception {
		TestNGReportAppender appender = new TestNGReportAppender();
		m_assert = appender.getAssert();
		BasicConfigurator.configure(appender);
		logger.debug("Started demo test for");
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
		users = CsvFileReader.readUsersFile(Common.canonicalPath() + "\\users.csv", "student").get(0);
		allUsers = CsvFileReader.readUsersFile(Common.canonicalPath() + "\\users.csv", "student").get(1);
		return User.getUsers(users, PropertiesUtils.getInt("students_number"));
	}

	// @Test(groups = { "demo" }, dataProvider = "demoProvider")
	// public void student(User user) throws Exception {
	// final Student student = new Student(user.name, user.password, user.type);
	// logger.info("Open login page, fill username and password and click
	// login");
	// DashboardPage dashboardPage = login(user);
	//
	// logger.info("A student submits challenges");
	// dashboardPage.submitChallenges(student);
	//
	// logger.info("A student logs out");
	// dashboardPage.logout();
	//
	// students.add(student);
	// }

	@Test(groups = { "demo" }) // , dependsOnMethods = "student"
	public void teacher() throws Exception {
		// Get a teacher data from file
		User teacher = CsvFileReader.readUsersFile(Common.canonicalPath() + "\\users.csv", "teacher").get(0).get(0);

		// Go to Dashboard page
		DashboardTeacherPage dashboardPage = (DashboardTeacherPage) login(teacher);

//		Create mock student transactions
		HashMap<String, HashMap<String, String>> transactionsStudent = Transaction.getTransactionsHashMap();
		transactionsStudent.get("engineering").put("testcoauto_student2", "bc573a158d2f4182.27928201");
		
		// Check transactions are shown on Dashboard
		dashboardPage.checkAssignments(dashboardPage.allAssignments, transactionsStudent);

		// Grade an assignment
		dashboardPage.grade(teacher, 1);

		// Write teacher transactions to a file
		Transaction.writeTeacherTransactions(teacher.getTransactions());

		// Compare students' and teacher's transactions
		ArrayList<HashMap<String, HashMap<String, String>>> transactionsToCompare = new ArrayList<HashMap<String, HashMap<String, String>>>();
		transactionsToCompare.add(transactionsStudent);
		transactionsToCompare.add(teacher.getTransactions());
		Transaction.compareTransactions(transactionsToCompare, Common.canonicalPath() + "\\TestResult.csv");
	}

	// @Test(groups = { "demo" }, dependsOnMethods = "teacher")
	// public void supervisor() throws Exception {
	// User supervisor = CsvFileReader.readUsersFile((new
	// Common.canonicalPath() + "\\users.csv",
	// "supervisor").get(0).get(0);
	// login(supervisor);
	// }

	@AfterMethod(groups = { "demo" })
	public void tear() throws IOException {
		Transaction.writeTransactions(students);
		// WebDriverCommon.takeScreenshot(driver.get());
		driver.get().close();
		driver.get().quit();
		logger.removeAllAppenders();
		m_assert.assertAll();
	}

	/**
	 * This method is used to login
	 * 
	 * @param user
	 * @return DashboardPage
	 * @throws IOException
	 */
	public Object login(User user) throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver.get());
		return loginPage.login(user);
	}

}
