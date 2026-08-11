package com.automation.hooks;

import com.automation.driver.DriverFactory;
import com.automation.utils.ScreenshotUtil;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class Hooks {
    @Before
    public void beforeScenario(){
        DriverFactory.getDriver();
    }

    @After
    public void afterScenario(Scenario scenario){
        // AUTOMATIC FAILURE SCREEN CAPTURE ENGINE
        if (scenario.isFailed()) {
            try {
                // Take a high-resolution screenshot directly from the active driver instance
                final byte[] screenshot = ScreenshotUtil.takeScreenshotBytes();
                if (screenshot.length > 0) {
                    Allure.addAttachment("Bukti_Visual_Gagal_" + scenario.getName().replace(" ", "_"), new ByteArrayInputStream(screenshot));
                    scenario.attach(screenshot, "image/png", "Evidence_Failure_" + scenario.getName().replace(" ", "_"));
                }

                // Attach current page source for deeper debugging
                String pageSource = ScreenshotUtil.getPageSource();
                if (pageSource != null && !pageSource.isEmpty()) {
                    Allure.addAttachment("PageSource_" + scenario.getName().replace(" ", "_"), "text/html", new ByteArrayInputStream(pageSource.getBytes(StandardCharsets.UTF_8)), ".html");
                }

                // Attach browser console logs
                String consoleLogs = ScreenshotUtil.getBrowserConsoleLogs();
                if (consoleLogs != null && !consoleLogs.isEmpty()) {
                    Allure.addAttachment("ConsoleLogs_" + scenario.getName().replace(" ", "_"), "text/plain", new ByteArrayInputStream(consoleLogs.getBytes(StandardCharsets.UTF_8)), ".log");
                }

                System.out.println("[INFO] Visual & diagnostic evidence attached for failed scenario: " + scenario.getName());
            } catch (Exception e) {
                System.out.println("[WARNING] Failed to capture visual state evidence: " + e.getMessage());
            }
        }
        // Terminate and clean up the active browser memory stack safely
        DriverFactory.quitDriver();
    }
}
