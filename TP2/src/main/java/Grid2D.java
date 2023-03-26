import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import helpers.Pair;
import interfaces.Grid;

public class Grid2D implements Grid<int[][]> {

    private final int size;
    private final int domain;
    private int[][] grid;

    private final Integer neighboursForRevive;
    private Set<Pair<Integer, Integer>> liveCells = new HashSet<>();
    private boolean hasCellsOutside = false;
    
    public Grid2D(int size, int domain, Integer neighboursForRevive) {
        this.size = size;
        this.domain = domain;
        this.neighboursForRevive = neighboursForRevive;
        this.grid = new int[size][];
        for (int i=0 ; i < size ; i++) {
            this.grid[i] = new int[size];
        }
    }

    private void addAndCheckIfBorderCell(int x, int y) {
        Pair<Integer, Integer> cell = new Pair<>(x, y);
        this.liveCells.add(cell);
        if (x == 0 || x == size-1 || y == 0 || y == size-1) {
            this.hasCellsOutside = true;
        }
    }

    public void insertLiveCells() {
        for (int x=0 ; x < size ; x++) {
            this.grid[x] = new int[size];
            for (int y=0 ; y < size ; y++) {
                if (this.grid[x][y] == 1) {
                    this.addAndCheckIfBorderCell(x, y);
                }
            }
        }
    }

    /**
     * Sets the initial percentage of cells alive in the main domain of the grid (center)
     * @param percentage float between 0 and 1 indicating the percentage of cells alive
    */
    @Override
    public void generateRandomCells(double percentage) {
        int totalDomainCells = domain*domain;
        int cellsToSet = (int) (totalDomainCells*percentage);
        int cellsSet = 0;
        while (cellsSet < cellsToSet){
            int x = (int)((0.5 - Math.random())*domain) + size/2;
            int y = (int)((0.5 - Math.random())*domain) + size/2;
            if (grid[x][y] == 0){
                grid[x][y] = 1;
                this.addAndCheckIfBorderCell(x, y);
                cellsSet++;
            }
        }
    }

    public boolean hasCellsOutside() {
        return hasCellsOutside;
    }



    @Override
    public int[][] getGrid() {
        return this.grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
        this.liveCells = new HashSet<>();
        this.hasCellsOutside = false;
        insertLiveCells();
    }

    @Override
    public void nextGeneration() {
        int[][] nextGeneration = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int neighbors = countLiveNeighbors(grid, row, col);

                if (grid[row][col] == 1) {
                    if (neighbors < 2 || neighbors > 3) {
                        nextGeneration[row][col] = 0;
                        this.liveCells.remove(new Pair<>(row, col));
                    } else {
                        nextGeneration[row][col] = 1;
                        this.addAndCheckIfBorderCell(row, col);
                    }
                } else if (neighboursForRevive != null) {
                    if (neighbors == neighboursForRevive) {
                        nextGeneration[row][col] = 1;
                        this.addAndCheckIfBorderCell(row, col);
                    } else {
                        nextGeneration[row][col] = 0;
                        this.liveCells.remove(new Pair<>(row, col));
                    }
                }
            }
        }

        this.grid = nextGeneration;
    }

    public int countLiveNeighbors(int[][] grid, int row, int col) {
        int count = 0;
        int[] rows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int neighborRow = row + rows[i];
            int neighborCol = col + cols[i];

            if (neighborRow >= 0 && neighborRow < grid.length && neighborCol >= 0 && neighborCol < grid[0].length) {
                if (grid[neighborRow][neighborCol] == 1) {
                    count++;
                }
            }
        }

        return count;
    }

    public int getLiveCellsAmount() {
        return liveCells.size();
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for(int i=0 ; i < this.size ; i++) {
            returnString.append(Arrays.toString(grid[i]).replace("1", "X").replace("0", "."));
            returnString.append("\n");
        }
        return returnString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Grid2D grid2D = (Grid2D) o;
        return size == grid2D.size && Objects.equals(liveCells, grid2D.liveCells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, liveCells);
    }
}
