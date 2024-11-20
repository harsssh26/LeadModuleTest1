Feature: Contact Functionality

  Scenario : verifying Contact after creating Lead
    Given the user is on the login page
    When they enter valid login credentials
    Then they should be logged in successfully
    Given the user is on the leads creation page
    When they enter valid lead details
    Then the lead should be created successfully
    When conversion of lead to opportunity is done
    Then Contact page is opened and it must contain all relevant information