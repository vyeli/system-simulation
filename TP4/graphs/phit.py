import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import math

from sklearn.metrics.pairwise import euclidean_distances

# Importing the dataset
df = pd.read_csv('phit.csv')

n = df['n'].unique()

x = []
phi3 = []
phi4 = []
phi5 = []
# "k", "t", "n", "x", "y"
# "k" is the dt
# "t" is the time
# "n" is the number of the ball
# "x" is the x coordinate
# "y" is the y coordinate

df['t'] = df['t'].round(3)
group_by_t = df.groupby('t')

for t, grouped_t in group_by_t:
    x.append(float(t))

    phi3_dist = 0
    phi4_dist = 0
    phi5_dist = 0

    for n_val in n:
        points = grouped_t.loc[grouped_t['n'] == n_val]

        phi3_value = points.loc[points['k'] == 3]
        phi4_value = points.loc[points['k'] == 4]
        phi5_value = points.loc[points['k'] == 5]
        phi6_value = points.loc[points['k'] == 6]

        # print(phi3_value)
        # print(phi4_value)
        # print(phi5_value)
        # print(phi6_value)

        # phi3_dist += math.sqrt(math.pow(phi3_value['x'] - phi4_value['x'], 2) + math.pow(phi3_value['y'] - phi4_value['y'], 2))
        # phi4_dist += math.sqrt(math.pow(phi4_value['x'] - phi5_value['x'], 2) + math.pow(phi4_value['y'] - phi5_value['y'], 2))
        # phi5_dist += math.sqrt(math.pow(phi5_value['x'] - phi6_value['x'], 2) + math.pow(phi5_value['y'] - phi6_value['y'], 2))
        
        phi3_point = np.array([phi3_value['x'], phi3_value['y']])
        phi4_point = np.array([phi4_value['x'], phi4_value['y']])
        phi5_point = np.array([phi5_value['x'], phi5_value['y']])
        phi6_point = np.array([phi6_value['x'], phi6_value['y']])

        phi3_dist += np.linalg.norm(phi3_point - phi4_point)
        phi4_dist += np.linalg.norm(phi4_point - phi5_point)
        phi5_dist += np.linalg.norm(phi5_point - phi6_point)

    phi3.append(phi3_dist)
    phi4.append(phi4_dist)
    phi5.append(phi5_dist)
    print(t)

    # print(grouped_t.loc[grouped_t['n'] == 3])

# print(phi3)

plt.plot(x, phi3, label='k=3')
plt.plot(x, phi4, label='k=4')
plt.plot(x, phi5, label='k=5')

plt.xlabel('Tiempo [s]')
plt.ylabel('Φk(t)')

plt.yscale('log')
plt.legend()

plt.show()

# df_diff = df.groupby(['k', 'n', 't'])

# vector of the position of each ball


# df_norm = df_diff.apply(lambda x: np.linalg.norm(x[['x', 'y']].diff(), axis=1))

# df_phi = df_norm.groupby(['k', 'n']).sum()

# # Group the df_phi data by k and plot each group separately
# for k, group in df_phi.groupby('k'):
#     group.plot(label='k={}'.format(k), marker='o')

# # Add labels and legend to the plot
# plt.xlabel('Time')
# plt.ylabel('Φk(t)')
# plt.legend()

# # Show the plot
# plt.show()
