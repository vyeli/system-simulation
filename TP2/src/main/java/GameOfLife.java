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

        Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);
        SimpleRegression regression = new SimpleRegression();

        // Set up control values
        Set<Grid2D> previousStates = new HashSet<>();
        int initialCells = grid.getLiveCellsAmount();
        int finalCells = initialCells, steps = 0;
        
        do {
            previousStates.add(grid);
            regression.addData(steps, finalCells);
            grid.nextGeneration();
            finalCells = grid.getLiveCellsAmount();
            steps++;
        } while (!grid.hasCellsOutside() && !previousStates.contains(grid));

        System.out.println("- Iteración " + (j+1) + ": " + regression.getSlope());
        regression.clear();
    }
}
