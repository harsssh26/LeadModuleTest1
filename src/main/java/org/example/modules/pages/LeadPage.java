package org.example.modules.pages;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

import com.github.javafaker.Faker;
import org.example.framework.Utility;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LeadPage {
    private final Utility utility;
    private final Faker faker = new Faker();
    String salutation = "Mr.";
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String phone1 = faker.phoneNumber().cellPhone();
    String mobile = faker.phoneNumber().phoneNumber();
    String website = faker.company().name();
    String email = faker.internet().emailAddress();
    String title = faker.job().title();
    String company = faker.company().name();
    String leadConversionMessage="";
    WebDriver driver=null;

    public LeadPage(WebDriver driver) {
        this.utility = new Utility(driver);
        this.driver=driver;
    }

    public void navigateToLeads() throws InterruptedException {
        utility.jsClick(By.xpath("//button[@title='App Launcher']"));
        Thread.sleep(1500);
        utility.enterText(By.xpath("//input[@placeholder='Search apps and items...']"), "Leads");
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//span//p//b[text()='Leads']"));
    }

    public void navigateToOpportunities() throws InterruptedException
    {
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//button[@title='App Launcher']"));
        Thread.sleep(1500);
        utility.enterText(By.xpath("//input[@placeholder='Search apps and items...']"), "Opportunities");
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//span//p//b[text()='Opportunities']"));
    }

    public void createNewLead() throws InterruptedException {


        utility.jsClick(By.xpath("//div[@title='New']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//button[@aria-label='Salutation']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//lightning-base-combobox-item//span[normalize-space()='" + salutation + "']"));
        Thread.sleep(1500);
        utility.enterText(By.xpath("//input[@name='firstName']"), firstName);
        Thread.sleep(1500);
        utility.enterText(By.xpath("//input[@name='lastName']"), lastName);
        utility.enterText(By.xpath("//input[@name='Company']"), company);
        utility.enterText(By.xpath("//input[@name='Phone']"), phone1);
        utility.enterText(By.xpath("//input[@name='MobilePhone']"), mobile);
        utility.enterText(By.xpath("//input[@name='Website']"), website);
        utility.enterText(By.xpath("//input[@name='Email']"), email);
        utility.enterText(By.xpath("//input[@name='Title']"), title);
        utility.jsClick(By.xpath("//button[@aria-label='Lead Status']"));
        utility.jsClick(By.xpath("//lightning-base-combobox-item//span[normalize-space()='Open - Not Contacted']"));
        utility.clickElement(By.xpath("//button[@name='SaveEdit']"));
    }

    public void searchOpportunity(String accountName) throws InterruptedException //Function to open the opportunity when account name is passed
    {
        navigateToOpportunities();
        Thread.sleep(2000);
        utility.jsClick(By.xpath("//span[@class='slds-truncate'][normalize-space()='Opportunities']"));
        Thread.sleep(1000);
        String opportunityName=accountName+"-";
        utility.scrollToElement(By.xpath("//a[@title='" + opportunityName + "']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//a[@title='" + opportunityName + "']"));
        Thread.sleep(1500);
    }
    public void searchOpportunityByOpportunityName(String opportunityName) throws InterruptedException //Function to open the opportunity when opportunity name is passed
    {
        Thread.sleep(1000);
        utility.scrollToElement(By.xpath("//a[@title='" + opportunityName + "']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//a[@title='" + opportunityName + "']"));
    }
    public String getOpportunityName() {
        WebElement opportunityElement = utility.waitForVisibility(By.xpath("//lightning-formatted-text[@slot='primaryField']"));
        return opportunityElement.getText();
    }

    public boolean checkCreatedLead()
    {
        String leadName=salutation+" "+firstName+" "+lastName;
        WebElement leadNameHeaderElement= utility.waitForVisibility(By.xpath("//lightning-formatted-name[@slot='primaryField']"));
        String leadNameValue=leadNameHeaderElement.getText();
        return leadNameValue.equals(leadName);

    }

    public void conversionOfLeadToOpportunityAndNavigationToOpportunity() throws InterruptedException {
        utility.scrollToElement(By.xpath("//lightning-button-menu[@data-target-reveals='sfdc:StandardButton.Lead.Clone,sfdc:StandardButton.Lead.XClean,sfdc:StandardButton.Lead.Delete,sfdc:StandardButton.Lead.Share,sfdc:StandardButton.Lead.Edit,sfdc:StandardButton.Lead.ChangeOwnerOne,sfdc:StandardButton.Lead.Convert']//lightning-primitive-icon[@variant='bare']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//lightning-button-menu[@data-target-reveals='sfdc:StandardButton.Lead.Clone,sfdc:StandardButton.Lead.XClean,sfdc:StandardButton.Lead.Delete,sfdc:StandardButton.Lead.Share,sfdc:StandardButton.Lead.Edit,sfdc:StandardButton.Lead.ChangeOwnerOne,sfdc:StandardButton.Lead.Convert']//lightning-primitive-icon[@variant='bare']"));
        Thread.sleep(2000);
        utility.scrollToElement(By.xpath("//lightning-button-menu[@class='menu-button-item slds-dropdown-trigger slds-dropdown-trigger_click slds-is-open']//runtime_platform_actions-action-renderer[@title='Convert']//a[@role='menuitem']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//lightning-button-menu[@class='menu-button-item slds-dropdown-trigger slds-dropdown-trigger_click slds-is-open']//runtime_platform_actions-action-renderer[@title='Convert']//a[@role='menuitem']"));
        Thread.sleep(2000);
        String opportunityName= utility.getElementValue(By.xpath("//span[text()='Opportunity Name']/ancestor::div[contains(@class, 'slds-form-element')]//input[contains(@class, 'input')]"));
        System.out.println("Opportunity Name is : "+opportunityName);
        Thread.sleep(1500);
        utility.scrollToElement(By.xpath("//button[@class='slds-button slds-button_brand' and normalize-space()='Convert']"));
        utility.jsClick(By.xpath("//button[@class='slds-button slds-button_brand' and normalize-space()='Convert']"));
        Thread.sleep(2000);
        utility.scrollToElement(By.xpath("//button[normalize-space()='Go to Leads']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//button[normalize-space()='Go to Leads']"));
        Thread.sleep(1500);
//        utility.scrollToElement(By.xpath("//span[normalize-space()='Opportunities']"));
//        Thread.sleep(1500);
//        utility.jsClick(By.xpath("//span[normalize-space()='Opportunities']"));
        navigateToOpportunities();
        Thread.sleep(1500);
        searchOpportunityByOpportunityName(opportunityName);

    }



    public void changeLeadStatus(String status) throws InterruptedException
    {
        Thread.sleep(3000);
        utility.scrollToElement(By.xpath("//lightning-button-menu[@class='menu-button-item slds-dropdown-trigger slds-dropdown-trigger_click']//lightning-primitive-icon[@variant='bare']"));
        Thread.sleep(3000);
        utility.jsClick(By.xpath("//lightning-button-menu[@class='menu-button-item slds-dropdown-trigger slds-dropdown-trigger_click']//lightning-primitive-icon[@variant='bare']"));
        Thread.sleep(2500);
        utility.scrollToElement(By.xpath("//lightning-menu-item[@data-target-selection-name='sfdc:StandardButton.Lead.Edit']//a[@role='menuitem']"));
        Thread.sleep(1500);
        utility.jsClick(By.xpath("//lightning-menu-item[@data-target-selection-name='sfdc:StandardButton.Lead.Edit']//a[@role='menuitem']"));
        Thread.sleep(1000);
        utility.scrollToElement(By.xpath("//button[@aria-label='Lead Status']"));
        utility.jsClick(By.xpath("//button[@aria-label='Lead Status']"));
        Thread.sleep(1000);
        utility.scrollToElement(By.xpath("//lightning-base-combobox-item//span[normalize-space()='" + status + "']"));
        Thread.sleep(1000);
        utility.jsClick(By.xpath("//lightning-base-combobox-item//span[normalize-space()='" + status + "']"));
        Thread.sleep(1500);
        utility.scrollToElement(By.xpath("//button[@name='SaveEdit']"));
        Thread.sleep(1500);
        utility.clickElement(By.xpath("//button[@name='SaveEdit']"));
    }

    public boolean leadConversionFlagDisplay()
    {
        WebElement leadNameHeaderElement= utility.waitForVisibility(By.xpath("//h2[normalize-space()='Your lead has been converted']"));
        leadConversionMessage=leadNameHeaderElement.getText();
        return leadConversionMessage.equals("Your lead has been converted");
    }

    public boolean isOpportunityPageOpened()
    {
        WebElement opportunityPage=utility.waitForVisibility(By.xpath("//records-entity-label[normalize-space()='Opportunity']"));
        String message=opportunityPage.getText();
        return message.equals("Opportunity");
    }

//    public void clickOpportunityForAccount(String accountName) throws InterruptedException
//    {
//
//        Set<String> windowHandles = driver.getWindowHandles();
//        if (windowHandles.size() > 1) {
//            for (String handle : windowHandles) {
//                driver.switchTo().window(handle);
//            }
//        }
//        boolean opportunityClicked = false;
//        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        for (int i = 1; i <= 50; i++) {
//            try {
//                By rowLocator = By.xpath("//table//tr[" + i + "]");
//                WebElement rowElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
//
//                if (!rowElement.isDisplayed()) {
//                    utility.scrollToElement(rowLocator);
//                }
//
//                By accountNameLocator = By.xpath("//table//tr[" + i + "]//td[contains(@class, 'slds-cell-edit')]//a[contains(text(), \"" + accountName + "\")]");
//                WebElement accountNameElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(accountNameLocator));
//                if (accountNameElement.getText().equalsIgnoreCase(accountName)) {
//                    By opportunityLinkLocator = By.xpath("(//table//tr[" + i + "]//a[contains(@data-refid, 'recordId') and contains(@class, 'outputLookupLink')])[1]");
//                    WebElement opportunityLinkElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(opportunityLinkLocator));
//                    if (!opportunityLinkElement.isDisplayed()) {
//                        utility.scrollToElement(opportunityLinkLocator);
//                    }
//                    utility.jsClick(opportunityLinkLocator);
//                    System.out.println("Navigated to the opportunity page for account: " + accountName);
//                    opportunityClicked = true;
//                    break;
//                }
//            } catch (NoSuchElementException e) {
//                System.out.println("Account name or opportunity link not found in row " + i + ". Moving to the next row.");
//            } catch (TimeoutException e) {
//                System.out.println("Timeout waiting for account name or opportunity link visibility in row " + i + ". Moving to the next row.");
//            } catch (StaleElementReferenceException e) {
//                System.out.println("Encountered stale element in row " + i + ", re-locating element.");
//            }
//        }
//
//        if (!opportunityClicked) {
//            System.out.println("Opportunity for account " + accountName + " was not found.");
//        }
//    }

//    public void findAccountAndClickOpportunity(String accountNameToFind) {
//        // First, find the index of "Account Name" column
//        int accountNameIndex = -1;
//        List<WebElement> headers = driver.findElements(By.xpath("//table/thead/tr/th//span[@class='slds-truncate']"));
//
//        // Find the index of Account Name column
//        for (int i = 0; i < headers.size(); i++) {
//            String headerText = headers.get(i).getText().trim();
//            System.out.println("Header " + i + ": " + headerText);
//            if (headerText.equals("Account Name")) {
//                accountNameIndex = i + 1;
//                break;
//            }
//        }
//
//        if (accountNameIndex == -1) {
//            throw new RuntimeException("Account Name column not found in the table headers");
//        }
//
//        // Now find and process the table rows using the dynamic index
//        List<WebElement> tableRows = driver.findElements(By.xpath("//table[contains(@class, 'slds-table')]//tbody//tr"));
//
//        for (WebElement row : tableRows) {
//            System.out.println("entering");
//            // Use the dynamic index to find the account name cell
//            System.out.println("accountNameIndex" +accountNameIndex);
//            WebElement accountNameCell = row.findElement(By.xpath(".//td[" + accountNameIndex + "]"));
//            String accountName = accountNameCell.getText();
//            System.out.println("Found account name: " + accountName);
//
//            if (accountName.equals(accountNameToFind)) {
//                // Find and click the opportunity link in the first cell
//                WebElement opportunityLink = row.findElement(By.xpath(".//th[contains(@class, 'slds-cell-edit')]//a"));
//                opportunityLink.click();
//                break;
//            }
//        }
//    }


    public void clickOpportunityForAccount(String accountName) throws InterruptedException {
        // Switch to the correct window if multiple windows are open
        Set<String> windowHandles = driver.getWindowHandles();
        if (windowHandles.size() > 1) {
            for (String handle : windowHandles) {
                driver.switchTo().window(handle);
            }
        }

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Increased timeout
        boolean opportunityClicked = false;

        // Step 1: Find column indices for "Opportunity Name" and "Account Name"
        int opportunityColumnIndex = -1;
        int accountColumnIndex = -1;

        List<WebElement> headers = driver.findElements(By.xpath("//table/thead/tr/th//span[@class='slds-truncate']"));
        for (int i = 0; i < headers.size(); i++) {
            String headerText = headers.get(i).getText().trim();
            System.out.println("Header found: '" + headerText + "' at index: " + i);
            if (headerText.equalsIgnoreCase("Opportunity Name")) {
                opportunityColumnIndex = i + 1; // Adding 1 for XPath index
            } else if (headerText.equalsIgnoreCase("Account Name")) {
                accountColumnIndex = i + 1;
            }
        }

        if (opportunityColumnIndex == -1 || accountColumnIndex == -1) {
            throw new RuntimeException("Required columns (Opportunity Name or Account Name) not found in the table headers");
        }

        // Step 2: Iterate through rows to find the correct account name and click the opportunity link
        for (int i = 1; i <= 50; i++) {  // Adjust the range if there are more or fewer rows
            try {
                By rowLocator = By.xpath("//table//tr[" + i + "]");
                WebElement rowElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));

                if (!rowElement.isDisplayed()) {
                    utility.scrollToElement(rowLocator);  // Scroll if row is not visible
                }

                By accountNameLocator = By.xpath("//table//tr[" + i + "]//td[" + accountColumnIndex + "]");
                WebElement accountNameElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(accountNameLocator));

                if (accountNameElement.getText().equalsIgnoreCase(accountName)) {
                    By opportunityLinkLocator = By.xpath("//table//tr[" + i + "]//td[" + opportunityColumnIndex + "]//a[contains(@data-refid, 'recordId') and contains(@class, 'outputLookupLink')]");
                    WebElement opportunityLinkElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(opportunityLinkLocator));

                    if (!opportunityLinkElement.isDisplayed()) {
                        utility.scrollToElement(opportunityLinkLocator); // Scroll to ensure visibility
                    }
                    utility.jsClick(opportunityLinkLocator);
                    System.out.println("Navigated to the opportunity page for account: " + accountName);
                    opportunityClicked = true;
                    break;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Account name or opportunity link not found in row " + i + ". Moving to the next row.");
            } catch (TimeoutException e) {
                System.out.println("Timeout waiting for account name or opportunity link visibility in row " + i + ". Moving to the next row.");
            } catch (StaleElementReferenceException e) {
                System.out.println("Encountered stale element in row " + i + ", re-locating element.");
            }
        }

        if (!opportunityClicked) {
            System.out.println("Opportunity for account " + accountName + " was not found.");
        }
    }

}
