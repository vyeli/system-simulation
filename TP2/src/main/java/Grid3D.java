import interfaces.Grid;

public class Grid3D implements Grid<int[][][]> {

    private final int size;
    private final int domain;
    private int[][][] grid;

    public Grid3D(int size, int domain) {
        this.size = size;
        this.domain = domain;
        this.grid = new int[size][size][size];
    }

    @Override
    public void generateRandomCells(double percentage) {
        int totalDomainCells = domain*domain*domain;
        int cellsToSet = (int) (totalDomainCells*percentage);
        int cellsSet = 0;
        while (cellsSet < cellsToSet){
            int x = (int) (2 * Math.random()*domain - 1 + (int)size/2);
            int y = (int) (2 * Math.random()*domain - 1 + (int)size/2);
            int z = (int) (2 * Math.random()*domain - 1 + (int)size/2);
            if (grid[x][y][z] == 0){
                grid[x][y][z] = 1;
                cellsSet++;
            }
        }
    }

    @Override
    public int[][][] getGrid() {
        return this.grid;
    }

    @Override
    public int[][][] getNextGeneration(int[][][] grid) {
        int[][][] nextGeneration = new int[size][size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                for (int zcol = 0; zcol < size; zcol++) {
                    int neighbors = countLiveNeighbors(grid, row, col);

                    if (grid[row][col][zcol] == 1) {
                        if (neighbors < 2 || neighbors > 3) {
                            nextGeneration[row][col][zcol] = 0;
                        } else {
                            nextGeneration[row][col][zcol] = 1;
                        }
                    } else {
                        if (neighbors == 3) {
                            nextGeneration[row][col][zcol] = 1;
                        } else {
                            nextGeneration[row][col][zcol] = 0;
                        }
                    }
                }
            }
        }

        return nextGeneration;
    }

    // TODO: Transform to 3D
    @Override
    public int countLiveNeighbors(int[][][] grid, int row, int col) {
        int count = 0;
        int[] rows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int neighborRow = row + rows[i];
            int neighborCol = col + cols[i];

            if (neighborRow >= 0 && neighborRow < grid.length && neighborCol >= 0 && neighborCol < grid[0].length) {
                if (grid[neighborRow][neighborCol][0] == 1) {
                    count++;
                }
            }
        }

        return count;
    }

}
