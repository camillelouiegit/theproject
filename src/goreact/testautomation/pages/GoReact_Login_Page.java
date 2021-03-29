package goreact.testautomation.pages;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver.Navigation;

import goreact.testautomation.utilities.TestBase;

public class GoReact_Login_Page extends TestBase {

	// ************************************************************
	// START - PAGE OBJECT DECLARATIONS
	// ************************************************************

	// INSTRUCTOR
	public static By _imgGoreact = By
			.xpath("//img[@src='https://staticassets.goreact.com/logo-goreact-dash-2020.svg']");
	public static By _btnLogin = By.xpath("//span[text()='Log In']/ancestor::button");
	public static By _btnCreateAccount = By.xpath("//button[@ng-click='$ctrl.toSignUp()'and text()='Create Account']");
	public static By _btnInstructor = By
			.xpath("//button[@ng-click='$ctrl.toInstructorSignUp()'and text()='INSTRUCTOR']");
	public static By _txtFirstName = By.xpath("//input[@id='first-name']");
	public static By _txtLastName = By.xpath("//input[@id='last-name']");
	public static By _txtPhoneNumber = By.xpath("//input[@id='phone-number']");
	public static By _txtEmail = By.xpath("//input[@id='email']");
	public static By _txtPassword = By.xpath("//input[@id='password']");
	public static By _txtConfirmPassword = By.xpath("//input[@id='confirm-password']");
	public static By _tickTerms = By.xpath("//input[@id='terms']");
	public static By _btnContinue = By.xpath("//button[@type='submit']");

	public static By _clickUsing = By.xpath("//uib-dropdown[@id='org_type_selector']");
	public static By _selectUsing = By.xpath("//a//span[text()='Personal Use']");

	public static By _clickTrainingType = By.xpath("//uib-dropdown[@id='course_type_selector']");
	public static By _selectTrainingType = By.xpath("//a//span[text()='Teacher Preparation']");

	public static By _clickCourseFormat = By.xpath("//uib-dropdown[@id='course_format']");
	public static By _selectCourseFormat = By.xpath("//a//span[text()='Online']");

	public static By _btnCreateCourse = By.xpath("//button[@ng-click='$ctrl.createCourse(selected.group)']");
	public static By _txtCourseName = By.xpath("//input[@id='course-name']");
	public static By _btnCreateCourseName = By
			.xpath("//div[@class='goreact-modal-footer']//button[text()='Create Course']");
	public static By _btnClose = By.xpath("//button[text()='Close']");
	public static By _btnDots = By.xpath("//group-menu-dropdown[@ng-if='$ctrl.group.hasInstructorRole(true)']");
	public static By _linkInviteUsers = By.xpath("//div[@class='sub-header']//a[text()='Invite Users']");
	public static By _btnCopyToClipboard = By.xpath("//button[@class='btn-none ng-isolate-scope']");
	public static By _btnCloseInvite = By.xpath("//button[@class='secondary-btn goreact-btn ng-scope']");

	public static By _btnDropdown = By.xpath("//button[@id='user-navigation-dropdown-btn']");
	public static By _btnLogOut = By.xpath("//a[text()='Log Out']");

	// STUDENT
	public static By _btnContinueStudent = By.xpath("//button[text()='Continue']");
	public static By _modalTermsPrivacy = By.xpath("//h4[text()='Updated User Terms and Privacy Policy']");
	public static By _modalAcceptTerms = By.xpath("//input[@id='accept-terms']");

	// ************************************************************
	// END - PAGE OBJECT DECLARATIONS
	// ************************************************************
	public void verify_login_page() throws InterruptedException, IOException {
		validateElementExists("GOREACT Image exist", GoReact_Login_Page._imgGoreact);
		validateElementExists("Log In", _btnLogin);
		validateElementExists("CREATE ACCOUNT button", _btnCreateAccount);
	}

	public void Sign_Up_As_Instructor() throws InterruptedException, IOException {
		Random rand = new Random();
		String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
		String randomString = "";
		int length = 10;

		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rand.nextInt(characters.length()));
		}
		for (int i = 0; i < text.length; i++) {
			randomString += text[i];
		}

		smartDataSetting(_btnCreateAccount, "click", "CREATE ACCOUNT", "");
		smartDataSetting(_btnInstructor, "click", "INSTRUCTOR", "");
		smartDataSetting(_txtFirstName, "set", "First Name", "Instructor" + randomString);
		smartDataSetting(_txtLastName, "set", "Last Name", randomString);
		smartDataSetting(_txtPhoneNumber, "set", "Phone Number", "1234");
		smartDataSetting(_txtEmail, "set", "Email", randomString + "@testing.com");
		smartDataSetting(_txtPassword, "set", "Password", "Test@1234");
		smartDataSetting(_txtConfirmPassword, "set", "Confirm Password", "Test@1234");
		smartDataSetting(_tickTerms, "click", "Terms", "");
		smartDataSetting(_btnContinue, "click", "Continue", "");
		smartDataSetting(_clickUsing, "click", "Using", "");
		smartDataSetting(_selectUsing, "click", "select Using", "");
		smartDataSetting(_clickTrainingType, "click", "Training Type", "");
		smartDataSetting(_selectTrainingType, "click", "select Training Type", "");
		smartDataSetting(_clickCourseFormat, "click", "Course Type", "");
		smartDataSetting(_selectCourseFormat, "click", "select Course Type", "");
		smartDataSetting(_btnContinue, "click", "Continue", "");
	}

	public void Create_Course() throws InterruptedException, IOException {
		smartDataSetting(_btnCreateCourse, "click", "Create Course", "");
		smartDataSetting(_txtCourseName, "set", "Course Name", "Test Course");
		smartDataSetting(_btnCreateCourseName, "click", "", "");
		smartDataSetting(_btnClose, "click", "Close", "");
		smartDataSetting(_btnDots, "click", "dots", "");
		smartDataSetting(_linkInviteUsers, "click", "Invite Users", "");
		smartDataSetting(_btnCopyToClipboard, "click", "Copy to Clipboard", "");

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			Object data = clipboard.getData(DataFlavor.stringFlavor);
			String url = data.toString();
			smartDataSetting(_btnCloseInvite, "click", "Close Invite", "");
			smartDataSetting(_btnDropdown, "click", "dropdown", "");
			smartDataSetting(_btnLogOut, "click", "Logout", "");
			waitForElement(driver, _btnCreateAccount);
			Navigation nav = driver.navigate();
			nav.to(url);
			logInfo(logger, "Navigated to " + url);
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Sign_Up_As_Student() throws InterruptedException, IOException {
		Random rand = new Random();
		String characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
		String randomString = "";
		int length = 10;

		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rand.nextInt(characters.length()));
		}
		for (int i = 0; i < text.length; i++) {
			randomString += text[i];
		}
		smartDataSetting(_btnCreateAccount, "click", "CREATE ACCOUNT", "");
		smartDataSetting(_btnContinueStudent, "click", "Continue", "");
		smartDataSetting(_txtFirstName, "set", "First Name", "Student" + randomString);
		smartDataSetting(_txtLastName, "set", "Last Name", randomString);
		smartDataSetting(_txtEmail, "set", "Email", randomString + "@testing.com");
		smartDataSetting(_txtPassword, "set", "Password", "Test@1234");
		smartDataSetting(_txtConfirmPassword, "set", "Confirm Password", "Test@1234");
		smartDataSetting(_tickTerms, "click", "Terms", "");
		smartDataSetting(_btnContinue, "click", "Continue", "");
		smartDataSetting(_btnClose, "click", "Close", "");
	}

	public void verify_student_page() throws InterruptedException, IOException {
		validateElementExists("GDPR", GoReact_Login_Page._modalTermsPrivacy);
		smartDataSetting(_modalAcceptTerms, "click", "Terms and Privacy", "");
		smartDataSetting(_btnContinue, "click", "Continue", "");
	}

	public void logout() throws InterruptedException, IOException {
		smartDataSetting(_btnDropdown, "click", "dropdown", "");
		smartDataSetting(_btnLogOut, "click", "Logout", "");
	}
}
