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
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotPath = TestAutomationFramework.captureScreenshot(scenario.getName(), RetryLogic.getRetryCount(scenario));
            if (screenshotPath != null) {
                String testCaseId = getTestCaseIdFromTags(scenario);
                if (testCaseId != null) {
                    AioIntegration.addFailedTestScreenshot(testCaseId, screenshotPath);
                }
            }
        }
        TestAutomationFramework.closeBrowser();
    }

    private String getTestCaseIdFromTags(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .filter(tag -> tag.startsWith("@SCRUM-TC-"))
                .findFirst()
                .map(tag -> tag.replace("@", ""))
                .orElse(null);
    }
}
