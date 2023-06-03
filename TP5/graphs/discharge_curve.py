from math import sqrt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv('removedPedestrians.csv')

iterations = 20
grouped_by_iteration = df.groupby('iteration')
dt = df['dt'][0]

max_t = df.loc[df['t'].idxmax()]['t']
t = np.arange(0, max_t + dt, dt)
n = {}              # With 200 initial balls

# print(t)

for iteration, grouped_iter in grouped_by_iteration:
    if 0 not in n:
        n[0] = []
    n[0].append(0)
    prev_val = 0
    i = 0
    count = 0
    timestamp_amount = len(grouped_iter['t'])
    for j in np.arange(1, len(t)):
        if i < timestamp_amount and t[j] >= grouped_iter['t'].iat[i]:
            prev_val = grouped_iter['removed_pedestrians'].iat[i]
            i += 1
        if j not in n:
            n[j] = []
        n[j].append(prev_val)


n_vals = []
n_errs = []

for values_arr in n.values():
    np_array = np.array(values_arr)
    n_vals.append(np_array.mean())
    n_errs.append(np_array.std())

plt.vlines(x=[20, 170], ymin=0, ymax=max_t, linestyles='dashed', colors='red', label='Límites caudal cte')
plt.xticks(range(0, 201, 20))
plt.ylabel('Tiempo [s]', fontsize=12, labelpad=8)
plt.xlabel('Cantidad de partículas salientes', fontsize=12, labelpad=8)

plt.legend()
plt.gca().invert_yaxis()
plt.errorbar(n_vals, t, xerr=n_errs)
plt.show()
