package fr.isen.neurone;

public class NeuroneSigmoide extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return (float) (1.f / (1.f * Math.exp(0.f-valeur)));}

    // Constructeur
    public NeuroneSigmoide(final int nbEntrees) {super(nbEntrees);}
}
