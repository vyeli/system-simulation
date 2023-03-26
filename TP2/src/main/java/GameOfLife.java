import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class GameOfLife {

    public static void main(String[] args) {
        /*
        double percentage = 0.5;
        if (args.length == 1) {
            percentage = Double.parseDouble(args[0]);
        }
        Grid2D grid = new Grid2D(17, 11, 3);
        grid.generateRandomCells(percentage);
        System.out.println(grid);

         */
        random2D(null);
        System.out.println();
        random2D(3);
        System.out.println();
        random2D(6);
    }

    public static void random2D(Integer neighboursForRevive) {
        int gridSize = 19;
        int domain = 11;

        Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);

        for (int p=10 ; p < 100 ; p += 10) {
            double percentage = (double) p / 100;
            System.out.println("Sistema con " + p + "%:");
            for (int j = 0; j < 10; j++) {
                // Set up initial pattern
                grid.setGrid(new int[gridSize][gridSize]);
                grid.generateRandomCells(percentage);

                // Set up control values
                Set<Grid2D> previousStates = new HashSet<>();
                int initialCells = grid.getLiveCellsAmount();
                int finalCells, steps = 0;
                do {
                    previousStates.add(grid);
                    grid.nextGeneration();
                    finalCells = grid.getLiveCellsAmount();
                    steps++;
                } while (!grid.hasCellsOutside() && !previousStates.contains(grid));

                System.out.println("- Iteración " + (j+1) + ": " + ((double)(finalCells - initialCells) / steps));

            }
        }
    }

}
