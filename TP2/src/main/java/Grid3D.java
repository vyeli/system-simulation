import helpers.Pair;
import helpers.Trio;
import interfaces.Grid;

import java.util.HashSet;
import java.util.Set;

public class Grid3D implements Grid<int[][][]> {

    private final int size;
    private final int domain;
    private int[][][] grid;

    private final Integer neighboursForRevive;
    private boolean hasCellsOutside = false;
    private Set<Trio<Integer, Integer, Integer>> liveCells = new HashSet<>();


    public Grid3D(int size, int domain, Integer neighboursForRevive) {
        this.size = size;
        this.domain = domain;
        this.neighboursForRevive = neighboursForRevive;
        this.grid = new int[size][size][size];
    }

    private void addAndCheckIfBorderCell(int x, int y, int z) {
        Trio<Integer, Integer, Integer> cell = new Trio<>(x, y, z);
        this.liveCells.add(cell);
        if (x == 0 || x == size-1 || y == 0 || y == size-1 || z == 0 || z == size-1) {
            this.hasCellsOutside = true;
        }
    }

    public void insertLiveCells(int[][][] grid) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if(grid[x][y][z] == 1)
                        this.addAndCheckIfBorderCell(x, y, z);
                }
            }
        }
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
    public void nextGeneration() {
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
                        if (neighbors == neighboursForRevive) {
                            nextGeneration[row][col][zcol] = 1;
                        } else {
                            nextGeneration[row][col][zcol] = 0;
                        }
                    }
                }
            }
        }

        this.grid = nextGeneration;
    }

    // TODO: Transform to 3D
    @Override
    public int countLiveNeighbors(int[][][] grid, int row, int col) {
        int count = 0;
        int[] rows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cols = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] zcols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int neighborRow = row + rows[i];
            int neighborCol = col + cols[i];
            int neighborZCol = col + zcols[i];

            if (neighborRow >= 0 && neighborRow < grid.length && neighborCol >= 0 && neighborCol < grid[0].length && neighborZCol >= 0 && neighborZCol < grid[0][0].length) {
                if (grid[neighborRow][neighborCol][neighborZCol] == 1) {
                    count++;
                }
            }
        }

        return count;
    }



}
