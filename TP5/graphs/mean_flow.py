from math import sqrt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv('mean_flow.csv')
dt = df['dt'][0]
grouped_by_value = df.groupby(['d', 'n'])

max_t = df.loc[df['t'].idxmax()]['t']

for (d, N), grouped_value in grouped_by_value:
    value_max_t = grouped_value.loc[grouped_value['t'].idxmax()]['t']
    value_min_t = grouped_value.loc[grouped_value['t'].idxmin()]['t']
    t = np.arange(value_min_t, value_max_t + dt, dt)
    n = {}

    grouped_value_by_iteration = grouped_value.groupby('iteration')
    for iteration, grouped_iter in grouped_value_by_iteration:
        prev_val = 20
        i = 0
        count = 0
        timestamp_amount = len(grouped_iter['t'])
        for j in np.arange(len(t)):
            if i < timestamp_amount and t[j] >= grouped_iter['t'].iat[i]:
                prev_val = grouped_iter['exited_pedestrians'].iat[i]
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
    
    plt.plot(t, n_vals, label='d={}, n={}'.format(d, N))

plt.legend()
plt.show()