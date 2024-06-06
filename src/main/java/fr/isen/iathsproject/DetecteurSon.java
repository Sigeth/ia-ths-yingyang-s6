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
    private static final int tailleFFT = 512;

    public DetecteurSon() {
        sons = new ArrayList<>(List.of());

        for (String songName : songsname) {
            sons.add(new Son("src/main/resources/Sources-sonores/" + songName + ".wav"));
        }
    }

    private static Neurone trainNeuroneSon(Neurone n, float[] resultats) {
        for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");

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

        for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");

        return n;
    }

    private static float testNeuroneSon(Neurone n, float[] resultats) {
        int tests = 0;
        int cptEchecs = 0;

        for (float bruit = -128.f; bruit < 128.f; bruit += 1.f) {
            Complexe[][] signals = new Complexe[sons.size()][tailleFFT];
            Complexe[][] ffts = new Complexe[sons.size()][tailleFFT];
            float[][] entrees = new float[sons.size()][tailleFFT];
            for (int j = 0; j < sons.size(); j++) {
                Complexe[] signal = signals[j];
                Son s = sons.get(j);
                for (int i = 0; i < tailleFFT; ++i)
                    signal[i] = new ComplexeCartesien(s.bloc_deTaille(1, tailleFFT)[i] + bruit, 0);

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
                    if (n.getClass() == NeuroneHeaviside.class) {
                        if (n.sortie() != resultats[i])
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i]);
                    } else if (n.getClass() == NeuroneSigmoide.class || n.getClass() == NeuroneReLU.class) {
                        if (n.sortie() < resultats[i] * 0.5f)
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                    } else if (n.getClass() == NeuroneTanH.class) {
                        if (resultats[i] == 0 ? n.sortie() > 0 : n.sortie() <= 0)
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i]);
                    }

                }
            } catch (AssertionError e) {
                // si notre sortie n'est pas celle que l'on attendait, on génére une AssertionError
                // on incrémente ensuite notre compteur d'échecs et on passe au test suivant
                cptEchecs += 1;
                // chaque AssertionError a une raison, que l'on peut afficher grâce au bout de code ci-dessous
                // System.out.println("Test non réussi\nRaison : " + e.getMessage());
            }
        }

        return 1.f - ((float) cptEchecs / tests);
    }

    public static void main(String[] args) {

        /*System.out.println("Neurone du signal Bruit");
        NeuroneHeaviside nBruit = (NeuroneHeaviside) trainNeuroneSon(new NeuroneHeaviside(tailleFFT), new float[]{1,0,0,0,0});
        System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(nBruit, new float[]{1,0,0,0,0}) * 100.f) + "%");

        System.out.println("Neurone du signal Carré");
        NeuroneHeaviside nCarre = (NeuroneHeaviside) trainNeuroneSon(new NeuroneHeaviside(tailleFFT), new float[]{0,1,0,0,0});
        System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(nCarre, new float[]{0,1,0,0,0}) * 100.f) + "%");

        System.out.println("Neurone du signal Sinusoide");
        NeuroneReLU nSinus = (NeuroneReLU) trainNeuroneSon(new NeuroneReLU(tailleFFT), new float[]{0,0,1,1,1});
        System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(nSinus, new float[]{0,0,1,1,1}) * 100.f) + "%");*/


        for (int i = 0; i < sons.size(); i++) {
            float[] resultatsTrain = new float[sons.size()];
            float[] resultatsTest = new float[sons.size()];
            for (int j=0; j<sons.size(); j++) {
                resultatsTrain[j] = j == i ? 1 : 0;
                resultatsTest[j] = j == i ? 1 : 0;
            }

            if (i == 2) {
                resultatsTrain = new float[]{0,0,1,1,1};
                resultatsTest = new float[]{0,0,1,1,1};
            }

            if (i < 3) {
                System.out.println(Arrays.toString(resultatsTrain));
                System.out.println(Arrays.toString(resultatsTest));

                System.out.println("Neurone du signal " + songsname[i]);
                NeuroneHeaviside n = (NeuroneHeaviside) trainNeuroneSon(new NeuroneHeaviside(tailleFFT), resultatsTrain);
                System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(n, resultatsTest) * 100.f) + "%");
            }
        }
    }
}
