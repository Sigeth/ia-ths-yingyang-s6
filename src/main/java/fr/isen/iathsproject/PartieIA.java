package fr.isen.iathsproject;

import fr.isen.neurone.*;

public class PartieIA {
    /**
     * trainNeurone - Permet d'entraîner un neurone Heaviside ou Sigmoide pour faire une porte (OR ou AND)
     * @param n - le neurone à entraîner
     * @param resultats - les résultats attendus pour les entrées (dans l'ordre) : [0,0] [1,0] [0,1] [1,1]
     * @return le neurone qui a appris à être une bonne porte
     */
    private static Neurone trainNeurone(Neurone n, float[] resultats) {
        System.out.println("Perceptron avant apprentissage : " + n);

        float[][] entrees = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};

        int cptEchecs = n.apprentissage(entrees, resultats);

        System.out.println("Perceptron après apprentissage : " + n + "\nAvec " + cptEchecs + " échecs.");

        return n;
    }

    /**
     * testNeurone - Permet de tester des portes (OR et AND) avec des neurones Heaviside et Sigmoide
     * @param n - le neurone à tester. Peut être NeuroneHeaviside ou NeuroneSigmoide
     * @param resultats - les résultats attendus, sous forme de tableaux de float. On peut réutiliser le tableau de résultat utilisé lors de l'apprentissage
     * @return float représentant le taux de réussite du neurone testé
     */
    private static float testNeurone(Neurone n, float[] resultats) {
        // ces deux variables sont des compteurs permettant de générer le taux de réussite
        float tests = 0;
        float cptEchecs = 0;
        // on génère un bruit de -0.1 à 0.1, soit un bruit contenu dans notre tolérance de sortie
        for (float bruit=-0.1f; bruit<0.1f; bruit += 1.e-2f) {
            tests += 1;
            try {
                // on génère des entrées bruitées
                float[][] entrees = new float[][]{{0+bruit, 0+bruit}, {1+bruit, 0+bruit}, {0+bruit, 1+bruit}, {1+bruit, 1+bruit}};

                for (int i=0; i < entrees.length; i++) {
                    float[] entree = entrees[i];
                    // le neurone calcule sa sortie
                    n.metAJour(entree);

                    // on vérifie que notre sortie est bien le résultat attendu
                    // on différencie ici les différent Neurone qui n'ont pas la même manière de tester leur sortie
                    if (n.getClass() == NeuroneHeaviside.class) {
                        if (n.sortie() != resultats[i])
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i]);
                    } else if (n.getClass() == NeuroneSigmoide.class || n.getClass() == NeuroneReLU.class) {
                        // ici le 0 et le 1 sont déterminé si le résultat est au-dessus ou en dessous de 0.5
                        // en effet, la fonction ReLU et Sigmoïd renverra rarement 1 ou 0 exactement
                        // on doit donc s'adapter à ce type de fonction d'activation
                        if (n.sortie() < resultats[i] * 0.5f) {
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                        }
                    } else if (n.getClass() == NeuroneTanH.class) {
                        // ici le tanh fait correspondre le 0 avec le -1 et le 1 avec le 1
                        // donc on teste si la sortie est négative ou positive selon le résultat attendu
                        if (resultats[i] == 0 ? n.sortie() > 0 : n.sortie() <= 0) {
                            throw new AssertionError("Le neurone a renvoyé " + n.sortie() + " au lieu de " + resultats[i] * 0.5f);
                        }
                    }

                }
                // System.out.println("Test RÉUSSI avec un bruit de " + bruit);
            } catch(AssertionError e) {
                // si notre sortie n'est pas celle que l'on attendait, on génére une AssertionError
                // on incrémente ensuite notre compteur d'échecs et on passe au test suivant
                cptEchecs += 1;
                // chaque AssertionError a une raison, que l'on peut afficher grâce au bout de code ci-dessous
                // System.out.println("Test non réussi pour un bruit de " + bruit + "\nRaison : " + e.getMessage());
            }
        }
        // on renvoie le taux de réussite
        return 1.f - (cptEchecs / tests);
    }

    public static void main(String[] args) {
        System.out.println("Entraînement OR Heaviside");
        Neurone orH = trainNeurone(new NeuroneHeaviside(2), new float[]{0,1,1,1});
        System.out.println("Entraînement AND Heaviside");
        Neurone andH = trainNeurone(new NeuroneHeaviside(2), new float[]{0,0,0,1});

        System.out.println("Entraînement OR ReLU");
        Neurone orReLU = trainNeurone(new NeuroneReLU(2), new float[]{0,1,1,1});
        System.out.println("Entraînement AND ReLU");
        Neurone andReLU = trainNeurone(new NeuroneReLU(2), new float[]{0,0,0,1});

        System.out.println("Entraînement OR Sigmoïd");
        Neurone orSigmoid = trainNeurone(new NeuroneSigmoide(2), new float[]{0,1,1,1});
        System.out.println("Entraînement AND Sigmoïd");
        Neurone andSigmoid = trainNeurone(new NeuroneSigmoide(2), new float[]{0,0,0,1});

        // ici on change l'entraînement et on transforme les 0 en -1, conformément la courbe de tanh
        System.out.println("Entraînement OR tanh");
        Neurone ortanH = trainNeurone(new NeuroneTanH(2), new float[]{-1,1,1,1});
        System.out.println("Entraînement AND tanh");
        Neurone andtanH = trainNeurone(new NeuroneTanH(2), new float[]{-1,-1,-1,1});

        System.out.println("Test OR Heaviside");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(orH, new float[]{0,1,1,1}) * 100.f) + "%");
        System.out.println("Test AND Heaviside");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(andH, new float[]{0,0,0,1}) * 100.f) + "%");
        System.out.println("Test OR ReLU");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(orReLU, new float[]{0,1,1,1}) * 100.f) + "%");
        System.out.println("Test AND ReLU");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(andReLU, new float[]{0,0,0,1}) * 100.f) + "%");
        System.out.println("Test OR Sigmoid");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(orSigmoid, new float[]{0,1,1,1}) * 100.f) + "%");
        System.out.println("Test AND Sigmoid");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(andSigmoid, new float[]{0,0,0,1}) * 100.f) + "%");
        System.out.println("Test OR tanh");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(ortanH, new float[]{0,1,1,1}) * 100.f) + "%");
        System.out.println("Test AND tanh");
        System.out.println("Taux de réussite : " + Math.round(testNeurone(andtanH, new float[]{0,0,0,1}) * 100.f) + "%");
    }
}
