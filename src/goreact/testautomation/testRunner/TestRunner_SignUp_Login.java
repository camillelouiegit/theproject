package goreact.testautomation.testRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import goreact.testautomation.utilities.TestBase;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/goreact/testautomation/features/Sign_Up_Login.feature", // -------location
		// of
		// my feature
		plugin = { "com.cucumber.listener.ExtentCucumberFormatter:testResults/testreport.html", // -------location of my
																								// execution results
				"pretty:STDOUT", "html:testResults/pretty/test-output", "json:testResults/json/cucumber.json",
				"junit:testResults/junit/cucumber.xml" }, monochrome = true, // display console in readable format
		glue = { "goreact/testautomation/stepDefinitions" }, dryRun = false // check mapping
// if for both
// feature and
// step
// definition file
)

public class TestRunner_SignUp_Login {

	static TestBase testBase = new TestBase();

	@BeforeClass()
	public static void startTest() throws Throwable {
		testBase.initializeTest();

	}

	@AfterClass()
	public static void endTest() {
		testBase.tearDown();
		testBase.EndTest();
	}

}
