package org.example.stepDefinitions;

import io.cucumber.java.en.*;
import org.example.framework.OTPHandler;
import org.example.framework.TestAutomationFramework;
import org.example.modules.pages.LoginPage;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class LoginSteps {
    private final WebDriver driver = TestAutomationFramework.getDriver();
    private final LoginPage loginPage = new LoginPage(driver);
    private final OTPHandler otpHandler = new OTPHandler(driver);

    @Given("User is on the Login Page")
    public void userIsOnLoginPage() {
        TestAutomationFramework.openUrl("https://login.salesforce.com/");
    }

    @When("User logs in with valid credentials")
    public void userLogsInWithValidCredentials() {
        loginPage.enterValidCredentials();
    }

    @And("User verifies Login with valid TOTP")
    public void userVerifiesLoginWithValidTOTP()
    {
        otpHandler.handleVerification();
    }

    @Then("User should see the home page")
    public void userShouldSeeHomePage() {
        assertTrue("Home page not displayed", loginPage.isHomePageDisplayed());
    }

    @When("User logs in with invalid credentials")
    public void userLogsInWithInvalidCredentials() {
        loginPage.enterInvalidCredentials();
    }

    @Then("User should see an error message")
    public void userShouldSeeErrorMessage() {
        assertTrue("Error message not displayed", loginPage.isErrorMessageDisplayed());
    }

    @When("User logs in with blank credentials")
    public void userLogsInWithBlankCredentials() {
        loginPage.enterBlankCredentials();
    }

    @Then("User should see the login page")
    public void userShouldSeeLoginPage() {
        assertTrue("Login page not displayed", loginPage.isLoginPageDisplayed());
    }
}
