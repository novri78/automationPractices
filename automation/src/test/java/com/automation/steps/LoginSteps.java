package com.automation.steps;

import com.automation.assertions.LoginAssertion;
import com.automation.pages.DashboardPage;
import com.automation.pages.LoginPage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;
    private final LoginAssertion loginAssertion;

    public LoginSteps() {
        this.loginPage = new LoginPage();
        this.dashboardPage = new DashboardPage();
        this.loginAssertion = new LoginAssertion();
    }

    @Given("Pengguna berada di halaman login")
    public void openLogin() {
        loginPage.open();
    }
//
//    @When("Pengguna memasukkan username {string} dan password {string}")
//    public void inputCredential(String username, String password){
//        loginPage.inputUsername(username);
//        loginPage.inputPassword(password);
//    }
//
//    @And("Pengguna menekan tombol klik login")
//    public void clickLogin(){
//        loginPage.clickLogin();
//    }
//
//    @Then("Pengguna otomatis diarahkan ke halaman dashboard")
//    public void verifyDashboard(){
//        Assert.assertEquals(
//                "Congratulations student. You successfully logged in!", dashboardPage.getTitle());
//
//    }

    @When("Pengguna memasukkan username {string}")
    public void penggunaMemasukkanUsername(String username) {
        loginPage.inputUsername(username);
    }

    @And("Pengguna memasukkan password {string}")
    public void penggunaMemasukkanPassword(String password) {
        loginPage.inputPassword(password);
    }

    @And("Pengguna klik tombol Login")
    public void penggunaKlikTombolLogin() {
        loginPage.clickLogin();
    }

    @Then("Login berhasil")
    public void loginBerhasil() {
        loginAssertion.verifyLoginSuccess();
    }

    @And("Pengguna klik tombol Logout")
    public void pengguna_klik_tombol_logout() {
        dashboardPage.clickLogout();
    }

    @Then("Pengguna kembali ke halaman Login")
    public void pengguna_kembali_ke_halaman_login() {
        loginAssertion.verifyOnLoginPage();
    }

    @Then("Pesan error username tampil")
    public void pesan_error_username_tampil() {
        loginAssertion.verifyUsernameFailed();
    }

    @Then("Pesan error password tampil")
    public void pesan_error_password_tampil() {
        loginAssertion.verifyPasswordFailed();
    }

    @Then("URL halaman baru harus mengandung {string}")
    public void urlHalamanBaruHarusMengandung(String urlSegemnt) {
        loginAssertion.verifyConttenUrlPage(urlSegemnt);
    }

    @And("Teks halaman baru harus mengandung kata {string} atau {string}")
    public void teksHalamanBaruHarusMengandungKataAtau(String text1, String text2) {
        loginAssertion.verifyPageContainsText(text1, text2);
    }

    @And("Tombol Log out harus ditampilkan di halaman baru")
    public void tombolLogOutHarusDitampilkanDiHalamanBaru() {
        loginAssertion.verifyLogoutButtonDisplayed();
    }

    @Given("Pengguna berada di halaman contentTable")
    public void penggunaBeradaDiHalamanContentTable() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("Pengguna memilih filter bahasa {string}")
    public void penggunaMemilihFilterBahasa(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("Hanya baris data dengan bahasa {string} yang ditampilkan pada tabel")
    public void hanyaBarisDataDenganBahasaYangDitampilkanPadaTabel(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
