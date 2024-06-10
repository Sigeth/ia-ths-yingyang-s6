# Projet IA/JAVA/THS du groupe YingYang
## Étapes préalables
### Logiciels requis
Avant de pouvoir lancer notre programme, vous devez avoir :
- JDK Java 20 (le code a été écrit en utilisant Java 20, il n'a pas été testé avec d'autres versions de Java)
- Python 3.5 ou plus (requis pour générer le dataset)
- Gradle (optionnel)
### Générer le dataset
Pour générer le dataset, vous devez lancer le script Python `create_dataset_wav.py` dans le dossier source du projet.<br>
Le dataset se générera alors automatiquement, attendez que le programme vous indique que le dataset a été généré avant de lancer le code
## Lancer le programme
### Avec Gradle
Faites `./gradlew run` pour lancer le programme
### Sans Gradle...
Vous pouvez utiliser l'archive disponible dans le dossier racine `ia-ths-yingyang-s6-1.0-SNAPSHOT.jar`<br>
Pour lancer cette archive, vous devez lancer dans le terminal de votre machine
`java -jar ia-ths-yingyang-s6-1.0-SNAPSHOT.jar`<br><br>
**VEILLEZ À UTILISER JAVA 20 SINON LE PROGRAMME PEUT NE PAS COMPILER** (si vous n'êtes pas sûr, faites `java -version`)
## Accéder aux parties IA et THS
Nous avons séparé le code en 3 parties : THS, IA et DetecteurSon.<br>
Pour accéder à ces différentes parties, allez dans le code source : `src/main/java/fr/isen/iathsproject/Main.java` et décommentez la ligne de la partie que vous voulez accéder.<br>
Veillez à n'avoir qu'une seule ligne décommentée dans ce `Main.java` pour éviter tout problème.
## Visualisation FFT
Dans `src/main/java/fr/isen/FFT` vous trouverez le fichier `courbe_test_FFT.py` permettant la visualisation du résultat de `FFTCplx.java`.
Pour l'utiliser lancer `FFTCplx.java`, copier/coller le réultat dans un fichier .txt que vous enregistrez dans le meme dossier.
Modifier le nom du fichier avec le votre dans le code python et lancer le.

