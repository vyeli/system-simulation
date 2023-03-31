import math
from operator import ge
from manim import *
from gameParser import GameParser

import json

# Source: https://github.com/SergenKaraoglan/Manim_projects
class GameOfLife(Scene):
    def construct(self):
        # time interval between generations
        time = 0.3
        self.parse_generations()

        side_length = 7.65 / self.grid_size     # For screen adjust

        # initialise grid
        self.create_grid(self.grid_size, self.grid_size, side_length)
        self.add(self.grid)
        self.wait(time)

        gens = len(self.generations)
        gens_to_iter = self.config['max_generations'] if gens > self.config['max_generations'] else gens

        # simulate game of life
        for generation in range(1, gens_to_iter):
            self.update_grid(generation)
            self.add(self.grid)
            self.wait(time)
    
    def parse_generations(self):
        self.generations = []
        json_file = open('./animations/config.json', 'r')
        self.config = json.load(json_file)

        file = open(self.config['file'], "r")

        game_parser = GameParser(self.config['file'])
        self.grid_size, self.domain, self.generations = game_parser.parse()
        file.close()
        json_file.close()

    def create_grid(self, num_row, num_col, side_length):
        # create cells
        cells = [Square(side_length=side_length) for _ in range(num_row*num_col)]
        # arrange cells into a grid
        self.grid = VGroup(*cells).arrange_in_grid(rows=num_row, cols=num_col, buff=0,)

        # set cell colours based on rules
        self.update_grid(0)

    def update_grid(self, generation):
        prev_gen = generation - 1
        if prev_gen > -1:
            for (i, j) in self.generations[prev_gen]:
                pos = i * self.grid_size + j
                if not (i, j) in self.generations[generation]:
                    self.grid[pos].set_fill(color=BLACK, opacity = 1)
        
        for (i, j) in self.generations[generation]:
            pos = i * self.grid_size + j
            self.grid[pos].set_fill(color=self.get_distance_color(i, j), opacity = 1)
        # for i in range(self.grid_size):
        #     for j in range(self.grid_size):
        #         pos = i * self.grid_size + j
        #         if (i, j) in self.generations[generation]:
        #             self.grid[pos].set_fill(self.get_distance_color(i, j, self.grid_size), opacity = 1)
        #         else:
        #             self.grid[pos].set_fill(BLACK, opacity = 1)
    
    def get_distance_color(self, x: int, y: int):
        max_dist = self.get_center_distance(self.grid_size, self.grid_size)
        dist = self.get_center_distance(x, y)
        return utils.color.rgb_to_color((dist/max_dist, 0, 1 - dist/max_dist))
    
    def get_center_distance(self, x: int, y: int):
        center = int(self.grid_size / 2) + 1
        x_dist = math.pow(x - center, 2)
        y_dist = math.pow(y - center, 2)
        return math.sqrt(x_dist + y_dist)
