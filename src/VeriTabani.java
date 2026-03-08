import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;

public class VeriTabani {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/obs_db";
    private static final String DB_USER = "root";
    private static String DB_PASS = "";

    private static Connection connection = null;

    // Sınıf ilk çalıştığında .env dosyasını okur
    static {
        try {
            File envFile = new File(".env");

            if (envFile.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(envFile));

                // trim() ile kazara konulmuş boşlukları temizliyoruz
                if(props.getProperty("DB_PASS") != null) {
                    DB_PASS = props.getProperty("DB_PASS").trim();
                }
            } else {
                System.out.println("🛑 SİSTEM UYARISI: .env dosyası bulunamadı!");
                System.out.println("Lütfen projenin ana klasörüne (src'nin hemen dışına) '.env' adında bir dosya oluşturun.");
            }
        } catch (Exception e) {
            System.out.println("🛑 SİSTEM HATASI: .env dosyası okunamadı: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                // System.out.println("Veritabanı bağlantısı BAŞARILI! ✅");
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı HATASI! ❌ .env dosyanızdaki şifreyi veya veritabanı ayarlarınızı kontrol edin.");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Bağlantı kapatıldı. 🔒");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}