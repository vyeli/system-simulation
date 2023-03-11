from manim import *

class CellGrid(Scene):
    def construct(self):
        L = 20
        M = 4
        cell = 128

        grid = NumberPlane(
            x_range=(0, L, L/M),
            y_range=(0, L, L/M),
            x_length=M,               # TODO: Adjust for scale
            y_length=M,
        )

        neighbours = []
        neighbours_file = open('../../cell-neighbours.txt', 'r')
        while True:
            line = neighbours_file.readline()
            if not line:
                break
            processed_line = line.split('\t')
            if int(processed_line[0]) != cell:
                continue
            processed_line = processed_line[1].split()
            for neighbour in processed_line:
                neighbours.append(int(neighbour))
            break

        cells = []
        cell_file = open('../../particle-coordinates.txt', 'r')
        while True:
            color = '#FFFFFF'
            line = cell_file.readline()
            if not line:
                break
            processed_line = line.split()
            if int(processed_line[0]) == cell:
                color = '#FC0000'
            if int(processed_line[0]) in neighbours:
                color = '#26FC00'
            x = float(processed_line[1])
            y = float(processed_line[2])
            cells.append(Dot(point=grid.c2p(x, y), radius=0.05, color=color))

        self.add(grid, *cells)
        # self.play(Create(grid))
        # for cell in cells:
        #    self.play(ChangeSpeed(Create(cell), speedinfo={1: 5}))