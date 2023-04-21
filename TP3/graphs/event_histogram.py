import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

events = pd.read_csv('./execution_data.csv')

fig, ax = plt.subplots()

bins = [0, 0.05, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5, 20]

plt.hist(events['event_time'], bins=bins)

print(events['event_time'])

# plt.stairs(counts, bins)

# ax.plot(bins, events['event_time'], '--')

plt.xlabel('Tiempo entre eventos')
plt.ylabel('Cantidad de eventos')

# fig.tight_layout()
plt.show()