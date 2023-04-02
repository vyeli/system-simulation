import pandas as pd
import matplotlib.pyplot as plt

systems_strings_2d = ["2", "3", "4"]
systems_strings_3d = ["2", "3", "4"]
system_idx = 0

cells_fig_2d, cells_axs_2d = plt.subplots(3, figsize=(12, 8), layout="tight")
radius_fig_2d, radius_axs_2d = plt.subplots(3, figsize=(12, 8), layout="tight")

cells_fig_3d, cells_axs_3d = plt.subplots(3, figsize=(12, 8), layout="tight")
radius_fig_3d, radius_axs_3d = plt.subplots(3, figsize=(12, 8), layout="tight")

obs_fig_2d, obs_axs_2d = plt.subplots(3, figsize=(12, 8), layout="tight")
obs_fig_3d, obs_axs_3d = plt.subplots(3, figsize=(12, 8), layout="tight")
obs_fig_2d.xsticks = [0.15, 0.3, 0.45, 0.6, 0.75, 0.9]
obs_fig_3d.xsticks = [0.15, 0.3, 0.45, 0.6, 0.75, 0.9]

total_domain_number_of_cells_2d = 15 * 15
total_domain_number_of_cells_3d = 15 * 15 * 15

##################################### 2D #############################################################

for string in systems_strings_2d:
    legends = []
    df = pd.read_csv('./output/2d_configs_N{}.csv'.format(string))
    groupby_perc = df.groupby('porcentaje')

    for perc, grouped_table in groupby_perc:
        if perc == 0.15 or perc == 0.45 or perc == 0.75:
        # if perc is not None:
            cells = []
            radius = []
            #print(grouped_table['cant_celulas_vivas'])
            for count in grouped_table['cant_celulas_vivas']:
                live_cells_percentage = count / total_domain_number_of_cells_2d * 100
                cells.append(live_cells_percentage)
            # print(grouped_table['dist_al_centro'])
            for count in grouped_table['dist_al_centro']:
                radius.append(count)
            legends.append('{}%'.format(int(perc * 100)))
            cells_axs_2d[system_idx].plot(range(len(cells)), cells)
            radius_axs_2d[system_idx].plot(range(len(radius)), radius)
    
    x_obs = []
    y_obs = []
    y_errs_obs = []
    df_obs = pd.read_csv('./output/2d_obs_N{}.csv'.format(string))
    groupby_perc_obs = df_obs.groupby('porcentaje')

    for perc, grouped in groupby_perc_obs:
        slopes = grouped['pendiente']
        x_obs.append(perc)
        y_obs.append(slopes.mean())
        y_errs_obs.append(slopes.std())
    
    #print(x_obs)
    #print(y_obs)
    
    obs_axs_2d[system_idx].errorbar(x=x_obs, y=y_obs, yerr=y_errs_obs, linestyle='dotted', fmt='o', capsize=4, color='blue')
        #slopes = []
        #for slope in grouped['pendiente']:
        #    slopes.append(slope)
        #legends.append('{}%'.format(int(perc * 100)))
        

    cells_axs_2d[system_idx].set_title('Sistema {} en 2D'.format(system_idx+1))
    cells_axs_2d[system_idx].set_xlabel('N° de generación'.format(system_idx))
    cells_axs_2d[system_idx].set_ylabel('Porcentaje de celdas vivas %')

    radius_axs_2d[system_idx].set_title('Sistema {} en 2D'.format(system_idx+1))
    radius_axs_2d[system_idx].set_xlabel('N° de generación'.format(system_idx))
    radius_axs_2d[system_idx].set_ylabel('Distancia al centro')

    obs_axs_2d[system_idx].set_title('Sistema {} en 2D'.format(system_idx+1))
    obs_axs_2d[system_idx].set_xlabel('Porcentaje de celdas vivas en el dominio acotado')
    obs_axs_2d[system_idx].set_ylabel('Pendiente de celdas vivas')
    obs_axs_2d[system_idx].set_xticks([0.15, 0.3, 0.45, 0.6, 0.75, 0.9])

    # print something on 3d

    cells_axs_2d[system_idx].legend(legends)
    radius_axs_2d[system_idx].legend(legends)

    system_idx += 1

############################################# 3D ###################################################

system_idx = 0

for string in systems_strings_3d:
    legends = []
    df = pd.read_csv('./output/3d_configs_N{}.csv'.format(string))
    
    groupby_perc = df.groupby('porcentaje')

    for perc, grouped_table in groupby_perc:
        if perc == 0.15 or perc == 0.45 or perc == 0.75:
        # if perc is not None:
            cells = []
            radius = []
            #print(grouped_table['cant_celulas_vivas'])
            for count in grouped_table['cant_celulas_vivas']:
                live_cells_percentage = count / total_domain_number_of_cells_3d * 100
                cells.append(live_cells_percentage)
            #print(grouped_table['dist_al_centro'])
            for count in grouped_table['dist_al_centro']:
                radius.append(count)
            legends.append('{}%'.format(int(perc * 100)))
            cells_axs_3d[system_idx].plot(range(len(cells)), cells)
            radius_axs_3d[system_idx].plot(range(len(radius)), radius)
    
    x_obs = []
    y_obs = []
    y_errs_obs = []
    df_obs = pd.read_csv('./output/3d_obs_N{}.csv'.format(string))
    groupby_perc_obs = df_obs.groupby('porcentaje')

    for perc, grouped in groupby_perc_obs:
        slopes = grouped['pendiente']
        x_obs.append(perc)
        y_obs.append(slopes.mean())
        y_errs_obs.append(slopes.std())
    
    #print(x_obs)
    #print(y_obs)
    
    obs_axs_3d[system_idx].errorbar(x=x_obs, y=y_obs, yerr=y_errs_obs, linestyle='dotted', fmt='o', capsize=4, color='blue')
        #slopes = []
        #for slope in grouped['pendiente']:
        #    slopes.append(slope)
        #legends.append('{}%'.format(int(perc * 100)))

    cells_axs_3d[system_idx].set_title('Sistema {} en 3D'.format(system_idx+1))
    cells_axs_3d[system_idx].set_xlabel('N° de generación'.format(system_idx))
    cells_axs_3d[system_idx].set_ylabel('Porcentaje de celdas vivas %')

    radius_axs_3d[system_idx].set_title('Sistema {} en 3D'.format(system_idx+1))
    radius_axs_3d[system_idx].set_xlabel('N° de generación'.format(system_idx))
    radius_axs_3d[system_idx].set_ylabel('Distancia al centro')

    obs_axs_3d[system_idx].set_title('Sistema {} en 3D'.format(system_idx+1))
    obs_axs_3d[system_idx].set_xlabel('Porcentaje de celdas vivas en el dominio acotado')
    obs_axs_3d[system_idx].set_ylabel('Pendiente de celdas vivas')
    obs_axs_3d[system_idx].set_xticks([0.15, 0.3, 0.45, 0.6, 0.75, 0.9])

    cells_axs_3d[system_idx].legend(legends)
    radius_axs_3d[system_idx].legend(legends)

    system_idx += 1

plt.xlim(left=0)

cells_fig_2d.savefig("porcentaje_celdas_vivas_2d.png", dpi=800)
radius_fig_2d.savefig("distancia_al_centro_2d.png", dpi=800)
obs_fig_2d.savefig("pendiente_2d.png", dpi=800)

cells_fig_3d.savefig("porcentaje_celdas_vivas_3d.png", dpi=800)
radius_fig_3d.savefig("distancia_al_centro_3d.png", dpi=800)
obs_fig_3d.savefig("pendiente_3d.png", dpi=800)

plt.show()


