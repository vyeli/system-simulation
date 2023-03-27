import helpers.Serializer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class GameOfLife {

    public static void main(String[] args) {
        random2D(null);
        System.out.println();
        random2D(3);
        System.out.println();
        random2D(6);
    }

    public static void random2D(Integer neighboursForRevive) {
        int gridSize = 19;
        int domain = 11;

        try {
            Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);
            SimpleRegression regression = new SimpleRegression();
            for (int p = 10; p < 100; p += 10) {
                double percentage = (double) p / 100;
                System.out.println("Sistema con " + p + "%:");
                for (int j = 0; j < 10; j++) {

                    // Set up initial pattern
                    grid.setGrid(new int[gridSize][gridSize]);
                    grid.generateRandomCells(percentage);
                    PrintWriter outputWriter = null;

                    if ((p == 30 || p == 60 || p == 90) && j == 0) {
                        outputWriter = new PrintWriter("./2DPositionsN" + (neighboursForRevive == null ? "" : neighboursForRevive) + "P" + percentage + ".txt");
                        outputWriter.println(gridSize);
                        outputWriter.println(domain);
                        outputWriter.println();
                        outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                    }

                    // Set up control values
                    Set<Grid2D> previousStates = new HashSet<>();
                    int finalCells= grid.getLiveCellsAmount(), steps = 0;
                    do {
                        previousStates.add(grid);
                        regression.addData(steps, finalCells);
                        grid.nextGeneration();
                        if ((p == 30 || p == 60 || p == 90) && j == 0) {
                            outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                        }
                        finalCells = grid.getLiveCellsAmount();
                        steps++;
                    } while (!grid.hasCellsOutside() && !previousStates.contains(grid));
                    if (outputWriter != null) {
                        outputWriter.close();
                    }
                    System.out.println("- Iteración " + (j+1) + ": " + regression.getSlope());
                    regression.clear();

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }
    }
}
