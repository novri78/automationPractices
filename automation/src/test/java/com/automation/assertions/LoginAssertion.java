package com.automation.assertions;

import com.automation.pages.DashboardPage;
import com.automation.pages.LoginPage;
import org.junit.Assert;

public class LoginAssertion {
    LoginPage loginPage = new LoginPage();
    DashboardPage dashboardPage = new DashboardPage();

    public void verifyLoginSuccess() {
        Assert.assertEquals(
                "Logged In Successfully", dashboardPage.getSuccessTitle()
        );
    }

    public void verifyUsernameFailed() {
        Assert.assertEquals(
                "Your username is invalid!", loginPage.getErrorMessage()
        );
    }

    public void verifyPasswordFailed() {
        Assert.assertEquals(
                "Your password is invalid!", loginPage.getErrorMessage()
        );
    }

    public void verifyOnLoginPage() {
        Assert.assertEquals(
                "Test login", loginPage.getLoginPageMessage()
        );
    }

    public void verifyConttenUrlPage(String urlSegment) {
        String actualUrl = loginPage.getUrlContent();
        Assert.assertTrue("Gagal: URL '" + actualUrl + "' tidak mengandung '" + urlSegment + "'",
                actualUrl.contains(urlSegment)
        );
    }

    // VERIFIKASI BARU: Cek Teks Kombinasi dinamis
    public void verifyPageContainsText(String text1, String text2) {
        String pageText = loginPage.getWholePageText();
        boolean isText1Present = pageText.contains(text1);
        boolean isText2Present = pageText.contains(text2);

        Assert.assertTrue("Gagal: Halaman tidak memuat kata '" + text1 + "' maupun '" + text2 + "'",
                isText1Present || isText2Present);
    }

    // VERIFIKASI BARU: Cek Tombol Logout tampil
    public void verifyLogoutButtonDisplayed() {
        Assert.assertTrue("Gagal: Tombol Log out tidak ditampilkan di halaman dasbor!",
                dashboardPage.isLogoutButtonDisplayed()); // Pastikan method ini ada di DashboardPage Anda
    }
}
