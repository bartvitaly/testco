package test.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;
import com.brainchase.common.HTMLBuilder;
import com.brainchase.common.PropertiesUtils;
import com.brainchase.common.SoftAssert;
import com.brainchase.common.TestNGReportAppender;
import com.brainchase.common.WebDriverCommon;
import com.brainchase.items.Challenge;
import com.brainchase.items.Student;
import com.brainchase.items.Transaction;
import com.brainchase.items.User;
import com.brainchase.pages.DashboardStudentPage;
import com.brainchase.pages.DashboardSupervisorPage;
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

	static int gradeNumber = PropertiesUtils.getInt("grade_number");

	ArrayList<HashMap<String, HashMap<String, String>>> transactions = new ArrayList<HashMap<String, HashMap<String, String>>>();

	HashMap<String, HashMap<String, String>> transactionsStudent = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> dashboardTeacher = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> transactionsTeacher = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> dashboardSupervisor = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> transactionsSupervisor = Transaction.getTransactionsHashMap();

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

	@Test(groups = { "demo" })
	public void gradeAllAssignments() throws Exception {
		// Grade all assignments
		gradeNumber = 30;
		teacher();
		supervisor();
		gradeNumber = PropertiesUtils.getInt("grade_number");
	}

	@Test(groups = { "demo" }, dataProvider = "demoProvider", dependsOnMethods = { "gradeAllAssignments" })
	public void student(User user) throws Exception {
		final Student student = new Student(user.login, user.name, user.password, user.type);

		logger.info("Open login page, fill username and password and click login");
		DashboardStudentPage dashboardPage = (DashboardStudentPage) login(user);

		logger.info("A student submits challenges");
		dashboardPage.submitChallenges(student);

		logger.info("A student logs out");
		dashboardPage.logout();

		transactionsStudent = Transaction.addTransactions(transactionsStudent, student.getTransactions());
	}

	@Test(groups = { "demo" }, dependsOnMethods = { "student" })
	public void teacher() throws Exception {
		// Get a teacher data from file
		User teacher = CsvFileReader.readUsersFile(Common.canonicalPath() + "\\users.csv", "teacher").get(0).get(0);

		// Go to Dashboard page
		DashboardTeacherPage dashboardPage = (DashboardTeacherPage) login(teacher);

		// Check transactions are shown on Dashboard
		dashboardPage.checkAssignments(transactionsStudent);
		dashboardTeacher = dashboardPage.allAssignments;

		// Grade an assignment
		dashboardPage.grade(teacher, gradeNumber);
		dashboardPage.logout();

		// Write teacher transactions to a file
		transactionsTeacher = teacher.getTransactions();
	}

	@Test(groups = { "demo" }, dependsOnMethods = { "teacher" })
	public void supervisor() throws Exception {
		// Get a teacher data from file
		User supervisor = CsvFileReader.readUsersFile(Common.canonicalPath() + "\\users.csv", "supervisor").get(0)
				.get(0);

		// Go to Dashboard page
		DashboardSupervisorPage dashboardPage = (DashboardSupervisorPage) login(supervisor);

		// Check transactions are shown on Dashboard
		dashboardPage.checkAssignments(transactionsStudent);
		dashboardSupervisor = dashboardPage.allAssignments;

		// Grade an assignment
		dashboardPage.grade(supervisor, gradeNumber);
		dashboardPage.logout();

		// Write teacher transactions to a file
		transactionsSupervisor = supervisor.getTransactions();
	}

	@AfterMethod(groups = { "demo" })
	public void tear() throws IOException {
		WebDriverCommon.takeScreenshot(driver.get());
		driver.get().close();
		driver.get().quit();

		logger.removeAllAppenders();
		// m_assert.assertAll();
	}

	@AfterGroups(groups = { "demo" })
	public void results() throws IOException {
		// Compare students' and teacher's transactions
		ArrayList<HashMap<String, HashMap<String, String>>> transactionsToCompare = new ArrayList<HashMap<String, HashMap<String, String>>>();
		transactionsToCompare.add(transactionsStudent);
		transactionsToCompare.add(dashboardTeacher);
		transactionsToCompare.add(transactionsTeacher);
		transactionsToCompare.add(dashboardSupervisor);
		transactionsToCompare.add(transactionsSupervisor);

		HTMLBuilder.create(transactionsToCompare, Common.canonicalPath() + "\\transactions.html");
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
