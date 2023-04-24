import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


def clear_time_by_y0():
    df = pd.read_csv('./execution_data.csv')

    # Calculate the clear time by initial position (y0) and generation
    clear_time = df.groupby(['generation', 'y0'])['timestamp'].last()

    # Remove outliers
    # clear_time = clear_time[clear_time['timestamp'] < 80000]['timestamp']

    # Calculate the mean clear time by initial position (y0)
    mean_clear_time = clear_time.groupby('y0').mean() 

    # Calculate the standard deviation of the clear time by initial position (y0)
    sem_clear_time = clear_time.groupby('y0').sem() / np.sqrt(16)

    # Plot the mean clear time by initial position (y0) with error bars
    plt.errorbar(mean_clear_time.index, mean_clear_time.values, yerr=sem_clear_time.values, fmt='o', linestyle='dotted', capsize=4)

    # Define the desired x coordinates
    plt.xticks(np.linspace(42, 56, num=10))

    plt.tight_layout()

    # Add axis labels and title
    plt.xlabel('Posición inicial de la bola blanca [cm]', fontsize=12, labelpad=10)
    plt.ylabel('Tiempo medio de desaparición [s]', fontsize=12, labelpad=10)


    plt.savefig('clear_time_by_y0.png', dpi=300, bbox_inches='tight')

def clear_time_by_v0():
    df = pd.read_csv('./execution_data_vx0.csv')

    # Calculate the clear time by initial velocity (vx0) and generation
    clear_time = df.groupby(['generation', 'vx0'])[['timestamp']].last()

    # Remove the outliers
    clear_time = clear_time[clear_time['timestamp'] < 80000]['timestamp']

    # calculate the mean clear time by initial velocity (vx0)
    mean_clear_time = clear_time.groupby('vx0').mean() 

    # Calculate the standard deviation of the clear time by initial velocity (vx0)
    sem_clear_time = clear_time.groupby('vx0').sem() / np.sqrt(16)

    # Plot the mean clear time by initial velocity (vx0) with error bars
    plt.errorbar(mean_clear_time.index, mean_clear_time.values, yerr=sem_clear_time.values, fmt='o', linestyle='dotted', capsize=4)

    # Define the desired x coordinates
    plt.xticks(np.linspace(100, 1000, num=10))


    # Add axis labels and title
    plt.tight_layout()

    plt.xlabel('Velocidad inicial de la bola blanca [cm/s]', fontsize=12, labelpad=10)
    plt.ylabel('Tiempo medio de desaparicion [s]', fontsize=12, labelpad=10)

    plt.savefig('clear_time_by_v0.png', dpi=300, bbox_inches='tight')


if __name__ == '__main__':
    clear_time_by_y0()
    plt.close()
    clear_time_by_v0()