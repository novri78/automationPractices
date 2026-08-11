Feature: Detail Table Feature
  Background:
    Given Pengguna berada di halaman table
  Scenario: Language filter
    When Pengguna klik filter bahasa "Java"
    Then Hanya baris data dengan bahasa "Java" yang ditampilkan pada table
  Scenario: Level filter
    When Pengguna uncheck level "Intermediate"
    And Pengguna uncheck level "Advanced"
    Then Hanya baris data dengan level "Beginner" yang ditampilkan pada tabel
  Scenario: Filter choose with value
    When User select dropdown Min Enrollments with value "10,000+"
    Then The table list show only min enrollments data with value minimum 10000
  Scenario: Combined filters
    When Pengguna klik filter bahasa "Python"
    And Pengguna uncheck level "Intermediate"
    And Pengguna uncheck level "Advanced"
    And User select dropdown Min Enrollments with value "10,000+"
    Then Verify only "Python" "Beginner" courses with minimum 10000 enrollments are visible
  Scenario: Reset button visibility and behavior
    When Pengguna klik filter bahasa "Python"
    Then Tombol Reset harus ditampilkan di layar
    When Pengguna klik tombol Reset
    Then Pengaturan filter harus kembali ke kondisi default
    And Tombol Reset harus disembunyikan dan seluruh baris tabel terlihat kembali
  Scenario: Sort by Enrollments (ascending, numeric)
    When User select dropdown Sort By with value "Enrollments"
    Then Verify visible rows are ordered from smallest to largest enrollment
    And Verify numbers with commas sort correctly