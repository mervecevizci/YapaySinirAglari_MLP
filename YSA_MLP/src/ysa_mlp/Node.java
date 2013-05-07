/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa_mlp;

/**
 *
 * @author Merve
 */
public class Node {
    private final Fonksiyon fonk;
    private double giris;
    private double cikis;
    private double hata;

    public Fonksiyon getFonk() {
        return fonk;
    }

    public double getGiris() {
        return giris;
    }

    public double getCikis() {
        return cikis;
    }

    public double getHata() {
        return hata;
    }

    public Node(Fonksiyon fonk) {
        this.fonk=fonk;
        giris=0;
        cikis=0;
        hata=0;
    }

    public double hesapla(double giris) {
        this.giris=giris;
        this.cikis=fonk.hesapla(giris);
        return cikis;
    }


    double hataHesapla(double hata) {
        hata=fonk.ilkTurevHesapla(cikis)*hata;
        return hata;
    }
    
}
