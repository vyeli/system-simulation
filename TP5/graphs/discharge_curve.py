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
axins = ax.inset_axes([0.65, 0.15, 0.3, 0.3])

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

plt.errorbar(t_values, n_values, xerr=t_values_err, linestyle='dashed')
axins.errorbar(t_values, n_values, xerr=t_values_err, linestyle='dashed')

axins.set_ylim(168.4, 172.6)
axins.set_xlim(50.4, 52.5)

mark_inset(ax, axins, loc1=1, loc2=2, fc="none", ec="0.5")

plt.savefig('figures/discharge_curve.png')
plt.show()

ms = []
window_size = int(config['windowSize'])
n_size = len(n_values)
left_window = int(window_size/2)
window_ns = np.arange(left_window, n_size - left_window)
max_m = None
min_m = None

for i in window_ns:
    if i >= left_window and i < n_size - left_window:
        m = np.polyfit(t_values[i:i + window_size], n_values[i:i + window_size], 1)[0]
        ms.append(m)
        if max_m is None or m > max_m:
            max_m = m
        if min_m is None or m < min_m:
            min_m = m

plt.plot(window_ns, ms, 'o')
plt.vlines(x=[int(config['lowerFlowLimit'][0]), int(config['upperFlowLimit'][0])], ymin=min_m, ymax=max_m, linestyles='dashed', colors='red', label='Límites caudal cte')
plt.legend()
plt.xticks(range(0, 201, 20))
plt.ylabel('Caudal [n/s]', fontsize=12, labelpad=8)
plt.xlabel('Cantidad de partículas salientes', fontsize=12, labelpad=8)
plt.savefig('figures/windowed_constant_flow.png')
plt.show()

f.close()