Feature: Login Feature
  Background:
    Given Pengguna berada di halaman login
  Scenario: Positive LogIn test with assert
    When Pengguna memasukkan username "student"
    And Pengguna memasukkan password "Password123"
    And Pengguna klik tombol Login
    Then URL halaman baru harus mengandung "practicetestautomation.com/logged-in-successfully/"
    And Teks halaman baru harus mengandung kata "Congratulations" atau "successfully logged in"
    And Tombol Log out harus ditampilkan di halaman baru
  Scenario: Success Login with valid credential
    When Pengguna memasukkan username "student"
    And Pengguna memasukkan password "Password123"
    And Pengguna klik tombol Login
    Then Login berhasil
  Scenario: Logout after success Login
    When Pengguna memasukkan username "student"
    And Pengguna memasukkan password "Password123"
    And Pengguna klik tombol Login
    And Pengguna klik tombol Logout
    Then Pengguna kembali ke halaman Login
  Scenario: Failed login because invalid username
    When Pengguna memasukkan username "automation"
    And Pengguna memasukkan password "Password123"
    And Pengguna klik tombol Login
    Then Pesan error username tampil
  Scenario: Failed login because invalid password
    When Pengguna memasukkan username "student"
    And Pengguna memasukkan password "Password321"
    And Pengguna klik tombol Login
    Then Pesan error password tampil
  Scenario: Login without input password
    When Pengguna memasukkan username "student"
    And Pengguna memasukkan password ""
    And Pengguna klik tombol Login
    Then Pesan error password tampil


#  Scenario: Login Berhasil
#    When Pengguna memasukkan username "student" dan password "Password123"
#    And Pengguna menekan tombol klik login
#    Then Pengguna otomatis diarahkan ke halaman dashboard


