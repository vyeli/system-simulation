from manim import *

class CellGrid(Scene):
    def construct(self):
        L = 20
        M = 4

        grid = NumberPlane(
            x_range=(0, L, L/M),
            y_range=(0, L, L/M),
            x_length=M,               # TODO: Adjust for scale
            y_length=M,
        )

        cells = []
        cell_file = open('../../particle-coordinates.txt', 'r')
        while True:
            line = cell_file.readline()
            if not line:
                break
            processed_line = line.split()
            x = float(processed_line[1])
            y = float(processed_line[2])
            cells.append(Dot(point=grid.c2p(x, y), radius=0.25))

        # self.add(grid, *cells)
        self.play(Create(grid))
        for cell in cells:
            self.play(ChangeSpeed(Create(cell), speedinfo={1: 5}))