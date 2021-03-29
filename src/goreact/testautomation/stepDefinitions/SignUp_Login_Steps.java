package goreact.testautomation.stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import goreact.testautomation.pages.GoReact_Login_Page;
import goreact.testautomation.utilities.TestBase;

public class SignUp_Login_Steps extends TestBase {
	GoReact_Login_Page goreact_login_page = new GoReact_Login_Page();
	Scenario scenario;

	Boolean execFlag;

	@Before()
	public void startTest(Scenario scenario) {
		this.scenario = scenario;
		System.out.println("***********************************************************");
		System.out.println("                  STARTING TEST EXECUTION            	   ");
		System.out.println("***********************************************************");
		System.out.println("Executing Scenario = " + scenario.getName());
	}

	@Given("the user is in the GOREACT login page.")
	public void Verify_GoReact_Page() throws Throwable {
		goreact_login_page.verify_login_page();
	}

	@When("^user Sign up as an instructor$")
	public void Sign_Up_As_Instructor() throws Throwable {
		goreact_login_page.Sign_Up_As_Instructor();
	}

	@Then("^user is able to sign up as an instructor$")
	public void Verify_Logged_As_Instructor() throws Throwable {
		goreact_login_page.Create_Course();

	}

	@When("^user Sign up as a Student$")
	public void Sign_Up_As_Student() throws Throwable {
		goreact_login_page.Sign_Up_As_Student();
	}

	@Then("^user is able to sign up as a student$")
	public void Verify_Logged_As_Student() throws Throwable {
		goreact_login_page.verify_student_page();
		goreact_login_page.logout();
	}

}