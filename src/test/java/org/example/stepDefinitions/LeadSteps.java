package org.example.stepDefinitions;

import org.example.framework.TestAutomationFramework;
import org.example.modules.pages.LeadPage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


public class LeadSteps {
    WebDriver driver;
    LeadPage leadModule;


    public LeadSteps() {
        driver = TestAutomationFramework.getDriver(); // Access static method directly from the class
        leadModule = new LeadPage(driver);
    }

    @Given("the user is on the leads creation page")
    public void navigateToLeads() throws InterruptedException {
        leadModule.navigateToLeads();
    }

    @When("they enter valid lead details")
    public void createNewLead() throws InterruptedException {
        leadModule.createNewLead();
    }

    @Then("the lead should be created successfully")
    public void verifyCreatedLead() throws InterruptedException {
        boolean isLeadCreated = leadModule.checkCreatedLead();
        Assert.assertTrue("Lead was not created successfully", isLeadCreated);
        System.out.println("Lead successfully created.");
    }


    @When("the user searches for the given account name in opportunity")
    public void opportunitySearch() throws InterruptedException {
        leadModule.searchOpportunity("wWBr Company");
    }

    @Then("the corresponding opportunity page must be opened")
    public void checkCreatedOpportunity() throws InterruptedException {
        String opportunityName = leadModule.getOpportunityName();
        Assert.assertEquals("Opportunity name does not match", "wWBr Company-", opportunityName);
        System.out.println("Correct Opportunity Opened: " + opportunityName);
        TestAutomationFramework.closeBrowser();
    }


    @When("conversion of lead to opportunity is done and we navigate to Opportunity")
    public void conversionOfLeadToOpportunity() throws InterruptedException
    {
        leadModule.changeLeadStatus("Working - Contacted");
        System.out.println("Lead converted to Working - Contacted");
        leadModule.changeLeadStatus("Closed - Not Converted");
        System.out.println("Lead converted to Closed - Not Converted");
        leadModule.changeLeadStatus("Closed - Converted");
        System.out.println("Lead converted to Closed - Converted");
        System.out.println("Converting Lead to Opportunity");
        leadModule.conversionOfLeadToOpportunityAndNavigationToOpportunity();
    }

    @Then("corresponding opportunity must be created")
    public void leadCreationSuccessMessage() throws InterruptedException
    {
        if(leadModule.leadConversionFlagDisplay())
        {
            System.out.println("lead Conversion and opportunity creation Successful");
        }
        else {
            System.out.println("lead Conversion and opportunity creation Unsuccessful");
        }

    }

    @Then("Opportunity Page Must be Opened")
    public void VerifyCreatedLead()
    {

        if(leadModule.isOpportunityPageOpened())
        {
            System.out.println("Opportunity opened");
        }
        else {
            System.out.println("Opportunity not opened");
        }
        TestAutomationFramework.closeBrowser();
    }

    @When("user navigates to opportunity Tab")
    public void navigatingToOpportunity() throws InterruptedException {
        leadModule.navigateToOpportunities();
    }

    @Then("the given account's opportunity must be opened")
    public void openCorrespondingOpportunity() throws InterruptedException
    {
        leadModule.clickOpportunityForAccount("a'b'c'd");
        TestAutomationFramework.closeBrowser();
    }

}
