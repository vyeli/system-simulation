from manim import *
from manim.utils.color import Colors
import numpy as np

class Pool(Scene):
    def construct(self):
        self.generations = self.parse_balls_file('output.txt')
        balls = []
        print('Total gens: {}'.format(len(self.generations)))

        self.table_height = self.resize_to_graph(1.12)
        self.border_height = self.resize_to_graph(1.12 + 0.01)

        self.table_width = self.resize_to_graph(2.24)
        self.border_width = self.resize_to_graph(2.24 + 0.01)

        table = Rectangle(width=self.table_width, height=self.table_height, fill_color=Colors.green_e.value, fill_opacity=1, stroke_width=0)
        table_border = Rectangle(width=self.border_width, height=self.border_height, stroke_color=Colors.dark_brown.value, stroke_width=8)

        self.add(table, table_border)

        # Load balls
        for ball in self.generations[0]['balls']:
            ball_number = ball['number']
            new_ball = None
            # new_ball = Circle(radius = ball['radius'], stroke_width=0, fill_color=ball['color'], fill_opacity=1)
            match ball_number:
                case 16:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=PI/2)
                case 17:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=-PI/2)
                case 18:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=PI)
                case 19:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=-PI)
                case 20:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=-PI/2, start_angle=PI)
                case 21:
                    new_ball = Sector(outer_radius = ball['radius'], stroke_width=0, angle=PI/2, start_angle=PI)
                case _:
                    new_ball = Circle(radius = ball['radius'], stroke_width=0, fill_color='#00ffff', fill_opacity=1)
            if ball_number > 15:         # A corner
                new_ball.set_color(Colors.black.value)
            new_ball.shift(RIGHT * ball['x_pos'], UP * ball['y_pos'])
            balls.append(new_ball)
            self.add(new_ball)
            if ball['number'] == 0:         # White ball
                new_ball.next_pos = [ball['x_pos'] + ball['x_vel'] * self.generations[1]['time_elapsed'], ball['y_pos'], 0]
            else:
                new_ball.next_pos = [-1, -1, -1]

        max_alive_balls = len(balls) - 6
        self.play(AnimationGroup(balls[0].animate(run_time=self.generations[1]['time_elapsed'], rate_func=rate_functions.linear).move_to(Point(location=balls[0].next_pos))))

        current_gen = 1
        for gen in self.generations[1:-1]:
            alive_balls = []
            gen_animations = []
            runtime = self.generations[current_gen]['time_elapsed']

            for ball in gen['balls']:
                ball_number = ball['number']
                alive_balls.append(ball_number)
                if balls[ball_number].next_pos[0] > 0 or balls[ball_number].next_pos[1] > 0:
                    balls[ball_number].set_x(balls[ball_number].next_pos[0])
                    balls[ball_number].set_y(balls[ball_number].next_pos[1])
                if ball_number <= 15 and ball['x_vel'] != 0 or ball['y_vel'] != 0:
                    runtime = self.generations[current_gen+1]['time_elapsed']
                    # balls[ball_number].next_pos = [balls[ball_number].get_x() + ball['x_vel'] * runtime, balls[ball_number].get_y() + ball['y_vel'] * runtime, 0]
                    balls[ball_number].next_pos = [ball['x_pos'], ball['y_pos'], 0]
                    gen_animations.append(balls[ball_number].animate(run_time=runtime, rate_func=rate_functions.linear).move_to(Point(location=balls[ball_number].next_pos)))
            
            for ball in np.arange(max_alive_balls):
                if ball not in alive_balls and balls[ball] is not None:
                    self.remove(balls[ball])
                    balls[ball] = None
            
            self.play(AnimationGroup(*gen_animations))
            current_gen += 1
    
    
    def resize_to_graph(self, value):
        return value / 0.16

    def x_coordinate_to_graph(self, x):
        return self.resize_to_graph(x - 2.24/2)
    

    def y_coordinate_to_graph(self, y):
        return self.resize_to_graph(y - 1.12/2)


    def next_line(self, file):
        next_line = file.readline()
        if next_line is not None and next_line != '\n':
            next_line = next_line.split('\n')[0]
        return next_line

        
    def parse_balls_file(self, filename):
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
                generations[current_gen]['balls_alive'] = int(line)
                line = self.next_line(file)
                if current_gen == 0:
                    generations[current_gen]['time_elapsed'] = float(line)
                else:
                    generations[current_gen]['time_elapsed'] = float(line) - prev_time_elapsed
                    prev_time_elapsed += generations[current_gen]['time_elapsed']
                generations[current_gen]['balls'] = []
                line = self.next_line(file)
                while line != '\n':
                    line = line.split()
                    generations[current_gen]['balls'].append({
                        'number': int(line[0]),
                        'x_pos': self.x_coordinate_to_graph(float(line[1])),
                        'y_pos': self.y_coordinate_to_graph(float(line[2])),
                        'x_vel': self.resize_to_graph(float(line[3])),
                        'y_vel': self.resize_to_graph(float(line[4])),
                        'radius': self.resize_to_graph(float(line[5]))
                    })
                    line = self.next_line(file)
                current_gen += 1
        return generations