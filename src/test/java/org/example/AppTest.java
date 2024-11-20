
package org.example;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.example.framework.TestAutomationFramework;
import org.example.modules.pages.LoginPage;
import org.example.modules.pages.LeadPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class AppTest {

     WebDriver driver;
    private LoginPage loginPage;
    private LeadPage leadPage;

    @BeforeClass
    public void displayS() // Executes once before first test method in the overall test class execution mainly used to set up shared resources for all test methods[Runs only once per test class]
    {
        System.out.println("Starting Test Execution!!");
    }

    @AfterClass
    public void displayE()  // Executes once after all test methods have been executed[Runs only once per test class irrespective of the number of test methods] mainly used to clear up shared resources for example closing a WebDriver
    {
        System.out.println("All Test Methods Run!! Exiting.. ");
    }

    @BeforeMethod // This method runs before every method having @Test Annotation, and is used to set up environment, Test Data before each method
    public void setup() {
        driver = TestAutomationFramework.getDriver();
        TestAutomationFramework.openUrl("https://ck-qe-dev-ed.develop.lightning.force.com/lightning/setup/SetupOneHome/home");
        loginPage = new LoginPage(driver);
        leadPage = new LeadPage(driver);
    }

    @AfterMethod // This is a tear down method, which is used to free space, temporary clearing data or logging out as in this case
    public void tearDown() {

        TestAutomationFramework.closeBrowser();
    }



    @Test
    @Epic("User Authentication")
    @Feature("Login with valid credentials")
    @Description("This test verifies that a user can log in with valid credentials.")
    public void testValidLogin() throws InterruptedException {
        performLogin("saikat@cloudkaptan.com.dev", "Sat1234567");
        verifyHomePage();
        performLogout();
    }

    @Test
    @Epic("User Authentication")
    @Feature("Login with invalid credentials")
    @Description("This test verifies that a user cannot log in with invalid credentials.")
    public void testInvalidLogin() {
        performLogin("invalid@example.com", "wrong password");
        verifyLoginError();
    }

    @Test
    @Epic("User Authentication")
    @Feature("Login with no credentials")
    @Description("This test verifies that a user cannot login after entering null data.")
    public void testNoCredentialLogin()
    {
        performLoginWithoutCredentials();
        verifyStillOnLoginPage();
    }

    @Test
    @Epic("Lead Management")
    @Feature("Create and verify new lead")
    @Description("This test verifies that a new lead can be created and verified.")
    public void testCreateNewLead() throws InterruptedException {
        performLogin("saikat@cloudkaptan.com.dev", "Sat1234567");
        navigateToLeadPage();
        createNewLead();
        verifyLeadCreation();
        performLogout();
    }


    @Step("Login with username: {0} and password: {1}")
    public void performLogin(String username, String password) {
        loginPage.login(username, password);
    }

    @Step("Verify home page is displayed")
    public void verifyHomePage() throws InterruptedException {
        Assert.assertTrue(loginPage.isHomePageDisplayed(), "Home page should be displayed.");
    }

    @Step("Verify login error message is displayed")
    public void verifyLoginError() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
    }

    @Step("Login with no credentials ")
    public void performLoginWithoutCredentials()
    {
        loginPage.enterBlankCredentials();
    }

    @Step("Verify that user is still on login page")
    public void verifyStillOnLoginPage()
    {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "User should be on the login Page");
    }

    @Step("Logout from the application")
    public void performLogout() {
        loginPage.log_out();
    }

    @Step("Navigate to Lead page")
    public void navigateToLeadPage() throws InterruptedException {
        leadPage.navigateToLeads();
    }

    @Step("Create a new lead")
    public void createNewLead() throws InterruptedException {
        leadPage.createNewLead();
    }

    @Step("Verify lead was created successfully")
    public void verifyLeadCreation() {
        Assert.assertTrue(leadPage.checkCreatedLead(), "The new lead should be created and visible.");
    }
}
