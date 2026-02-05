# 🎓 Öğrenci Bilgi Sistemi (OBS)

Bu proje, Java (JDK 25) ve MySQL (MariaDB) kullanılarak geliştirilmiş konsol tabanlı bir **Öğrenci Bilgi Sistemi Otomasyonu**dur.

Proje; Yönetici, Akademisyen ve Öğrenci olmak üzere 3 farklı kullanıcı rolünü destekler ve veritabanı bağlantısı ile gerçek zamanlı veri işleme yeteneğine sahiptir.

## 🚀 Özellikler

Sistem, kullanıcı rollerine göre ayrılmış menülerden oluşur:

### 1. 👨‍💼 Yönetici (Admin) Modülü
- **Kullanıcı Ekleme:** Sisteme yeni Akademisyen veya Öğrenci tanımlayabilir.
- **Ders Ekleme:** Dersin adını, kredisini ve dersi verecek hocayı belirleyerek ders açabilir.
- **Listeleme:** Sistemdeki kayıtlı dersleri ve hocaları görüntüleyebilir.

### 2. 👨‍🏫 Akademisyen Modülü
- **Ders Listeleme:** Sadece kendi verdiği dersleri görüntüler.
- **Güvenli Not Girişi:**
    - Vize ve Final notlarını girer.
    - Sistem başarı notunu (Ortalama) otomatik hesaplar.
    - **Güvenlik Kontrolü:** Hoca, sadece kendine ait derslere ve o dersi alan öğrencilere not verebilir.

### 3. 👨‍🎓 Öğrenci Modülü
- **Ders Seçimi:** Açılan derslerden seçim yaparak kaydını oluşturur.
- **Transkript Görüntüle:**
    - Aldığı dersleri, Vize/Final notlarını ve Ortalamasını görür.
    - **Durum Kontrolü:** Sistem, ortalamaya göre "GEÇTİ" veya "KALDI" durumunu otomatik gösterir.

---

## 🛠️ Teknolojiler

- **Dil:** Java (JDK 25)
- **Veritabanı:** MySQL / MariaDB
- **Bağlantı:** JDBC
- **IDE:** IntelliJ IDEA / Eclipse

---

## ⚙️ Kurulum ve Çalıştırma

Projeyi çalıştırmak için sırasıyla aşağıdaki adımları uygulayın:

### Adım 1: Veritabanı Kurulumu

Proje klasöründeki `database.sql` dosyasını veritabanına yüklemeniz gerekmektedir.

- HeidiSQL veya MySQL Workbench uygulamasını açın.
- `database.sql` dosyasını içeri aktarın (Load SQL file).
- Dosyayı çalıştırın (Run/Execute).
- *Bu işlem tabloları ve varsayılan Admin kullanıcısını oluşturacaktır.*

### Adım 2: Şifre Ayarı

Güvenlik gereği veritabanı şifresi kod içerisinden kaldırılmıştır. İndirdikten sonra şu ayarı yapmalısınız:

- `src/Veritabani.java` dosyasını açın.
- Aşağıdaki satırı bulun:

```java
DriverManager.getConnection("jdbc:mysql://localhost:3306/obs_db", "root", "SIFRE_BURAYA");
```

- `SIFRE_BURAYA` yazan yere kendi yerel veritabanı şifrenizi yazın (Örn: "1234").

### Adım 3: Başlatma

- `Main.java` dosyasını çalıştırın ve konsol menüsünü takip edin.

---

## 🔑 Varsayılan Giriş Bilgileri

Veritabanı kurulduğunda aşağıdaki yönetici hesabı otomatik tanımlanır:

- **Kullanıcı Adı:** `admin`
- **Şifre:** `1234`

*Not: Sisteme admin ile giriş yaptıktan sonra "Kullanıcı Ekle" menüsünden yeni Akademisyen ve Öğrenci hesapları oluşturabilirsiniz.*

---

## 👨‍💻 Geliştirici

**Muhammed Cengiz** - [GitHub Profilim](https://github.com/mhmdcngz)