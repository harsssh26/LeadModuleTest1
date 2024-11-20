package org.example.stepDefinitions;

import io.cucumber.java.en.*;
import org.example.framework.Utility;
import org.example.modules.pages.LoginPage;
import org.example.framework.TestAutomationFramework;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;

public class LoginSteps {
    WebDriver driver = TestAutomationFramework.getDriver();
    LoginPage loginPage = new LoginPage(driver);
    Utility utility = new Utility(driver);

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() throws InterruptedException {
        Thread.sleep(1000);
        TestAutomationFramework.openUrl("https://ck-qe-dev-ed.develop.lightning.force.com/lightning/setup/SetupOneHome/home");
    }

    @When("they enter valid login credentials")
    public void they_enter_valid_login_credentials() {
        loginPage.enterValidCredentials();
    }

    @Then("they should be logged in successfully")
    public void they_should_be_logged_in_successfully() throws InterruptedException {
        boolean isHomePageDisplayed = loginPage.isHomePageDisplayed();
        Assert.assertTrue("Login failed: Home page is not displayed", isHomePageDisplayed);
        System.out.println("Login successful: Home page is displayed.");
    }

    @And("they log out")
    public void they_log_out()
    {
        loginPage.log_out();
        TestAutomationFramework.closeBrowser();
    }


    @When("they enter invalid login credentials")
    public void they_enter_invalid_login_credentials() {
        loginPage.enterInvalidCredentials();
    }

    @Then("they should see an error message")
    public void they_should_see_an_error_message() {
        boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue("Error message not displayed", isErrorDisplayed);
        System.out.println("Error message displayed as expected.");
        TestAutomationFramework.closeBrowser();
    }

    @When("they leave the login credentials blank")
    public void they_leave_the_login_credentials_blank() {
        loginPage.enterBlankCredentials();
    }

    @Then("they should remain on the login page due to missing credentials")
    public void they_should_remain_on_the_login_page_due_to_missing_credentials() {
        boolean isOnLoginPage = loginPage.isLoginPageDisplayed();
        Assert.assertTrue("User was not redirected to login page due to missing credentials.", isOnLoginPage);
        System.out.println("User remains on login page due to missing credentials.");
        TestAutomationFramework.closeBrowser();
    }


}
