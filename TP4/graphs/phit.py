import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import math

# Importing the dataset
df = pd.read_csv('phit.csv')


# "k", "t", "n", "x", "y"
# "k" is the dt
# "t" is the time
# "n" is the number of the ball
# "x" is the x coordinate
# "y" is the y coordinate

df_diff = df.groupby(['k', 'n', 't'])
df_norm = df_diff.apply(lambda x: np.linalg.norm(x[['x', 'y']].diff(), axis=1))

df_phi = df_norm.groupby(['k', 'n']).sum()

# Group the df_phi data by k and plot each group separately
for k, group in df_phi.groupby('k'):
    group.plot(label='k={}'.format(k), marker='o')

# Add labels and legend to the plot
plt.xlabel('Time')
plt.ylabel('Φk(t)')
plt.legend()

# Show the plot
plt.show()
