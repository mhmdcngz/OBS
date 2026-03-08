import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotSistemi {

    // 1. Veritabanından sadece notu girilen öğrencileri çekme
    public static List<Double> girilenNotlariGetir(Connection connection, int dersId) {
        List<Double> notlar = new ArrayList<>();
        String sorgu = "SELECT average FROM transcripts WHERE course_id = ? AND average IS NOT NULL";
        try (PreparedStatement st = connection.prepareStatement(sorgu)){
            st.setInt(1, dersId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                notlar.add(rs.getDouble("average"));
            }
        } catch (SQLException e) {
            System.out.println("Notlar çekilirken hata oluştu: " + e.getMessage());
        }
        return notlar;
    }

    // 2. Öğrencinin Genel Not Ortalamasını (GNO) AKTS'ye Göre Hesaplar
    public static double genelOrtalamaHesapla(Connection connection, int ogrenciId) {
        double toplamAgirlik = 0;
        int toplamAkts = 0;
        String sorgu = "SELECT t.average, c.akts FROM transcripts t " +
                "JOIN courses c ON t.course_id = c.id " +
                "WHERE t.student_id = ? AND t.average IS NOT NULL";
        try (PreparedStatement st = connection.prepareStatement(sorgu)) {
            st.setInt(1, ogrenciId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                double ort = rs.getDouble("average");
                int akts = rs.getInt("akts");
                toplamAgirlik += (ort * akts);
                toplamAkts += akts;
            }
        } catch (SQLException e) {
            System.out.println("GNO Hesaplanırken hata: " + e.getMessage());
        }

        if (toplamAkts == 0) return 0.0;
        return toplamAgirlik / toplamAkts;
    }

    // 3. Sabit Skala (Mutlak Sistem - 20 Kişiden Az veya Standart Sapma 0 ise)
    private static String sabitNotlar(double not) {
        if (not >= 90) return "AA";
        if (not >= 85) return "BA";
        if (not >= 80) return "BB";
        if (not >= 75) return "CB";
        if (not >= 70) return "CC";
        if (not >= 60) return "DC";
        if (not >= 50) return "DD";
        return "FF";
    }

    // 4. PROFESYONEL ÇAN EĞRİSİ (Z-Skoru ve T-Skoru Mantığı)
    private static String canEgrisi(double ogrenciNotu, List<Double> tumNotlar) {
        int n = tumNotlar.size();
        double toplam = 0;
        for (double not : tumNotlar) toplam += not;
        double ortalama = toplam / n;

        // Standart Sapma Hesaplama
        double varyansToplami = 0;
        for (double not : tumNotlar) varyansToplami += Math.pow(not - ortalama, 2);
        double stnSapma = Math.sqrt(varyansToplami / n);

        // UÇ DURUM 4 KORUMASI: Kusursuz Eşitlik (Sıfıra Bölme Hatasını Engelle)
        if (stnSapma == 0) {
            return sabitNotlar(ogrenciNotu);
        }

        // Z-Skoru ve T-Skoru Hesaplama
        double zSkoru = (ogrenciNotu - ortalama) / stnSapma;
        double tSkoru = (10 * zSkoru) + 50;

        // UÇ DURUM 2 KORUMASI: Negatif Çarpıklık (Başarı Kalkanı)
        // Eğer sınıf ortalaması 95 ise ve öğrenci 85 almışsa, T-Skoru çok düşük (örn: 10) çıkıp FF alabilir.
        // Bunu önlemek için mutlak başarı sınırı koyuyoruz: 80 üstü alan mağdur edilemez, minimum BB alır.
        if (ogrenciNotu >= 80 && tSkoru < 60) {
            tSkoru = 60;
        }

        // Üniversitelerin Standart T-Skoru Harf Tablosu
        if (tSkoru >= 70) return "AA";
        if (tSkoru >= 65) return "BA";
        if (tSkoru >= 60) return "BB";
        if (tSkoru >= 55) return "CB";
        if (tSkoru >= 50) return "CC";
        if (tSkoru >= 45) return "DC";
        if (tSkoru >= 40) return "DD";
        return "FF";
    }

    // 5. ANA METOT: Geçme Kalma Durumunu ve Harf Notunu Veritabanına Yazar
    public static void harfNotuGuncelle(Connection connection, int dersId) {
        try {
            List<Double> sinifNotlari = girilenNotlariGetir(connection, dersId);

            String selectSorgu = "SELECT student_id, average, final_score FROM transcripts WHERE course_id = ? AND average IS NOT NULL";
            PreparedStatement st = connection.prepareStatement(selectSorgu);
            st.setInt(1, dersId);
            ResultSet rs = st.executeQuery();

            String updateSorgu = "UPDATE transcripts SET letter_grade = ?, is_passed = ? WHERE student_id = ? AND course_id = ?";
            PreparedStatement updatePrs = connection.prepareStatement(updateSorgu);

            while (rs.next()) {
                int ogrenciId = rs.getInt("student_id");
                double ogrenciNotu = rs.getDouble("average");
                int finalNotu = rs.getInt("final_score");

                String harf = "FF";
                boolean isPassed = false;

                // --- 1. ADIM: FİNAL BARAJI KONTROLÜ ---
                // Çan eğrisi veya sabit skala fark etmez; finalden 50 alamayan doğrudan kalır!
                if (finalNotu < 50) {
                    harf = "FF";
                    isPassed = false;
                } else {
                    // --- 2. ADIM: HARF NOTU HESAPLAMA ---
                    if (sinifNotlari.size() > 20) {
                        harf = canEgrisi(ogrenciNotu, sinifNotlari);
                    } else {
                        harf = sabitNotlar(ogrenciNotu);
                    }

                    // DC/DD şartlı geçme kontrolü
                    if (harf.equals("FF")) {
                        isPassed = false;
                    } else if (harf.equals("DC") || harf.equals("DD")) {
                        double gno = genelOrtalamaHesapla(connection, ogrenciId);

                        if (gno < 60.0) { // GNO'su 60'tan düşükse (Kredisi yetmiyorsa)
                            harf = "FF";  // FF'e düşür ve bırak
                            isPassed = false;
                        } else {
                            isPassed = true; // GNO'su yetiyorsa şartlı olarak geçer
                        }
                    } else {
                        isPassed = true; // AA, BA, BB, CB, CC şartsız geçer
                    }
                }

                // veritabanını güncelle
                updatePrs.setString(1, harf);
                updatePrs.setBoolean(2, isPassed);
                updatePrs.setInt(3, ogrenciId);
                updatePrs.setInt(4, dersId);
                updatePrs.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Harf notları güncellenirken hata: " + e.getMessage());
        }
    }
}