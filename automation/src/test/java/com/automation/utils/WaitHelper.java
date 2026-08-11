package com.automation.utils;

import com.automation.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelper {
    public static void waitUntilElementDisappears(By loaderLocator, int timeout) {
        WebDriver driver = DriverFactory.getDriver();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeout))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        } catch (Exception e) {
            System.out.println("[WARNING] waitUntilElementDisappears timed out or failed: " + e.getMessage());
            // keep test flow — caller can decide to fail/assert if needed
        }
    }
}
