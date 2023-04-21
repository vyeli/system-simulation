from cmath import sqrt
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def events_pdf():
    y0_plots = [42.0, 48.22222222222222, 56.0]

    df = pd.read_csv('./execution_data.csv')

    for y0, grouped_by_y0 in df.groupby('y0'):
        if y0 in y0_plots:
            event_times = grouped_by_y0['event_time']

            hist, bin_egdes = np.histogram(event_times, bins='scott')
            bins = len(hist)

            y = []
            for i in range(bins):
                y.append(hist[i] / ((bin_egdes[i+1] - bin_egdes[i]) * bins))

            plt.loglog(y, label=round(y0, 3))

    plt.legend()

    plt.xlabel('Tiempo entre eventos [s]', fontsize=12, labelpad=10)
    plt.ylabel('Densidad de probabilidad', fontsize=12, labelpad=10)

    plt.tight_layout()
    plt.show()


def events_mean_time():
    df = pd.read_csv('./execution_data.csv')
    x = []
    y = []
    yerrs = []

    for y0, grouped_by_y0 in df.groupby('y0'):
        event_times = grouped_by_y0['event_time']

        x.append(y0)
        y.append(event_times.mean())
        yerrs.append(event_times.std() / sqrt(len(event_times)))

    plt.errorbar(x=x, y=y, yerr=yerrs, linestyle='dotted', fmt='o', capsize=4)
    plt.xticks(x)

    plt.xlabel('Posición inicial [cm]', fontsize=12, labelpad=10)
    plt.ylabel('Tiempo medio [s]', fontsize=12, labelpad=10)

    plt.yscale('log')

    plt.tight_layout()
    plt.show()

def events_mean_frecuency():
    df = pd.read_csv('./execution_data.csv')
    x = []
    y = []
    yerrs = []

    for y0, grouped_by_y0 in df.groupby('y0'):
        events_frec = 1 / grouped_by_y0['event_time']

        x.append(y0)
        y.append(events_frec.mean())
        yerrs.append(events_frec.std() / sqrt(len(events_frec)))

    plt.errorbar(x=x, y=y, yerr=yerrs, linestyle='dotted', fmt='o', capsize=4)
    plt.xticks(x)

    plt.xlabel('Posición inicial [cm]', fontsize=12, labelpad=10)
    plt.ylabel('Frecuencia media [1/s]', fontsize=12, labelpad=10)

    plt.yscale('log')

    plt.tight_layout()
    plt.show()

events_pdf()
events_mean_time()
events_mean_frecuency()