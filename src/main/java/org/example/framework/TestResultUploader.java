package org.example.framework;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestResultUploader {

    private static final String API_URL = "https://tcms.aiojiraapps.com/aio-tcms/api/v1/project/{projectKey}/testcycle/{testCycleKey}/import/results?type=Cucumber";

    private static final String PROJECT_KEY = "SCRUM";
    private static final String TEST_CYCLE_KEY = "SCRUM-CY-Adhoc";
    private static final String AUTH_TOKEN = "AioAuth ZGE4NmMxYjctMjM2MC0zNWRhLTgzNDMtMWJmNzNiYzdlYmJkLjlhZDA4MWM1LWNlODMtNGNlYS1hOTI3LWFiOWMxMTc3OWYxMg==";

    public void uploadTestResults(File cucumberJsonFile) {
        String apiUrl = API_URL
                .replace("{projectKey}", PROJECT_KEY)
                .replace("{testCycleKey}", TEST_CYCLE_KEY);

        OkHttpClient client = createHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", cucumberJsonFile.getName(), RequestBody.create(cucumberJsonFile, MediaType.parse("application/json")));
        builder.addFormDataPart("actualResults", sanitizeActualResults("Test failed at step X"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", AUTH_TOKEN)
                .post(builder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Test results uploaded successfully!");
            } else {
                System.err.println("Failed to upload test results. Response Code: " + response.code());
                System.err.println("Response Body: " + response.body().string());
            }
        } catch (IOException e) {
            System.err.println("Error uploading test results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String sanitizeActualResults(String actualResults) {
        // Only include concise information, such as the error message
        if (actualResults.contains("org.openqa.selenium.TimeoutException")) {
            return "Timeout occurred while waiting for the page to load or an element to be clickable.";
        } else {
            return actualResults; // Default if no specific cleaning is required
        }
    }

    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
