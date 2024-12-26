package org.example.framework;

import okhttp3.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AioIntegration {

    private static final String BASE_URL = "https://tcms.aiojiraapps.com/aio-tcms/api/v1/project";
    private static final String UPLOAD_ATTACHMENT_ENDPOINT_TEMPLATE = BASE_URL + "/{jiraProjectId}/testcycle/{testCycleId}/testcase/{testCaseId}/attachment";
    private static final String AUTH_TOKEN = "AioAuth ZGE4NmMxYjctMjM2MC0zNWRhLTgzNDMtMWJmNzNiYzdlYmJkLjlhZDA4MWM1LWNlODMtNGNlYS1hOTI3LWFiOWMxMTc3OWYxMg==";

    private static final String JIRA_PROJECT_ID = "SCRUM";
    private static final String TEST_CYCLE_ID = "SCRUM-CY-Adhoc";

    private static final Map<String, String> failedScreenshots = new HashMap<>();

    public static void addFailedTestScreenshot(String testCaseId, String screenshotPath) {
        failedScreenshots.put(testCaseId, screenshotPath);
    }

    public static List<String> getFailedTestScreenshots() {
        return new ArrayList<>(failedScreenshots.values());
    }

    public static String getTestCaseIdFromScreenshotPath(String screenshotPath) {
        return failedScreenshots.entrySet().stream()
                .filter(entry -> entry.getValue().equals(screenshotPath))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public static void uploadScreenshotToAio(String testCaseId, String filePath) {
        String endpoint = UPLOAD_ATTACHMENT_ENDPOINT_TEMPLATE
                .replace("{jiraProjectId}", JIRA_PROJECT_ID)
                .replace("{testCycleId}", TEST_CYCLE_ID)
                .replace("{testCaseId}", testCaseId);

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Screenshot file not found: " + filePath);
            return;
        }

        System.out.println("Uploading screenshot: " + filePath + " for Test Case: " + testCaseId);

        OkHttpClient client = createHttpClient();

        RequestBody fileBody = RequestBody.create(file, MediaType.parse("image/png"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", AUTH_TOKEN)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "No response body";
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Body: " + responseBody);

            if (response.isSuccessful()) {
                System.out.println("Screenshot uploaded successfully for Test Case: " + testCaseId);
            } else {
                System.err.println("Failed to upload screenshot for Test Case: " + testCaseId);
                System.err.println("Response Code: " + response.code());
                System.err.println("Response Body: " + responseBody);
            }
        } catch (IOException e) {
            System.err.println("Error uploading screenshot for Test Case: " + testCaseId);
            e.printStackTrace();
        }
    }

    private static OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
