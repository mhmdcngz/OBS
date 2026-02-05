import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("\n========== ÖĞRENCİ BİLGİ SİSTEMİNE HOŞGELDİNİZ ==========");
        System.out.print("Kullanıcı Adı Giriniz: ");
        String kullaniciAdi = keyboard.next();
        System.out.print("Parolanızı Giriniz: ");
        String parola = keyboard.next();

        //
        Kullanici girisYapan = girisYap(kullaniciAdi, parola);

        // sonuç kontrol etme
        if (girisYapan != null) {
            System.out.println("\n✅ GİRİŞ BAŞARILI!");
            System.out.println("Hoşgeldin: " + girisYapan.getAdSoyad());
            System.out.println("Yetkiniz: " + girisYapan.getRoller());

            // menü yönlendirme
            if (girisYapan.getRoller() == Roller.DEKAN) {
                YoneticiMenu.menuGoster(girisYapan);
            } else if (girisYapan.getRoller() == Roller.AKADEMISYEN) {
                AkademisyenMenu.menuGoster(girisYapan);
            } else if (girisYapan.getRoller() == Roller.OGRENCI) {
                OgrenciMenu.menuGoster(girisYapan);
            }

        } else {
            System.out.println("\n❌ HATALI GİRİŞ!");
            System.out.println("Kullanıcı adı veya şifre yanlış");
        }
    }

    public static Kullanici girisYap(String kAdi, String parola) {
        Kullanici bulunanKullanici = null;

        //parolayı şifreleme
        String kontrolSifresi = Guvenlik.sifrele(parola);

        //sql sorgusu
        String sqlSorgu = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try {
            Connection connection = VeriTabani.getConnection();
            PreparedStatement ifade = connection.prepareStatement(sqlSorgu);
            ifade.setString(1, kAdi);
            ifade.setString(2, kontrolSifresi);

            ResultSet sonuc = ifade.executeQuery();

            // kullanıcı bulunduysa
            if (sonuc.next()) {
                //
                String rolMetni = sonuc.getString("role_enum");
                rolMetni = rolMetni.toUpperCase().trim();
                Roller kullaniciRolu = Roller.valueOf(rolMetni);

                // kullanıcı nesnesi
                bulunanKullanici = new Kullanici(
                        sonuc.getInt("id"),
                        sonuc.getString("username"),
                        sonuc.getString("password_hash"),
                        sonuc.getString("full_name"),
                        kullaniciRolu,
                        sonuc.getString("title")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bulunanKullanici;
    }
}