public class Grid {

    final int size = 10;

    final int domain = 3;
    int [][] Grid2D;
    int [][][] Grid3D;

    boolean is2D;
    public Grid(int dimension) {
        if (dimension == 2) {
            Grid2D = new int[size][size];
            is2D = true;
        } else if (dimension == 3) {
            Grid3D = new int[size][size][size];
        }
    }

    /**
     * Sets the initial percentage of cells alive in the main domain of the grid (center)
     * @param percentage float between 0 and 1 indicating the percentage of cells alive
     */
    public void setInitialPercentage(float percentage){
        if (is2D){
            setInitialPercentage2D(percentage);
        } else {
            setInitialPercentage3D(percentage);
        }
    }



    private void setInitialPercentage3D(float percentage) {
        int totalDomainCells = domain*domain*domain;
        int cellsToSet = (int) (totalDomainCells*percentage);
        int cellsSet = 0;
        while (cellsSet < cellsToSet){
            int x = (int) (Math.random()*domain + domain);
            int y = (int) (Math.random()*domain + domain);
            int z = (int) (Math.random()*domain + domain);
            if (Grid3D[x][y][z] == 0){
                Grid3D[x][y][z] = 1;
                cellsSet++;
            }
        }
    }

    private void setInitialPercentage2D(float percentage) {
        int totalDomainCells = domain*domain;
        int cellsToSet = (int) (totalDomainCells*percentage);
        int cellsSet = 0;
        while (cellsSet < cellsToSet){
            int x = (int) (Math.random()*domain + domain);
            int y = (int) (Math.random()*domain + domain);
            if (Grid2D[x][y] == 0){
                Grid2D[x][y] = 1;
                cellsSet++;
            }
        }
    }


}
