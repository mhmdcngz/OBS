# 🎓 Gelişmiş Öğrenci Bilgi Sistemi (OBS)

Bu proje, Java ve MySQL (MariaDB) kullanılarak geliştirilmiş, **gerçek dünya üniversite notlandırma senaryolarını (Çan Eğrisi, T-Skoru, Şartlı Geçiş)** matematiksel olarak kusursuz bir şekilde simüle eden konsol tabanlı bir Öğrenci Bilgi Sistemi (OBS) otomasyonudur.

## 🚀 Öne Çıkan Özellikler

Sistem, klasik öğrenci-öğretmen ilişkilerinin yanı sıra karmaşık iş mantıklarını barındırır:

### 📊 Dinamik Notlandırma Algoritması (Projenin Kalbi)
Sistem, bir derse kayıtlı öğrenci sayısına göre otomatik karar verir:
* **Sabit Skala (Mutlak Sistem):** Öğrenci sayısı 20 ve altındaysa, standart sabit üniversite not skalası (90=AA, 50=DD) uygulanır.
* **İstatistiksel Çan Eğrisi:** Öğrenci sayısı 20'yi aştığı an sistem otomatik olarak Z-Skoru ve T-Skoru hesaplamasına geçer.
  * Z-Skoru: Z = (Öğrenci Notu - Sınıf Ortalaması) / Standart Sapma
  * T-Skoru: T = 10 * Z + 50 formülü ile T-Skoru hesaplanır ve harf notuna dönüştürülür.
* **Uç Durum (Edge Case) Korumaları:**
  * **Sıfıra Bölme Koruması:** Sınıftaki herkes aynı notu alırsa (Standart Sapma = 0), sistem çökmez; otomatik olarak Mutlak Sisteme döner.
  * **Negatif Çarpıklık Kalkanı:** Sınav çok kolaysa ve sınıf ortalaması çok yüksekse (Örn: 95), 85 alan bir öğrencinin çan eğrisi kurbanı olup kalmasını engellemek için "Başarı Kalkanı" devreye girer (80 üstü alan minimum BB alır).
* **Şartlı Geçiş (DC/DD):** Öğrenci Çan Eğrisi veya Sabit Skala ile DC/DD alırsa, sistem arka planda öğrencinin **Genel Not Ortalamasını (GNO)** hesaplar. GNO 60'ın altındaysa (kredisi yetmiyorsa) harf notunu FF'e çekerek öğrenciyi dersten bırakır.
* **Final Barajı:** Ortalaması ne olursa olsun, Final sınavından 50'nin altında alan öğrenci doğrudan kalır.

### 🛡️ Siber Güvenlik ve Mimari
* **Çevresel Değişkenler (.env):** Veritabanı şifresi gibi hassas veriler kodun içine gömülmek (hardcoded) yerine `.env` dosyası üzerinden güvenli bir şekilde okunur.
* **Rol Tabanlı Erişim (RBAC):** Yönetici (Dekan), Akademisyen ve Öğrenci olmak üzere 3 farklı yetki seviyesi bulunur. Hocalar sadece kendi derslerine ve o dersi alan öğrencilere not girebilir.

## 🛠️ Teknolojiler

- **Dil:** Java (JDK 25)
- **Veritabanı:** MySQL / MariaDB
- **Bağlantı:** JDBC
- **Güvenlik:** Custom Properties Loader (.env)
- **IDE:** IntelliJ IDEA

---

## ⚙️ Kurulum ve Çalıştırma

Projeyi kendi bilgisayarınızda çalıştırmak için sırasıyla şu adımları izleyin:

### 1. Veritabanı Kurulumu
Proje dizininde bulunan `database.sql` dosyasını MySQL/MariaDB sunucunuza aktarın. Bu işlem tabloları ve varsayılan verileri oluşturacaktır.

### 2. Çevresel Değişkenlerin Ayarlanması (ÖNEMLİ)
Güvenlik gereği veritabanı şifresi GitHub'a yüklenmemiştir. Projeyi çalıştırmadan önce:
1. Projenin **ana dizininde** (src klasörünün hemen dışında) `.env` adında yeni bir dosya oluşturun.
2. İçine kendi veritabanı şifrenizi şu formatta yazıp kaydedin:

   DB_PASS=sizin_sifreniz

### 3. Projeyi Başlatma
`src/Main.java` dosyasını çalıştırın ve konsol menüsünü takip edin.

---

## 🔑 Varsayılan Giriş Bilgileri

Sistemi test etmek için veritabanında hazır gelen hesaplar:

- **Yönetici (Dekan)**
  - Kullanıcı Adı: `erenmente`
  - Şifre: `3434`
- **Akademisyen**
  - Kullanıcı Adı: `omrdgn`
  - Şifre: `4646`
- **Öğrenci**
  - Kullanıcı Adı: `mhmdcngz`
  - Şifre: `2323`

---

## 👨‍💻 Geliştirici

**Muhammed Cengiz** - [GitHub Profilim](https://github.com/mhmdcngz)