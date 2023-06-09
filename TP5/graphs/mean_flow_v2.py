from math import sqrt
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import json

if not os.path.exists('figures'):
    os.makedirs('figures')

f = open('config.json')
config = json.load(f)

df = pd.read_csv('mean_flow.csv')
dt = df['dt'][0]
ds = []
grouped_by_value = df.groupby(['d', 'n'])

max_t = df.loc[df['t'].idxmax()]['t']

all_ts = []

for (d, N), grouped_value in grouped_by_value:
    t = {}
    t[0] = []

    grouped_value_by_iteration = grouped_value.groupby('iteration')
    for iteration, grouped_iter in grouped_value_by_iteration:
        last_exited = 0
        t[0].append(last_exited)
        for i in np.arange(len(grouped_iter)):
            exited_pedestrians = grouped_iter['exited_pedestrians'].iat[i]
            exit_time = grouped_iter['t'].iat[i]
            if exited_pedestrians > last_exited:                # If more pedestrians exited since last registered, fill n's values until updated
                while last_exited < exited_pedestrians:
                    last_exited += 1
                    # print('last < exited')
                    # print(last_idx)
                    if last_exited not in t:
                        t[last_exited] = []
                    t[last_exited].append(exit_time)
            else:                                               # Else update exit values on pedestrian amounts
                # print('last >= exited')
                # print(exited_idx)
                t[exited_pedestrians][iteration] = exit_time
    
    t_values = []
    t_values_err = []
    
    n_values = np.arange(0, last_exited + 1)

    for times in t.values():
        np_values = np.array(times)
        t_values.append(np_values.mean())
        t_values_err.append(np_values.std())
    
    all_ts.append(t_values)
    ds.append(d)
    
    plt.plot(n_values, t_values, label='d={}, n={}'.format(d, N), linestyle='dotted')

# mean_flows = []
# mean_flows_err = []
# ds = []

# for (d, N), grouped_value in grouped_by_value:
#     value_mean_flows = []
#     value_max_t = grouped_value.loc[grouped_value['t'].idxmax()]['t']
#     value_min_t = grouped_value.loc[grouped_value['t'].idxmin()]['t']
#     t = np.arange(value_min_t, value_max_t + dt, dt)
#     n = {}

#     grouped_value_by_iteration = grouped_value.groupby('iteration')
#     for iteration, grouped_iter in grouped_value_by_iteration:
#         prev_val = config['lowerFlowLimit']
#         i = 0
#         timestamp_amount = len(grouped_iter['t'])
#         for j in np.arange(len(t)):
#             if i < timestamp_amount and t[j] >= grouped_iter['t'].iat[i]:
#                 prev_val = grouped_iter['exited_pedestrians'].iat[i]
#                 i += 1
#             if j not in n:
#                 n[j] = []
#             n[j].append(prev_val)

#     all_vals = []
#     n_vals = []
#     n_errs = []

#     for values_arr in n.values():
#         all_vals.append(values_arr)
#         np_array = np.array(values_arr)
#         n_vals.append(np_array.mean())
#         n_errs.append(np_array.std())

#     for iter_values in np.array(all_vals).T:
#         value_mean_flows.append(np.polyfit(t, iter_values, 1)[0])
    
#     value_mean_flows = np.array(value_mean_flows)
#     mean_flows.append(value_mean_flows.mean())
#     mean_flows_err.append(value_mean_flows.std())
#     ds.append(d)

#     plt.plot(t, n_vals, label='d={}, n={}'.format(d, N))

xticks = np.arange(0, int(config['particles'][3]) + 1, 40)
# plt.vlines(x=[int(config['lowerFlowLimit']), int(config['upperFlowLimit'])], ymin=0, ymax=max_t, linestyles='dashed', colors='red', label='Límites caudal cte')
plt.xticks(xticks)
plt.ylabel('Tiempo [s]', fontsize=12, labelpad=8)
plt.xlabel('Cantidad de partículas salientes', fontsize=12, labelpad=8)
plt.legend()
plt.savefig('figures/mean_flows.png')
plt.show()

mean_flows = []
mean_flows_err = []

for j in np.arange(len(all_ts)):
    n_values = np.arange(0, int(config['particles'][j]) + 1)
    ms = []
    window_size = int(config['windowSize'])
    n_size = len(n_values)
    left_window = int(window_size/2)
    window_ns = np.arange(left_window, n_size - left_window)
    max_m = None
    min_m = None

    constant_flow_values = []

    lower_limit = int(config['lowerFlowLimit'][j])
    upper_limit = int(config['upperFlowLimit'][j])

    for i in window_ns:
        if i >= left_window and i < n_size - left_window:
            m = np.polyfit(all_ts[j][i:i + window_size], n_values[i:i + window_size], 1)[0]
            ms.append(m)
            if max_m is None or m > max_m:
                max_m = m
            if min_m is None or m < min_m:
                min_m = m
            if i >= lower_limit and i <= upper_limit:
                constant_flow_values.append(m)

    constant_flow_values = np.array(constant_flow_values)
    mean_flows.append(np.mean(constant_flow_values))
    mean_flows_err.append(np.std(constant_flow_values))

    plt.plot(window_ns, ms, 'o')
    vlines = plt.vlines(x=[lower_limit, upper_limit], ymin=mean_flows[j] - 0.8, ymax=mean_flows[j] + 0.8, linestyles='dashed', colors='black')
    if j == 0:
        vlines.set_label('Límites caudal cte')

m_min = 2.6
m_max = 3
m_step = 0.01
m_possible_values = np.arange(m_min, m_max + m_step, m_step)
m_errs = []

best_m = None
m_min_err = None
m_max_err = None

for m in m_possible_values:
    s_squared = 0
    for j in np.arange(len(ds)):
        s_squared += pow((mean_flows[j] - m * ds[j]), 2) / len(ds)
    
    m_err = sqrt(s_squared)
    if m_min_err is None or m_err < m_min_err:
        m_min_err = m_err
        best_m = m
    if m_max_err is None or m_err > m_max_err:
        m_max_err = m_err    
    
    m_errs.append(m_err)

adjusted_x = ds
adjusted_y = []

for x in adjusted_x:
    adjusted_y.append(x * best_m)

plt.legend()
plt.xticks(range(0, int(config['particles'][3]) + 1, 40))
plt.ylabel('Caudal [n/s]', fontsize=12, labelpad=8)
plt.xlabel('Cantidad de partículas salientes', fontsize=12, labelpad=8)
plt.savefig('figures/windowed_flows.png')
plt.show()

plt.errorbar(ds, mean_flows, yerr=mean_flows_err, fmt='o', linestyle='none', capsize=4)
plt.plot(adjusted_x, adjusted_y)
plt.xticks(ds)
plt.ylabel('Caudal medio [n/s]', fontsize=12, labelpad=8)
plt.xlabel('Ancho de la salida [m]', fontsize=12, labelpad=8)
plt.savefig('figures/mean_flows_by_d.png')
plt.show()

plt.plot(m_possible_values, m_errs)
plt.vlines(x=[best_m], ymin=0, ymax=m_min_err, linestyles='dashed', color='black')
xticks = list(np.arange(m_min, m_max + m_step, (m_max - m_min) / 8))
xticks.append(best_m)
xticks.sort()
del xticks[xticks.index(best_m) + 1]
plt.xticks(xticks)
plt.ylabel('Error de pendiente', fontsize=12, labelpad=8)
plt.xlabel('Pendiente [n/s/m]', fontsize=12, labelpad=8)
plt.ylim((0, m_max_err * 1.1))
plt.savefig('figures/m_error_graph.png')
plt.show()