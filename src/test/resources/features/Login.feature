Feature: Login functionality

  @Login
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When they enter valid login credentials
    Then they should be logged in successfully
    And they log out

  @Login
  Scenario: Unsuccessful login with invalid credentials
    Given the user is on the login page
    When they enter invalid login credentials
    Then they should see an error message

    @Login
  Scenario: Unsuccessful login with blank credentials
    Given the user is on the login page
    When they leave the login credentials blank
    Then they should remain on the login page due to missing credentials
