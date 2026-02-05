import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AkademisyenMenu {
    public static void menuGoster(Kullanici akademisyen) {

        while (true) {
            System.out.println("\n==============================");
            System.out.println("\t  AKADEMİSYEN PANELİ");
            System.out.println("\t" + akademisyen.getAdSoyad());
            System.out.println("==============================");
            System.out.println("1- Derslerimi Listele");
            System.out.println("2- Not Girişi Yap");
            System.out.println("0- Çıkış Yap");
            System.out.print("Seçiminiz: ");
            int secim = Metod.guvenliSayiAl();

            if (secim == 0) {
                Metod.cikisYap();
                break;
            } else if (secim == 1) {
                dersListele(akademisyen);
            } else if (secim == 2) {
                notGiris(akademisyen);
            } else {
                System.out.println("Hatalı seçim yaptınız");
            }
        }
    }

    public static void dersListele(Kullanici hoca) {
        System.out.println("\n--- VERDİĞİNİZ DERSLER ---");
        try {
            Connection connection = VeriTabani.getConnection();
            String sql = "SELECT * FROM courses WHERE teacher_id = ?";

            PreparedStatement ifade = connection.prepareStatement(sql);
            ifade.setInt(1, hoca.getId());
            ResultSet sonuc = ifade.executeQuery();

            while (sonuc.next()) {
                System.out.println("ID: " + sonuc.getInt("id") + " | Ders: " + sonuc.getString("name"));
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static void notGiris(Kullanici hoca) {
        dersListele(hoca);

        System.out.print("\nNot girmek istediğiniz dersin ID'sini giriniz: ");
        int dersId = Metod.guvenliSayiAl();


        try {
            Connection connection = VeriTabani.getConnection();

            // hoca kontrol
            String kotrolSql = "SELECT id FROM courses WHERE id = ? AND teacher_id = ?";
            PreparedStatement kontrolIfade = connection.prepareStatement(kotrolSql);
            kontrolIfade.setInt(1, dersId);
            kontrolIfade.setInt(2, hoca.getId());

            ResultSet kontrolSonuc = kontrolIfade.executeQuery();

            if (!kontrolSonuc.next()) {
                System.out.println("\n🛑 GÜVENLİK UYARISI: Bu ders size ait değil veya böyle bir ders yok!");
                return;
            }

            // dersi alan öğrencileri gösterme
            System.out.println("\n--- DERSİ ALAN ÖĞRENCİLER ---");
            String sql = "SELECT u.id, u.full_name, t.midterm_score, t.final_score, t.average " + "FROM transcripts t " + "JOIN users u ON t.student_id = u.id " + "WHERE t.course_id = ? ";

            PreparedStatement ifade = connection.prepareStatement(sql);
            ifade.setInt(1, dersId);
            ResultSet sonuc = ifade.executeQuery();

            boolean ogrenci = false;
            while (sonuc.next()) {
                ogrenci = true;

                // sıfır yerine girilmedi yaz
                String vize = (sonuc.getObject("midterm_score") == null) ? "Girilmedi" : String.valueOf(sonuc.getInt("midterm_score"));
                String finall = (sonuc.getObject("final_score") == null) ? "Girilmedi" : String.valueOf(sonuc.getInt("final_score"));

                System.out.println("Öğr ID: " + sonuc.getInt("id") + " | İsim: " + sonuc.getString("full_name") + " | Vize: " + vize + " | Final: " + finall + " | Ort: " + sonuc.getDouble("average"));
            }

            // öğrenci kontrol
            System.out.print("\nNot vermek istediğiniz öğrencinin ID'si: ");
            int ogrenciId = Metod.guvenliSayiAl();

            String ogrenciKontrolSql = "SELECT id FROM transcripts WHERE student_id = ? AND course_id = ?";            PreparedStatement ogrenciKontrol = connection.prepareStatement(ogrenciKontrolSql);
            ogrenciKontrol.setInt(1, ogrenciId);
            ogrenciKontrol.setInt(2, dersId);

            if (!ogrenciKontrol.executeQuery().next()) {
                System.out.println("\n🛑 HATA: Girilen ID'ye sahip öğrenci bu dersi almıyor!");
                return;
            }

            if (!ogrenci) {
                System.out.println("❌ Bu dersi alan öğrenci yok");
                return;
            }

            // not girme
            System.out.print("Vize Notu (0-100): ");
            int vizeNotu = Metod.notAlma();
            System.out.print("Final Notu (0-100): ");
            int finalNotu = Metod.notAlma();

            double ortalama = (vizeNotu * 0.4) + (finalNotu * 0.6);
            boolean gecme = (ortalama >= 50) && (finalNotu >= 50);

            // kaydetme
            String guncellemeSql = "UPDATE transcripts SET midterm_score = ?, final_score = ?, average = ?, is_passed = ? WHERE student_id = ? AND course_id = ?";
            PreparedStatement guncelIfade = connection.prepareStatement(guncellemeSql);
            guncelIfade.setInt(1, vizeNotu);
            guncelIfade.setInt(2, finalNotu);
            guncelIfade.setDouble(3, ortalama);
            guncelIfade.setBoolean(4, gecme);
            guncelIfade.setInt(5, ogrenciId);
            guncelIfade.setInt(6, dersId);

            int kayitSayi = guncelIfade.executeUpdate();
            if (kayitSayi > 0) {
                System.out.println("✅ Notlar kaydedildi! Ortalama: " + ortalama + " -> " + (gecme ? "GEÇTİ" : "KALDI"));
            } else {
                System.out.println("❌ Hata: Öğrenci veya ders bulunamadı.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}