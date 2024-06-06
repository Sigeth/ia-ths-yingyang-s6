package fr.isen.neurone;

public class NeuroneTanH extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return (float) ((Math.exp(valeur) - Math.exp(0 - valeur)) / (Math.exp(valeur) + Math.exp(0 - valeur)));}

    // Constructeur
    public NeuroneTanH (final int nbEntrees) {super(nbEntrees);}
}

