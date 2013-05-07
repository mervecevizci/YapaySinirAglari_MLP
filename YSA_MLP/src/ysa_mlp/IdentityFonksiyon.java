/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa_mlp;

/**
 *
 * @author Merve
 */
class IdentityFonksiyon implements Fonksiyon {

    public IdentityFonksiyon() {
    }

    @Override
    public double hesapla(double giris) {
        return giris;
    }

    @Override
    public double ilkTurevHesapla(double giris) {
        return 1;
    }
}
