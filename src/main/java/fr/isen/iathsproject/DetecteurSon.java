package fr.isen.iathsproject;

import fr.isen.FFT.Complexe;
import fr.isen.FFT.ComplexeCartesien;
import fr.isen.Son.Son;
import fr.isen.neurone.NeuroneSigmoide;

import java.util.ArrayList;
import java.util.List;

import static fr.isen.FFT.FFTCplx.appliqueSur;

public class DetecteurSon {
    private static List<Son> sons;

    public DetecteurSon() {
        sons = new ArrayList<>(List.of());

        for (String songName : new String[]{"Bruit", "Carre, Sinusoide", "Sinusoide2", "Sinusoide3Harmoniques"}) {
            sons.add(new Son("src/main/resources/Sources-sonores/" + songName + ".wav"));
        }
    }

    private static NeuroneSigmoide trainNeuroneSon(int sonIndex, int tailleFFT, float[] resultats) {
        NeuroneSigmoide n = new NeuroneSigmoide(tailleFFT);
        Son s = sons.get(sonIndex);

        Complexe[][] signals = new Complexe[sons.size()][tailleFFT];
        Complexe[][] ffts = new Complexe[sons.size()][tailleFFT];
        float[][] entrees = new float[sons.size()][tailleFFT];
        for (int j = 0; j < sons.size(); j++) {
            Complexe[] signal = signals[j];
            for (int i = 0; i < tailleFFT; ++i)
                signal[i] = new ComplexeCartesien(s.bloc_deTaille(1, tailleFFT)[i], 0);

            ffts[j] = appliqueSur(signal);
            for (int i = 0; i < tailleFFT; ++i) {
                float[] entree = entrees[j];
                Complexe[] fft = ffts[j];
                entree[i] = (float) (fft[i].reel() + fft[i].imag());
            }
        }

        int cptEchecs = n.apprentissage(entrees, resultats);
        System.out.println("Apprentissage terminé avec " + cptEchecs + " échecs");

        return n;
    }


    public static void main(String[] args) {

    }
}
