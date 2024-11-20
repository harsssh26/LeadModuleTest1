Feature: Lead Functionality

  @Lead
  Scenario: Creating a new lead
    Given the user is on the login page
    When they enter valid login credentials
    Then they should be logged in successfully
    Given the user is on the leads creation page
    When they enter valid lead details
    Then the lead should be created successfully

  @Lead
  Scenario: Checking the Opportunity of an account name as given
    Given the user is on the login page
    When they enter valid login credentials
    Then they should be logged in successfully
    Given the user is on the leads creation page
    When the user searches for the given account name in opportunity
    Then the corresponding opportunity page must be opened

    Scenario: Conversion of Lead to Opportunity
      Given the user is on the login page
      When they enter valid login credentials
      Then they should be logged in successfully
      Given the user is on the leads creation page
      When they enter valid lead details
      Then the lead should be created successfully
      When conversion of lead to opportunity is done and we navigate to Opportunity
      Then Opportunity Page Must be Opened

  Scenario: Opening of Opportunity Page of Given Account Name
    Given the user is on the login page
    When they enter valid login credentials
    Then they should be logged in successfully
    When user navigates to opportunity Tab
    Then the given account's opportunity must be opened