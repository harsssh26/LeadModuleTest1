package org.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.example.framework.TestResultUploader;
import org.example.framework.AioIntegration;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "org.example.stepDefinitions",
                "org.example.framework"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json" // Generates Cucumber JSON report
        },
        monochrome = true
)
public class TestRunner {

        @AfterClass
        public static void uploadTestResultsAndScreenshots() {
                File cucumberJsonFile = new File("target/cucumber.json");

                // Upload Cucumber JSON file first
                if (cucumberJsonFile.exists()) {
                        System.out.println("Uploading Cucumber JSON test results...");
                        TestResultUploader uploader = new TestResultUploader();
                        uploader.uploadTestResults(cucumberJsonFile);
                } else {
                        System.err.println("Cucumber JSON file not found: " + cucumberJsonFile.getAbsolutePath());
                        return;
                }

                // Upload screenshots for failed tests
                List<String> failedTestScreenshots = AioIntegration.getFailedTestScreenshots();
                if (!failedTestScreenshots.isEmpty()) {
                        System.out.println("Uploading screenshots for failed tests...");
                        for (String screenshotPath : failedTestScreenshots) {
                                String testCaseId = AioIntegration.getTestCaseIdFromScreenshotPath(screenshotPath);
                                if (testCaseId != null) {
                                        AioIntegration.uploadScreenshotToAio(testCaseId, screenshotPath);
                                }
                        }
                } else {
                        System.out.println("No failed test screenshots to upload.");
                }
        }
}
