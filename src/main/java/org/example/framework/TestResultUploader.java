package org.example.framework;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestResultUploader {

    private static final String API_URL = "https://tcms.aiojiraapps.com/aio-tcms/api/v1/project/{projectKey}/testcycle/{testCycleKey}/testcase/{testCaseId}/testrun?createNewRun=true";
    private static final String PROJECT_KEY = "SCRUM";
    private static final String TEST_CYCLE_KEY = "SCRUM-CY-Adhoc";
    private static final String AUTH_TOKEN = "AioAuth ZGE4NmMxYjctMjM2MC0zNWRhLTgzNDMtMWJmNzNiYzdlYmJkLjlhZDA4MWM1LWNlODMtNGNlYS1hOTI3LWFiOWMxMTc3OWYxMg==";

    public void uploadTestResults(File cucumberJsonFile) {
        Map<String, TestCaseResult> testCaseResults = logTestCaseResults(cucumberJsonFile);

        for (Map.Entry<String, TestCaseResult> entry : testCaseResults.entrySet()) {
            String testCaseId = entry.getKey();
            TestCaseResult result = entry.getValue();

            // Upload results for each test case, including step details
            createRunInAio(testCaseId, result);
        }
    }

    private void createRunInAio(String testCaseId, TestCaseResult result) {
        String endpoint = API_URL
                .replace("{projectKey}", PROJECT_KEY)
                .replace("{testCycleKey}", TEST_CYCLE_KEY)
                .replace("{testCaseId}", testCaseId);

        OkHttpClient client = createHttpClient();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("testCaseKey", testCaseId);
        bodyMap.put("testCaseVersion", 1);
        bodyMap.put("testRunStatus", result.getStatus());
        bodyMap.put("effort", 60);
        bodyMap.put("isAutomated", true);

        if ("Failed".equalsIgnoreCase(result.getStatus()) && result.getComment() != null && !result.getComment().isEmpty()) {
            bodyMap.put("comments", new String[]{result.getComment()});
        }

        // Add step-level results
        List<Map<String, Object>> stepResults = new ArrayList<>();
        for (StepResult step : result.getStepResults()) {
            Map<String, Object> stepResultMap = new HashMap<>();
            stepResultMap.put("stepName", step.getStepName());
            stepResultMap.put("stepStatus", step.getStepStatus());
            stepResultMap.put("stepComment", step.getStepComment());
            stepResults.add(stepResultMap);
        }
        bodyMap.put("stepResults", stepResults);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestBody = objectMapper.writeValueAsString(bodyMap);

            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(endpoint)
                    .addHeader("Authorization", AUTH_TOKEN)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Test run created successfully for Test Case: " + testCaseId + " with status: " + result.getStatus());
                } else {
                    System.err.println("Failed to create test run for Test Case: " + testCaseId);
                    System.err.println("Response Code: " + response.code());
                    System.err.println("Response Body: " + response.body().string());
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating test run for Test Case: " + testCaseId);
            e.printStackTrace();
        }
    }

    private Map<String, TestCaseResult> logTestCaseResults(File cucumberJsonFile) {
        Map<String, TestCaseResult> testCaseResults = new HashMap<>();
        try {
            String jsonContent = new String(Files.readAllBytes(cucumberJsonFile.toPath()));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            for (JsonNode feature : rootNode) {
                JsonNode scenarios = feature.get("elements");

                for (JsonNode scenario : scenarios) {
                    String testCaseId = null;
                    for (JsonNode tag : scenario.get("tags")) {
                        if (tag.get("name").asText().startsWith("@SCRUM-TC-")) {
                            testCaseId = tag.get("name").asText().replace("@", "");
                            break;
                        }
                    }

                    if (testCaseId == null) {
                        continue; // Skip scenarios without a valid test case ID
                    }

                    String finalStatus = "Passed";
                    String failureReason = "";
                    List<StepResult> stepResults = new ArrayList<>();

                    JsonNode steps = scenario.get("steps");
                    for (JsonNode step : steps) {
                        String stepName = step.get("name").asText();
                        String stepStatus = step.get("result").get("status").asText();
                        String stepComment = "";

                        if ("failed".equalsIgnoreCase(stepStatus)) {
                            finalStatus = "Failed";
                            failureReason = step.get("result").get("error_message").asText();
                            stepComment = failureReason;
                        }

                        stepResults.add(new StepResult(stepName, stepStatus, stepComment));
                    }

                    testCaseResults.put(testCaseId, new TestCaseResult(finalStatus, failureReason, stepResults));
                    System.out.println("Test Case: " + testCaseId + " | Status: " + finalStatus + " | Comment: " + failureReason);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading or parsing cucumber.json: " + e.getMessage());
            e.printStackTrace();
        }
        return testCaseResults;
    }

    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private static class TestCaseResult {
        private final String status;
        private final String comment;
        private final List<StepResult> stepResults;

        public TestCaseResult(String status, String comment, List<StepResult> stepResults) {
            this.status = status;
            this.comment = comment;
            this.stepResults = stepResults;
        }

        public String getStatus() {
            return status;
        }

        public String getComment() {
            return comment;
        }

        public List<StepResult> getStepResults() {
            return stepResults;
        }
    }

    private static class StepResult {
        private final String stepName;
        private final String stepStatus;
        private final String stepComment;

        public StepResult(String stepName, String stepStatus, String stepComment) {
            this.stepName = stepName;
            this.stepStatus = stepStatus;
            this.stepComment = stepComment;
        }

        public String getStepName() {
            return stepName;
        }

        public String getStepStatus() {
            return stepStatus;
        }

        public String getStepComment() {
            return stepComment;
        }
    }
}
