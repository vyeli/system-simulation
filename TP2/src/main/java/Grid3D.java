import helpers.Trio;
import interfaces.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
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

    private boolean isBorder(int coord, int border) {
        return coord == 0 || coord == border;
    }

    private void addAndCheckIfBorderCell(Trio<Integer, Integer, Integer> cell) {
        int border = size - 1;
        this.liveCells.add(cell);
        if (isBorder(cell.getX(), border) || isBorder(cell.getY(), border) || isBorder(cell.getZ(), border)) {
            this.hasCellsOutside = true;
        }
    }

    public void insertLiveCells() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    if(grid[x][y][z] == 1)
                        this.addAndCheckIfBorderCell(new Trio<>(x, y, z));
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
            int x = (int) ((0.5 - Math.random())*domain) + size/2;
            int y = (int) ((0.5 - Math.random())*domain) + size/2;
            int z = (int) ((0.5 - Math.random())*domain) + size/2;
            if (grid[x][y][z] == 0){
                grid[x][y][z] = 1;
                this.addAndCheckIfBorderCell(new Trio<>(x, y, z));
                cellsSet++;
            }
        }
    }

    public boolean hasCellsOutside() {
        return hasCellsOutside;
    }

    @Override
    public int[][][] getGrid() {
        return this.grid;
    }

    public void setGrid(int[][][] grid) {
        this.grid = grid;
        this.liveCells = new HashSet<>();
        this.hasCellsOutside = false;
        insertLiveCells();
    }

    @Override
    public void nextGeneration() {
        int[][][] nextGeneration = new int[size][size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                for (int zcol = 0; zcol < size; zcol++) {
                    int neighbors = countLiveNeighbors(grid, row, col, zcol);
                    Trio<Integer, Integer, Integer> cell = new Trio<>(row, col, zcol);

                    if (grid[row][col][zcol] == 1) {
                        if (neighbors < 4 || neighbors > 9) {
                            nextGeneration[row][col][zcol] = 0;
                            this.liveCells.remove(cell);
                        } else {
                            nextGeneration[row][col][zcol] = 1;
                            this.addAndCheckIfBorderCell(cell);
                        }
                    } else {
                        if (neighbors == neighboursForRevive) {
                            nextGeneration[row][col][zcol] = 1;
                            this.addAndCheckIfBorderCell(cell);
                        } else {
                            nextGeneration[row][col][zcol] = 0;
                            this.liveCells.remove(cell);
                        }
                    }
                }
            }
        }

        this.grid = nextGeneration;
    }
    
    public int countLiveNeighbors(int[][][] grid, int row, int col, int zcol) {
        int count = 0;
        int[] rows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cols = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] zcols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int neighborRow = row + rows[i];
            int neighborCol = col + cols[i];
            int neighborZCol = zcol + zcols[i];

            if (neighborRow >= 0 && neighborRow < grid.length && neighborCol >= 0 && neighborCol < grid[0].length && neighborZCol >= 0 && neighborZCol < grid[0][0].length) {
                if (grid[neighborRow][neighborCol][neighborZCol] == 1) {
                    count++;
                }
            }
        }

        return count;
    }

    public int getLiveCells() {
        return this.liveCells.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Grid3D grid3D = (Grid3D) o;
        return size == grid3D.size && Objects.equals(liveCells, grid3D.liveCells);
    }

    @Override
    public int hashCode() {
       return Objects.hash(size, liveCells);
    }
}
