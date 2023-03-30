# TODO: Make observable graphs with generated data
import pandas as pd
import matplotlib.pyplot as plt

systems_strings = ["2", "3", "4"]
system_idx = 0

cells_fig, cells_axs = plt.subplots(3, figsize=(12, 8), layout="tight")
radius_fig, radius_axs = plt.subplots(3, figsize=(12, 8), layout="tight")

for string in systems_strings:
    legends = []
    df = pd.read_csv('./output/2d_configs_N{}.csv'.format(string))
    
    groupby_perc = df.groupby('porcentaje')

    for perc, grouped_table in groupby_perc:
        cells = []
        radius = []
        print(grouped_table['cant_celulas_vivas'])
        for count in grouped_table['cant_celulas_vivas']:
            cells.append(count)
        print(grouped_table['dist_al_centro'])
        for count in grouped_table['dist_al_centro']:
            radius.append(count)
        legends.append('{}%'.format(int(perc * 100)))
        cells_axs[system_idx].plot(range(len(cells)), cells)
        radius_axs[system_idx].plot(range(len(radius)), radius)

    cells_axs[system_idx].set_title('Porcentaje de celulas vivas en el sistema {} para cada generación'.format(system_idx+1))
    cells_axs[system_idx].set_xlabel('N° de generación'.format(system_idx))
    cells_axs[system_idx].set_ylabel('% de celulas vivas')
    radius_axs[system_idx].set_title('Distancia al centro de la configuración en el sistema {} para cada generación'.format(system_idx+1))
    radius_axs[system_idx].set_xlabel('N° de generación'.format(system_idx))
    radius_axs[system_idx].set_ylabel('Distancia al centro')

    cells_axs[system_idx].legend(legends)
    radius_axs[system_idx].legend(legends)

    system_idx += 1

plt.show()