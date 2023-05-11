import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from mpl_toolkits.axes_grid1.inset_locator import mark_inset

df = pd.read_csv('oscillator_methods.csv')

fig, ax = plt.subplots(figsize=(12, 8))
axins = ax.inset_axes([0.65, 0.65, 0.3, 0.3])

grouped_dt = df.loc[df['dt'] == 0.01]
grouped_methods = grouped_dt.groupby('method')

for name, grouped in grouped_methods:
    ax.plot(grouped['t'], grouped['value'], label=name)
    axins.plot(grouped['t'], grouped['value'])

axins.set_xlim(4.28, 4.34)
axins.set_ylim(-0.01, 0.04)

mark_inset(ax, axins, loc1=3, loc2=4, fc="none", ec="0.5")

ax.legend(loc=4)
plt.show()