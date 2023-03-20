import java.util.Arrays;
import interfaces.Grid;

public class Grid2D implements Grid<int[][]> {

    private final int size;
    private final int domain;
    private int[][] grid;
    
    public Grid2D(int size, int domain) {
        this.size = size;
        this.domain = domain;
        this.grid = new int[size][size];
    }

    /**
     * Sets the initial percentage of cells alive in the main domain of the grid (center)
     * @param percentage float between 0 and 1 indicating the percentage of cells alive
    */
    @Override
    public void generateRandomCells(double percentage){
        int totalDomainCells = domain*domain;
        int cellsToSet = (int) (totalDomainCells*percentage);
        int cellsSet = 0;
        while (cellsSet < cellsToSet){
            int x = (int) (int)((0.5 - Math.random())*domain) + (int)size/2;
            int y = (int) (int)((0.5 - Math.random())*domain) + (int)size/2;
            if (grid[x][y] == 0){
                grid[x][y] = 1;
                cellsSet++;
            }
        }
    }

    @Override
    public int[][] getGrid() {
        return this.grid;
    }

    @Override
    public int[][] getNextGeneration(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] nextGeneration = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int neighbors = countLiveNeighbors(grid, row, col);

                if (grid[row][col] == 1) {
                    if (neighbors < 2 || neighbors > 3) {
                        nextGeneration[row][col] = 0;
                    } else {
                        nextGeneration[row][col] = 1;
                    }
                } else {
                    if (neighbors == 3) {
                        nextGeneration[row][col] = 1;
                    } else {
                        nextGeneration[row][col] = 0;
                    }
                }
            }
        }

        return nextGeneration;
    }

    @Override
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

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for(int i=0 ; i < this.size ; i++) {
            returnString.append(Arrays.toString(grid[i]).replace("1", "X").replace("0", "."));
            returnString.append("\n");
        }
        return returnString.toString();
    }

}
