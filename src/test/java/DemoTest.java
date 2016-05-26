package test.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
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
import com.brainchase.items.Student;
import com.brainchase.items.Transaction;
import com.brainchase.items.User;
import com.brainchase.pages.DashboardStudentPage;
import com.brainchase.pages.DashboardSupervisorPage;
import com.brainchase.pages.DashboardTeacherPage;
import com.brainchase.pages.LoginPage;

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

	static HashMap<String, HashMap<String, String>> transactionsStudent = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> dashboardTeacher = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> transactionsTeacher = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> dashboardSupervisor = Transaction.getTransactionsHashMap();
	HashMap<String, HashMap<String, String>> transactionsSupervisor = Transaction.getTransactionsHashMap();

	static ConcurrentHashMap<String, HashMap<String, String>> transactionsCopyTeacher = Transaction
			.getTransactionsConcurrentHashMap();
	static ConcurrentHashMap<String, HashMap<String, String>> transactionsCopySupervisor = Transaction
			.getTransactionsConcurrentHashMap();

	Boolean teacherDashboardVerified = false;
	Boolean supervisorDashboardVerified = false;

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
		users = CsvFileReader.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "student").get(0);
		allUsers = CsvFileReader.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "student").get(1);
		return User.getUsers(users, PropertiesUtils.getInt("students_number"));
	}

	/**
	 * This is data provider for running the same test with different data
	 * 
	 * @return
	 * @throws IOException
	 */
	@DataProvider(parallel = true, name = "transactionsProvider")
	public static Transaction[][] transactions() throws IOException {
		return Transaction.getTransactions(transactionsStudent);
	}

	@Test(groups = { "demo" })
	public void gradeAllAssignments() throws Exception {
		// Grade all assignments
		gradeNumber = PropertiesUtils.getInt("grade_number");
		User teacher = CsvFileReader.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "teacher")
				.get(0).get(0);
		User supervisor = CsvFileReader
				.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "supervisor").get(0).get(0);

		clear(teacher);
		clear(supervisor);
	}

	@Test(groups = { "demo" }, dataProvider = "demoProvider", dependsOnMethods = { "gradeAllAssignments" })
	public void student(User user) throws Exception {
		final Student student = new Student(user.login, user.name, user.password, user.type);

		logger.info("Open login page, fill credentials and login");
		DashboardStudentPage dashboardPage;
		dashboardPage = (DashboardStudentPage) login(student);

		logger.info("A student submits challenges");
		dashboardPage.submitChallenges(student);

		logger.info("A student logs out");
		dashboardPage.logout();

		synchronized (transactionsStudent) {
			transactionsStudent = Transaction.addTransactions(transactionsStudent, student.getTransactions());
		}
		synchronized (transactionsCopyTeacher) {
			transactionsCopyTeacher = Transaction.addTransactions(transactionsCopyTeacher, student.getTransactions());
		}
		synchronized (transactionsCopySupervisor) {
			transactionsCopySupervisor = Transaction.addTransactions(transactionsCopySupervisor,
					student.getTransactions());
		}
	}

	@Test(groups = { "demo" }, dataProvider = "transactionsProvider", dependsOnMethods = { "student" })
	public void teacher(Transaction transaction) throws Exception {
		User teacher = CsvFileReader.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "teacher")
				.get(0).get(0);
		// Go to Dashboard page
		LoginPage loginPage = new LoginPage(driver.get());
		DashboardTeacherPage dashboardPage = (DashboardTeacherPage) loginPage.login(teacher);

		// Check transactions are shown on Dashboard
		synchronized (teacherDashboardVerified) {
			if (!teacherDashboardVerified) {
				teacherDashboardVerified = true;
				dashboardPage.checkAssignments(transactionsStudent);
				dashboardTeacher = Transaction.addTransactions(dashboardTeacher, dashboardPage.allAssignments);
			}
		}

		// Grade an assignment
		dashboardPage.grade(teacher, transactionsCopyTeacher);
		dashboardPage.logout();

		// Write teacher transactions to a file
		transactionsTeacher = Transaction.addTransactions(transactionsTeacher, teacher.getTransactions());
	}

	@Test(groups = { "demo" }, dataProvider = "transactionsProvider", dependsOnMethods = { "teacher" })
	public void supervisor(Transaction transaction) throws Exception {
		User supervisor = CsvFileReader
				.readUsersFile(Common.canonicalPath() + File.separator + "users.csv", "supervisor").get(0).get(0);
		// Go to Dashboard page
		LoginPage loginPage = new LoginPage(driver.get());
		DashboardSupervisorPage dashboardPage = (DashboardSupervisorPage) loginPage.login(supervisor);

		// Check transactions are shown on Dashboard
		synchronized (supervisorDashboardVerified) {
			if (!supervisorDashboardVerified) {
				supervisorDashboardVerified = true;
				dashboardPage.checkAssignments(transactionsStudent);
				dashboardSupervisor = Transaction.addTransactions(dashboardSupervisor, dashboardPage.allAssignments);
			}
		}

		// Grade an assignment
		dashboardPage.grade(supervisor, transactionsCopySupervisor);
		dashboardPage.logout();

		// Write teacher transactions to a file
		transactionsSupervisor = Transaction.addTransactions(transactionsSupervisor, supervisor.getTransactions());
	}

	public void clear(User user) throws Exception {
		// Go to Dashboard page
		LoginPage loginPage = new LoginPage(driver.get());
		DashboardTeacherPage dashboardPage = (DashboardTeacherPage) loginPage.login(user);

		// Grade an assignment
		dashboardPage.grade(user, PropertiesUtils.getInt("grade_number"));
		dashboardPage.logout();
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
		HashMap<String, HashMap<String, HashMap<String, String>>> transactionsToCompare = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		transactionsToCompare.put("transactionsStudent", transactionsStudent);
		transactionsToCompare.put("dashboardTeacher", dashboardTeacher);
		transactionsToCompare.put("transactionsTeacher", transactionsTeacher);
		transactionsToCompare.put("dashboardSupervisor", dashboardSupervisor);
		transactionsToCompare.put("transactionsSupervisor", transactionsSupervisor);

		HTMLBuilder.create(transactionsToCompare, Common.canonicalPath() + File.separator + "transactions.html");
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
