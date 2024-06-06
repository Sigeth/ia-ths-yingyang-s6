package fr.isen.iathsproject;

import fr.isen.FFT.Complexe;
import fr.isen.FFT.ComplexeCartesien;
import fr.isen.Son.Son;
import fr.isen.neurone.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.isen.FFT.FFTCplx.appliqueSur;

public class DetecteurSon {
    private static final String[] songsname = new String[]{"Bruit", "Carre", "Sinusoide", "Sinusoide2", "Sinusoide3Harmoniques"};
    private static List<Son> sons;

    public DetecteurSon() {
        sons = new ArrayList<>(List.of());

        for (String songName : songsname) {
            sons.add(new Son("src/main/resources/Sources-sonores/" + songName + ".wav"));
        }
    }

    private static NeuroneSigmoide trainNeuroneSon(int tailleFFT, float[] resultats) {
        NeuroneSigmoide n = new NeuroneSigmoide(tailleFFT);
        /*for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");*/
        // Son s = sons.get(sonIndex);

        Complexe[][] signals = new Complexe[sons.size()][tailleFFT];
        Complexe[][] ffts = new Complexe[sons.size()][tailleFFT];
        float[][] entrees = new float[sons.size()][tailleFFT];
        for (int j = 0; j < sons.size(); j++) {
            Complexe[] signal = signals[j];
            Son s = sons.get(j);
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

        /*for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");*/

        return n;
    }

    private static float testNeuroneSon(Neurone n, int tailleFFT, float[] resultats) {
        int tests = 0;
        int cptEchecs = 0;

        Complexe[][] signals = new Complexe[sons.size()][tailleFFT];
        Complexe[][] ffts = new Complexe[sons.size()][tailleFFT];
        float[][] entrees = new float[sons.size()][tailleFFT];
        for (int j = 0; j < sons.size(); j++) {
            Complexe[] signal = signals[j];
            Son s = sons.get(j);
            for (int i = 0; i < tailleFFT; ++i)
                signal[i] = new ComplexeCartesien(s.bloc_deTaille(1, tailleFFT)[i], 0);

            ffts[j] = appliqueSur(signal);
            for (int i = 0; i < tailleFFT; ++i) {
                float[] entree = entrees[j];
                Complexe[] fft = ffts[j];
                entree[i] = (float) (fft[i].reel() + fft[i].imag());
            }
        }

        try {
            tests += 1;
            for (int i=0; i < entrees.length; i++) {
                float[] entree = entrees[i];
                // le neurone calcule sa sortie
                n.metAJour(entree);
                if (n.sortie() < resultats[i] * 0.5f) {
                    throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                }
            }
        } catch (AssertionError e) {
            // si notre sortie n'est pas celle que l'on attendait, on génére une AssertionError
            // on incrémente ensuite notre compteur d'échecs et on passe au test suivant
            cptEchecs += 1;
            // chaque AssertionError a une raison, que l'on peut afficher grâce au bout de code ci-dessous
            System.out.println("Test non réussi\nRaison : " + e.getMessage());
        }

        return 1.f - ((float) cptEchecs / tests);
    }

    public static void main(String[] args) {
        for (int i = 0; i < sons.size(); i++) {
            float[] resultatsTrain = new float[sons.size()];
            float[] resultatsTest = new float[sons.size()];
            for (int j=0; j<sons.size(); j++) {
                resultatsTrain[j] = j == i ? 1 : 0;
                resultatsTest[j] = j == i ? 1 : 0;
            }

            System.out.println(Arrays.toString(resultatsTrain));
            System.out.println(Arrays.toString(resultatsTest));

            System.out.println("Neurone du signal " + songsname[i]);
            NeuroneSigmoide n = trainNeuroneSon(512, resultatsTrain);
            System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(n, 512, resultatsTest) * 100.f) + "%");
        }
    }
}
