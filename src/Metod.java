import java.util.InputMismatchException;
import java.util.Scanner;

public class Metod {

    static Scanner keyboard = new Scanner(System.in);

    public static void cikisYap() {
        System.out.println("======================================");
        System.out.println("Çıkış yapılıyor. İyi günler dileriz.");
        System.out.println("======================================");
    }

    public static String guvenliMetinAl() {
        String metin = keyboard.nextLine().trim();

        if (metin.isEmpty()) {
            System.out.println("Giriş boş olamaz");
            guvenliMetinAl();
        }

        return metin;
    }

    public static int guvenliSayiAl() {
        int deger = -1;
        try {
            deger = keyboard.nextInt();
            keyboard.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Sadece sayı giriniz\n");
        }
        return deger;
    }

    public static int notAlma() {
        int not = guvenliSayiAl();

        while (true) {
            if (not <= 100 && not >= 0) {
                return not;
            } else {
                System.out.print("❌ Lütfen 0 ve 100 arasında bir değer giriniz:");
            }
        }
    }
}
