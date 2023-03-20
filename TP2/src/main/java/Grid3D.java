import interfaces.Grid;

public class Grid3D implements Grid {

    private final int size;
    private final int domain;
    private int [][][] grid;

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

    public int[][][] getGrid() {
        return this.grid;
    }

}
