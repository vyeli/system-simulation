import math
from operator import ge
from manim import *
from gameParser import GameParser
import numpy as np

import json
from timeit import default_timer as timer

# Source: https://github.com/SergenKaraoglan/Manim_projects
class GameOfLife(Scene):
    def construct(self):
        start = timer()
        self.size = 7.65
        # time interval between generations
        time = 0.3
        self.parse_generations()

        self.side_length = 7.65 / self.grid_size     # For screen adjust

        # initialise grid
        self.create_grid()
        # self.add(self.grid)
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


    def create_grid(self):
        self.grid = {}

        for x in np.arange(self.grid_size):
            for y in np.arange(self.grid_size):
                self.grid[(x, y)] = Square(side_length=self.side_length, stroke_width=1).move_to(self.__index_to_position((x, y)))
                if (x, y) in self.generations[0]:
                    self.grid[(x, y)].set_fill(self.get_distance_color(x, y), opacity=1)
                else:
                    self.grid[(x, y)].set_fill(BLACK, opacity=1)
                self.add(self.grid[(x, y)])

    
    def update_grid(self, generation):
        prev_gen = generation - 1
        if prev_gen > -1:
            for cell in self.generations[prev_gen]:
                if cell not in self.generations[generation]:
                    self.grid[cell].set_fill(BLACK, opacity=1)
        
        for cell in self.generations[generation]:
            self.grid[cell].set_fill(self.get_distance_color(cell[0], cell[1]), opacity=1)

    
    def get_distance_color(self, x: int, y: int):
        max_dist = self.get_center_distance(self.grid_size, self.grid_size)
        dist = self.get_center_distance(x, y)
        return utils.color.rgb_to_color((1 - dist/max_dist, 0, dist/max_dist))

    
    def get_center_distance(self, x: int, y: int):
        center = int(self.grid_size / 2) + 1
        x_dist = math.pow(x - center, 2)
        y_dist = math.pow(y - center, 2)
        return math.sqrt(x_dist + y_dist)


    def __index_to_position(self, index):
        """Convert the index of a cell to its position in 3D."""
        dirs = [UP, RIGHT]

        # be careful!
        # we can't just add stuff to ORIGIN, since it doesn't create new objects,
        # meaning we would be moving the origin, which messes with the animations
        result = list(ORIGIN)
        for dir, value in zip(dirs, index):
            result += ((value - (self.grid_size - 1) / 2) / self.grid_size) * dir * self.size

        return result