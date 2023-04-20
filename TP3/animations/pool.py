from tkinter import RIGHT
from turtle import circle
from manim import *
from manim.utils.color import Colors

class Pool(Scene):
    def construct(self):
        self.generations = self.parse_balls_file('output.txt')
        print(self.generations)

        self.table_height = self.resize_to_graph(112)
        self.border_height = self.resize_to_graph(112 + 1)

        self.table_width = self.resize_to_graph(224)
        self.border_width = self.resize_to_graph(224 + 1)

        table = Rectangle(width=self.table_width, height=self.table_height, fill_color=Colors.green_e.value, fill_opacity=1, stroke_width=0)
        table_border = Rectangle(width=self.border_width, height=self.border_height, stroke_color=Colors.dark_brown.value, stroke_width=8)

        white_ball = Circle(radius = self.resize_to_graph(2.85), stroke_width=0, fill_color=Colors.white.value, fill_opacity=1).shift(RIGHT * self.x_coordinate_to_graph(56), UP * self.y_coordinate_to_graph(56))
        
        upper_mid_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0,angle=-PI).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(112), UP * self.y_coordinate_to_graph(112))
        lower_mid_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0, angle=PI).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(112), UP * self.y_coordinate_to_graph(0))

        upper_left_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0,angle=-PI/2).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(0), UP * self.y_coordinate_to_graph(112))
        lower_left_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0, angle=PI/2).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(0), UP * self.y_coordinate_to_graph(0))

        upper_right_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0,angle=PI/2, start_angle=PI).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(224), UP * self.y_coordinate_to_graph(112))
        lower_right_ball = Sector(outer_radius = self.resize_to_graph(5.7), stroke_width=0, angle=-PI/2, start_angle=PI).set_color(Colors.black.value).shift(RIGHT * self.x_coordinate_to_graph(224), UP * self.y_coordinate_to_graph(0))

        self.add(table, table_border, white_ball, upper_mid_ball, lower_mid_ball, upper_left_ball, lower_left_ball, upper_right_ball, lower_right_ball)
        self.play(white_ball.animate(run_time=1.3).shift(RIGHT * 2))
        self.play(white_ball.animate(run_time=0.645).shift(UP * 2))
    
    def resize_to_graph(self, value):
        return value / 16
    
    def x_coordinate_to_graph(self, x):
        return self.resize_to_graph(x - 224/2)
    
    def y_coordinate_to_graph(self, y):
        return self.resize_to_graph(y - 112/2)

    def next_line(self, file):
        next_line = file.readline()
        if next_line is not None and next_line != '\n':
            next_line = next_line.split('\n')[0]
        return next_line

        
    def parse_balls_file(self, filename):
        generations = []
        current_gen = 0
        with open(filename, 'r') as file:
            line = self.next_line(file)
            while line != '':
                while line == '\n':
                    line = self.next_line(file)
                    if line == '':
                        return generations
                generations.append({})
                print(line)
                generations[current_gen]['balls_alive'] = int(line)
                line = self.next_line(file)
                if current_gen == 0:
                    generations[current_gen]['time_elapsed'] = float(line)
                else:
                    generations[current_gen]['time_elapsed'] = float(line) - generations[current_gen - 1]['time_elapsed']
                generations[current_gen]['balls'] = []
                line = self.next_line(file)
                while line != '\n':
                    line = line.split()
                    generations[current_gen]['balls'].append({
                        'number': int(line[0]),
                        'x_pos': float(line[1]),
                        'y_pos': float(line[2]),
                        'x_vel': float(line[3]),
                        'y_vel': float(line[4]),
                        'mass': float(line[5]),
                        'radius': float(line[6]),
                        'color': line[7]
                    })
                    line = self.next_line(file)
                current_gen += 1
        return generations
