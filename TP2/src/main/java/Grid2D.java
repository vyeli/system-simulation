import java.util.Arrays;

import interfaces.Grid;

public class Grid2D implements Grid {

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
            int x = (int) (Math.random()*domain + domain);
            int y = (int) (Math.random()*domain + domain);
            if (grid[x][y] == 0){
                grid[x][y] = 1;
                cellsSet++;
            }
        }
    }

    public int[][] nextGeneration(int[][] grid, int M, int N) {
        int[][] future = new int[M][N];

        // Loop through every cell
        for (int l = 0; l < M; l++) {
            for (int m = 0; m < N; m++) {
                // finding number Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if ((l + i >= 0 && l + i < M) && (m + j >= 0 && m + j < N))
                            aliveNeighbours += grid[l + i][m + j];

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= grid[l][m];

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((grid[l][m] == 1) && (aliveNeighbours < 2))
                    future[l][m] = 0;

                    // Cell dies due to over population
                else if ((grid[l][m] == 1) && (aliveNeighbours > 3))
                    future[l][m] = 0;

                    // A new cell is born
                else if ((grid[l][m] == 0) && (aliveNeighbours == 3))
                    future[l][m] = 1;

                    // Remains the same
                else
                    future[l][m] = grid[l][m];
            }
        }
        return future;
    }

    public int[][] getGrid() {
        return this.grid;
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
