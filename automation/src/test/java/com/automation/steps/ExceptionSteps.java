package com.automation.steps;

import com.automation.pages.ExceptionPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

public class ExceptionSteps {

    private final ExceptionPage exceptionPage;
    private boolean isTimeoutTriggered = false;
    private WebElement savedInstructionsElement;

    public ExceptionSteps(){
        this.exceptionPage = new ExceptionPage();
    }

    @Given("Pengguna berada di halaman exceptions")
    public void penggunaBeradaDiHalamanExceptions() {
        exceptionPage.open();
    }

    @When("Pengguna klik tombol Add")
    public void penggunaKlikTombolAdd() {
        exceptionPage.clickAddButton();
    }

    @Then("Input field baris kedua harus ditampilkan")
    public void inputFieldBarisKeduaHarusDitampilkan() {
        // Selenium otomatis menunggu proses "Loading..." selesai di baris ini
        WebElement row2Input = exceptionPage.waitForRow2InputField();

        // Melakukan verifikasi akhir bahwa elemen benar-benar aktif dan muncul di layar
        Assert.assertNotNull("Gagal: Elemen input baris ke-2 tidak berhasil dimuat di DOM!", row2Input);
        Assert.assertTrue("Gagal: Input field baris ke-2 ada di DOM tapi tidak tampil di layar!", row2Input.isDisplayed());
    }

    @And("Pengguna memasukkan teks {string} ke input field baris kedua")
    public void penggunaMemasukkanTeksKeInputFieldBarisKedua(String textInput) {
        exceptionPage.typeIntoRow2Input(textInput);
    }

    @And("Pengguna klik tombol Save di baris kedua")
    public void penggunaKlikTombolSaveDiBarisKedua() {
        exceptionPage.clickSaveButtonRow2();
    }

    @Then("Pesan konfirmasi teks tersimpan harus ditampilkan")
    public void pesanKonfirmasiTeksTersimpanHarusDitampilkan() {
        String actualMessage = exceptionPage.getConfirmationMessage();
        // Memastikan teks konfirmasi sukses muncul di layar (misal: "Row 2 was saved")
        Assert.assertTrue("Gagal: Pesan konfirmasi tidak sesuai atau kosong! Aktual: " + actualMessage,
                actualMessage.contains("saved"));
    }

    @When("Pengguna klik tombol edit")
    public void penggunaKlikTombolEdit() {
        exceptionPage.clickEditButton1();
    }

    @And("Pengguna memasukkan teks {string} ke input field baris pertama")
    public void penggunaMemasukkanTeksKeInputFieldBarisPertama(String textInput) {
        exceptionPage.typeIntoRow1Input(textInput);
    }


    @And("Pengguna klik tombol save di baris pertama")
    public void penggunaKlikTombolSaveDiBarisPertama() {
        exceptionPage.clickSaveButtonRow1();
    }

    @And("Pengguna menunggu baris kedua dengan batas waktu ketat {int} detik")
    public void penggunaMenungguBarisKeduaDenganBatasWaktuKetatDetik(int seconds) {
        this.isTimeoutTriggered = exceptionPage.verifyTimeoutOnRow2WithThreeSecondsWait();
    }

    @Then("Input field baris kedua harus gagal ditampilkan karena batas waktu habis")
    public void inputFieldBarisKeduaHarusGagalDitampilkanKarenaBatasWaktuHabis() {
        Assert.assertTrue("Gagal: Baris ke-2 malah berhasil muncul atau tidak memicu TimeoutException dalam 2 detik!",
                isTimeoutTriggered);
    }

    @And("Pengguna menemukan elemen teks instruksi")
    public void penggunaMenemukanElemenTeksInstruksi() {
        this.savedInstructionsElement = exceptionPage.getInstructionsElement();
        Assert.assertNotNull("Gagal: Elemen teks instruksi awal tidak ditemukan!", savedInstructionsElement);
    }

    @Then("Elemen teks instruksi harus sudah tidak ditampilkan di halaman")
    public void elemenTeksInstruksiHarusSudahTidakDitampilkanDiHalaman() {
        boolean isStale = exceptionPage.waitForInstructionToDisappear(savedInstructionsElement);
        Assert.assertTrue("Gagal: Elemen teks instruksi masih terpasang di DOM!", isStale);
    }
}
