package org.example.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.PageLoadStrategy;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class TestAutomationFramework {

    // ThreadLocal to manage WebDriver instances for parallel tests
    private static final ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        return webDriver;
    });

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void openUrl(String url) {
        getDriver().get(url);
    }

    public static void closeBrowser() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
