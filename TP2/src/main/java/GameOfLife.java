import helpers.Serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class GameOfLife {

    // TODO: Get percentages info from config file
    private static final String[] CSV_HEADERS = {"iteracion", "15%", "30%", "45%", "60%", "75%", "90%"};

    private static final String OBSERVABLE_DATA_FOLDER_PATH = "./output/";
    private static final String OBSERVABLE_DATA_FILE_NAME = "2d_obs.csv";

    public static void main(String[] args) {
        try {
            File folder = new File(OBSERVABLE_DATA_FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            random2D(null);
            random2D(3);
            random2D(6);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isGenerationOnGraphic(int percentageCells, int generation) {
        return (percentageCells == 15 || percentageCells == 45 || percentageCells == 75) && generation == 0;
    }

    public static void random2D(Integer neighboursForRevive) throws IOException {
        int gridSize = 19;
        int domain = 11;

        BufferedWriter bw = Files.newBufferedWriter(Paths.get(OBSERVABLE_DATA_FOLDER_PATH + OBSERVABLE_DATA_FILE_NAME));
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADERS).build();
        final CSVPrinter printer = new CSVPrinter(bw, csvFormat);

        Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);
        SimpleRegression regression = new SimpleRegression();

        for (int j = 0; j < 10; j++) {
            List<Double> csvLine = new ArrayList<>();
            csvLine.add((double)j);

            // System.out.println("Sistema con " + p + "%:");
            for (int p = 15; p < 100; p += 15) {
                double percentage = (double) p / 100;

                // Set up initial pattern
                grid.setGrid(new int[gridSize][gridSize]);
                grid.generateRandomCells(percentage);
                PrintWriter outputWriter = null;

                if (isGenerationOnGraphic(p, j)) {
                    outputWriter = new PrintWriter("./output/2DPositionsN" + (neighboursForRevive == null ? "" : neighboursForRevive) + "P" + percentage + ".txt");
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
                    if (isGenerationOnGraphic(p, j)) {
                        outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                    }
                    finalCells = grid.getLiveCellsAmount();
                    steps++;
                } while (!grid.hasCellsOutside() && !previousStates.contains(grid));

                if (outputWriter != null) {
                    outputWriter.close();
                }

                // System.out.println("- Iteración " + (j+1) + ": " + regression.getSlope());
                csvLine.add(regression.getSlope());
                regression.clear();
            }
            printer.printRecord(csvLine);
        }

        printer.flush();
    }
}