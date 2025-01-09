package org.example.framework;

import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class RetryHook {

    @Before
    public void beforeScenario(Scenario scenario) {
        int retryCount = RetryLogic.getRetryCount(scenario);
        if (retryCount > 0) {
            System.out.println("Retrying scenario: " + scenario.getName() + " | Retry attempt: " + retryCount);
        } else {
            System.out.println("Starting scenario: " + scenario.getName());
        }
    }
    @After
    public void afterScenario(Scenario scenario) {
        String testCaseId = getTestCaseIdFromTags(scenario);
        System.out.println("-----------------------------------LOGGING TO CHECK TEST CASE ID");
        System.out.println("Extracted Test Case ID: " + testCaseId);
        // Log the final status of the scenario
        System.out.println("Scenario Final Status: " + (scenario.isFailed() ? "FAILED" : "PASSED") + " | Name: " + scenario.getName());

        if (scenario.isFailed()) {
            // Handle failed scenario
            System.out.println("Scenario failed: " + scenario.getName());
            int retryCount = RetryLogic.getRetryCount(scenario);
            String screenshotPath = TestAutomationFramework.captureScreenshot(scenario.getName(), retryCount);

            if (screenshotPath != null) {
                System.out.println("Captured screenshot for failed scenario: " + scenario.getName() + " | Path: " + screenshotPath);

                // Add screenshot to AioIntegration for failed test case
                if (testCaseId != null) {
                    System.out.println("Linking screenshot to Test Case ID: " + testCaseId);
                    AioIntegration.addFailedTestScreenshot(testCaseId, screenshotPath);
                } else {
                    System.err.println("Test Case ID is null for failed scenario: " + scenario.getName());
                }
            } else {
                System.err.println("Failed to capture screenshot for scenario: " + scenario.getName());
            }
        } else {
            // Handle passed scenario
            System.out.println("Scenario passed: " + scenario.getName());

            if (testCaseId != null) {
                // Remove any stale screenshots for the passing test case
                if (AioIntegration.getFailedTestScreenshots().contains(testCaseId)) {
                    System.out.println("Removing screenshot entry for passed scenario: " + testCaseId);
                    AioIntegration.getFailedTestScreenshots().remove(testCaseId);
                } else {
                    System.out.println("No screenshot entry found for Test Case ID: " + testCaseId);
                }
            } else {
                System.out.println("Test Case ID is null for passed scenario: " + scenario.getName());
            }
        }

        // Ensure the browser is closed after each scenario
        try {
            System.out.println("Closing browser for scenario: " + scenario.getName());
            TestAutomationFramework.closeBrowser();
        } catch (Exception e) {
            System.err.println("Error occurred while closing the browser for scenario: " + scenario.getName());
            e.printStackTrace();
        }
    }




    private String getTestCaseIdFromTags(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .filter(tag -> tag.startsWith("@SCRUM-TC-"))
                .findFirst()
                .map(tag -> tag.replace("@", "")) // Remove "@" from the tag
                .orElse(null);

    }
}
