import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


def clear_time_by_y0():
    df = pd.read_csv('./execution_data.csv')

    # Calculate the clear time by initial position (y0) and generation
    clear_time = df.groupby(['generation', 'y0'])['timestamp'].last()

    # Calculate the mean clear time by initial position (y0)
    mean_clear_time = clear_time.groupby('y0').mean()

    # Calculate the standard deviation of the clear time by initial position (y0)
    std_clear_time = clear_time.groupby('y0').std()

    # Plot the mean clear time by initial position (y0) with error bars
    plt.errorbar(mean_clear_time.index, mean_clear_time.values, yerr=std_clear_time.values)

    # Define the desired x coordinates
    plt.xticks(np.linspace(42, 56, num=10))

    

    # Add axis labels and title
    plt.xlabel('Posiciones iniciales de la bola blanca (cm)')
    plt.ylabel('Tiempo medio de que desaparezcan todas las bolas (s)')

    plt.savefig('clear_time_by_y0.png', dpi=300, bbox_inches='tight')

def clear_time_by_v0():
    df = pd.read_csv('./execution_data.csv')

    # Calculate the clear time by initial position (y0) and generation
    clear_time = df.groupby(['generation', 'v0'])['timestamp'].last()

    # Calculate the mean clear time by initial position (y0)
    mean_clear_time = clear_time.groupby('v0').mean()

    # Calculate the standard deviation of the clear time by initial position (y0)
    std_clear_time = clear_time.groupby('v0').std()

    # Plot the mean clear time by initial position (y0) with error bars
    plt.errorbar(mean_clear_time.index, mean_clear_time.values, yerr=std_clear_time.values)

    # Define the desired x coordinates
    plt.xticks(np.linspace(42, 56, num=10))


    # Add axis labels and title
    plt.xlabel('Velocidades iniciales de la bola blanca (cm/s)')
    plt.ylabel('Tiempo medio de que desaparezcan todas las bolas (s)')

    plt.savefig('clear_time_by_v0.png', dpi=300, bbox_inches='tight')

    # Display the plot
    plt.show()


if __name__ == '__main__':
    clear_time_by_y0()