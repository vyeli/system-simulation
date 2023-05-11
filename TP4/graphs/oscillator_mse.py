import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from sklearn.metrics import mean_squared_error

df = pd.read_csv('oscillator_methods.csv')

grouped_by_dt = df.groupby(['dt'])

x = []
verlet_mse = []
beeman_mse = []
gear_pred_mse = []

for dt, grouped_dt in grouped_by_dt:
    verlet_group = grouped_dt.loc[grouped_dt['method'] == 'verlet']
    beeman_group = grouped_dt.loc[grouped_dt['method'] == 'beeman']
    gear_pred_group = grouped_dt.loc[grouped_dt['method'] == 'gear_pred']
    analytic_group = grouped_dt.loc[grouped_dt['method'] == 'analytic']

    x.append(dt)
    verlet_mse.append(mean_squared_error(verlet_group['value'], analytic_group['value']))
    beeman_mse.append(mean_squared_error(beeman_group['value'], analytic_group['value']))
    gear_pred_mse.append(mean_squared_error(gear_pred_group['value'], analytic_group['value']))
    # verlet_mse.append(((verlet_group['value'] - analytic_group['value'])**2).mean(axis=None))
    # beeman_mse.append(((beeman_group['value'] - analytic_group['value'])**2).mean(axis=None))
    # gear_pred_mse.append(((gear_pred_group['value'] - analytic_group['value'])**2).mean(axis=None))

plt.loglog(x, verlet_mse, '-o', label='verlet')
plt.loglog(x, beeman_mse, '-o', label='beeman')
plt.loglog(x, gear_pred_mse, '-o', label='gear_pred')

plt.xlabel('Paso simulado [s]', fontsize=12, labelpad=10)
plt.ylabel('Error cuadrático medio', fontsize=12, labelpad=10)

plt.xticks()
plt.legend()
plt.show()