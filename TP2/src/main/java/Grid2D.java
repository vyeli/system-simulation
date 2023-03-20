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
