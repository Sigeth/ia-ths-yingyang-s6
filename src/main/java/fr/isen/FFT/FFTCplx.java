package fr.isen.FFT;

public class FFTCplx
{
	public final static int TailleFFTtest = 16;
	public final static double Periode = 1;

 	private int taille;

 // Indiquer la taille permettra des optimisations par la suite
 	public FFTCplx(int taille)
 	{
 		this.taille = taille;
 	}

	// Sous-signal obligatoirement découpé par pas de deux
	private static Complexe[] demiSignal(Complexe[] signal, int depart)
	{
		Complexe[] sousSignal = new Complexe[signal.length/2];
		for (int i = 0; i < sousSignal.length; ++i)
			sousSignal[i] = signal[depart+2*i];
		return sousSignal;
	}
	
	public static Complexe[] appliqueSur(Complexe[] signal)
	{
		Complexe[] trSignal = new Complexe[signal.length];
		if (signal.length == 1)	// Cas trivial de la FFT d'une valeur unique
			trSignal[0] = new ComplexeCartesien(signal[0].reel(), signal[0].imag());
		else
		{
			// Découpage des deux sous-groupes de données sur lesquels on applique la FFT
			// (voir définition de cours)
			final Complexe[] P0 = appliqueSur(demiSignal(signal, 0));
			final Complexe[] P1 = appliqueSur(demiSignal(signal, 1));
			// On applique le regroupement "papillon" pour créer le résultat final
			for (int k = 0; k < signal.length/2; ++k)
			{
				final ComplexePolaire expo = new ComplexePolaire(1., -2.*Math.PI*k/signal.length);
				final Complexe temp = P0[k];
				trSignal[k] = temp.plus(expo.fois(P1[k]));
				trSignal[k+signal.length/2] = temp.moins(expo.fois(P1[k]));
			}
		}
		return trSignal;
	}

	public static void main(String[] args)
	{
		// Création d'un signal test simple
		Complexe[] signalTest = new Complexe[TailleFFTtest];
		for (int i = 0; i < TailleFFTtest; ++i) {
			if (args.length == 1) {
				switch (args[0]) {
					case "cosReel":
						signalTest[i] = new ComplexeCartesien(Math.cos(2. * Math.PI * i / TailleFFTtest * Periode), 0);
						break;
					case "sinReel":
						signalTest[i] = new ComplexeCartesien(Math.sin(2. * Math.PI * i / TailleFFTtest * Periode), 0);
						break;
					case "cosImag":
						signalTest[i] = new ComplexeCartesien(0, Math.cos(2. * Math.PI * i / TailleFFTtest * Periode));
						break;
					case "sinImag":
						signalTest[i] = new ComplexeCartesien(0, Math.sin(2. * Math.PI * i / TailleFFTtest * Periode));
						break;
				}
			} else {
				signalTest[i] = new ComplexeCartesien(Math.cos(2. * Math.PI * i / TailleFFTtest * Periode), 0);
			}
		}
		// On applique la FFT sur ce signal
		Complexe[] resultat = appliqueSur(signalTest);
		// On affiche les valeurs du résultat et de l'entrée
		for (int i = 0; i < resultat.length; ++i) {
			System.out.println("=======");
			System.out.print("x[" + i + "] : ("+(float)signalTest[i].reel()+" ; "+(float)signalTest[i].imag()+"i)");
			System.out.println(", ("+(float)signalTest[i].mod()+" ; "+(float)signalTest[i].arg()+" rad)");
			System.out.print("X[" + i + "] : ("+(float)resultat[i].reel()+" ; "+(float)resultat[i].imag()+"i)");
			System.out.println(", ("+(float)resultat[i].mod()+" ; "+(float)resultat[i].arg()+" rad)");
		}
	}
}
