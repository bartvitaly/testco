package test.java;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;

import com.brainchase.common.PropertiesUtils;
import com.brainchase.common.WebDriverCommon;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Initialize {

	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	final static Logger logger = Logger.getLogger(Initialize.class);
	
	@BeforeGroups(groups = { "demo" })
	public void setAppender() {
		BasicConfigurator.configure();
		String logLevel = PropertiesUtils.get("log.level");
		Logger.getRootLogger().setLevel(Level.toLevel(logLevel));
	}

	@BeforeMethod(groups = { "demo" })
	public WebDriver init() {
		String browser = PropertiesUtils.get("browser");
		String chromeDriver = PropertiesUtils.get("webdriver.chrome.driver");
		String iexploreDriver = PropertiesUtils.get("webdriver.iexplore.driver");

		File file;
		if (browser.equals("chrome")) {
			file = new File(chromeDriver);
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver.set(new ChromeDriver());
		} else if (browser.equals("iexplore")) {
			file = new File(iexploreDriver);
			System.setProperty("webdriver.iexplore.driver", file.getAbsolutePath());
			driver.set(new InternetExplorerDriver());
		} else if (browser.equals("firefox")) {
			driver.set(new FirefoxDriver());
		} else {
			driver.set(new HtmlUnitDriver());
		}

		driver.get().manage().timeouts().implicitlyWait(Integer.parseInt(PropertiesUtils.get("timeout")),
				TimeUnit.SECONDS);
		driver.get().manage().deleteAllCookies();

		return driver.get();
	}

	@AfterMethod(groups = { "demo" })
	public void tear() {
		WebDriverCommon.takeScreenshot(driver.get());
		driver.get().close();
		driver.get().quit();
	}

}
