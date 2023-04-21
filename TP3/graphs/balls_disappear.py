import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

df = pd.read_csv('./execution_data.csv')

# Calculate the clear time by initial position (y0) and generation
clear_time = df.groupby(['generation', 'y0'])['timestamp'].last()

# Calculate the mean clear time by initial position (y0)
mean_clear_time = clear_time.groupby('y0').mean()

# Calculate the standard deviation of the clear time by initial position (y0)
std_clear_time = clear_time.groupby('y0').std()

# Plot the mean clear time by initial position (y0) with error bars
plt.errorbar(mean_clear_time.index, mean_clear_time.values, yerr=std_clear_time.values)


# Add axis labels and title
plt.xlabel('Posiciones iniciales de la bola blanca (cm)')
plt.ylabel('Tiempo medio de que desaparezcan todas las bolas (s)')
plt.title('Tiempo promedio y desviación estándar para que desaparezcan todas las bolas frente a la posición inicial')

# Display the plot
plt.show()

