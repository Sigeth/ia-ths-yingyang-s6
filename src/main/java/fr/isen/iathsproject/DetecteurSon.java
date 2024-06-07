package fr.isen.iathsproject;

import fr.isen.FFT.Complexe;
import fr.isen.FFT.ComplexeCartesien;
import fr.isen.Son.Son;
import fr.isen.neurone.*;

import java.util.*;

import static fr.isen.FFT.FFTCplx.appliqueSur;

public class DetecteurSon {
    private static final String[] songsname = new String[]{"Bruit", "Carre", "Sinusoide", "Sinusoide2", "Sinusoide3Harmoniques"};
    private static List<Son> sons;
    private static final int tailleFFT = 1024;

    public DetecteurSon() {
        sons = new ArrayList<>(List.of());

        for (String songName : songsname) {
            sons.add(new Son("src/main/resources/Sources-sonores/" + songName + ".wav"));
        }
    }

    private static float[] signalToModuleFFT(float[] signalData) {
        Complexe[] signal = new Complexe[tailleFFT];
        for (int i = 0; i < tailleFFT; ++i)
            signal[i] = new ComplexeCartesien(signalData[i], 0);

        Complexe[] fft = appliqueSur(signal);
        float[] entree = new float[tailleFFT];
        for (int i = 0; i < tailleFFT; ++i) {
            entree[i] = (float) (Math.sqrt(Math.pow(fft[i].reel(), 2) + Math.pow(fft[i].imag(), 2)));
        }

        return entree;
    }

    private static NeuroneSigmoide trainNeuroneFromDataset(float[][] entreesSon, float[] resultats) {
        NeuroneSigmoide n = new NeuroneSigmoide(tailleFFT);

        /*for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");*/

        float[][] entrees = new float[entreesSon.length][tailleFFT];
        for (int j = 0; j < entreesSon.length; j++) {
            entrees[j] = signalToModuleFFT(entreesSon[j]);
        }

        int cptEchecs = n.apprentissage(entrees, resultats);
        System.out.println("Apprentissage terminé avec " + cptEchecs + " échecs");

        /*for (float synapse : n.synapses())
            System.out.print(synapse + " ");
        System.out.println(" ");*/

        return n;
    }

    private static float testNeuroneSon(NeuroneSigmoide n, float[] resultats) {
        int tests = 0;
        int cptEchecs = 0;
        float[][] entrees = new float[sons.size()][tailleFFT];
        for (int j = 0; j < sons.size(); j++) {
            entrees[j] = signalToModuleFFT(sons.get(j).bloc_deTaille(1, tailleFFT));
        }

        for (int i=0; i < entrees.length; i++) {
            tests += 1;
            try {
                float[] entree = entrees[i];
                // le neurone calcule sa sortie
                n.metAJour(entree);
                if (resultats[i] == 1 ? n.sortie() < 0.5f : n.sortie() >= 0.5f)
                    throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                /*else
                    System.out.println("Le neurone a renvoyé " + n.sortie() + " comme demandé : " + resultats[i] * 0.5f);*/
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

    private static float testNeuroneFromDataset(NeuroneSigmoide n, float[][] entreesSon, float[] resultats) {
        int tests = 0;
        int cptEchecs = 0;

        float[][] entrees = new float[entreesSon.length][tailleFFT];
        for (int j = 0; j < entreesSon.length; j++) {
            entrees[j] = signalToModuleFFT(entreesSon[j]);
        }

        for (int i=0; i < entrees.length; i++) {
            tests += 1;
            try {
                float[] entree = entrees[i];
                // le neurone calcule sa sortie
                n.metAJour(entree);
                if (resultats[i] == 1 ? n.sortie() < 0.5f : n.sortie() >= 0.5f)
                    throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                /*else
                    System.out.println("Le neurone a renvoyé " + n.sortie() + " comme demandé : " + resultats[i] * 0.5f);*/
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

    private static float[] normaliseBloc(float[] bloc) {
        ArrayList<Float> arrayBloc = new ArrayList<>();
        for (float b : bloc) {
            arrayBloc.add(Math.abs(b));
        }
        float max = Collections.max(arrayBloc);

        for (int i=0; i<bloc.length; i++) {
            bloc[i] = bloc[i] / max;
        }
        return bloc;
    }

    public static void main(String[] args) {

        Son[] datasetCarre = new Son[200];
        Son[] datasetSinus = new Son[200];
        Son[] datasetNoisySinus = new Son[200];
        Son[] datasetNoisyCarre = new Son[200];

        // Son[] datasetBruit = new Son[200];

        for (int i=0; i < 200; i++) {
            datasetCarre[i] = new Son("src/main/resources/dataset/carre/" + (i*100+20) + ".wav");
            datasetSinus[i] = new Son("src/main/resources/dataset/sinus/" + (i*100+20) + ".wav");
            datasetNoisySinus[i] = new Son("src/main/resources/dataset/noisy_sinus/" + (i*100+20) + ".wav");
            datasetNoisyCarre[i] = new Son("src/main/resources/dataset/noisy_carre/" + (i*100+20) + ".wav");
            // datasetBruit[i] = new Son("src/main/resources/dataset/bruit/" + (i*100+20) + ".wav");
        }

        float[][] entreesDataset = new float[600][tailleFFT];
        float[] resultatsDatasetCarre = new float[600];
        float[] resultatsDatasetSinus = new float[600];
        // float[] resultatsDatasetBruit = new float[600];

        float[][] testNeurone = new float[400][tailleFFT];
        float[] resultatsTestCarre = new float[400];
        float[] resultatsTestSinus = new float[400];


        for (int i=0; i<400; i++) {
            if (i < 200) {
                entreesDataset[i] = normaliseBloc(datasetCarre[i].bloc_deTaille(1, tailleFFT));
                resultatsDatasetCarre[i] = 1;
                resultatsDatasetSinus[i] = 0;
                // resultatsDatasetBruit[i] = 0;

                testNeurone[i] = normaliseBloc(datasetNoisyCarre[i].bloc_deTaille(1, tailleFFT));
                resultatsTestCarre[i] = 1;
                resultatsTestSinus[i] = 0;
            } else if (i < 400){
                entreesDataset[i] = normaliseBloc(datasetSinus[i-200].bloc_deTaille(1, tailleFFT));
                resultatsDatasetCarre[i] = 0;
                resultatsDatasetSinus[i] = 1;
                // resultatsDatasetBruit[i] = 0;

                testNeurone[i] = normaliseBloc(datasetNoisySinus[i-200].bloc_deTaille(1, tailleFFT));
                resultatsTestCarre[i] = 0;
                resultatsTestSinus[i] = 1;
            } /*else {
                entreesDataset[i] = datasetBruit[i-400].bloc_deTaille(1, tailleFFT);
                resultatsDatasetCarre[i] = 0;
                resultatsDatasetSinus[i] = 0;
                resultatsDatasetBruit[i] = 1;
            }*/
        }

        /*System.out.println("Neurone du bruit");
        NeuroneSigmoide nBruit = (NeuroneSigmoide) trainNeuroneFromDataset(new NeuroneSigmoide(tailleFFT), entreesDataset, resultatsDatasetBruit);
        System.out.println("Taux de réussite : " + Math.round(testNeuroneSon(nBruit, new float[]{1,0,0,0,0}) * 100.f) + "%");*/

        System.out.println("Neurone du signal Carré");
        NeuroneSigmoide nCarre = trainNeuroneFromDataset(entreesDataset, resultatsDatasetCarre);
        System.out.println("Taux de réussite sur l'échantillon proposé : " + Math.round(testNeuroneSon(nCarre, new float[]{0,1,0,0,0}) * 100.f) + "%");
        System.out.println("Taux de réussite sur le dataset bruité : " + Math.round(testNeuroneFromDataset(nCarre, testNeurone, resultatsTestCarre) * 100.f) + "%");


        System.out.println("Neurone du signal Sinusoide");
        NeuroneSigmoide nSinus = trainNeuroneFromDataset(entreesDataset, resultatsDatasetSinus);
        System.out.println("Taux de réussite sur l'échantillon proposé : " + Math.round(testNeuroneSon(nSinus, new float[]{0,0,1,1,1}) * 100.f) + "%");
        System.out.println("Taux de réussite sur le dataset bruité : " + Math.round(testNeuroneFromDataset(nSinus, testNeurone, resultatsTestSinus) * 100.f) + "%");
    }
}
