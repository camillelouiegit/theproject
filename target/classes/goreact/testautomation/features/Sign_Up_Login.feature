Feature: Go React Sign up and Login.

Scenario: Sign up and login as Instructor
    Given the user is in the GOREACT login page.
    When user Sign up as an instructor
    Then user is able to sign up as an instructor
   
Scenario: Sign up and login as Student
    Given the user is in the GOREACT login page.
    When user Sign up as a Student
    Then user is able to sign up as a student