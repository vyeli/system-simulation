from math import sqrt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import json
import os

from mpl_toolkits.axes_grid1.inset_locator import mark_inset

if not os.path.exists('figures'):
    os.makedirs('figures')

f = open('config.json')
config = json.load(f)
df = pd.read_csv('constant_flow.csv')

fig, ax = plt.subplots()

max_t = df.loc[df['t'].idxmax()]['t']
grouped_by_iteration = df.groupby('iteration')
t = {}
t[0] = []

for iteration, grouped_iter in grouped_by_iteration:
    last_exited = 0
    t[0].append(0)
    for i in np.arange(len(grouped_iter)):
        exited_pedestrians = grouped_iter['exited_pedestrians'].iat[i]
        exit_time = grouped_iter['t'].iat[i]
        if exited_pedestrians > last_exited:                # If more pedestrians exited since last registered, fill n's values until updated
            while last_exited < exited_pedestrians:
                last_exited += 1
                if last_exited not in t:
                    t[last_exited] = []
                t[last_exited].append(exit_time)
        else:                                               # Else update exit values on pedestrian amounts
            t[exited_pedestrians][iteration] = exit_time

t_values = []
t_values_err = []
n_values = np.arange(0, config['particles'][0] + 1)

for times in t.values():
    np_values = np.array(times)
    t_values.append(np_values.mean())
    t_values_err.append(np_values.std())

xticks = np.arange(0, config['particles'][0] + 1, 20)
plt.xticks(xticks)
plt.xlabel('Tiempo [s]', fontsize=12, labelpad=8)
plt.ylabel('Cantidad de partículas salientes', fontsize=12, labelpad=8)

plt.plot(t_values, n_values)

plt.savefig('figures/discharge_curve_example.png')
plt.show()