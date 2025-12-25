# 🎬 Sistem Ticketing Bioskop (Aurora Cinema)
Aplikasi desktop berbasis Java Swing untuk simulasi pemesanan tiket bioskop. Aplikasi ini mencakup fitur manajemen untuk Admin (CRUD Film & Jadwal) dan antarmuka pemesanan visual untuk Guest (Pilih Kursi & Cetak Tiket).
Dibangun menggunakan Java Murni tanpa database eksternal (menggunakan Java Serialization), sehingga mudah dijalankan di mana saja tanpa konfigurasi server.

## ✨ Fitur Utama
### 👨‍💻 Panel Admin
CRUD Data Film: Tambah, Edit, dan Hapus film beserta detailnya (Judul, Harga, Durasi, Cast, Sinopsis, Gambar).
Mode Input Cepat: Fitur checkbox untuk memasukkan banyak jam tayang sekaligus tanpa mengetik ulang data film.
Reset System:
 Reset Tiket: Mengosongkan semua kursi yang terisi tanpa menghapus data film.
 Reset Database: Menghapus seluruh data film dan tiket (Factory Reset).

### 🍿 Panel Guest (Pengunjung)
Galeri Film Visual: Tampilan daftar film menggunakan kartu poster yang rapi.
Detail Film Split-View: Klik poster untuk melihat Sinopsis, Aktor, dan Durasi di panel samping.
Pilih Kursi Interaktif:
Visualisasi layar bioskop dengan efek glow.
Indikator warna kursi: Hijau/Abu (Kosong) dan Merah (Terisi).
Real-time update saat mengganti jam tayang.
Cetak Tiket: Tiket otomatis tersimpan dalam format .txt (struk fisik) dan tersimpan di riwayat aplikasi.

🛠️ Teknologi yang Digunakan
Bahasa: Java (JDK 8 atau lebih baru).
GUI: Java Swing (JFrame, JPanel, Graphics2D, GridBagLayout).
Penyimpanan Data: Native Java Serialization (.dat files) - Tanpa database SQL/MySQL.
Input Output: Java IO (File handling untuk gambar & tiket txt).

## 📂 Struktur Proyek
Berikut adalah susunan folder dan file dalam proyek ini:

```text
Sistem-Ticketing-Bioskop/
├── src/
│   └── app/
│       ├── Main.java               # File utama untuk menjalankan aplikasi
│       ├── db/
│       │   └── DataStorage.java    # Menyimpan data (User, Jadwal, Tiket) & handle file .dat
│       ├── models/
│       │   ├── User.java           # Class objek untuk Pengguna (Admin/Guest)
│       │   ├── Showtime.java       # Class objek untuk Jadwal Film & Kursi
│       │   └── Ticket.java         # Class objek untuk Tiket & Cetak Struk
│       └── ui/
│           ├── LoginFrame.java     # GUI Halaman Login
│           ├── AdminFrame.java     # GUI Dashboard Admin (CRUD Film)
│           └── GuestFrame.java     # GUI Dashboard Guest (Booking Kursi)
├── logo.png                        # (Opsional) File logo bioskop
├── data_jadwal.dat                 # File database jadwal (Otomatis dibuat sistem)
├── data_tiket.dat                  # File database tiket (Otomatis dibuat sistem)
└── README.md                       # Dokumentasi proyek
```

##🚀 Cara Menjalankan Aplikasi
Prasyarat: Pastikan Java Development Kit (JDK) sudah terinstall.
Clone/Download: Unduh source code proyek ini.
Buka di IDE: Buka proyek menggunakan IntelliJ IDEA, NetBeans, atau Eclipse.
Siapkan Gambar (Opsional):
Siapkan file gambar logo (beri nama logo.png) di root folder proyek.
Siapkan gambar poster film jika ingin mencoba fitur upload gambar.
Run: Jalankan file src/app/Main.java.

## 🔑 Akun Default
Gunakan akun berikut untuk masuk:
| Role  | Username    | Password | Akses                   |
| :---  | :---:       | ---:     | |---:                   |
| Admin | admin       | admin    | Full CRUD, Reset System |
| Guest | guest       | guest    | Booking Tiket, History  |

## 📸 Panduan Penggunaan Singkat
### 1. Masuk sebagai Admin
Login dengan username admin dan password admin.
Isi form (Judul, Harga, Durasi, Path Gambar, dll).
Masukkan Jam Tayang (contoh: 13:00).
Jika ingin memasukkan jam lain untuk film yang sama, centang "Mode Input Banyak Jam", lalu klik Simpan. Form jam akan kosong, tapi data film tetap ada.
Gunakan tombol Reset jika ingin membersihkan data.

### 2. Masuk sebagai Guest
Login dengan username guest dan password guest.
Pilih film dari daftar poster.
Pilih Jam Tayang di dropdown.
Klik kursi yang tersedia (Abu-abu). Kursi Merah berarti sudah dibooking orang lain.
Konfirmasi pemesanan. Tiket .txt akan otomatis dibuat di folder proyek.

![Absolute Cinema](https://media1.tenor.com/m/UNGgIJAFRL8AAAAC/cinema.gif)
