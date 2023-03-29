class GameParser:
    def __init__(self, filename):
        self.filename = filename

    def parse(self):
        with open(self.filename) as f:
            lines = f.readlines()

        # Get size and domain
        size = int(lines[0])
        domain = int(lines[1])

        # Parse positions for each iteration
        iterations = []
        cells = []
        for line in lines[2:]:
            if not line.strip():  # Empty line indicates end of iteration
                iterations.append(cells)
                cells = []
            else:
                cells.append(tuple(map(int, line.strip().split())))

        # Append last iteration
        if cells:
            iterations.append(cells)

        return size, domain, iterations
