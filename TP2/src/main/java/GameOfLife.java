import java.util.Arrays;

public class GameOfLife {
    public static void main(String[] args) {
        boolean[][] grid = {
                {false, false, false, false},
                {false, true, true, false},
                {false, true, true, false},
                {false, false, false, false}
        };
        int generations = 10;

        for (int i = 0; i < generations; i++) {
            printGrid(grid);
            grid = getNextGeneration(grid);
        }
    }

    public static boolean[][] getNextGeneration(boolean[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] nextGeneration = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int neighbors = countLiveNeighbors(grid, row, col);

                if (grid[row][col]) {
                    if (neighbors < 2 || neighbors > 3) {
                        nextGeneration[row][col] = false;
                    } else {
                        nextGeneration[row][col] = true;
                    }
                } else {
                    if (neighbors == 3) {
                        nextGeneration[row][col] = true;
                    } else {
                        nextGeneration[row][col] = false;
                    }
                }
            }
        }

        return nextGeneration;
    }

    public static int countLiveNeighbors(boolean[][] grid, int row, int col) {
        int count = 0;
        int[] rows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int neighborRow = row + rows[i];
            int neighborCol = col + cols[i];

            if (neighborRow >= 0 && neighborRow < grid.length && neighborCol >= 0 && neighborCol < grid[0].length) {
                if (grid[neighborRow][neighborCol]) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void printGrid(boolean[][] grid) {
        for (boolean[] row : grid) {
            System.out.println(Arrays.toString(row).replace("true", "X").replace("false", "."));
        }
        System.out.println();
    }
}
