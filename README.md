# Projet IA/JAVA/THS du groupe YingYang
## Étapes préalables
### Logiciels requis
Avant de pouvoir lancer notre programme, vous devez avoir :
- JDK Java 20 (le code a été écrit en utilisant Java 20, il n'a pas été testé avec d'autres versions de Java)
- Gradle
- Python 3.5 ou plus (requis pour générer le dataset)
### Générer le dataset
Pour générer le dataset, vous devez lancer le script Python `create_dataset_wav.py` dans le dossier source du projet.<br>
Le dataset se générera alors automatiquement, attendez que le programme vous indique que le dataset a été généré avant de lancer le code
## Lancer le programme
### Pour Linux
Faites `./gradlew run` pour lancer le programme
### Pour Windows
Euh faut que je demande à Yin
## Accéder aux parties IA et THS
Nous avons séparé le code en 3 parties : THS, IA et DetecteurSon.<br>
Pour accéder à ces différentes parties, allez dans le code source : `src/main/java/fr/isen/iathsproject/Main.java` et décommentez la ligne de la partie que vous voulez accéder.<br>
Veillez à n'avoir qu'une seule ligne décommentée dans ce `Main.java` pour éviter tout problème.
