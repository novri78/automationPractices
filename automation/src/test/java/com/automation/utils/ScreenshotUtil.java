package com.automation.utils;

import com.automation.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import java.util.stream.Collectors;

public class ScreenshotUtil {

   public static byte[] takeScreenshotBytes() {
        try {
            return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.out.println("[WARN] takeScreenshotBytes failed: " + e.getMessage());
            return new byte[0];
        }
    }

   public static String getPageSource() {
        try {
            return DriverFactory.getDriver().getPageSource();
        } catch (Exception e) {
            System.out.println("[WARN] getPageSource failed: " + e.getMessage());
            return "";
        }
    }

   public static String getBrowserConsoleLogs() {
        try {
            LogEntries logs = DriverFactory.getDriver().manage().logs().get(LogType.BROWSER);
            return logs.getAll().stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("[WARN] getBrowserConsoleLogs failed: " + e.getMessage());
            return "";
        }
    }
}
