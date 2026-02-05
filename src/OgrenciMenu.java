import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class OgrenciMenu {
    public static void menuGoster(Kullanici ogrenci) {

        while (true) {
            System.out.println("\n==============================");
            System.out.println("\t  ÖĞRENCİ PANELİ");
            System.out.println("\t" + ogrenci.getAdSoyad());
            System.out.println("==============================");
            System.out.println("1- Ders Kaydı Yap");
            System.out.println("2- Notlarımı Görüntüle (Transkript)");
            System.out.println("0- Çıkış Yap");
            System.out.print("Seçiminiz: ");
            int secim = Metod.guvenliSayiAl();

            if (secim == 0) {
                Metod.cikisYap();
                break;
            } else if (secim == 1) {
                dersKayit(ogrenci);
            } else if (secim == 2) {
                transkript(ogrenci);
            } else {
                System.out.println("Hatalı seçim yaptınız");
            }
        }
    }

    public static void dersKayit(Kullanici ogrenci) {
        System.out.println("\n--- AÇIK DERSLER ---");
        try {
            Connection connection = VeriTabani.getConnection();

            // dersleri listeleme
            String sql = "SELECT * FROM courses ";
            PreparedStatement ifade = connection.prepareStatement(sql);
            ResultSet sonuc = ifade.executeQuery();

            while (sonuc.next()) {
                System.out.println("ID: " + sonuc.getInt("id") + " | Ders: " + sonuc.getString("name") + " | AKTS: " + sonuc.getInt("akts"));
            }

            System.out.print("\nAlmak istediğiniz dersin ID'sini giriniz: ");
            int dersId = Metod.guvenliSayiAl();

            // transkripte null atma
            String sqlKayit = "INSERT INTO transcripts (student_id, course_id) VALUES (?, ?) ";
            PreparedStatement ps = connection.prepareStatement(sqlKayit);
            ps.setInt(1, ogrenci.getId());
            ps.setInt(2, dersId);
            ps.executeUpdate();
            System.out.println("✅ Ders başarıyla eklendi!");
        } catch (Exception e) {
            System.out.println("❌ Hata (Dersi zaten almış olabilirsiniz): " + e.getMessage());
        }
    }

    public static void transkript(Kullanici ogrenci) {
        System.out.println("\n--- ALINAN DERSLER VE NOTLAR ---");
        System.out.printf("%-40s %-12s %-12s %-10s %-10s\n", "Ders", "Vize", "Final", "Ort", "Durum");
        System.out.println("---------------------------------------------------------------------------------------------");
        try {
            Connection connection = VeriTabani.getConnection();

            String sql = "SELECT c.name, t.midterm_score, t.final_score, t.average, t.is_passed " + "FROM transcripts t " + "JOIN courses c ON t.course_id = c.id " + "WHERE t.student_id = ? ";

            PreparedStatement ifade = connection.prepareStatement(sql);
            ifade.setInt(1, ogrenci.getId());
            ResultSet sonuc = ifade.executeQuery();

            while (sonuc.next()) {
                int vizeNot = sonuc.getInt("midterm_score");
                String vizeYazi = sonuc.wasNull() ? "Girilmedi" : String.valueOf(vizeNot);

                int finalNot = sonuc.getInt("final_score");
                String finalYazi = sonuc.wasNull() ? "Girilmedi" : String.valueOf(finalNot);

                double ortalamaNot = sonuc.getDouble("average");
                String ortalamaYazi = sonuc.wasNull() ? "-" : String.format("%.1f", ortalamaNot);

                String durum;
                if (sonuc.wasNull()) {
                    durum = "Belirsiz";
                } else {
                    durum = sonuc.getBoolean("is_passed") ? "GEÇTİ" : "KALDI";
                }

                System.out.printf("%-40s %-12s %-12s %-10s %-10s\n", sonuc.getString("name"), vizeYazi, finalYazi, ortalamaYazi, durum);
            }

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}