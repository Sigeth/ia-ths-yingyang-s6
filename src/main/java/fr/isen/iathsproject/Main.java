package fr.isen.iathsproject;

import fr.isen.FFT.Complexe;
import fr.isen.FFT.ComplexeCartesien;
import fr.isen.FFT.FFTCplx;
import fr.isen.Son.Son;
import static fr.isen.FFT.FFTCplx.appliqueSur;

public class Main {

    private static void sonFFT(Son son) {
        int tailleFFT = 16;

        Complexe[] signalTest = new Complexe[tailleFFT];
        for (int i = 0; i < tailleFFT; ++i)
            signalTest[i] = new ComplexeCartesien(son.bloc_deTaille(1, tailleFFT)[i], 0);
        // On applique la FFT sur ce signal
        Complexe[] resultat = appliqueSur(signalTest);
        // On affiche les valeurs du résultat
        for (int i = 0; i < resultat.length; ++i) {
            System.out.print(i+" : ("+(float)resultat[i].reel()+" ; "+(float)resultat[i].imag()+"i)");
            System.out.println(", ("+(float)resultat[i].mod()+" ; "+(float)resultat[i].arg()+" rad)");
        }
    }

    public static void main(String[] args) {
        Son carre = new Son("src/main/resources/Sources-sonores/Carre.wav");

        System.out.println("Fréquence: " + carre.frequence() + "Hz");
        System.out.println("Longueur de données: " + carre.donnees().length);
        System.out.println("Longueur de données du bloc 1 de longueur 512: " + carre.bloc_deTaille(1, 512).length);

        for (String el : new String[]{"cosReel", "sinReel", "cosImag", "sinImag"}) {
            System.out.println(el);
            FFTCplx.main(new String[]{el});
        }
    }
}