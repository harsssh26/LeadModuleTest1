package org.example.framework;

import io.cucumber.java.Scenario;

import java.util.HashMap;
import java.util.Map;

public class RetryLogic {

    private static final int MAX_RETRY_COUNT = 0; // Maximum number of retries per scenario
    private static final Map<String, Integer> retryCounts = new HashMap<>(); // Track retry counts for each scenario

    public static int getRetryCount(Scenario scenario) {

        return retryCounts.getOrDefault(scenario.getId(), 0);
    }

    public static void incrementRetryCount(Scenario scenario) {
        retryCounts.put(scenario.getId(), getRetryCount(scenario) + 1);
    }

    public static boolean shouldRetry(Scenario scenario) {
        return getRetryCount(scenario) < MAX_RETRY_COUNT;
    }

    public static void resetRetryCount(Scenario scenario) {
        retryCounts.remove(scenario.getId());
    }
}