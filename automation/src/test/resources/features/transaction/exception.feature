Feature: Exception Feature
  Background:
    Given Pengguna berada di halaman exceptions
  Scenario: NoSuchElementException
    When Pengguna klik tombol Add
    Then Input field baris kedua harus ditampilkan
  Scenario: ElementNotInteractableException
    When Pengguna klik tombol Add
    And Pengguna memasukkan teks "Makanan Favorit" ke input field baris kedua
    And Pengguna klik tombol Save di baris kedua
    Then Pesan konfirmasi teks tersimpan harus ditampilkan
  Scenario: InvalidElementStateException
    When Pengguna klik tombol edit
    And Pengguna memasukkan teks "Ketoprak" ke input field baris pertama
    And Pengguna klik tombol save di baris pertama
    Then Pesan konfirmasi teks tersimpan harus ditampilkan
  Scenario: TimeoutException
    Given Pengguna berada di halaman exceptions
    When Pengguna klik tombol Add
    And Pengguna menunggu baris kedua dengan batas waktu ketat 2 detik
    Then Input field baris kedua harus gagal ditampilkan karena batas waktu habis
  Scenario: StaleElementReferenceException
    Given Pengguna berada di halaman exceptions
    And Pengguna menemukan elemen teks instruksi
    When Pengguna klik tombol Add
    Then Elemen teks instruksi harus sudah tidak ditampilkan di halaman