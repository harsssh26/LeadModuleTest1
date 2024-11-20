package org.example.modules.pages;

import com.github.javafaker.Faker;
import org.example.framework.Utility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private static final String VALID_USERNAME = "saikat@cloudkaptan.com.dev";
    private static final String VALID_PASSWORD = "Sat1234567";

    private final Utility utility;
    private final Faker faker = new Faker();

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("Login");
    private final By errorMessage = By.id("error");

    public LoginPage(WebDriver driver) {
        this.utility = new Utility(driver);
    }

    public void login(String username, String password) {
        utility.enterText(usernameField, username);
        utility.enterText(passwordField, password);
        utility.clickElement(loginButton);
    }

    public void enterValidCredentials() {
        login(VALID_USERNAME, VALID_PASSWORD);
    }

    public void enterInvalidCredentials() {
        String invalidUsername = faker.internet().emailAddress();
        String invalidPassword = faker.internet().password();
        login(invalidUsername, invalidPassword);
    }

    public void enterBlankCredentials() {
        login("", "");
    }

    public boolean isErrorMessageDisplayed() {
        return utility.verifyElementPresence(errorMessage);
    }

    public String getErrorMessageText() {
        return utility.getElementValue(errorMessage);
    }

    public  boolean isLoginPageDisplayed() {
        return utility.verifyElementPresence(usernameField);
    }

    public boolean isHomePageDisplayed() throws InterruptedException {
        Thread.sleep(5000);
        utility.waitForVisibility(By.xpath("//div[@class='slds-page-header branding-setup onesetupSetupHeader']//span[normalize-space()='Home']"));
        return utility.verifyElementPresence(By.xpath("//div[@class='slds-page-header branding-setup onesetupSetupHeader']//span[normalize-space()='Home']"));
    }

    public void log_out()//logging out
    {
        utility.jsClick(By.xpath("//div[@class='profileTrigger branding-user-profile bgimg slds-avatar slds-avatar_profile-image-small circular forceEntityIcon']//span[@class='uiImage']"));
        utility.jsClick(By.xpath("//a[@class='profile-link-label logout uiOutputURL']"));
    }
}
