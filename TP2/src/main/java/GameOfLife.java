import helpers.Serializer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class GameOfLife {

    public static void main(String[] args) {
        //random2D(null);
        System.out.println();
        random2D(3);
        System.out.println();
        //random2D(6);
    }

    public static void random2D(Integer neighboursForRevive) {
        int gridSize = 19;
        int domain = 11;

        try {
            final PrintWriter outputWriter = new PrintWriter("cellsPositions.txt");
            outputWriter.println(gridSize);
            outputWriter.println(domain);
            outputWriter.println();

            Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);

            for (int p = 10; p < 100; p += 10) {
                double percentage = (double) p / 100;
                System.out.println("Sistema con " + p + "%:");
                for (int j = 0; j < 10; j++) {
                    // Set up initial pattern
                    grid.setGrid(new int[gridSize][gridSize]);
                    grid.generateRandomCells(percentage);
                    outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                    // Set up control values
                    Set<Grid2D> previousStates = new HashSet<>();
                    int initialCells = grid.getLiveCellsAmount();
                    int finalCells, steps = 0;
                    do {
                        previousStates.add(grid);
                        grid.nextGeneration();
                        outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                        finalCells = grid.getLiveCellsAmount();
                        steps++;
                    } while (!grid.hasCellsOutside() && !previousStates.contains(grid));
                    outputWriter.close();
                    System.out.println("- Iteración " + (j + 1) + ": " + ((double) (finalCells - initialCells) / steps));

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }
    }
}
