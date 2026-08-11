package com.automation.pages;

import com.automation.configuration.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By txtUsername = By.id("username");
    private final By txtPassword = By.id("password");
    private final By btnSubmit = By.id("submit");
    private final By lblError = By.id("error");
    private final By loginPage = By.tagName("h2");

    public void open() {
        driver.get(ConfigReader.get("url1"));
    }

    public void inputUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtUsername))
                .sendKeys(username);
    }

    public void inputPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword))
                .sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSubmit))
                .click();
    }

    public String getErrorMessage() {
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(lblError))
                .getText();
    }

    public String getLoginPageMessage() {
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(loginPage))
                .getText();
    }

    public String getUrlContent() {
        return driver.getCurrentUrl();
    }

    public String getWholePageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))).getText();
    }
}
