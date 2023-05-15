import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv('execution_data.csv')

grouped_by_y0 = df.groupby('y0')

x = []
y = []
yerr = []

for name, grouped_y0 in grouped_by_y0:
    mean_val = np.mean(grouped_y0['duration'])
    err = np.std(grouped_y0['duration'])

    x.append(float(name) * 100)
    y.append(mean_val)
    yerr.append(err)
    
plt.errorbar(x, y, yerr=yerr, fmt='o', linestyle='dotted', capsize=4)

plt.xlabel('Posición inicial de la bola blanca [cm]', fontsize=12, labelpad=10)
plt.ylabel('Tiempo medio de desaparición [s]', fontsize=12, labelpad=10)

plt.show()