import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeriTabani {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/obs_db";
    private static final String DB_USER = "root"; // kendi adın
    private static final String DB_PASS = "          şifren     ";     // kendi şifren

    private static Connection connection = null;

    // Bağlantıyı Getiren Metot (Singleton Mantığı)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Bağlantı kapalıysa veya yoksa yenisini aç
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.println("Veritabanı bağlantısı BAŞARILI! ✅");
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı HATASI! ❌");
            e.printStackTrace();
        }
        return connection;
    }

    // Bağlantıyı Kapatan Metot (İş bitince temizlik için)
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