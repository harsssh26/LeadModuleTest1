Feature: Lead Management

  @Retry
  @SCRUM-TC-4
  Scenario: Create a new lead
    Given User is on the Login Page
    When User logs in with valid credentials
    Given User navigates to the Leads page
    When User creates a new lead
    Then The lead should be created successfully

