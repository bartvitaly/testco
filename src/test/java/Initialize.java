package test.java;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
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
				
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", (new File("drivers/chromedriver.exe")).getAbsolutePath());
			driver.set(new ChromeDriver());
		} else if (browser.equals("ie")) {
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("requireWindowFocus", true);
			System.setProperty("webdriver.ie.driver", (new File("drivers/IEDriverServer.exe")).getAbsolutePath());
			driver.set(new InternetExplorerDriver(capabilities));
		} else if (browser.equals("firefox")) {
			driver.set(new MarionetteDriver());
		} else {
			driver.set(new HtmlUnitDriver());
		}

		driver.get().manage().timeouts().implicitlyWait(Integer.parseInt(PropertiesUtils.get("timeout")),
				TimeUnit.SECONDS);
		driver.get().manage().deleteAllCookies();
		driver.get().manage().window().maximize();

		return driver.get();
	}

}
