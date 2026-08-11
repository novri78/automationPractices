package com.automation.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final int MAX_RETRIES = 3;

    public static WebDriver getDriver() {
        if (tlDriver.get() == null) {
            int attempt = 0;
            Exception lastEx = null;
            while (attempt < MAX_RETRIES) {
                try {
                    WebDriverManager.chromedriver().setup();

                    ChromeOptions options = new ChromeOptions();
                    Map<String, Object> prefs = new HashMap<>();

                    // Menonaktifkan pop-up asisten internal penyimpanan kata sandi Chrome
                    prefs.put("profile.password_manager_leak_detection", false);
                    prefs.put("credentials_enable_service", false);
                    prefs.put("profile.password_manager_enabled", false);
                    options.setExperimentalOption("prefs", prefs);
                    options.addArguments("--password-store=basic");

                    // logging prefs to collect browser console logs for post-mortem
                    LoggingPreferences logPrefs = new LoggingPreferences();
                    logPrefs.enable(LogType.BROWSER, java.util.logging.Level.ALL);
                    options.setCapability("goog:loggingPrefs", logPrefs);

                    // Headless support for CI/CD
                    if (System.getProperty("headless", "false").equalsIgnoreCase("true")) {
                        options.addArguments("--headless=new");
                        options.addArguments("--disable-gpu");
                        options.addArguments("--window-size=1920,1080");
                    }

                    // Remote WebDriver support (Grid / Selenium Hub / cloud)
                    String remoteUrl = System.getProperty("remoteUrl");
                    if (remoteUrl == null || remoteUrl.isEmpty()) {
                        remoteUrl = System.getenv("REMOTE_WEBDRIVER_URL");
                    }

                    if (remoteUrl != null && !remoteUrl.isEmpty()) {
                        tlDriver.set(new RemoteWebDriver(new URL(remoteUrl), options));
                    } else {
                        tlDriver.set(new ChromeDriver(options));
                        tlDriver.get().manage().window().maximize();
                    }

                    return tlDriver.get();
                } catch (Exception e) {
                    lastEx = e;
                    attempt++;
                    System.out.println("[WARN] WebDriver creation failed (attempt " + attempt + "): " + e.getMessage());
                    try {
                        Thread.sleep(1000L * attempt);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            throw new RuntimeException("Failed to create WebDriver after " + MAX_RETRIES + " attempts", lastEx);
        }
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            try {
                // attempt to fetch browser logs before quitting for diagnostics
                try {
                    WebDriver drv = tlDriver.get();
                    LogEntries logs = drv.manage().logs().get(LogType.BROWSER);
                    for (LogEntry entry : logs) {
                        System.out.println("[BROWSER LOG] " + entry.getLevel() + ": " + entry.getMessage());
                    }
                } catch (Exception ignored) {
                }
                tlDriver.get().quit();
            } finally {
                tlDriver.remove(); // Membersihkan memori kontekstual thread agar tidak leaker
            }
        }
    }
}
