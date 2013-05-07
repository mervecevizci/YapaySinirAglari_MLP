/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa_mlp;

/**
 *
 * @author Merve
 */
public class SigmoidFonksiyon implements Fonksiyon {

    public SigmoidFonksiyon() {
    }

    @Override
    public double hesapla(double giris) {
        return 1.0/(1.0 + Math.pow(Math.E, giris));
    }

    @Override
    public double ilkTurevHesapla(double giris) {
        return giris * (1 - giris);
    }
    
}
