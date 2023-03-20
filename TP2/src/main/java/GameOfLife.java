import java.util.Arrays;

public class GameOfLife {
    public static void main(String[] args) {
        /*
        int N = 4;
        boolean[][] grid = new boolean[N][N];
        // Set live cells
        grid[0][1] = true;
        grid[0][2] = true;
        grid[0][3] = true;
        int generations = 10;

        for (int i = 0; i < generations; i++) {
            printGrid(grid);
            grid = getNextGeneration(grid);
        }
         */
        double percentage = 0.5;
        if(args.length == 1) {
            percentage = Double.parseDouble(args[0]);
        }
        Grid2D grid = new Grid2D(11, 3);
        grid.generateRandomCells(percentage);
        System.out.println(grid);
    }

    public static void printGrid(boolean[][] grid) {
        for (boolean[] row : grid) {
            System.out.println(Arrays.toString(row).replace("true", "X").replace("false", "."));
        }
        System.out.println();
    }

}
