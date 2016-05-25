package test.java;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;

import com.brainchase.common.Common;
import com.brainchase.common.PropertiesUtils;

/**
 * 
 * This class is to initialise WebDriver
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Initialize {

	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	final static Logger logger = Logger.getLogger(Initialize.class);

	/**
	 * This method sets Appender
	 * 
	 */
	@BeforeGroups(groups = { "demo" })
	public void setAppender() {
		BasicConfigurator.configure();
		String logLevel = PropertiesUtils.get("log.level");
		Logger.getRootLogger().setLevel(Level.toLevel(logLevel));
	}

	/**
	 * This method initialises WebDriver
	 * 
	 * @return WebDriver
	 * 
	 */
	@BeforeMethod(groups = { "demo" })
	public WebDriver init() {
		String browser = PropertiesUtils.get("browser");

		if (browser.equals("random")) {
			if (Common.getRandomBoolean()) {
				start_chrome();
			} else {
				start_ie();
			}
		} else {
			if (browser.equals("chrome")) {
				start_chrome();
			} else if (browser.equals("ie")) {
				start_ie();
			} else if (browser.equals("firefox")) {
				driver.set(new MarionetteDriver());
			} else if (browser.equals("safari")) {
				driver.set(new SafariDriver());
			} else {
				driver.set(new HtmlUnitDriver());
			}
		}

		driver.get().manage().timeouts().implicitlyWait(Integer.parseInt(PropertiesUtils.get("timeout")),
				TimeUnit.SECONDS);
		driver.get().manage().deleteAllCookies();
		driver.get().manage().window().maximize();

		return driver.get();
	}

	/**
	 * This method is to start chrome browser
	 * 
	 * @return WebDriver
	 * 
	 */
	void start_chrome() {
		String chromePath = (new File("drivers/chromedriver.exe")).getAbsolutePath();
		if (!System.getProperty("os.name").contains("Windows")) {
			chromePath = (new File("drivers/chromedriver_mac")).getAbsolutePath();
		}
		System.setProperty("webdriver.chrome.driver", chromePath);
		driver.set(new ChromeDriver());
	}

	/**
	 * This method is to start ie browser
	 * 
	 * @return WebDriver
	 * 
	 */
	void start_ie() {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability("ignoreZoomSetting", true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability("requireWindowFocus", true);
		System.setProperty("webdriver.ie.driver", (new File("drivers/IEDriverServer.exe")).getAbsolutePath());
		driver.set(new InternetExplorerDriver(capabilities));
	}

}
