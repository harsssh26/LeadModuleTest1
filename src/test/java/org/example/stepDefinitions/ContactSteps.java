package org.example.stepDefinitions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.modules.pages.ContactPage;

public class ContactSteps {

    ContactPage contactModule;

    @When("conversion of lead to opportunity is done")
    public void conversionToOpportunityAndSwitchToHomePage() throws InterruptedException
    {
        contactModule.convertToOpportunityAndOpenHomePage();
    }

    @Then("Contact page is opened and it must contain all relevant information")
    public void verifyContactInformation() throws InterruptedException
    {

    }
}
