package fr.isen.iathsproject;

import fr.isen.FFT.Complexe;
import fr.isen.FFT.ComplexeCartesien;
import fr.isen.FFT.FFTCplx;
import fr.isen.Son.Son;
import static fr.isen.FFT.FFTCplx.appliqueSur;

public class Main {

    private static void sonFFT(Son son) {
        // tailleFFT doit être la taille la plus optimisée, mais j'ai toujours pas compris comment on fait ça
        int tailleFFT = 512;
        FFTCplx fft = new FFTCplx(tailleFFT);

        Complexe[] signalTest = new Complexe[tailleFFT];
        for (int i = 0; i < tailleFFT; ++i)
            signalTest[i] = new ComplexeCartesien(son.bloc_deTaille(1, tailleFFT)[i], 0);
        // On applique la FFT sur ce signal
        Complexe[] resultat = fft.appliqueSur(signalTest);
        // On affiche les valeurs du résultat
        for (int i = 0; i < resultat.length; ++i) {
            System.out.println("=======");
            System.out.print("x[" + i + "] : ("+(float)signalTest[i].reel()+" ; "+(float)signalTest[i].imag()+"i)");
            System.out.println(", ("+(float)signalTest[i].mod()+" ; "+(float)signalTest[i].arg()+" rad)");
            System.out.print("X[" + i + "] : ("+(float)resultat[i].reel()+" ; "+(float)resultat[i].imag()+"i)");
            System.out.println(", ("+(float)resultat[i].mod()+" ; "+(float)resultat[i].arg()+" rad)");
        }
    }

    public static void main(String[] args) {
        Son carre = new Son("src/main/resources/Sources-sonores/Carre.wav");
        Son sinus = new Son("src/main/resources/Sources-sonores/Sinusoide.wav");

        System.out.println("Fréquence carré: " + carre.frequence() + "Hz");
        System.out.println("Longueur de données carré: " + carre.donnees().length);

        /* for (String el : new String[]{"cosReel", "sinReel", "cosImag", "sinImag"}) {
            System.out.println(el);
            FFTCplx.main(new String[]{el});
        }*/

        sonFFT(sinus);
    }
}