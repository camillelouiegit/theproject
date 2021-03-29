$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/goreact/testautomation/features/Sign_Up_Login.feature");
formatter.feature({
  "line": 1,
  "name": "Go React Sign up and Login.",
  "description": "",
  "id": "go-react-sign-up-and-login.",
  "keyword": "Feature"
});
formatter.before({
  "duration": 6971101,
  "status": "passed"
});
formatter.scenario({
  "line": 3,
  "name": "Sign up and login as Instructor",
  "description": "",
  "id": "go-react-sign-up-and-login.;sign-up-and-login-as-instructor",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 4,
  "name": "the user is in the GOREACT login page.",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "user Sign up as an instructor",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "user is able to sign up as an instructor",
  "keyword": "Then "
});
formatter.match({
  "location": "SignUp_Login_Steps.Verify_GoReact_Page()"
});
formatter.result({
  "duration": 2451393900,
  "status": "passed"
});
formatter.match({
  "location": "SignUp_Login_Steps.Sign_Up_As_Instructor()"
});
formatter.result({
  "duration": 15294146399,
  "status": "passed"
});
formatter.match({
  "location": "SignUp_Login_Steps.Verify_Logged_As_Instructor()"
});
formatter.result({
  "duration": 17722367501,
  "status": "passed"
});
formatter.before({
  "duration": 2648099,
  "status": "passed"
});
formatter.scenario({
  "line": 8,
  "name": "Sign up and login as Student",
  "description": "",
  "id": "go-react-sign-up-and-login.;sign-up-and-login-as-student",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 9,
  "name": "the user is in the GOREACT login page.",
  "keyword": "Given "
});
formatter.step({
  "line": 10,
  "name": "user Sign up as a Student",
  "keyword": "When "
});
formatter.step({
  "line": 11,
  "name": "user is able to sign up as a student",
  "keyword": "Then "
});
formatter.match({
  "location": "SignUp_Login_Steps.Verify_GoReact_Page()"
});
formatter.result({
  "duration": 2280025900,
  "status": "passed"
});
formatter.match({
  "location": "SignUp_Login_Steps.Sign_Up_As_Student()"
});
formatter.result({
  "duration": 11735036001,
  "status": "passed"
});
formatter.match({
  "location": "SignUp_Login_Steps.Verify_Logged_As_Student()"
});
formatter.result({
  "duration": 4334482700,
  "status": "passed"
});
});