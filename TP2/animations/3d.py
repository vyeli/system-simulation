import json
import math

from gameParser import GameParser
from manim import *
from random import random, seed
from enum import Enum
from timeit import default_timer as timer

class GameOfLife3D(ThreeDScene):
    def construct(self):
        start = timer()
        # time interval between generations
        time = 0.3
        self.size = 3.5
        self.parse_generations()

        self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)
        self.begin_ambient_camera_rotation(rate=0.15)

        side_length = 7.65 / self.grid_size     # For screen adjust

        # initialise grid
        self.create_grid()
        self.wait(time)

        gens = len(self.generations)

        gens_to_iter = self.config['max_generations'] if gens > self.config['max_generations'] else gens

        # print(self.generations[0])

        # simulate game of life
        for generation in range(1, gens_to_iter):
            self.update_grid(generation)
            # self.add(self.grid)
            self.wait(time)
        
        end = timer()
        print('Execution time: {}s'.format(end - start))

    def parse_generations(self):
        self.generations = []
        json_file = open('./animations/config.json', 'r')
        self.config = json.load(json_file)

        file = open(self.config['file'], "r")

        game_parser = GameParser(self.config['file'])
        self.grid_size, self.domain, self.generations = game_parser.parse()
        print(len(self.generations))
        file.close()
        json_file.close()

#     def toggle(self, index):
#         """Toggle a given cell."""
#         if index in self.grid:
#             self.scene.remove(self.grid[index][0])
#             del self.grid[index]
#         else:
#             self.grid[index] = self.__get_new_cell(index)
#             self.scene.add(self.grid[index][0])

#     def __get_new_cell(self, index):
#         """Create a new cell"""
#         cell = (
#             Cube(
#                 side_length=1 / self.grid_size * self.size, color=BLUE, fill_opacity=1
#             ).move_to(self.__index_to_position(index)),
#             self.state_count - 1,
#         )
#
#         self.__update_cell_color(index, *cell)
#
#         return cell

#     def __update_cell_color(self, index, cell, age):
#         """Update the color of the specified cell."""
#         if self.color_type == self.ColorType.FROM_PALETTE:
#             state_colors = color_gradient(self.palette, self.state_count - 1)
#
#             cell.set_color(state_colors[age - 1])
#         else:
#
#             def coordToHex(n):
#                 return hex(int(n * (256 / self.grid_size)))[2:].ljust(2, "0")
#
#             cell.set_color(
#                 f"#{coordToHex(index[0])}{coordToHex(index[1])}{coordToHex(index[2])}"
#             )

    def update_grid(self, generation):
        prev_gen = generation - 1
        if prev_gen > -1:
            for cell in self.generations[prev_gen]:
                if cell not in self.generations[generation] and cell in self.grid:
                    self.remove(self.grid[cell])
                    del self.grid[cell]
                    # self.grid[pos].set_fill(color=BLACK, opacity = 1)
        
        for cell in self.generations[generation]:
            self.grid[cell] = Cube(side_length=1 / self.grid_size * self.size, fill_opacity=1)
            self.grid[cell].set_color(self.get_distance_color(cell[0], cell[1], cell[2]))
            self.add(self.grid[cell])
    
    def create_grid(self):
        self.grid = {}
        self.bounding_box = Cube(side_length=0.5, color=GRAY, fill_opacity=0.05)
        self.add(self.bounding_box)

        self.update_grid(0)

        # create cells
        # cells = [Square(side_length=side_length, stroke_width=1) for _ in np.arange(self.grid_size * 59)]# self.grid_size)])
        # print('CHAU 1')
        # arrange cells into a grid
        # self.grid = VGroup(cells).arrange_in_grid(rows=self.grid_size, cols=self.grid_size, buff=0,)

        # print('CHAU 2')
        # set cell colours based on rules
        # self.update_grid(0) 
    
    def get_distance_color(self, x: int, y: int, z: int):
        max_dist = self.get_center_distance(self.grid_size, self.grid_size, self.grid_size)
        dist = self.get_center_distance(x, y, z)
        return utils.color.rgb_to_color((1 - dist/max_dist, 0, dist/max_dist))
    
    def get_center_distance(self, x: int, y: int, z: int):
        center = int(self.grid_size / 2) + 1
        x_dist = math.pow(x - center, 2)
        y_dist = math.pow(y - center, 2)
        z_dist = math.pow(y - center, 2)
        return math.sqrt(x_dist + y_dist + z_dist)

# class RandomGrid:
#     class ColorType(Enum):
#         FROM_COORDINATES = 0
#         FROM_PALETTE = 1

#     def __init__(
#         self,
#         scene,
#         grid_size,
#         survives_when,
#         revives_when,
#         state_count=2,
#         size=1,
#         palette=["#000b5e", "#001eff"],
#         color_type=ColorType.FROM_PALETTE,
#     ):
#         self.grid = {}
#         self.scene = scene
#         self.grid_size = grid_size
#         self.size = size
#         self.survives_when = survives_when
#         self.revives_when = revives_when
#         self.state_count = state_count
#         self.palette = palette
#         self.color_type = color_type

#         self.bounding_box = Cube(side_length=self.size, color=GRAY, fill_opacity=0.05)
#         self.scene.add(self.bounding_box)

#     def fadeOut(self):
#         self.scene.play(
#             FadeOut(self.bounding_box),
#             *[FadeOut(self.grid[index][0]) for index in self.grid],
#         )

#     def __index_to_position(self, index):
#         """Convert the index of a cell to its position in 3D."""
#         dirs = [RIGHT, UP, OUT]

#         # be careful!
#         # we can't just add stuff to ORIGIN, since it doesn't create new objects,
#         # meaning we would be moving the origin, which messes with the animations
#         result = list(ORIGIN)
#         for dir, value in zip(dirs, index):
#             result += ((value - (self.grid_size - 1) / 2) / self.grid_size) * dir * self.size

#         return result

#     def __get_new_cell(self, index):
#         """Create a new cell"""
#         cell = (
#             Cube(
#                 side_length=1 / self.grid_size * self.size, color=BLUE, fill_opacity=1
#             ).move_to(self.__index_to_position(index)),
#             self.state_count - 1,
#         )

#         self.__update_cell_color(index, *cell)

#         return cell

#     def __return_neighbouring_cell_coordinates(self, index):
#         """Return the coordinates of the neighbourhood of a given index."""
#         neighbourhood = set()
#         for dx in range(-1, 1 + 1):
#             for dy in range(-1, 1 + 1):
#                 for dz in range(-1, 1 + 1):
#                     if dx == 0 and dy == 0 and dz == 0:
#                         continue

#                     nx = index[0] + dx
#                     ny = index[1] + dy
#                     nz = index[2] + dz

#                     # don't loop around (although we could)
#                     if (
#                         nx < 0
#                         or nx >= self.grid_size
#                         or ny < 0
#                         or ny >= self.grid_size
#                         or nz < 0
#                         or nz >= self.grid_size
#                     ):
#                         continue

#                     neighbourhood.add((nx, ny, nz))

#         return neighbourhood

#     def __count_neighbours(self, index):
#         """Return the number of neighbouring cells for a given index (excluding itself)."""
#         total = 0
#         for neighbour_index in self.__return_neighbouring_cell_coordinates(index):
#             if neighbour_index in self.grid:
#                 total += 1

#         return total

#     def __return_possible_cell_change_indexes(self):
#         """Return the indexes of all possible cells that could change."""
#         changes = set()
#         for index in self.grid:
#             changes |= self.__return_neighbouring_cell_coordinates(index).union({index})
#         return changes

#     def toggle(self, index):
#         """Toggle a given cell."""
#         if index in self.grid:
#             self.scene.remove(self.grid[index][0])
#             del self.grid[index]
#         else:
#             self.grid[index] = self.__get_new_cell(index)
#             self.scene.add(self.grid[index][0])

#     def __update_cell_color(self, index, cell, age):
#         """Update the color of the specified cell."""
#         if self.color_type == self.ColorType.FROM_PALETTE:
#             state_colors = color_gradient(self.palette, self.state_count - 1)

#             cell.set_color(state_colors[age - 1])
#         else:

#             def coordToHex(n):
#                 return hex(int(n * (256 / self.grid_size)))[2:].ljust(2, "0")

#             cell.set_color(
#                 f"#{coordToHex(index[0])}{coordToHex(index[1])}{coordToHex(index[2])}"
#             )

#     def do_iteration(self):
#         """Perform the automata generation, returning True if a state of any cell changed."""
#         new_grid = {}
#         something_changed = False

#         for index in self.__return_possible_cell_change_indexes():
#             neighbours = self.__count_neighbours(index)

#             # alive rules
#             if index in self.grid:
#                 cell, age = self.grid[index]

#                 # always decrease age
#                 if age != 1:
#                     age -= 1
#                     something_changed = True

#                 # survive if within range or age isn't 1
#                 if neighbours in self.survives_when or age != 1:
#                     self.__update_cell_color(index, cell, age)
#                     new_grid[index] = (cell, age)
#                 else:
#                     self.scene.remove(self.grid[index][0])
#                     something_changed = True

#             # dead rules
#             else:
#                 # revive if within range
#                 if neighbours in self.revives_when:
#                     new_grid[index] = self.__get_new_cell(index)
#                     self.scene.add(new_grid[index][0])
#                     something_changed = True

#         self.grid = new_grid

#         return something_changed

# class GOLSecond(ThreeDScene):
#     def construct(self):
#         seed(0xDEADBEEF)

#         self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)
#         self.begin_ambient_camera_rotation(rate=0.15)
#         size = 3.5

#         self.parse_generations()

#         grid = RandomGrid(
#             self,
#             self.grid_size,
#             [2, 6, 9],
#             [4, 6, 8, 9],
#             state_count=10,
#             size=size,
#             color_type=RandomGrid.ColorType.FROM_PALETTE,
#         )

#         # for i in range(grid_size):
#         #     for j in range(grid_size):
#         #         for k in range(grid_size):
#         #             if grid.grid[i][j][k] == 1:
#         #                 grid.toggle((i, j, k))

#         grid.toggle((5, 5, 5))
#         grid.toggle((5, 5, 6))
#         grid.toggle((5, 5, 7))
#         grid.toggle((6, 5, 5))
#         grid.toggle((6, 5, 6))
#         grid.toggle((6, 5, 7))

#         self.wait(0.5)

#         for i in range(5):
#             something_changed = grid.do_iteration()

#             if not something_changed:
#                 break

#             self.wait(1)

#         self.wait(0.5)

#         # grid.fadeOut()
    
#     def parse_generations(self):
#         self.generations = []
#         json_file = open('./animations/config.json', 'r')
#         self.config = json.load(json_file)

#         file = open(self.config['file'], "r")

#         game_parser = GameParser(self.config['file'])
#         self.grid_size, self.domain, self.generations = game_parser.parse()
#         file.close()
#         json_file.close()
