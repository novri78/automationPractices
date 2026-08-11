package com.automation.pages;

import com.automation.configuration.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablePage extends BasePage {

    private final By tblRows = By.xpath("//table[@id='courses_table']/tbody/tr");
    private final By tblLanguageCells = By.xpath("//table[@id='courses_table']/tbody/tr/td[3]");
    private final By tblLevelCells = By.xpath("//table[@id='courses_table']/tbody/tr/td[4]");
    //private final By contentLanguage = By.xpath("//tr[contains(.,'Loading')]");
    //private final By tblEnrollmentCells = By.xpath("//table[@id='courses_table']/tbody/tr/td[2]");
    // private final By tblVisibleEnrollmentCells = By.xpath(
    //         "//table[@id='courses_table']/tbody/tr[not(contains(@style, 'display: none'))]/td[5]"
    // );
    // MULTI-LOCATOR FALLBACK SYSTEM FOR MIN ENROLLMENTS TEXT
    private final By[] minEnrollmentLabelLocators = {
            By.id("min-enrollments-select"), // Standard native select element ID pattern
            By.xpath("//*[@id='enrollDropdown']"), // Custom button trigger ID container
            By.xpath("//select[contains(@id,'enroll')]"), // General select tag
            By.xpath("//button[contains(@id,'enroll')]")  // General custom button tag
    };
    private final By btnReset = By.xpath("//button[text()='Reset']");
    private final By radioLangAny = By.xpath("//input[@name='lang' and @value='Any']");
    private final By checkBoxesLevel = By.xpath("//input[@name='level']");
    private final By drpMinEnrollmentsToggle = By.id("enrollDropdown");
    private final By tblVisibleRows = By.xpath("//table[@id='courses_table']/tbody/tr[not(contains(@style, 'display: none'))]");
    private final By tblHeaders = By.xpath("//table[@id='courses_table']/thead/tr/th");
    private final By drpByToggle = By.id("sortBy");

    private By getRadioLanguageLocator(String languageName){
        return By.xpath("//input[@name='lang' and @value='" + languageName + "']");
    }
    private By getCheckboxLevelLocator(String levelName) {
        // Targets input elements under the level checkbox container
        return By.xpath("//input[@name='level' and @value='" + levelName + "']");
    }
    private By getDropdownOptionLocator(String optionText) {
        // Targets the <li> or text element matching your choice (e.g., "10,000+")
        return By.xpath("//*[@id='enrollDropdown']/ul//li[contains(text(),'" + optionText + "') or contains(.,'" + optionText + "')]");
    }
    private By getDropdownOptionLocatorDropBy(String optionText) {
        // Dynamic path covers standard option nodes or custom nested li/anchor text trees
        return By.xpath("//*[@id='sortBy']//option[text()=' " + optionText + " ' or text()='" + optionText + "'] | " +
                "//*[@id='sortBy']/following-sibling::ul//li[contains(text(),'" + optionText + "')]");
    }

    public void open(){
        driver.get(ConfigReader.get("url3"));
    }

    public void selectLanguage(String languageName) {
        By dynamicButton = getRadioLanguageLocator(languageName);
        wait.until(ExpectedConditions.elementToBeClickable(dynamicButton)).click();
    }

    public List<String> getAllVisibleRowLanguages() {
        // Mengambil semua elemen baris kolom bahasa yang terlihat
        wait.until(ExpectedConditions.visibilityOfElementLocated(tblRows));
        List<WebElement> cells = driver.findElements(tblLanguageCells);
        List<String> languagesCollected = new ArrayList<>();

        for (WebElement cell : cells) {
            if (cell.isDisplayed()) {
                languagesCollected.add(cell.getText().trim());
            }
        }
        return languagesCollected;
    }

    public void uncheckLevelCheckbox(String levelName) {
        By checkboxLocator = getCheckboxLevelLocator(levelName);
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(checkboxLocator));

        // Only click to uncheck if the checkbox is currently checked/selected
        if (checkbox.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator)).click();
            System.out.println("[INFO] Successfully unchecked level: " + levelName);
        }
    }

    public List<String> getAllVisibleRowLevels() {
        // Wait until table rows update and become visible to prevent racing empty states
        wait.until(ExpectedConditions.visibilityOfElementLocated(tblRows));

        List<WebElement> cells = driver.findElements(tblLevelCells);
        List<String> levelsCollected = new ArrayList<>();

        for (WebElement cell : cells) {
            if (cell.isDisplayed()) {
                levelsCollected.add(cell.getText().trim());
            }
        }
        return levelsCollected;
    }

    public void selectMinEnrollmentsDropdown(String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(drpMinEnrollmentsToggle)).click();
        By dynamicLocator = getDropdownOptionLocator(optionText);
        wait.until(ExpectedConditions.elementToBeClickable(dynamicLocator)).click();
        System.out.println("[INFO] Selected min enrollments dropdown option: " + optionText);
    }

    public void selectDropByDropdown(String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(drpByToggle)).click();
        By dynamicLocator = getDropdownOptionLocatorDropBy(optionText);
        wait.until(ExpectedConditions.elementToBeClickable(dynamicLocator)).click();
        System.out.println("[INFO] Selected drop by dropdown option: " + optionText);
    }

    public List<Integer> getAllVisibleRowEnrollmentsAsIntegers(String optionText) {
        List<Integer> cleanNumbers = new ArrayList<>();

        try {
            // Berikan jeda milidetik singkat agar animasi penyaringan JavaScript selesai sepenuhnya
            Thread.sleep(600);

            // 1. Cari indeks kolom secara otomatis berdasarkan teks header (misal: "Enrollments")
            List<WebElement> headers = driver.findElements(tblHeaders);
            int columnIndex = -1;
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().equalsIgnoreCase(optionText)) {
                    columnIndex = i + 1; // XPath menggunakan indeks berbasis 1
                    break;
                }
            }

            // Jika nama kolom tidak ditemukan, default kembali ke indeks 5 (safety fallback)
            if (columnIndex == -1) {
                columnIndex = 5;
            }

            // 2. Ambil semua baris yang aktif/terlihat saat ini
            List<WebElement> visibleRows = driver.findElements(tblVisibleRows);
            System.out.println("[DEBUG] Jumlah baris aktif yang ditemukan di layar: " + visibleRows.size());

            // JIKA TABEL KOSONG (Misal karena pilih 50,000+), langsung kembalikan list kosong tanpa memicu eror
            if (visibleRows.isEmpty() || (visibleRows.size() == 1 && visibleRows.getFirst().getText().contains("No records"))) {
                System.out.println("[INFO] Tabel kosong secara valid. Tidak ada data yang memenuhi kriteria filter.");
                return cleanNumbers;
            }

            // 3. Jika ada data, ambil nilai dari kolom spesifik yang telah dihitung indeksnya tadi
            for (WebElement row : visibleRows) {
                WebElement cell = row.findElement(By.xpath("./td[" + columnIndex + "]"));
                String rawText = cell.getText().trim();
                System.out.println("[DEBUG] Memproses nilai teks baris pada kolom " + optionText + ": '" + rawText + "'");

                String digitOnlyString = rawText.replaceAll("[^0-9]", "");
                if (!digitOnlyString.isEmpty()) {
                    cleanNumbers.add(Integer.parseInt(digitOnlyString));
                }
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Gagal mengeksekusi ekstraksi data tabel dinamis: " + e.getMessage());
        }

        return cleanNumbers;
    }

    public List<Map<String, Object>> getVisibleRowDetails() {
        List<Map<String, Object>> tableDataMatrix = new ArrayList<>();
        try {
            // Stability buffer to let asynchronous JavaScript filtering animations settle completely
            Thread.sleep(800);

            List<WebElement> visibleRows = driver.findElements(tblVisibleRows);
            System.out.println("[DEBUG] Active rows rendering post combined-filter: " + visibleRows.size());

            // Handle clean empty-state exceptions natively (e.g. if zero courses match the combination)
            if (visibleRows.isEmpty() || (visibleRows.size() == 1 && visibleRows.getFirst().getText().contains("No records"))) {
                return tableDataMatrix;
            }

            for (WebElement row : visibleRows) {
                Map<String, Object> rowMetadata = new HashMap<>();

                // Extract parameters based on the page's exact column coordinates
                String languageText = row.findElement(By.xpath("./td[3]")).getText().trim();
                String levelText = row.findElement(By.xpath("./td[4]")).getText().trim();
                String rawEnrollment = row.findElement(By.xpath("./td[5]")).getText().trim();

                // Standardize enrollment strings to pure numerical integers
                int enrollmentCount = 0;
                String digitOnlyString = rawEnrollment.replaceAll("[^0-9]", "");
                if (!digitOnlyString.isEmpty()) {
                    enrollmentCount = Integer.parseInt(digitOnlyString);
                }

                rowMetadata.put("language", languageText);
                rowMetadata.put("level", levelText);
                rowMetadata.put("enrollments", enrollmentCount);

                tableDataMatrix.add(rowMetadata);
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Error capturing combined row metrics: " + e.getMessage());
        }

        return tableDataMatrix;
    }

    public boolean isResetButtonDisplayed() {
        try {
            // Menggunakan durasi tunggu instan (0) khusus untuk pengecekan elemen tersembunyi
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(0));
            List<WebElement> elements = driver.findElements(btnReset);
            if(elements.isEmpty()) return false;
            return elements.getFirst().isDisplayed();
        } finally {
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        }
    }

    public void clickResetButton() {
        wait.until(ExpectedConditions.elementToBeClickable(btnReset)).click();
    }

    public boolean isLanguageDefaultedToAny() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(radioLangAny)).isSelected();
    }

    public boolean areAllLevelsChecked() {
        List<WebElement> checkboxes = driver.findElements(checkBoxesLevel);
        for (WebElement cb : checkboxes) {
            if (!cb.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public String getSelectedMinEnrollmentsText() {
        try {
            Thread.sleep(300); // Allow brief UI animation refresh window
        } catch (InterruptedException e) { e.printStackTrace(); }

        for (By locator : minEnrollmentLabelLocators) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty() && elements.getFirst().isDisplayed()) {
                    WebElement dropdown = elements.getFirst();

                    // If it's a native HTML select tag element, extract the active selected option text
                    if (dropdown.getTagName().equalsIgnoreCase("select")) {
                        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);
                        return select.getFirstSelectedOption().getText().trim();
                    }

                    // If it's a custom dropdown UI component, pull whatever visible header text it shows
                    String visibleText = dropdown.getText().trim();

                    // Filter out nested list items text if the dropdown is currently open/expanded
                    if (visibleText.contains("\n")) {
                        visibleText = visibleText.split("\n")[0].trim();
                    }

                    if (!visibleText.isEmpty()) {
                        System.out.println("[DEBUG] Found active filter text via dynamic locator: '" + visibleText + "'");
                        return visibleText;
                    }
                }
            } catch (Exception ignored) {
                // Advance to the next fallback locator structural strategy
            }
        }
        return "UNKNOWN";
    }

    public int getTotalRowsCount() {
        return driver.findElements(tblRows).size();
    }

    public int getVisibleRowsCount() {
        try {
            Thread.sleep(500); // Sinkronisasi transisi animasi filter DOM
        } catch (InterruptedException e) { e.printStackTrace(); }
        return driver.findElements(tblVisibleRows).size();
    }

    private int getColumnIndexDynamically(String columnName) {
        List<WebElement> headers = driver.findElements(tblHeaders);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().equalsIgnoreCase(columnName)) {
                return i + 1; // XPath coordinates use 1-based indexing structures
            }
        }
        // Fallback default index to Column 5 if the text scan fails
        System.out.println("[WARNING] Could not find header '" + columnName + "' dynamically. Using default fallback Column 5.");
        return 5;
    }

    public List<Integer> getVisibleEnrollmentsDropByAsIntegers() {
        List<Integer> cleanEnrollmentNumbers = new ArrayList<>();

        try {
            // 1. CRITICAL SYNC: Wait up to 10 seconds for at least one active row to render in the DOM
            wait.until(ExpectedConditions.presenceOfElementLocated(tblVisibleRows));

            // 2. STABILITY PENETRATION: Brief millisecond pause to allow JavaScript row-shuffling animation transitions to settle
            Thread.sleep(850);

            // 3. Resolve the target column coordinate dynamically (Looks up "Enrollments")
            int enrollColumnIndex = getColumnIndexDynamically("Enrollments");

            // 4. Capture all displayed rows on the screen
            List<WebElement> visibleRows = driver.findElements(tblVisibleRows);
            System.out.println("[DEBUG] Active rows rendering post-sort: " + visibleRows.size());

            // 5. Loop through the rows to extract and isolate data cells
            for (WebElement row : visibleRows) {
                // Focus strictly on the calculated Enrollments column within the current row loop
                WebElement cell = row.findElement(By.xpath("./td[" + enrollColumnIndex + "]"));
                String rawText = cell.getText().trim();

                // DATA CLEANING ENGINE: Strip out formatting commas, letters, or spaces (e.g., "12,500" -> "12500")
                String digitOnlyString = rawText.replaceAll("[^0-9]", "");

                if (!digitOnlyString.isEmpty()) {
                    int parsedInt = Integer.parseInt(digitOnlyString);
                    System.out.println("[DEBUG] Scraped raw text: '" + rawText + "' -> Saved Integer: " + parsedInt);
                    cleanEnrollmentNumbers.add(parsedInt);
                }
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Interruption encountered while parsing table data: " + e.getMessage());
        }

        return cleanEnrollmentNumbers;
    }




}
