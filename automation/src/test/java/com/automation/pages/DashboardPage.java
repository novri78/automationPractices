package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    private final By btnLogout = By.xpath("//*[@id=\"loop-container\"]/div/article/div[2]/div/div/div/a");
    private final By lblTitle =
            By.tagName("h1");
    private final By lblTitle2 = By.tagName("strong");
    //private final By buttonOkChangePassword = By.xpath("//button[text()='OK']");

    public String getSuccessTitle() {

        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(lblTitle))
                .getText();

    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(lblTitle2))
                .getText();
    }

    public void clickLogout() {

        wait.until(
                        ExpectedConditions.elementToBeClickable(btnLogout))
                .click();

    }

    public boolean isLogoutButtonDisplayed() {
        try {
            // Menunggu hingga tombol logout benar-benar terlihat di layar (maksimal durasi sesuai BasePage wait)
            return wait.until(ExpectedConditions.visibilityOfElementLocated(btnLogout)).isDisplayed();
        } catch (Exception e) {
            // Jika dalam batas waktu tunggu tombol tidak muncul, kembalikan nilai false secara aman ke Assertion
            System.out.println("[INFO] Tombol Log out tidak ditemukan di halaman aktif saat ini.");
            return false;
        }

    }
}