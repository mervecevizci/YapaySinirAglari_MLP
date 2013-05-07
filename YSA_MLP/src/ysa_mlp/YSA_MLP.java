/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa_mlp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Merve
 */
public class YSA_MLP {

    private static Fonksiyon idFonk = new IdentityFonksiyon();
    private final int giris_katman_sayisi;
    private final int gizli_katman_sayisi;
    private final int cikis_katman_sayisi;
    private final int momentum;
    private final Node[] giris_katmani;
    private final Node[] gizli_katman;
    private final Node[] cikis_katmani;
    private final Agirliklar giristen_gizliye;
    private final Agirliklar gizliden_cikisa;

    public YSA_MLP(int girisKatmanSayisi, int gizliKatmanSayisi, int cikisKatmanSayisi, Fonksiyon fonk, int momentum) {
        this.giris_katman_sayisi = girisKatmanSayisi;
        this.gizli_katman_sayisi = gizliKatmanSayisi;
        this.cikis_katman_sayisi = cikisKatmanSayisi;
        this.momentum = momentum;
        giris_katmani = new Node[girisKatmanSayisi];
        gizli_katman = new Node[gizliKatmanSayisi];
        cikis_katmani = new Node[cikisKatmanSayisi];
        giristen_gizliye = new Agirliklar(girisKatmanSayisi, gizliKatmanSayisi);
        gizliden_cikisa = new Agirliklar(gizliKatmanSayisi, cikisKatmanSayisi);
        for (int i = 0; i < girisKatmanSayisi; i++) {
            giris_katmani[i] = new Node(idFonk);
        }
        for (int i = 0; i < gizliKatmanSayisi; i++) {
            gizli_katman[i] = new Node(fonk);
        }
        for (int i = 0; i < cikisKatmanSayisi; i++) {
            cikis_katmani[i] = new Node(fonk);
        }
    }

    public double[] ileribesleme(double[] girdi) throws IllegalArgumentException {
        if (girdi.length != giris_katman_sayisi) {
            throw new IllegalArgumentException("Girdi sayısı giriş katman sayısına eşit değil. İleri besleme yapılamaz.");
        }
        double[] ciktilar = null;
        try {
            ciktilar = new double[gizli_katman_sayisi];
            for (int i = 0; i < giris_katman_sayisi; i++) {
                double cikti = giris_katmani[i].hesapla(girdi[i]);
                for (int j = 0; j < gizli_katman_sayisi; j++) {
                    ciktilar[j] = ciktilar[j] + giristen_gizliye.agirlikliciktihesapla(cikti, i, j);
                }
            }
            girdi = ciktilar;
            ciktilar = new double[cikis_katman_sayisi];
            for (int i = 0; i < cikis_katman_sayisi; i++) {
                ciktilar[i] = cikis_katmani[i].hesapla(girdi[i]);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        return ciktilar;
    }

    public double geriyayilim(double[] dogrucikti) throws IllegalArgumentException {
        if (dogrucikti.length != cikis_katman_sayisi) {
            throw new IllegalArgumentException("Dogru cikti sayısı çıkış katmanı sayısına eşit değil. Geir yayılım yapılamaz.");
        }
        double hata = 0;
        try {
            double[] cikisKatmaniHatalari = new double[cikis_katman_sayisi];
            for (int i = 0; i < cikis_katman_sayisi; i++) {
                cikisKatmaniHatalari[i] = cikis_katmani[i].hataHesapla(dogrucikti[i] - cikis_katmani[i].getCikis());
                hata = hata + Math.abs(cikisKatmaniHatalari[i]);
            }
            double[] gizliKatmanHatalari = new double[gizli_katman_sayisi];
            for (int i = 0; i < gizli_katman_sayisi; i++) {
                double hata1 = 0;
                for (int j = 0; j < cikis_katman_sayisi; j++) {
                    hata1 = hata1 + cikisKatmaniHatalari[j] * gizliden_cikisa.getAgirlik(i, j);
                    gizliden_cikisa.agirlikguncelle(i, j, cikisKatmaniHatalari[j], momentum, gizli_katman[i].getCikis());
                }
                gizliKatmanHatalari[i] = gizli_katman[i].hataHesapla(hata1);
                hata = hata + Math.abs(gizliKatmanHatalari[i]);
            }
            for (int i = 0; i < giris_katman_sayisi; i++) {
                for (int j = 0; j < gizli_katman_sayisi; j++) {
                    giristen_gizliye.agirlikguncelle(i, j, gizliKatmanHatalari[j], momentum, giris_katmani[i].getCikis());
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        return hata;
    }

    public int egitim(double[][] girdiler, double[][] dogrucikti, double esikdegeri) throws IllegalArgumentException {
        if (girdiler.length != dogrucikti.length) {
            throw new IllegalArgumentException("Girdilerin sayısı çıktıların sayısına eşit olmalıdır. Eğitim yapılamaz. ");
        }
        double hata;
        int sayac = 0;
        int girdilerinSayisi = girdiler.length;
        do {
            hata = 0;
            for (int i = 0; i < girdilerinSayisi; i++) {
                ileribesleme(girdiler[i]);
                hata = hata + geriyayilim(dogrucikti[i]);
            }
            sayac++;
        } while (hata > esikdegeri);
        return sayac;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        Fonksiyon sigmoid = new SigmoidFonksiyon();
        YSA_MLP m = new YSA_MLP(2, 2, 2, sigmoid, 2);
        int i = 0;
        double[] giris;
        giris = new double[2];
        double genislik;
        double oran;
        double[][] birinciKatman = new double[2][2];
        double[][] ikinciKatman = new double[2][2];
        birinciKatman[0][0] = 0.2;
        birinciKatman[0][1] = 0.3;
        birinciKatman[1][0] = 0.4;
        birinciKatman[1][1] = 0.5;
        ikinciKatman[0][0] = 0.9;
        ikinciKatman[0][1] = 0.8;
        ikinciKatman[1][0] = 0.7;
        ikinciKatman[1][1] = 0.6;
        int ii=0;
        Connection baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/inputs", "root", "");
        Statement statement = (Statement) baglanti.createStatement();
        ResultSet sonuc = statement.executeQuery("Select oran_1, elips from parametre_kapak");
        while (sonuc.next()) {
            genislik = (double) sonuc.getObject("oran_1");
            oran = (double) sonuc.getObject("elips");
            giris[0] = genislik;
            giris[1] = oran;
            m.giristen_gizliye.setAgirliklar(birinciKatman);
            m.gizliden_cikisa.setAgirliklar(ikinciKatman);
            System.out.println(ii);
            ii++;

            double[] output = m.ileribesleme(giris);

            for (double d : output) {
                System.out.println(d);
            }
        }
        giris[0] = 2;
            giris[1] =1 ;
        double[] output = m.ileribesleme(giris);

            for (double d : output) {
                System.out.println(d);
            }
    }
}
