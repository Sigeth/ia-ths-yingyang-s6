import matplotlib.pyplot as plt
import numpy as np

# Lecture des données
file_path = 'test1.txt'  #Mettre le fichier .txt
original_data = []
transformed_data = []

with open(file_path, 'r') as file:
    lines = file.readlines()
    for i, line in enumerate(lines):
        if line.startswith("x["):
            parts = line.split(":")[1].split("),")[0].replace("(", "").split(";")
            real = float(parts[0].strip())
            imag = float(parts[1].replace("i", "").strip())
            original_data.append((real, imag))
        if line.startswith("X["):
            parts = line.split(":")[1].split("),")[0].replace("(", "").split(";")
            real = float(parts[0].strip())
            imag = float(parts[1].replace("i", "").strip())
            transformed_data.append((real, imag))

# Caldul du module et de l'argument et transformation de la donnée 
def calculate_mod_arg(data):
    mod = [np.sqrt(re**2 + im**2) for re, im in data]
    arg = [np.arctan2(im, re) for re, im in data]
    return mod, arg

original_mod, original_arg = calculate_mod_arg(original_data)
transformed_mod, transformed_arg = calculate_mod_arg(transformed_data)

# eparation partie reel et imaginaire
original_real = [x[0] for x in original_data]
original_imag = [x[1] for x in original_data]
transformed_real = [x[0] for x in transformed_data]
transformed_imag = [x[1] for x in transformed_data]


plt.figure(figsize=(12, 12))

plt.subplot(4, 1, 1)
plt.title("Signal de base - Partie reel et imaginaire")
plt.plot(original_real, label="partie reel")
plt.plot(original_imag, label="partie imaginaire")
plt.legend()

plt.ylabel("Amplitude")

plt.subplot(4, 1, 2)
plt.title("Signal de base - Module et Argument")
plt.plot(original_mod, label="Module")
plt.plot(original_arg, label="Argument")
plt.legend()

plt.ylabel("Value")

plt.subplot(4, 1, 3)
plt.title("Signal FFT - Partie reel et imaginaire")
plt.plot(transformed_real, label="partie reel")
plt.plot(transformed_imag, label="partie imaginaire")
plt.legend()

plt.ylabel("Amplitude")

plt.subplot(4, 1, 4)
plt.title("Signal FFT - Module et Argument")
plt.plot(transformed_mod, label="Module")
plt.plot(transformed_arg, label="Argument")
plt.legend()

plt.ylabel("Value")

plt.tight_layout()
plt.show()
