package goreact.testautomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cucumber.listener.Reporter;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase {

	public static WebDriver driver;
	public static Properties prop;
	public static ExtentReports report;
	public static ExtentTest logger;
	public Map<String, String> testData;
	static int PAGE_LOAD_TIMEOUT = 60;
	static int IMPLICIT_WAIT = 5;
	static String PROPERTY_FILEPATH = "src\\goreact\\testautomation\\config\\config.properties";

	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream fis = new FileInputStream(PROPERTY_FILEPATH);
			prop.load(fis);
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void initializeTest() throws IOException, InterruptedException {

		report = new ExtentReports(System.getProperty("user.dir") + "\\testResults\\extentReports\\Report.html", true);
		logger = report.startTest("GoReact - Test Automation");
		File screenshotLocation = new File(System.getProperty("user.dir") + "\\Screenshot\\");
		logInfo(logger, "Initializing Test");
		String browserName = prop.getProperty("browser");
		ChromeOptions options = new ChromeOptions();
		logInfo(logger, "Removing old screenshot(s). See file(s) listed below:");
		deleteFiles(screenshotLocation);
		Thread.sleep(3000);
//		logInfo(logger, "Closing open browsers");
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");

		Thread.sleep(3000);
		logInfo(logger, "Deleting temp files");
		File tempDirectory = new File("C:\\Users\\"
				+ (System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\automation"));
		FileUtils.cleanDirectory(tempDirectory);
		Thread.sleep(3000);

		if (browserName.equals("chrome")) {
			logInfo(logger, "Chrome driver initiated");
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
			options.addArguments("test-type",
					"user-data-dir=C:/Users/" + System.getProperty("user.name")
							+ "/AppData/Local/Google/Chrome/User Data/automation",
					"disable-infobars", "start-maximized", "--disable-web-security", "--no-proxy-server", "--incognito",
					"--disable-gpu");

			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setExperimentalOption("useAutomationExtension", false);
			driver = new ChromeDriver(options);
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.navigate().to(prop.getProperty("url"));
		logger.log(LogStatus.INFO, "User was navigated to URL: " + prop.getProperty("url"));
		logInfo(logger, ("User was navigated to URL: " + prop.getProperty("url")));
	}

	public void tearDown() {

		report.endTest(logger);
		logger.log(LogStatus.INFO, "Ending the test");
		Reporter.loadXMLConfig(new File("src/goreact/testautomation/config/extent-config.xml"));
		Reporter.setSystemInfo("User Name", System.getProperty("user.name"));
		Reporter.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		Reporter.setSystemInfo("64 Bit", "Windows 10");
		Reporter.setTestRunnerOutput("Cucumber JUnit Test Runner");
		report.flush();
		report.close();
		final JDialog dialog = new JDialog();
		dialog.setAlwaysOnTop(true);

		int reply = JOptionPane.showConfirmDialog(null, "EXECUTION DONE. Do you want to close the browser/driver?",
				"Close Browser", JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.YES_OPTION) {
			driver.quit();
		}

	}

	/**
	 * Wait for 60 seconds until the element is visible in the DOM
	 * 
	 * @param driver
	 * @param object
	 */
	public void waitForElement(WebDriver driver, final By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(locator);
			}
		});
	}

	/**
	 * Checks whether element exists
	 * 
	 * @param driver  - WebDriver
	 * @param locator - Locator
	 * @return
	 */
	public boolean elementExist(WebDriver driver, By locator) {
		boolean exist = false;
		try {
			exist = driver.findElements(locator).size() > 0;
		} catch (Exception e) {
			return false;
		}

		return exist;
	}

	public String captureScreenshot(WebDriver driver) throws IOException {
		String userDirector = System.getProperty("user.dir");
		// String userDirector = System.getProperty("user.dir") + "/";

		String s1 = null, s2 = "";
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		if (true) {
			try {

				String failureImageFileName = new SimpleDateFormat("MM-dd-yyyy_HH-ss")
						.format(new GregorianCalendar().getTime()) + ".png";
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File("Screenshot\\" + failureImageFileName));
				s1 = userDirector + "\\Screenshot\\" + failureImageFileName;

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return s1;
	}

	// Click Element Methods
	// ***********************************************************
	/**
	 * Click element and wait until another element is visible (can be nothing to be
	 * waited)
	 * 
	 * @param driver
	 * @param objName
	 * @param locatorToClick
	 * @param objNameToWait
	 * @param locatorToWait
	 * @param logger
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public boolean clickWait(String objName, By locatorToClick, String objNameToWait, By locatorToWait,
			ExtentTest logger) throws InterruptedException, IOException {

		boolean found = false;
		try {
			// to scroll in view
			scrollObj(driver, locatorToClick);

			if (waitForObjectBoolean(driver, locatorToClick)) {

				smartDataSetting(locatorToClick, "click", objName, "");

				// validate if the locator to wait exist
				if (waitForObjectBoolean(driver, locatorToWait)) {
					if (!objNameToWait.equals("")) {
						logPass("Element Found: " + objNameToWait);
					}
					found = true;
				} else {
					logFail(logger, "Element Not Found:  " + objNameToWait, driver);
					found = false;
				}

			}

		} catch (Exception e) {
			found = false;
			logFail(logger, e.getMessage(), driver);
		}
		return found;
	}

	/**
	 * Click element and wait until another element is visible (can be nothing to be
	 * waited)
	 * 
	 * @param driver
	 * @param objName
	 * @param locatorToClick
	 * @param objNameToWait
	 * @param locatorToWait
	 * @param logger
	 * @throws IOException
	 */
	public boolean clickWait(WebDriver driver, String objName, WebElement locatorToClick, String objNameToWait,
			By locatorToWait, ExtentTest logger) throws InterruptedException, IOException {

		boolean found = false;
		try {
			// to scroll in view
			scrollElement(driver, locatorToClick);

			if (waitForElementBoolean(driver, locatorToClick)) {
				locatorToClick.click();

				if (!objName.equals("")) {
					logPass("Clicked Element: " + objName);
				}
				Thread.sleep(2000);
				// validate if the locator to wait exist
				if (waitForObjectBoolean(driver, locatorToWait)) {
					if (!objNameToWait.equals("")) {
						logPass("Element Found: " + objNameToWait);
					}
					found = true;
				} else {
					logFail(logger, "Element Not Found:  " + objNameToWait, driver);
					found = false;
				}
			}
		} catch (Exception e) {
			found = false;
			logFail(logger, e.getMessage(), driver);
		}
		return found;
	}

	/**
	 * Click element and wait until another element is visible (can be nothing to be
	 * waited)
	 * 
	 * @param driver
	 * @param objName
	 * @param locatorToClick
	 * @param objNameToWait
	 * @param locatorToWait
	 * @param logger
	 * @throws IOException
	 */
	public boolean clickWaitUntilGone(WebDriver driver, String objName, By locatorToClick, String objNameToWait,
			By locatorToWait, ExtentTest logger) throws InterruptedException, IOException {

		boolean found = false;
		try {
			// WebDriverWait wait = new WebDriverWait(driver, 60, 2);

			// to scroll in view
			scrollObj(driver, locatorToClick);
			if (waitForObjectBoolean(driver, locatorToClick)) {
				driver.findElement(locatorToClick).click();

				Thread.sleep(500);
				if (!objName.equals("")) {
					logPass("Clicked Element: " + objName);
				}

				found = waitUntilNotVisible(driver, locatorToWait);

				// validate if the locator to wait exist
				if (driver.findElements(locatorToWait).size() > 0) {
					found = false;
				}
			}
		} catch (Exception e) {
			logFail(logger, e.getMessage(), driver);
		}
		return found;
	}

	/**
	 * Click element and wait until another element is visible (can be nothing to be
	 * waited)
	 * 
	 * @param driver
	 * @param objName
	 * @param locatorToClick
	 * @param objNameToWait
	 * @param locatorToWait
	 * @param logger
	 * @throws IOException
	 */
	public boolean clickWaitUntilGone(WebDriver driver, String objName, WebElement locatorToClick, String objNameToWait,
			By locatorToWait, ExtentTest logger) throws InterruptedException, IOException {

		boolean found = false;
		try {
			// WebDriverWait wait = new WebDriverWait(driver, 60, 2);

			// to scroll in view
			scrollElement(driver, locatorToClick);
			if (waitForElementBoolean(driver, locatorToClick)) {
				locatorToClick.click();

				Thread.sleep(500);
				if (!objName.equals("")) {
					logPass("Clicked Element: " + objName);
				}

				found = waitUntilNotVisible(driver, locatorToWait);

				// validate if the locator to wait exist
				if (driver.findElements(locatorToWait).size() > 0) {
					found = false;
				}
			}
		} catch (Exception e) {
			logFail(logger, e.getMessage(), driver);
		}
		return found;
	}

	/**
	 * Wait for 30 seconds until the object is visible in the DOM and return true if
	 * found else false
	 * 
	 * @param driver
	 * @param object
	 */
	public boolean waitForObjectBoolean(WebDriver driver, By object) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15, 2);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(object));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Wait for 30 seconds until the element is visible in the DOM and return true
	 * if found else false
	 * 
	 * @param driver
	 * @param object
	 */
	public boolean waitForElementBoolean(WebDriver driver, WebElement el) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30, 5);
			wait.until(ExpectedConditions.visibilityOf(el));
			scrollElement(driver, el);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * An expectation for checking that an element is either invisible or not
	 * present on the DOM.
	 * 
	 * @param format
	 * @throws InterruptedException
	 * @throws Exception
	 */
	public boolean waitUntilNotVisible(WebDriver driver, By locator) throws InterruptedException {
		boolean x = false;
		try {
			int count = driver.findElements(locator).size();

			WebDriverWait wait = new WebDriverWait(driver, 15, 5);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			x = true;

			if (count > 1) {
				List<WebElement> el = driver.findElements(locator);

				for (WebElement we : el) {
					while (we.getCssValue("display").equals("block")) {
						if (!(we.getCssValue("display").equals("block"))) {
							x = true;
						}
					}
				}

			}
		} catch (StaleElementReferenceException e) {
			Thread.sleep(5000);
			x = true;
		} catch (Exception e) {
			x = false;
		}
		return x;
	}

	// Scroll Element Methods*************************************************
	/**
	 * Scroll until the object is visible
	 * 
	 * @param driver
	 * @param el
	 */
	public void scrollObj(WebDriver driver, By locator) throws InterruptedException {
		WebElement element = driver.findElement(locator);
		Point loc = element.getLocation();
		loc.x = element.getLocation().getX() - 200;
		loc.y = element.getLocation().getY() - 200;
		((JavascriptExecutor) driver).executeScript("scroll(0," + loc + ")");
		Thread.sleep(700);
	}

	/**
	 * Scroll until the object is visible
	 * 
	 * @param driver
	 * @param el
	 */
	public void scrollElement(WebDriver driver, WebElement el) throws InterruptedException {
		Point loc = el.getLocation();
		loc.x = el.getLocation().getX() - 200;
		loc.y = el.getLocation().getY() - 200;
		((JavascriptExecutor) driver).executeScript("scroll(0," + loc + ")");
		Thread.sleep(200);
	}

	/**
	 * Interact to different WebElements
	 * 
	 * @param locator  ---- object
	 * @param action   ---- what will be done to object --> set,click, radioselect,
	 *                 check, dropdownselect, hover
	 * @param objName  -- name for the object
	 * @param txtValue -- value to be used for
	 *                 set,click,radioselect,dropdownselect,check action(s)
	 */
	public void smartDataSetting(By locator, String strAction, String objName, String txtValue)
			throws InterruptedException, IOException {
		Actions action = new Actions(driver);
		try {
			switch (strAction.toLowerCase()) {
			case "set":
				WebElement field = driver.findElement(locator);
				if (!(field == null)) {
					scrollObj(driver, locator);

					field.clear();
					field.click();
					field.sendKeys(txtValue);

					String textvalue = field.getAttribute("value");

					if (txtValue.equals(textvalue)) {
						logPass("Successfully entered the text " + txtValue + " in the field: " + objName);
					}
				}
				break;
			case "hover":
				WebElement hoverElement = driver.findElement(locator);
				action.moveToElement(hoverElement).perform();
				Thread.sleep(1500);
				System.out.println("Hovering to WebElement: " + objName + ".");
				logInfo(logger, "Hovering to WebElement: " + objName + ".");
				break;
			case "radioselect":
				List<WebElement> select = driver.findElements(locator);
				if (!(select == null)) {
					scrollObj(driver, locator);
					for (WebElement radio : select) {
						if (radio.getAttribute("value").equalsIgnoreCase(txtValue))
							radio.click();
					}
					System.out.println("Entered " + txtValue + " to field name: " + objName + ".");
					logInfo(logger, "Entered " + txtValue + " to field name: " + objName + ".");
				}
				break;
			case "check":
				WebElement chk = driver.findElement(locator);
				if (!(chk == null)) {
					scrollObj(driver, locator);
					if (chk.getAttribute("value").equalsIgnoreCase(txtValue)) {
						chk.click();
					}
					System.out.println("Entered " + txtValue + " to field name: " + objName + ".");
					logInfo(logger, "Entered " + txtValue + " to field name: " + objName + ".");
				}
				break;
			case "dropdownselect":
				Select drpDownSelect = new Select(driver.findElement(locator));
				if (!(drpDownSelect == null)) {
					scrollObj(driver, locator);
					drpDownSelect.selectByVisibleText(txtValue);
					System.out.println("Selected " + txtValue + " from field name: " + objName + ".");
					logInfo(logger, "Selected " + txtValue + " from field name: " + objName + ".");
				}
				break;
			case "predictdropdownselect":
				WebElement predictDrpDwnSelect = driver.findElement(locator);
				if (!(predictDrpDwnSelect == null)) {
					predictDrpDwnSelect.click();
					predictDrpDwnSelect.sendKeys(txtValue);
					predictDrpDwnSelect.sendKeys(Keys.TAB);
					System.out.println("Selected " + txtValue + " from the auto-suggest dropdown: " + objName + ".");
					logInfo(logger, "Selected " + txtValue + " from the auto-suggest dropdown: " + objName + ".");
				}
				break;
			case "click":
				WebElement webElem = driver.findElement(locator);
				scrollObj(driver, locator);
				action.moveToElement(webElem).perform();
				if (!(webElem == null)) {
					webElem.click();
					System.out.println("Clicked WebElement: " + objName + ".");
					logInfo(logger, "Clicked WebElement: " + objName + ".");
				} else {
					logFail(logger, objName + " not found", driver);
				}
				break;
			case "clear":
				WebElement fieldToClear = driver.findElement(locator);
				if (!(fieldToClear == null)) {
					scrollObj(driver, locator);
					fieldToClear.sendKeys("\u0008");
				}
				break;
			case "cleartext":
				WebElement fieldtext = driver.findElement(locator);
				if (!(fieldtext == null)) {
					scrollObj(driver, locator);
					fieldtext.clear();
					fieldtext.sendKeys("\u0020");
					fieldtext.sendKeys("\u0008");
				}
				break;
			default:
				driver.findElement(locator).click();
				System.out.println("Clicked WebElement: " + objName + ".");
				break;
			}
		} catch (ElementNotFoundException e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		} catch (InvalidElementStateException e) {
			logFail(logger, "Element is disabled or not in proper state to interact.", driver);
		} catch (Exception e) {
			logFail(logger, "Other Exceptions : " + e.getMessage(), driver);
		}

	}

	/**
	 * Prints the custom message in the Extend reports output - Info
	 * 
	 * @param logger - ExtendTest Logger
	 * @param msg    - Message
	 */
	public void logInfo(ExtentTest logger, String msg) {
		System.out.println("INFO: " + msg);
		logger.log(LogStatus.INFO, msg);
	}

	/**
	 * Prints the custom message in the Extend reports output with screenshot - Info
	 * 
	 * @param logger- Extend logger
	 * @param msg     - Message
	 * @param driver  - WebDriver
	 * @throws IOException
	 */
	public void logInfo(ExtentTest logger, String msg, WebDriver driver) throws IOException {
		logInfo(logger, msg);
		String screeshotPath = captureScreenshot(driver);
		String image = logger.addScreenCapture(screeshotPath);
		System.out.println("INFO: " + msg);
		logger.log(LogStatus.INFO, msg, image);
	}

	/**
	 * Prints the custom message in the Extend reports output - Pass
	 * 
	 * @param logger - ExtendTest Logger
	 * @param msg    - Message
	 */
	public void logPass(String msg) {
		System.out.println("PASSED: " + msg);
		logger.log(LogStatus.PASS, msg);
	}

	/**
	 * Prints the custom message in the Extend reports output with screenshot - Pass
	 * 
	 * @param logger- Extend logger
	 * @param msg     - Message
	 * @param driver  - WebDriver
	 * @throws IOException
	 */
	public void logPass(String msg, WebDriver driver) throws IOException {
		logPass(msg);
		String screeshotPath = captureScreenshot(driver);
		String image = logger.addScreenCapture(screeshotPath);
		System.out.println("PASSED: " + msg);
		logger.log(LogStatus.PASS, msg, image);
	}

	/**
	 * Prints the custom message in the Extend reports output - Fail
	 * 
	 * @param logger - ExtendTest Logger
	 * @param msg    - Message
	 */
	public void logFail(String msg) {
		System.out.println("FAILED: " + msg);
		logger.log(LogStatus.FAIL, msg);
	}

	/**
	 * Prints the custom message in the Extend reports output with screenshot - Fail
	 * 
	 * @param logger- Extend logger
	 * @param msg     - Message
	 * @param driver  - WebDriver
	 * @throws IOException
	 */
	public void logFail(ExtentTest logger, String msg, WebDriver driver) throws InterruptedException, IOException {
		logger.log(LogStatus.FAIL, msg);
		String screenshotPath = captureScreenshot(driver);
		String image = logger.addScreenCapture(screenshotPath);
		System.out.println("FAILED: " + msg);
		logger.log(LogStatus.FAIL, msg, image);
	}

	/**
	 * Prints the custom message in the Extend reports output - Skip
	 * 
	 * @param logger - ExtendTest Logger
	 * @param msg    - Message
	 */
	public void logSkip(ExtentTest logger, String msg) {
		System.out.println("SKIPPED: " + msg);
		logger.log(LogStatus.SKIP, msg);
	}

	/**
	 * Check is the element exist and confirm if found
	 * 
	 * @param objName
	 * @param element
	 * @throws IOException
	 */

	public void compareString(String A, String B, String pMessage, String nMessage)
			throws IOException, InterruptedException {
		try {
			if (A.equals(B)) {
				logPass(pMessage);
			} else {
				logFail(logger, nMessage, driver);
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			logFail(logger, e.getMessage(), driver);
		}
	}

	public void compareInts(int A, int B, String object) throws IOException, InterruptedException {
		try {
			if (A == B) {
				logPass(object + " is/are correct/visible");
			} else {
				logFail(logger, object + " error", driver);
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			logFail(logger, e.getMessage(), driver);
		}
	}

	/**
	 * Find if the list contains a certain string
	 * 
	 * @param a
	 * @param b
	 */
	public boolean findStringInArray(String a, String[] array) {
		for (int x = 0; x < array.length; x++) {
			String temp = array[x].trim();
			if (a.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find if the list contains a certain string
	 * 
	 * @param a
	 * @param b
	 */
	public boolean findStringInArrayList(String a, ArrayList<String> array) {
		for (int x = 0; x < array.size(); x++) {
			String temp = array.get(x).toString();
			if (a.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates if the element exists
	 * 
	 * @param objName
	 * @param element
	 * @throws IOException
	 */
	public boolean validateElementExists(String objName, By element) throws InterruptedException, IOException {
		if (waitForObjectBoolean(driver, element)) {
			scrollObj(driver, element);
			logPass("Element Found: " + objName);
			return true;
		} else {
			logFail(logger, "Element Not Found:  " + objName, driver);
			return false;
		}
	}

	public boolean validateElementNotExists(String objName, By element) throws InterruptedException, IOException {
		if (waitForObjectBoolean(driver, element)) {
			scrollObj(driver, element);
			logFail(logger, "Element Found:  " + objName, driver);
			return true;
		} else {
			logPass("Element Not Found: " + objName);
			return false;
		}
	}

	/**
	 * Connect to excel db to get field(s) and its corresponding value
	 * 
	 * @param dataSheet
	 * @param tcID
	 * @throws Exception
	 */
	public void getTCData(String dataSheet, String tcID) throws Exception {
		try {
			testData = ExcelHandler.getTestDataInMap(prop.getProperty("dataTableFilePath"), dataSheet, tcID);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	/**
	 * Connect to excel db to update field and its new value
	 * 
	 * @param dataSheet
	 * @param tcID
	 * @param fieldToUpdate
	 * @param valueForUpdate
	 * @throws Exception
	 */
	public void updateTCFieldData(String dataSheet, String tcID, String fieldToUpdate, String valueForUpdate)
			throws Exception {
		try {
			testData = ExcelHandler.getTestDataInMap(prop.getProperty("dataTableFilePath"), dataSheet, tcID);
			testData.replace(fieldToUpdate, valueForUpdate);
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, e.getMessage());
		}
	}

	/**
	 * Use to delete files on a repository
	 * 
	 * @param folder - path
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void deleteFiles(File folder) throws IOException, InterruptedException {
		try {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					boolean del = file.delete();
					System.out.println(fileName + " : deleted = " + del);
				} else if (file.isDirectory()) {
					deleteFiles(file);
				}
			}
		} catch (IOException e) {
			logInfo(logger, e.getMessage());
		}
	}

	/**
	 * Provide random number based on given maximum int
	 * 
	 * @param i
	 */
	public String randomNum(int i) throws InterruptedException {
		Random rand = new Random();
		int n = rand.nextInt(i - 1) + 1;
		String r = Integer.toString(n);

		return r;
	}

	/**
	 * Provide random number based on given maximum int
	 * 
	 * @param maxnum
	 */
	public int randomNumInt(int maxnum) throws InterruptedException {
		int n = 0;
		if (maxnum == 1) {
			n = 1;
		} else {
			Random rand = new Random();
			n = rand.nextInt(maxnum - 1) + 1;
		}
		return n;
	}

	/**
	 * Checks and validates object state
	 * 
	 * @param obj
	 * @param value
	 * @param expectedState
	 * @param expectedMessage
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public boolean checkEnabled(By obj, String objName, Boolean expectedState, String expectedMessage)
			throws InterruptedException, IOException {
		Boolean x = false;
		try {

			waitForElement(driver, obj);
			if (elementExist(driver, obj)) {

				x = driver.findElement(obj).isEnabled();
				if (x.equals(expectedState)) {
					logPass(expectedMessage);
				} else {
					logFail(logger, expectedMessage, driver);
				}
			} else {
				logFail(objName + " NOT FOUND!");
			}
		} catch (IOException e) {
			logInfo(logger, e.getMessage());
		}
		return x;
	}

	/**
	 * Use to get attribute value of object based on the attribute value that was
	 * passed
	 * 
	 * @param locator    ---- your object
	 * @param valueToGet ---- attribute to get
	 */
	public String getWebElementAttributeValue(By locator, String valueToGet) throws InterruptedException {
		String attribValue = null;
		WebElement objElement = driver.findElement(locator);
		Thread.sleep(3000);
		attribValue = objElement.getAttribute(valueToGet);
		return attribValue;
	}

	/**
	 * Use to get default value of Dropdown object
	 * 
	 * @param locator ---- your object
	 */
	public String getDropdownDefaultValue(By locator) throws InterruptedException {
		Select select = new Select(driver.findElement(locator));
		String selectedValue = select.getFirstSelectedOption().getText();
		return selectedValue;
	}

	public String getTheCurrentWindowHandle(WebDriver driver, ExtentTest logger)
			throws IOException, InterruptedException {
		try {
			String currentWindowName = driver.getWindowHandle();
			return currentWindowName;
		} catch (NoSuchWindowException e) {
			logFail(logger, "No window found", driver);
			return null;
		}
	}

	/**
	 * Switch to other window handles
	 * 
	 * @param driver        - WebDriver
	 * @param currentWindow - Window handle, where drive has to switch
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean switchToNewlyOpenedWindow(WebDriver driver, String currentWindow, ExtentTest logger)
			throws IOException, InterruptedException {
		try {
			Set<String> totalwindows = driver.getWindowHandles();
			for (String window : totalwindows) {
				if (!window.equalsIgnoreCase(currentWindow)) {
					driver.switchTo().window(window);
					logInfo(logger, "Successfully navigated to Newly opened window");
					break;
				}
			}
		} catch (NoSuchWindowException e) {
			logFail(logger, "No window found with the Window handle", driver);
			return false;
		}
		return true;
	}

	/**
	 * Format the date
	 * 
	 * @param format
	 */
	public String getDateFormat(String format) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * To select the list value from the dropdown using the list value
	 * 
	 * @param driver       - Webdriver
	 * @param locator      - Object locator
	 * @param lstIndex     - Index of the list value to be select
	 * @param dropdownName - Dropdown Name
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String selectValueFromListRandomly(WebDriver driver, By obj, String dropdownName, ExtentTest logger)
			throws IOException, InterruptedException {
		String textvalue = "";

		try {
			waitListLoad(driver, obj);
			if (elementExist(driver, obj)) {
				WebElement elm = driver.findElement(obj);
				Select s = new Select(elm);
				int r = randomNumInt(s.getOptions().size());

				if (s.getOptions().size() == 1) {
					s.selectByIndex(0);
				} else {
					s.selectByIndex(r);
				}

				textvalue = s.getFirstSelectedOption().getAttribute("innerText").trim();
				if (textvalue.equals("") || textvalue.equals("undefined"))
					while (true) {
						int o = randomNumInt(s.getOptions().size());
						s.selectByIndex(o);
						WebElement option = s.getFirstSelectedOption();
						textvalue = option.getText().trim();
						if (!textvalue.equals("") && !textvalue.equals("undefined")) {
							s.selectByIndex(r);
							logInfo(logger, "Selected the list value : " + textvalue);
							break;
						}
					}
				else {
					WebElement option = s.getFirstSelectedOption();
					logInfo(logger, "Selected the list value : " + textvalue);
				}
			} else {
				logFail(logger, "Element not found : " + dropdownName, driver);
			}
		} catch (Exception e) {
			logFail(logger, "Element not found : " + dropdownName + "Exception thrown is : " + e.getMessage(), driver);
		}
		return textvalue;
	}

	/**
	 * Wait for 30 seconds until the list box contains > 0
	 * 
	 * @param driver
	 * @param locator
	 */
	public void waitListLoad(WebDriver driver, final By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 30, 5);

		List<WebElement> el = driver.findElements(locator);

		if (el.size() == 1) {
			// Wait until expected condition size of the dropdown increases and becomes more
			// than 1
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					Select select = new Select(driver.findElement(locator));
					return select.getOptions().size() > 0;
				}
			});

		} else {
			for (WebElement we : el) {
				while (we.getCssValue("display").equals("block")) {
					try {
						if (!(we.getCssValue("display").equals("block"))) {
							break;
						}
					} catch (Exception e) {
						break;
					}
				}
			}

		}
	}

	/**
	 * Find if the list contains a certain string
	 * 
	 * @param a
	 * @param b
	 */
	public boolean compareString2Array(String a, String array[]) {
		for (int x = 0; x < array.length; x++) {
			// System.out.println(array[x]+"<<<");
			if (a.equalsIgnoreCase(array[x].trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find if the list contains a certain string
	 * 
	 * @param a
	 * @param b
	 */
	public boolean compareElements(String a, List<WebElement> b) {
		for (WebElement we : b) {
			if (a.equalsIgnoreCase(we.getAttribute("innerText").trim())) {
				return true;
			}
		}
		return false;
	}

	public void EndTest() {
		System.out.println("");
		System.out.println("***********************************************************");
		System.out.println("                  END OF TEST EXECUTION                    ");
		System.out.println("***********************************************************");
	}
}
