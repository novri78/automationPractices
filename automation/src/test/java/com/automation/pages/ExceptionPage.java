package com.automation.pages;

import com.automation.configuration.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExceptionPage extends BasePage{

    private final By btnAdd = By.id("add_btn");
    private final By btnEditRow1 = By.id("edit_btn");
    private final By inputRow1 = By.xpath("//div[@id='row1']/input[@type='text']");
    private final By inputRow2 = By.xpath("//div[@id='row2']/input");
    // SOLUSI UTAMA: Mengunci tombol Save spesifik yang berada di dalam kontainer DIV id='row2'
    private final By btnSaveRow2 = By.xpath("//div[@id='row2']//button[@name='Save']");
    private final By btnSaveRow1 = By.xpath("//div[@id='row1']//button[@name='Save']");
    // Locator untuk menangkap notifikasi sukses setelah data tersimpan
    private final By lblConfirmation = By.id("confirmation");
    private final By lblInstructions = By.id("instructions");

    public void open(){
        driver.get(ConfigReader.get("url2"));
    }
    public void clickAddButton(){
        wait.until(ExpectedConditions.elementToBeClickable(btnAdd)).click();
    }
    public void clickEditButton1(){
        wait.until(ExpectedConditions.elementToBeClickable(btnEditRow1)).click();
    }

    public WebElement waitForRow2InputField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inputRow2));
    }

    public void typeIntoRow1Input(String text) {
        // Otomatis menanti animasi "Loading..." selesai sebelum mengetik data
        wait.until(ExpectedConditions.elementToBeClickable(inputRow1));
        driver.findElement(inputRow1).clear();
        driver.findElement(inputRow1).sendKeys(text);

    }

    public void typeIntoRow2Input(String text) {
        // Otomatis menanti animasi "Loading..." selesai sebelum mengetik data
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputRow2)).sendKeys(text);
    }

    public void clickSaveButtonRow1() {
        // Menanti tombol Save baris kedua siap diklik, menembus ElementNotInteractableException
        wait.until(ExpectedConditions.elementToBeClickable(btnSaveRow1)).click();
    }

    public void clickSaveButtonRow2() {
        // Menanti tombol Save baris kedua siap diklik, menembus ElementNotInteractableException
        wait.until(ExpectedConditions.elementToBeClickable(btnSaveRow2)).click();
    }

    public String getConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(lblConfirmation)).getText();
    }

    public boolean verifyTimeoutOnRow2WithThreeSecondsWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        try {
            // Create a strict 3-second explicit wait observer
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(inputRow2));

            // If it reaches here, it means the element appeared under 2 seconds (Not expected)
            return false;

        } catch (TimeoutException e) {
            // Captures the expected TimeoutException successfully
            System.out.println("[INFO] Expected TimeoutException captured: Element did not load within 2 seconds.");
            return true;
        } finally {
            // Restore your standard framework implicit wait safety net
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
    }

    public WebElement getInstructionsElement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(lblInstructions));
    }

    /**
     * Checks if a previously rendered element has become disconnected from the active DOM tree.
     */
    public boolean waitForInstructionToDisappear(WebElement element) {
        try {
            // stalenessOf returns true as soon as the element is destroyed/deleted from the DOM
            return wait.until(ExpectedConditions.stalenessOf(element));
        } catch (Exception e) {
            return false;
        }
    }
}
