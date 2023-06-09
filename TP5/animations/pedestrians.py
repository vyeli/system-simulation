import json
from manim import *
import numpy as np
import math

class Box(Scene):
    def construct(self):
        f = open('config.json')
        self.config = json.load(f)
        self.box_width = self.resize_to_graph(20)
        self.hole_width = self.resize_to_graph(float(self.config['doorWidth'][int(self.config['animationCaseIdx'])]))

        self.balls_min_radius = self.resize_to_graph(0.1)
        self.balls_max_radius = self.resize_to_graph(0.37)

        self.max_neighbours = 0
        self.generations = self.parse_pedestrians_file(self.config['animationFile'])
        print('Gens: {}'.format(len(self.generations)))
        table = Rectangle(width=self.box_width, height=self.box_width, stroke_width=0.75)
        hole = Line([-self.hole_width/2, -self.box_width/2, 0], [self.hole_width/2, -self.box_width/2, 0]).set_color('#000')
        
        self.add(table, hole)
        
        pedestrians = []
        for pedestrian in self.generations[0]['pedestrians']:
            pedestrian_radius = pedestrian['radius']
            new_pedestrian = Circle(radius = pedestrian_radius, stroke_width=0, fill_opacity=1)
            new_pedestrian.set_color(self.get_density_color(pedestrian['neighbourAmount']))
            new_pedestrian.shift(RIGHT * pedestrian['x_pos'], UP * pedestrian['y_pos'])
            new_pedestrian.next_pos = [pedestrian['x_pos'] + pedestrian['x_vel'] * self.generations[1]['time_elapsed'], pedestrian['y_pos'], 0]
            new_pedestrian.prev_radius = pedestrian_radius
            pedestrians.append(new_pedestrian)
            self.add(new_pedestrian)
            
        max_alive_pedestrians = 200
        
        current_gen = 1
        max_gen = 50
        # for gen in self.generations[1:-1]:
        for gen in self.generations[1:-1]:
            alive_pedestrians = []
            gen_animations = []
            runtime = self.generations[current_gen]['time_elapsed']

            for pedestrian in gen['pedestrians']:
                pedestrian_number = pedestrian['number']
                alive_pedestrians.append(pedestrian_number)
                pedestrians[pedestrian_number].set_color(self.get_density_color(pedestrian['neighbourAmount']))
                pedestrians[pedestrian_number].scale(pedestrian['radius'] / pedestrians[pedestrian_number].prev_radius)
                pedestrians[pedestrian_number].prev_radius = pedestrian['radius']
                if pedestrians[pedestrian_number].next_pos[0] > 0 or pedestrians[pedestrian_number].next_pos[1] > 0:
                    pedestrians[pedestrian_number].set_x(pedestrians[pedestrian_number].next_pos[0])
                    pedestrians[pedestrian_number].set_y(pedestrians[pedestrian_number].next_pos[1])
                if pedestrian['x_vel'] != 0 or pedestrian['y_vel'] != 0:
                    runtime = self.generations[current_gen+1]['time_elapsed']
                    # balls[ball_number].next_pos = [balls[ball_number].get_x() + ball['x_vel'] * runtime, balls[ball_number].get_y() + ball['y_vel'] * runtime, 0]
                    pedestrians[pedestrian_number].next_pos = [pedestrian['x_pos'], pedestrian['y_pos'], 0]
                    gen_animations.append(pedestrians[pedestrian_number].animate(run_time=runtime, rate_func=rate_functions.linear).move_to(Point(location=pedestrians[pedestrian_number].next_pos)))
            
            for ball in np.arange(max_alive_pedestrians):
                if ball not in alive_pedestrians and pedestrians[ball] is not None:
                    self.remove(pedestrians[ball])
                    pedestrians[ball] = None
            
            self.play(AnimationGroup(*gen_animations))
            current_gen += 1
        
        f.close()
    

    def resize_to_graph(self, value):
        return value * 7/20
    

    def next_line(self, file):
        next_line = file.readline()
        if next_line is not None and next_line != '\n':
            next_line = next_line.split('\n')[0]
        return next_line
    

    def x_coordinate_to_graph(self, x):
        return self.resize_to_graph(x - 20/2)
    

    def y_coordinate_to_graph(self, y):
        return self.resize_to_graph(y - 20/2)
    
    
    def get_density_color(self, neighbourAmount):
        # scaled_value = math.log10(1 + 9 * (neighbourAmount / self.max_neighbours))
        scaled_value = neighbourAmount / self.max_neighbours
        return rgb_to_color(rgb=[1, scaled_value, 0])


    def parse_pedestrians_file(self, filename):
        generations = []
        current_gen = 0
        prev_time_elapsed = 0
        with open(filename, 'r') as file:
            line = self.next_line(file)
            while line != '':
                while line == '\n':
                    line = self.next_line(file)
                    if line == '':
                        return generations
                generations.append({})
                # print(line)
                generations[current_gen]['pedestrians_alive'] = int(line)
                line = self.next_line(file)
                if current_gen == 0:
                    generations[current_gen]['time_elapsed'] = float(line)
                else:
                    generations[current_gen]['time_elapsed'] = float(line) - prev_time_elapsed
                    prev_time_elapsed += generations[current_gen]['time_elapsed']
                generations[current_gen]['pedestrians'] = []
                line = self.next_line(file)
                i = 0
                while line != '\n':
                    line = line.split()
                    generations[current_gen]['pedestrians'].append({
                        'number': int(line[0]),
                        'x_pos': self.x_coordinate_to_graph(float(line[1])),
                        'y_pos': self.y_coordinate_to_graph(float(line[2])),
                        'x_vel': self.resize_to_graph(float(line[3])),
                        'y_vel': self.resize_to_graph(float(line[4])),
                        'radius': self.resize_to_graph(float(line[5])),
                        'neighbourAmount': float(line[6])
                    })
                    maybe_max_neighbours = generations[current_gen]['pedestrians'][-1]['neighbourAmount']
                    if maybe_max_neighbours > self.max_neighbours:
                        self.max_neighbours = maybe_max_neighbours
                    line = self.next_line(file)
                    i += 1
                current_gen += 1
        return generations