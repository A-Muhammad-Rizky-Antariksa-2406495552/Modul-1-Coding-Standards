### Muhammad Rizky Antariksa
### 2406495552
### Progjut A

## Reflection 1
## Clean Code Principles yang Diterapkan

### 1. Meaningful Names
Saya menggunakan nama yang jelas dan deskriptif untuk class, method, dan variable. Misalnya `ProductController`, `createProduct()`, `validateProduct()`, dan `productName`. Semua nama langsung menjelaskan fungsinya tanpa perlu membaca implementasinya.

### 2. Functions
Setiap method hanya memiliki satu tanggung jawab (Single Responsibility Principle). Method untuk validasi terpisah dari method untuk create atau edit. Panjang method juga dijaga agar tidak terlalu panjang dan mudah dibaca.

### 3. Don't Repeat Yourself (DRY)
Saya melakukan refactoring untuk menghilangkan code duplication. Awalnya validasi input ada di dua tempat (create dan edit), sekarang sudah di-extract ke satu method `validateProduct()` yang bisa dipanggil dari mana saja. Magic strings juga sudah diubah jadi constants.

### 4. Error Handling
Ada pengecekan null sebelum melakukan operasi edit atau delete. Input dari user juga divalidasi (nama tidak boleh kosong, quantity tidak boleh negatif). Error message ditampilkan ke user jika terjadi kesalahan.

### 5. Comments
Code sudah cukup self-explanatory sehingga tidak perlu banyak comment. Nama method dan variable yang jelas sudah cukup untuk menjelaskan maksud code.

---

## Secure Coding Practices yang Diterapkan

### 1. Input Data Validation
Semua input dari user divalidasi sebelum diproses. Product name harus diisi dan di-trim untuk menghindari input whitespace-only. Product quantity harus positif (tidak boleh negatif).

### 2. Output Data Encoding
Thymeleaf otomatis melakukan HTML escaping untuk mencegah XSS attack. Semua data yang ditampilkan ke user sudah aman dari injeksi script berbahaya.

### 3. Delete Confirmation
Untuk operasi delete yang bersifat permanen, ada konfirmasi JavaScript yang muncul sebelum data benar-benar dihapus. Ini mencegah user menghapus data secara tidak sengaja.

### 4. Null Safety
Sebelum melakukan operasi pada product, selalu dicek terlebih dahulu apakah product tersebut ada di database. Ini mencegah error NullPointerException.

---

## Kekurangan dan Improvement yang Bisa Dilakukan Kedepannya

### 1. Authentication & Authorization
Aplikasi saat ini tidak memiliki sistem login. Siapa saja bisa mengakses, mengedit, dan menghapus product. Seharusnya ditambahkan Spring Security untuk authentication dan role-based access control.

### 2. UUID Generation
Awalnya saya lupa menambahkan UUID generator saat create product, sehingga product ID tidak ter-generate dan menyebabkan error saat delete. Setelah diperbaiki dengan menambahkan UUID.randomUUID(), masalah teratasi.

### 3. Delete Method (GET vs POST)
Saya menggunakan GET request untuk delete, padahal best practice adalah menggunakan POST karena delete mengubah state aplikasi. Alasannya karena tanpa Spring Security, POST method menyebabkan error. Untuk production, seharusnya pakai POST dengan CSRF protection.

### 4. Error Messages
Error message masih generic dan dalam bahasa Inggris. Seharusnya dibuat lebih user-friendly, spesifik, dan dalam bahasa Indonesia. Tampilan error juga bisa diperbaiki dengan alert box yang lebih menarik.

### 5. Logging
Belum ada logging untuk tracking operasi CRUD. Seharusnya ditambahkan logger untuk mencatat siapa melakukan apa dan kapan, terutama untuk operasi yang sensitif seperti delete.

### 6. Unit Testing
Belum ada unit test sama sekali. Seharusnya dibuat unit test untuk Repository, Service, dan Controller layer untuk memastikan semua fungsi berjalan dengan benar dan mencegah regression bug.

---

## Git Workflow yang Diterapkan

Saya menggunakan Feature Branch Workflow dengan struktur:
- Branch `main`: Base code dengan fitur list product
- Branch `edit-product`: Fitur edit product saja
- Branch `delete-product`: Fitur delete product saja

Setiap fitur dikerjakan di branch terpisah, kemudian di-merge ke main setelah selesai. Ini membuat development lebih terorganisir dan mudah di-track. Saat merge terjadi conflict karena kedua branch sama-sama mengubah file yang sama, conflict di-resolve dengan memilih versi yang paling sesuai.

---

## Kesimpulan

Secara keseluruhan, code sudah menerapkan clean code principles seperti meaningful names, DRY, proper error handling, dan input validation. Untuk secure coding, sudah ada validasi input, output encoding, dan null safety. Namun masih ada beberapa kekurangan terutama di aspek security (belum ada authentication/authorization) dan quality assurance (belum ada unit testing). Untuk improvement selanjutnya, prioritas utama adalah menambahkan Spring Security dan unit testing.

---

## Reflection 2
### 1. Pengalaman Menulis Unit Test dan Code Coverage

Setelah menulis unit test, saya merasa lebih yakin terhadap perilaku kode yang saya buat karena fungsi-fungsi utama sudah diverifikasi secara otomatis. Jumlah unit test dalam satu class tidak memiliki angka pasti, namun idealnya setiap method publik dan setiap kemungkinan skenario penting (normal case dan edge case) memiliki test masing-masing.

Untuk memastikan unit test sudah cukup, salah satu metrik yang dapat digunakan adalah code coverage, yaitu seberapa banyak baris atau cabang kode yang dieksekusi oleh test. Code coverage membantu mengidentifikasi bagian kode yang belum teruji. Namun, 100% code coverage tidak menjamin kode bebas dari bug, karena test bisa saja hanya menguji skenario sederhana tanpa memvalidasi logika secara mendalam atau kasus ekstrem.

### 2. Clean Code pada Functional Test Suite

Jika saya membuat functional test suite baru (misalnya untuk memverifikasi jumlah item pada product list) dengan menyalin struktur dari CreateProductFunctionalTest, maka dari sisi fungsional test tersebut akan berjalan, tetapi dari sisi clean code terdapat beberapa potensi masalah.

### Masalah yang mungkin muncul:

Code Duplication (DRY violation)
Setup seperti konfigurasi Selenium, base URL, dan inisialisasi driver akan terulang di banyak class test.

Maintainability rendah
Jika ada perubahan pada setup (misalnya cara mengatur base URL atau browser), semua test suite harus diubah satu per satu.

Tight coupling antar test dan setup
Setiap class test memiliki logika setup sendiri-sendiri sehingga sulit dikelola dalam skala besar.

Perbaikan yang bisa dilakukan:

Mengekstrak logic setup yang sama ke abstract base class (misalnya BaseFunctionalTest)

Menggunakan utility/helper class untuk operasi yang sering dipakai (misalnya navigasi ke halaman tertentu)

Menjaga setiap test tetap fokus pada satu tujuan agar tetap readable dan mudah dipahami

Dengan pendekatan tersebut, kualitas kode test tetap terjaga meskipun jumlah functional test bertambah.

## Kesimpulan

Unit test dan functional test sangat membantu meningkatkan kepercayaan terhadap kualitas aplikasi. Code coverage berguna sebagai indikator awal, tetapi bukan jaminan bebas bug. Untuk functional test, menjaga kebersihan kode sama pentingnya dengan menjaga kebersihan kode aplikasi, terutama dengan menghindari duplikasi dan meningkatkan maintainability melalui reuse dan struktur yang baik.