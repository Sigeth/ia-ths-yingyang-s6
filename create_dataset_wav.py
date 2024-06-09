import math
import struct
import wave
from pathlib import Path # UTILISABLE UNIQUEMENT AVEC PYTHON >= 3.5
import os

import numpy as np

IS_STEREO = False
SAMPLE_RATE = 22050.0  # hertz
NUM_SECONDS = 5.0  # seconds
FREQUENCY = 420.0  # hertz
VOLUME = 10 * 100
ROOT_DIR = "src/main/resources/dataset"  # dirpath
signalI = 0
freqI = 0

for signal in ["bruit", "noisy_sinus", "noisy_carre", "carre", "sinus"]:
    for freq in range(20, 20000, 100):
        log = f"Génération du dataset............{math.floor(((signalI*200 + freqI) / 1000) * 100)}%\n"
        Path(f"{ROOT_DIR}/{signal}").mkdir(parents=True, exist_ok=True)
        if signal == "noisy_sinus" or signal == "noisy_carre" or signal == "bruit":
            noise = np.random.normal(0, 100, int(NUM_SECONDS * SAMPLE_RATE))
        FREQUENCY = freq
        OUTPUT_FILE = f"{ROOT_DIR}/{signal}/{freq}.wav"
        file = wave.open(OUTPUT_FILE, 'wb')
        file.setnchannels(2 if IS_STEREO else 1)
        file.setsampwidth(2)
        file.setframerate(SAMPLE_RATE)

        assert NUM_SECONDS > 0.0, 'Duration must be higher than 0 seconds.'
        assert 0 <= FREQUENCY <= 20000.0, 'Wave frequency must be positive and lesser than 20000 Hz.'
        assert 100 <= VOLUME <= 1000.0, 'Volume must be higher than 0 and lesser than 100.'

        for i in range(int(NUM_SECONDS * SAMPLE_RATE)):
            if signal == "sinus":
                value = int(VOLUME * math.sin(2 * FREQUENCY * math.pi * float(i) / float(SAMPLE_RATE)))
            elif signal == "carre":
                ampli = 1.0 if i % freq < math.floor(freq/2.0) else 0.0
                value = int(VOLUME * ampli)
            elif signal == "noisy_sinus":
                value = int(VOLUME * math.sin(2 * FREQUENCY * math.pi * float(i) / float(SAMPLE_RATE)))
                value = int(value + noise[i])
            elif signal == "noisy_carre":
                ampli = 1.0 if i % freq < math.floor(freq / 2.0) else 0.0
                value = int(VOLUME * ampli)
                value = int(value + noise[i])
            elif signal == "bruit":
                value = int(noise[i])
            data = struct.pack('<hh', value, value) if IS_STEREO else struct.pack('<h', value)
            file.writeframesraw(data)
        freqI += 1
        log += f"Génération du signal {signal}........{math.floor((freqI / 200) * 100)}%\n"
        log = "\n"*100 + log
        print(log)

        file.close()
    freqI = 0
    signalI += 1
print("Dataset de 1000 sons généré !")
