import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

y0_plots = [42.0, 48.22222222222222, 56.0]

fig, ax = plt.subplots()

df = pd.read_csv('./execution_data.csv')

for y0, grouped_by_y0 in df.groupby('y0'):
    print(y0)
    if y0 in y0_plots:
        event_times = grouped_by_y0['event_time']

        hist, bin_egdes = np.histogram(event_times, bins='scott')
        bins = len(hist)

        y = []
        for i in range(bins):
            y.append(hist[i] / ((bin_egdes[i+1] - bin_egdes[i]) * bins))

        plt.loglog(y, label=round(y0, 3))

plt.legend()
#data = df['event_time']

#bin_width = 3.5 * np.std(data) / np.power(len(data), 1/3)
#bins = int((np.max(data) - np.min(data)) / bin_width)

# plt.hist(events['event_time'], bins='scott')

# plt.stairs(counts, bins)

# ax.plot(bins, events['event_time'], '--')

plt.xlabel('Tiempo entre eventos [s]')
plt.ylabel('Densidad de probabilidad')

#plt.xlim(0, 50)
#plt.ylim(0, 40000)

# fig.tight_layout()
plt.show()