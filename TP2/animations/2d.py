from operator import ge
from manim import *

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
        current_generation = -1
        json_file = open('animations/config.json', 'r')
        self.config = json.load(json_file)

        file = open(self.config['file'], "r")

        self.grid_size = int(file.readline())
        self.domain = int(file.readline())

        while True:
            line = file.readline()

            if line == '':
                break

            maybe_cell_coords = line.split(' ')

            if len(maybe_cell_coords) < 2:
                current_generation = current_generation + 1
                self.generations.append([])
                continue
            
            x = int(maybe_cell_coords[0])
            y = int(maybe_cell_coords[1])
            self.generations[current_generation].append((x, y))

        file.close()
        json_file.close()


    def create_grid(self, num_row, num_col, side_length):
        # create cells
        cells = [Square(color=BLUE_E, side_length=side_length) for _ in range(num_row*num_col)]
        # arrange cells into a grid
        self.grid = VGroup(*cells).arrange_in_grid(rows=num_row, cols=num_col, buff=0,)

        # set cell colours based on rules
        self.update_grid(0)

    def update_grid(self, generation):
        for i in range(self.grid_size):
            for j in range(self.grid_size):
                pos = i * self.grid_size + j
                if (i, j) in self.generations[generation]:
                    self.grid[pos].set_fill(BLUE_C, opacity = 1)
                else:
                    self.grid[pos].set_fill(BLACK, opacity = 1)
