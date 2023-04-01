import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.animation import FuncAnimation
import json 
from gameParser import GameParser

# Define the update function for the animation
def update(frame, mat, living_cells):
    mat[0].remove()
    x_coords = [cell[0] for cell in living_cells[frame]]
    y_coords = [cell[1] for cell in living_cells[frame]]
    z_coords = [cell[2] for cell in living_cells[frame]]
    mat[0] = ax.scatter(x_coords, y_coords, z_coords, s=50, depthshade=False, color='b')
    return mat


# Read the file and parse the data
generations = []
json_file = open('./animations/config.json', 'r')
config = json.load(json_file)

file = open(config['file'], "r")

game_parser = GameParser(config['file'])
gridsize, domain, generations = game_parser.parse()

file.close()
json_file.close()


# Set up the figure and axis
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.set_facecolor('k')
ax.set_xlim([0, gridsize])
ax.set_ylim([0, gridsize])
ax.set_zlim([0, gridsize])
ax.set_xticks(range(0, gridsize+1, gridsize//5))
ax.set_yticks(range(0, gridsize+1, gridsize//5))
ax.set_zticks(range(0, gridsize+1, gridsize//5))
ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')


# Create the scatter plot for the initial living cells
mat = [ax.scatter(*zip(*generations[0]), s=50, depthshade=False, color='b')]

# Animate the Game of Life
ani = FuncAnimation(fig, update, frames=len(generations), fargs=(mat, generations), interval=100, blit=False)

# Save the animation to a file
ani.save('game_of_life.mp4', fps=30, extra_args=['-vcodec', 'libx264'])

plt.show()
