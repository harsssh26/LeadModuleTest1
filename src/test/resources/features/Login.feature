Feature: Login Functionality

  @Retry
  @SCRUM-TC-1
  Scenario: Login with valid credentials
    Given User is on the Login Page
    When User logs in with valid credentials
    And User verifies Login with valid TOTP
    Then User should see the home page

  @Retry
  @SCRUM-TC-3
  Scenario: Login with invalid credentials
    Given User is on the Login Page
    When User logs in with invalid credentials
    Then User should see an error message

  @Retry
  @SCRUM-TC-2
  Scenario: Login with blank credentials
    Given User is on the Login Page
    When User logs in with blank credentials
    Then User should see the login page
