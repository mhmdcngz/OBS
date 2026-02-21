import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotSistemi {
    // veritabanından sadece notu girilen öğrencileri çekme
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

    public static String harfNotu(double ogrenciNotu, List<Double> girilenNotlar) {
        int notGirilenSayi = girilenNotlar.size();

        if (notGirilenSayi > 20) {
            return canEgrisi(ogrenciNotu, girilenNotlar);
        } else {
            return sabitNotlar(ogrenciNotu);
        }
    }

    private static String sabitNotlar(double not) {
        if (not >= 90) return "AA";
        if (not >= 85) return "BA";
        if (not >= 80) return "BB";
        if (not >= 75) return "CB";
        if (not >= 70) return "CC";
        if (not >= 60) return "DC";
        if (not >= 50) return "DD";
        if (not >= 40) return "FD";
        return "FF";
    }

    private static String canEgrisi(double ogrenciNotu, List<Double> tumNotlar) {
        int n = tumNotlar.size();
        double toplam = 0;

        // ortalama
        for (double not : tumNotlar) {
            toplam += not;
        }
        double ortalama = toplam / n;

        // standart sapma
        double varyansToplami = 0;
        for (double not : tumNotlar) {
            varyansToplami += Math.pow(not - ortalama, 2);
        }
        double stnSapma = Math.sqrt(varyansToplami / n);

        // herkes aynı notu aldıysa
        if (stnSapma == 0) {
            return sabitNotlar(ogrenciNotu);
        }

        double zSkoru = (ogrenciNotu - ortalama) / stnSapma;

        if (zSkoru >= 1.5) return "AA";
        if (zSkoru >= 1.0) return "BA";
        if (zSkoru >= 0.5) return "BB";
        if (zSkoru >= 0.0) return "CB";
        if (zSkoru >= -0.5) return "CC";
        if (zSkoru >= -1.0) return "DC";
        if (zSkoru >= -1.5) return "DD";
        if (zSkoru >= -2.0) return "FD";
        return "FF";
    }

    public static void harfNotuGuncelle(Connection connection, int dersId) {
        try {
            List<Double> sinifNotlari = girilenNotlariGetir(connection, dersId);

            String selectSorgu = "SELECT student_id, average FROM transcripts WHERE course_id = ? AND average IS NOT NULL";
            PreparedStatement st = connection.prepareStatement(selectSorgu);
            st.setInt(1, dersId);
            ResultSet rs = st.executeQuery();

            String updateSorgu = "UPDATE transcripts SET letter_grade = ? WHERE student_id = ? AND course_id = ?";
            PreparedStatement updatePrs = connection.prepareStatement(updateSorgu);

            while (rs.next()) {
                int ogrenciId = rs.getInt("student_id");
                double ogrenciNotu = rs.getDouble("average");

                // Harf notunu mevcut sınıftaki (yukarıdaki) metotlarla hesaplatıyoruz
                String yeniHarfNotu = harfNotu(ogrenciNotu, sinifNotlari);

                // Veritabanını güncelle
                updatePrs.setString(1, yeniHarfNotu);
                updatePrs.setInt(2, ogrenciId);
                updatePrs.setInt(3, dersId);
                updatePrs.executeUpdate(); // Değişikliği veritabanına kaydet
            }
        } catch (SQLException e) {
            System.out.println("Harf notları güncellenirken hata: " + e.getMessage());
        }
    }
}
