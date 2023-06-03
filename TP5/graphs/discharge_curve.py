import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv('removedPedestrians.csv')

iterations = 20
grouped_by_iteration = df.groupby('iteration')
dt = df['dt'][0]

max_t = df.loc[df['t'].idxmax()]['t']
t = np.arange(0, max_t + dt, dt)
n = [0] * len(t)              # With 200 initial balls

# print(t)

for iteration, grouped_iter in grouped_by_iteration:
    n[0] = 0
    prev_val = 0
    i = 0
    count = 0
    timestamp_amount = len(grouped_iter['t'])
    # print(grouped_iter['t'][0])
    for j in np.arange(1, len(n)):
        if i < timestamp_amount and t[j] >= grouped_iter['t'].iat[i]:
            prev_val = grouped_iter['removed_pedestrians'].iat[i] / iterations
            i += 1
        n[j] += prev_val
    # print(count)

# print(n)

plt.gca().invert_yaxis()
plt.plot(n, t)
plt.show()
