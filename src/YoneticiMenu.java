import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class YoneticiMenu {
    public static void menuGoster(Kullanici dekan) {

        while (true) {
            System.out.println("\n==============================");
            System.out.println("\t  YÖNETİCİ PANELİ");
            System.out.println("\t" + dekan.getAdSoyad());
            System.out.println("==============================");
            System.out.println("1- Yeni Kullanıcı Ekle (Hoca/Öğrenci)");
            System.out.println("2- Ders Ekle");
            System.out.println("3- Dersleri Listele");
            System.out.println("0- Çıkış Yap");
            System.out.print("Seçiminiz: ");
            int secim = Metod.guvenliSayiAl();

            if (secim == 0) {
                Metod.cikisYap();
                break;
            } else if (secim == 1) {
                kullaniciEkle();
            } else if (secim == 2) {
                dersEkle();
            } else if (secim == 3) {
                dersListele();
            } else {
                System.out.println("Hatalı seçim yaptınız");
            }
        }
    }

    // yeni kullanıcı ekleme
    public static void kullaniciEkle() {
        System.out.println("\n--- YENİ KULLANICI OLUŞTUR ---");
        System.out.print("Kullanıcı Adı: ");
        String kAdi = Metod.guvenliMetinAl();
        System.out.print("Parola: ");
        String parola = Metod.guvenliMetinAl();
        System.out.print("Ad Soyad: ");
        String adSoyad = Metod.guvenliMetinAl();
        System.out.print("Ünvan (Örn: Dr., Prof., Öğrenci): ");
        String unvan = Metod.guvenliMetinAl();
        System.out.print("Rol Seçiniz (1: Akademisyen, 2: Öğrenci): ");
        int rolSecim = Metod.guvenliSayiAl();

        String rol;
        if (rolSecim == 1) {
            rol = "AKADEMISYEN";
        } else {
            rol = "OGRENCI";
        }

        // veritabanına kayıt
        try {
            Connection connection = VeriTabani.getConnection();
            String sql = "INSERT INTO USERS (username, password_hash, full_name, role_enum, title) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ifade = connection.prepareStatement(sql);
            ifade.setString(1, kAdi);
            ifade.setString(2, parola);
            ifade.setString(3, adSoyad);
            ifade.setString(4, rol);
            ifade.setString(5, unvan);

            int sonuc = ifade.executeUpdate();
            if (sonuc > 0) {
                System.out.println("✅ Kullanıcı başarıyla eklendi!");
            }
        } catch (Exception e) {
            System.out.println("❌ Hata oluştu: " + e.getMessage());
        }
    }

    // ders ekleme
    public static void dersEkle() {
        System.out.println("\n--- YENİ DERS EKLE ---");
        System.out.print("Dersin Adı: ");
        String dersAdi = Metod.guvenliMetinAl();
        System.out.print("Dersin AKTS'si: ");
        int akts = Metod.guvenliSayiAl();

        // müsait hocalaro görme
        System.out.println("\n--- MÜASİT HOCALAR ---");
        try {
            // akademisyenleri seçme
            Connection connection = VeriTabani.getConnection();
            String hocasql = "SELECT id, full_name, title FROM users WHERE role_enum = 'AKADEMISYEN'";
            PreparedStatement hocaIfade = connection.prepareStatement(hocasql);
            ResultSet hocalar = hocaIfade.executeQuery();

            while (hocalar.next()) {
                System.out.println("ID: " + hocalar.getInt("id") + " - " + hocalar.getString("title") + " " + hocalar.getString("full_name"));
            }

            System.out.println("\nSeçilen hocanın ID'sini giriniz: ");
            int hocaId = Metod.guvenliSayiAl();

            // kaydetme
            String kayitsql = "INSERT INTO courses (name, akts, teacher_id) VALUES (?, ?, ?)";
            PreparedStatement ifade = connection.prepareStatement(kayitsql);
            ifade.setString(1, dersAdi);
            ifade.setInt(2, akts);
            ifade.setInt(3, hocaId);

            int sonuc = ifade.executeUpdate();
            if (sonuc > 0) {
                System.out.println("✅ " + dersAdi + " dersi başarıyla oluşturuldu");
            }
        } catch (Exception e) {
            System.out.println("❌ Hata oluştu: " + e.getMessage());
        }
    }

    // dersleri listeleme
    public static void dersListele() {
        System.out.println("\n--- DERS LİSTESİ ---");
        try {
            Connection connection = VeriTabani.getConnection();
            String sql = "SELECT c.id, c.name, c.akts, u.full_name " + "FROM courses c " + "LEFT JOIN users u ON c.teacher_id = u.id";

            PreparedStatement ifade = connection.prepareStatement(sql);
            ResultSet sonuc = ifade.executeQuery();

            System.out.printf("%-5s %-30s %-10s %-20s\n", "ID", "Ders Adı", "AKTS", "Hoca");
            System.out.println("-----------------------------------------------------------------");

            while (sonuc.next()) {
                System.out.printf("%-5d %-30s %-10d %-20s\n",
                        sonuc.getInt("id"),
                        sonuc.getString("name"),
                        sonuc.getInt("akts"),
                        sonuc.getString("full_name"));
            }
        } catch (Exception e) {
            System.out.println("❌ Listeleme hatası: " + e.getMessage());
        }
    }
}