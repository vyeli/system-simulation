class GameParser:
    def __init__(self, filename):
        self.filename = filename

    def parse(self):
        with open(self.filename) as f:
            # Get size and domain
            size = int(f.readline())
            domain = int(f.readline())

            f.readline()

            cells = []
            iterations = []

            line = f.readline()

            # Parse positions for each iteration
            while line:
                if line == '\n':
                    iterations.append(cells)
                    cells = []
                else:
                    cells.append(tuple(map(int, line.split())))
                line = f.readline()

            return size, domain, iterations[:-1]
