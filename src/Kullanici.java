public class Kullanici {

    private int id;
    private String kullaniciAdi;
    private String sifreHash;
    private String adSoyad;
    private Roller roller;
    private String unvan;

    public Kullanici(int id, String kullaniciAdi, String sifreHash, String adSoyad, Roller roller, String unvan) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        this.sifreHash = sifreHash;
        this.adSoyad = adSoyad;
        this.roller = roller;
        this.unvan = unvan;
    }

    public int getId() {
        return id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public String getSifreHash() {
        return sifreHash;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public Roller getRoller() {
        return roller;
    }

    public String getUnvan() {
        return unvan;
    }
}
