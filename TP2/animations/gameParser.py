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
                line = f.readline()
                if line == '\n':
                    iterations.append(cells)
                    cells = []
                else:
                    cells.append(tuple(map(int, line.strip().split())))
                line = f.readline()

            # Append last iteration
            # if cells:
            #     iterations.append(cells)

            return size, domain, iterations
