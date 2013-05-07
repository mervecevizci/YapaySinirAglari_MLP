/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa_mlp;

import javax.swing.text.DefaultEditorKit;

/**
 *
 * @author Merve
 */
public class Agirliklar {
    private final int ilkKatmanSayisi;
    private final int ikinciKatmanSayisi;
    private double[][] agirliklar;

    public void setAgirliklar(double[][] agirliklar) {
        this.agirliklar = agirliklar;
    }

    public double[][] getAgirliklar() {
        double[][] kopya=new double[ilkKatmanSayisi][ikinciKatmanSayisi];
        for (int i = 0; i < ilkKatmanSayisi; i++) {
            for (int j = 0; j < ikinciKatmanSayisi; j++) {
                kopya[i][j]=agirliklar[i][j];
            }
            }
        return kopya;
    }
    

    public Agirliklar(int ilkKatmanSayisi, int ikinciKatmanSayisi) {
        this.ilkKatmanSayisi=ilkKatmanSayisi;
        this.ikinciKatmanSayisi=ikinciKatmanSayisi;
        agirliklar=new double[ilkKatmanSayisi][ikinciKatmanSayisi];
        for(int i=0;i<ilkKatmanSayisi;i++){
            for (int j = 0; j < ikinciKatmanSayisi; j++) {
                agirliklar[i][j]=Math.random();
            }
        }
    }

    double agirlikliciktihesapla(double cikti, int ilkKatmanPozisyonu, int ikinciKatmanPozisyonu) throws IllegalArgumentException{
        if(ilkKatmanPozisyonu>=ilkKatmanSayisi || ilkKatmanPozisyonu<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        if(ikinciKatmanPozisyonu>=ikinciKatmanSayisi || ikinciKatmanPozisyonu<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        return cikti*agirliklar[ilkKatmanPozisyonu][ikinciKatmanPozisyonu];
    }

  public final double getAgirlik(int ilkKatmanNode, int ikinciKatmanNode) {
        if(ilkKatmanNode>=ilkKatmanSayisi || ilkKatmanNode<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        if(ikinciKatmanNode>=ikinciKatmanSayisi || ikinciKatmanNode<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        return agirliklar[ilkKatmanNode][ikinciKatmanNode];
    }

    double agirlikguncelle(int ilkKatmanPozisyonu, int ikinciKatmanPozisyonu, double hata, int momentum, double cikti) throws IllegalArgumentException{
        if(ilkKatmanPozisyonu>=ilkKatmanSayisi || ilkKatmanPozisyonu<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        if(ikinciKatmanPozisyonu>=ikinciKatmanSayisi || ikinciKatmanPozisyonu<0){
            throw new IllegalArgumentException("Geçersiz giriş node'u.Hesaplanamıyor.");
        }
        return agirliklar[ilkKatmanPozisyonu][ikinciKatmanPozisyonu]=agirliklar[ilkKatmanPozisyonu][ikinciKatmanPozisyonu]+momentum*hata*cikti;
    }
    
}
