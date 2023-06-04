from math import sqrt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os

if not os.path.exists('figures'):
    os.makedirs('figures')

df = pd.read_csv('mean_flow.csv')
dt = df['dt'][0]
grouped_by_value = df.groupby(['d', 'n'])

max_t = df.loc[df['t'].idxmax()]['t']

mean_flows = []
mean_flows_err = []
ds = []

for (d, N), grouped_value in grouped_by_value:
    value_mean_flows = []
    value_max_t = grouped_value.loc[grouped_value['t'].idxmax()]['t']
    value_min_t = grouped_value.loc[grouped_value['t'].idxmin()]['t']
    t = np.arange(value_min_t, value_max_t + dt, dt)
    n = {}

    grouped_value_by_iteration = grouped_value.groupby('iteration')
    for iteration, grouped_iter in grouped_value_by_iteration:
        prev_val = 20
        i = 0
        timestamp_amount = len(grouped_iter['t'])
        for j in np.arange(len(t)):
            if i < timestamp_amount and t[j] >= grouped_iter['t'].iat[i]:
                prev_val = grouped_iter['exited_pedestrians'].iat[i]
                i += 1
            if j not in n:
                n[j] = []
            n[j].append(prev_val)

    all_vals = []
    n_vals = []
    n_errs = []

    for values_arr in n.values():
        all_vals.append(values_arr)
        np_array = np.array(values_arr)
        # value_mean_flows.append(np.polyfit(t, np_array, 1)[0])
        n_vals.append(np_array.mean())
        n_errs.append(np_array.std())

    for iter_values in np.array(all_vals).T:
        # print(iter_values)
        value_mean_flows.append(np.polyfit(t, iter_values, 1)[0])
    
    value_mean_flows = np.array(value_mean_flows)
    mean_flows.append(value_mean_flows.mean())
    mean_flows_err.append(value_mean_flows.std())
    ds.append(d)

    plt.plot(t, n_vals, label='d={}, n={}'.format(d, N))

plt.legend()
plt.savefig('figures/mean_flows.png')
plt.show()

plt.errorbar(ds, mean_flows, yerr=mean_flows_err, fmt='o', linestyle='dotted', capsize=4)
plt.xticks(ds)
plt.ylabel('Caudal medio [n/s]', fontsize=12, labelpad=8)
plt.xlabel('Ancho de la salida [m]', fontsize=12, labelpad=8)
plt.savefig('figures/mean_flows_by_d.png')
plt.show()