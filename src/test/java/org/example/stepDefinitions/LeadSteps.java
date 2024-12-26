package org.example.stepDefinitions;

import io.cucumber.java.en.*;
import org.example.framework.TestAutomationFramework;
import org.example.modules.pages.LeadPage;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class LeadSteps {
    private final WebDriver driver = TestAutomationFramework.getDriver();
    private final LeadPage leadPage = new LeadPage(driver);

    @Given("User navigates to the Leads page")
    public void userNavigatesToLeadsPage() {
        leadPage.navigateToLeads();
    }

    @When("User creates a new lead")
    public void userCreatesNewLead() {
        leadPage.createNewLead();
    }

    @Then("The lead should be created successfully")
    public void leadShouldBeCreatedSuccessfully() {
        assertTrue("Lead was not created successfully", leadPage.checkCreatedLead());
    }

    @When("User converts the lead to an opportunity")
    public void userConvertsLeadToOpportunity() {
        leadPage.conversionOfLeadToOpportunityAndNavigationToOpportunity();
    }

    @Then("The lead should be converted to an opportunity successfully")
    public void leadShouldBeConvertedSuccessfully() {
        assertTrue("Lead conversion flag not displayed", leadPage.leadConversionFlagDisplay());
        assertTrue("Opportunity page not opened", leadPage.isOpportunityPageOpened());
    }
}
