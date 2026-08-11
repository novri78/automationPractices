package com.automation.steps;

import com.automation.pages.TablePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class TableSteps {
    private final TablePage tablePage;
    private List<Integer> scrapedEnrollments;
    public TableSteps(){
        this.tablePage = new TablePage();
    }

    @Given("Pengguna berada di halaman table")
    public void penggunaBeradaDiHalamanTable() {
        tablePage.open();
    }

    @When("Pengguna klik filter bahasa {string}")
    public void penggunaKlikFilterBahasa(String language) {
        tablePage.selectLanguage(language);
    }

    @Then("Hanya baris data dengan bahasa {string} yang ditampilkan pada table")
    public void hanyaBarisDataDenganBahasaYangDitampilkanPadaTable(String language) {
        List<String> visibleLanguage = tablePage.getAllVisibleRowLanguages();
        System.out.println("Result table from chosen language: " + visibleLanguage);
        Assert.assertFalse("Gagal: Tidak ada data kursus yang tampil pada tabel!", visibleLanguage.isEmpty());
        for(String actualLanguage : visibleLanguage) {
            Assert.assertEquals("Gagal: Ditemukan baris data dengan bahasa lain yaitu '" + actualLanguage + "'!",
                    language, actualLanguage);
        }
        System.out.println("[SUCCESS] Verified that all " + visibleLanguage.size() + " visible rows match level: " + language);
    }

    @When("Pengguna uncheck level {string}")
    public void penggunaUncheckLevel(String levelName) {
        tablePage.uncheckLevelCheckbox(levelName);
    }

    @Then("Hanya baris data dengan level {string} yang ditampilkan pada tabel")
    public void hanyaBarisDataDenganLevelYangDitampilkanPadaTabel(String expectedLevel) {
        // Fetch all visible row strings from the Level column
        List<String> visibleLevels = tablePage.getAllVisibleRowLevels();

        // Guard rail: Verify the table did not get completely wiped out unexpectedly
        Assert.assertFalse("Gagal: Tidak ada data kursus yang tampil sama sekali pada tabel!", visibleLevels.isEmpty());

        // Validate row by row that no unauthorized levels leaked through the filter
        for (String actualLevel : visibleLevels) {
            Assert.assertEquals("Gagal: Ditemukan kebocoran data! Ada baris level '" + actualLevel + "' di dalam filter " + expectedLevel + "!",
                    expectedLevel, actualLevel);
        }
        System.out.println("[SUCCESS] Verified that all " + visibleLevels.size() + " visible rows match level: " + expectedLevel);
    }

    @Then("The table list show only min enrollments data with value minimum {int}")
    public void theTableListShowOnlyMinEnrollmentsDataWithValueMinimum(int expectedMinLimit) {
        List<Integer> actualEnrollmentsList = tablePage.getAllVisibleRowEnrollmentsAsIntegers("Enrollments");
        if (actualEnrollmentsList.isEmpty()) {
            System.out.println("[SUCCESS] Verifikasi Berhasil: Tabel kosong. Terbukti tidak ada kebocoran data di bawah " + expectedMinLimit);
            return; // Keluar dengan status sukses (Green Passed)
        }
        for (int actualEnrollmentCount : actualEnrollmentsList) {
            Assert.assertTrue("Gagal: Kebocoran data! Ditemukan nilai pendaftaran " + actualEnrollmentCount +
                            " yang kurang dari batas minimum " + expectedMinLimit + "!",
                    actualEnrollmentCount >= expectedMinLimit);
        }

        System.out.println("[SUCCESS] Checked " + actualEnrollmentsList.size() + " rows. Verified all records meet the minimum criteria of " + expectedMinLimit);
    }

    @When("User select dropdown Min Enrollments with value {string}")
    public void userSelectDropdownMinEnrollmentsWithValue(String optionText) {
        tablePage.selectMinEnrollmentsDropdown(optionText);
    }

    @Then("Verify only {string} {string} courses with minimum {int} enrollments are visible")
    public void verifyOnlyCoursesWithMinimumEnrollmentsAreVisible(String expectedLang, String expectedLevel, int expectedMinLimit) {
        List<Map<String, Object>> visibleRows = tablePage.getVisibleRowDetails();
        if (visibleRows.isEmpty()) {
            System.out.println("[SUCCESS] Filter combination matches zero active elements. Zero leakage verified.");
            return;
        }
        for (int i = 0; i < visibleRows.size(); i++) {
            Map<String, Object> row = visibleRows.get(i);

            String actualLang = (String) row.get("language");
            String actualLevel = (String) row.get("level");
            int actualEnrollments = (int) row.get("enrollments");

            System.out.println("[DEBUG] Validating Row #" + (i + 1) + " -> Lang: " + actualLang + " | Level: " + actualLevel + " | Enrollments: " + actualEnrollments);

            // Assert 1: Confirm accurate Language filtering
            Assert.assertEquals("Gagal: Kebocoran Bahasa! Mengharapkan '" + expectedLang + "' tetapi menemukan '" + actualLang + "'!",
                    expectedLang, actualLang);

            // Assert 2: Confirm accurate Level selection
            Assert.assertEquals("Gagal: Kebocoran Tingkatan Level! Mengharapkan '" + expectedLevel + "' tetapi menemukan '" + actualLevel + "'!",
                    expectedLevel, actualLevel);

            // Assert 3: Confirm accurate Enrollment threshold mathematics
            Assert.assertTrue("Gagal: Kebocoran Data Limit! Nilai pendaftaran " + actualEnrollments + " di bawah batas minimal " + expectedMinLimit + "!",
                    actualEnrollments >= expectedMinLimit);
        }

        System.out.println("[SUCCESS] Checked " + visibleRows.size() + " active records. All rows match: " + expectedLang + " + " + expectedLevel + " + (≥ " + expectedMinLimit + ")");
    }

    @Then("Tombol Reset harus ditampilkan di layar")
    public void tombolResetHarusDitampilkanDiLayar() {
        Assert.assertTrue("Gagal: Tombol Reset tidak muncul setelah filter diubah!", tablePage.isResetButtonDisplayed());
    }

    @When("Pengguna klik tombol Reset")
    public void penggunaKlikTombolReset() {
        tablePage.clickResetButton();
    }

    @Then("Pengaturan filter harus kembali ke kondisi default")
    public void pengaturanFilterHarusKembaliKeKondisiDefault() {
        Assert.assertTrue("Gagal: Filter bahasa tidak kembali ke 'Any'!", tablePage.isLanguageDefaultedToAny());
        Assert.assertTrue("Gagal: Tidak semua level kembali dicentang!", tablePage.areAllLevelsChecked());
        String minEnrollmentText = tablePage.getSelectedMinEnrollmentsText();
        Assert.assertTrue("Gagal: Min enrollments tidak kembali ke 'Any'! Aktual: " + minEnrollmentText,minEnrollmentText.equalsIgnoreCase("Any"));
    }

    @And("Tombol Reset harus disembunyikan dan seluruh baris tabel terlihat kembali")
    public void tombolResetHarusDisembunyikanDanSeluruhBarisTabelTerlihatKembali() {
        Assert.assertFalse("Gagal: Tombol Reset masih terlihat di layar!",tablePage.isResetButtonDisplayed());
        int totalRows = tablePage.getTotalRowsCount();
        int visibleRows = tablePage.getVisibleRowsCount();
        Assert.assertEquals("Gagal: Jumlah baris yang terlihat (" + visibleRows +
                        ") tidak sama dengan total data tabel semula (" + totalRows + ")!",
                totalRows, visibleRows);
    }

    @When("User select dropdown Sort By with value {string}")
    public void userSelectDropdownSortByWithValue(String optionText) {
        tablePage.selectDropByDropdown(optionText);
    }

    @Then("Verify visible rows are ordered from smallest to largest enrollment")
    public void verifyVisibleRowsAreOrderedFromSmallestToLargestEnrollment() {
        // Scrape and parse the cleaned integer column elements from the active viewport
        this.scrapedEnrollments = tablePage.getVisibleEnrollmentsDropByAsIntegers();

        System.out.println("[DEBUG] Verification Matrix Sequence: " + this.scrapedEnrollments);
        Assert.assertFalse("Gagal: Tabel kosong, tidak ada baris data urutan untuk divalidasi!",
                this.scrapedEnrollments.isEmpty());

        // Assert strict mathematical ascending sequence stability
        for (int i = 0; i < this.scrapedEnrollments.size() - 1; i++) {
            int currentVal = this.scrapedEnrollments.get(i);
            int nextVal = this.scrapedEnrollments.get(i + 1);

            Assert.assertTrue("Gagal: Kesalahan urutan numerik! Nilai '" + currentVal +
                            "' tidak boleh lebih besar daripada '" + nextVal + "'!",
                    currentVal <= nextVal);
        }
        System.out.println("[SUCCESS] Ascending numerical sort order validated cleanly.");
    }

    @And("Verify numbers with commas sort correctly")
    public void verifyNumbersWithCommasSortCorrectly() {
        if (this.scrapedEnrollments == null || this.scrapedEnrollments.isEmpty()) {
            throw new IllegalStateException("CRITICAL: Cannot verify formatting sort accuracy because the enrollment list data state is missing!");
        }

        // SPECIFIC COUNTER-LEXICOGRAPHICAL CHECK:
        // If the sort was bugged as a String, "12500" would incorrectly appear BEFORE "2000".
        // We look for any high numbers (like 10k+) and ensure they are positioned after smaller values.
        for (int i = 0; i < this.scrapedEnrollments.size() - 1; i++) {
            int current = this.scrapedEnrollments.get(i);
            int next = this.scrapedEnrollments.get(i + 1);

            // Lexicographical bug proofing condition trigger check
            if (String.valueOf(current).startsWith("1") && String.valueOf(next).startsWith("2")) {
                // If 'current' is 12500 and 'next' is 2000, standard string comparisons would allow it,
                // but true numeric comparisons will catch it and fail if the list is out of mathematical order.
                Assert.assertTrue("Gagal: Deteksi Bug Lexicographical! Angka berformat koma '" + current +
                        "' terurut secara teks di depan '" + next + "'!", current < next);
            }
        }
        System.out.println("[SUCCESS] Comma-separated numerical ordering verified. No lexicographical formatting leaks detected.");
    }
}
