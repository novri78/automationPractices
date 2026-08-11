package com.automation.pages;

import com.automation.configuration.ConfigReader;
import com.automation.driver.DriverFactory;
import com.automation.utils.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage extends WaitHelper {
    protected WebDriver driver;
    protected static WebDriverWait wait;

    public BasePage(){
        driver = DriverFactory.getDriver();

        wait = new WebDriverWait(this.driver,
                Duration.ofSeconds(
                        Integer.parseInt(ConfigReader.get("timeout"))
                ));
    }
}
