import json
import math
import numpy as np

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
        file.close()
        json_file.close()


    def update_grid(self, generation):
        prev_gen = generation - 1
        if prev_gen > -1:
            for cell in self.generations[prev_gen]:
                if cell not in self.generations[generation] and cell in self.grid:
                    self.grid[cell].set_fill(opacity=0)
        
        for cell in self.generations[generation]:
            if cell not in self.grid:
                self.grid[cell] = Cube(side_length=1 / self.grid_size * self.size).move_to(self.__index_to_position(cell))
                self.grid[cell].set_fill(color=self.get_distance_color(cell[0], cell[1], cell[2]), opacity=1)
                self.add(self.grid[cell])
            self.grid[cell].set_fill(opacity=1)

    
    def create_grid(self):
        self.grid = {}

        self.bounding_box = Cube(side_length=self.size, color=GRAY, fill_opacity=0.05)
        self.add(self.bounding_box)

        self.update_grid(0)

    
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

    
    def __index_to_position(self, index):
        """Convert the index of a cell to its position in 3D."""
        dirs = [RIGHT, UP, OUT]

        # be careful!
        # we can't just add stuff to ORIGIN, since it doesn't create new objects,
        # meaning we would be moving the origin, which messes with the animations
        result = list(ORIGIN)
        for dir, value in zip(dirs, index):
            result += ((value - (self.grid_size - 1) / 2) / self.grid_size) * dir * self.size

        return result
