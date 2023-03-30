import helpers.Pair;
import helpers.Serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import helpers.Trio;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class GameOfLife {

    // TODO: Get percentages info from config file
    private static final String[] CSV_HEADERS = {"iteracion", "15%", "30%", "45%", "60%", "75%", "90%"};

    private static final String OBSERVABLE_DATA_FOLDER_PATH = "./output/";
    private static final String OBSERVABLE_2D_DATA_FILE_NAME = "2d_obs.csv";
    private static final  String OBSERVABLE_3D_DATA_FILE_NAME = "3d_obs.csv";
    private static final int GRID_SIZE = 19;
    private static final int DOMAIN = 11;

    public static void main(String[] args) {
        try {
            File folder = new File(OBSERVABLE_DATA_FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            random(null, 2);
            random(3, 2);
            random(6, 2);
            random(9, 3);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isGenerationOnGraphic(int percentageCells, int generation) {
        return (percentageCells == 15 || percentageCells == 45 || percentageCells == 75) && generation == 0;
    }

    public static void random(Integer neighboursForRevive, int dimension) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(Paths.get(OBSERVABLE_DATA_FOLDER_PATH + (dimension == 2 ? OBSERVABLE_2D_DATA_FILE_NAME : OBSERVABLE_3D_DATA_FILE_NAME)));
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADERS).build();
        final CSVPrinter printer = new CSVPrinter(bw, csvFormat);
        SimpleRegression regression = new SimpleRegression();

        if (dimension != 2 && dimension != 3) {
            throw new IllegalArgumentException("Dimension must be 2 or 3");
        }

        RandomGrid grid = dimension == 2 ? new Grid2D(GRID_SIZE, DOMAIN, neighboursForRevive) : new Grid3D(GRID_SIZE, DOMAIN, neighboursForRevive);

        for (int j = 0; j < 10; j++) {
            List<Double> csvLine = new ArrayList<>();
            csvLine.add((double) j);

            // System.out.println("Sistema con " + p + "%:");
            for (int p = 15; p < 100; p += 15) {
                double percentage = (double) p / 100;

                // Set up initial pattern
                if (dimension == 2) {
                    grid.setGrid(new int[GRID_SIZE][GRID_SIZE]);
                } else {
                    grid.setGrid(new int[GRID_SIZE][GRID_SIZE][GRID_SIZE]);
                }
                grid.generateRandomCells(percentage);
                PrintWriter outputWriter = null;

                if (isGenerationOnGraphic(p, j)) {
                    outputWriter = new PrintWriter("./output/" + (dimension == 2 ? "2D" : "3D") + "PositionsN" + (neighboursForRevive == null ? "" : neighboursForRevive) + "P" + percentage + ".txt");
                    outputWriter.println(GRID_SIZE);
                    outputWriter.println(DOMAIN);
                    outputWriter.println();
                    if (dimension == 2) {
                        outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                    } else {
                        outputWriter.println(Serializer.serialize3D(grid.getLiveCells()));
                    }
                }


                // Set up control values
                Set<RandomGrid> previousStates = new HashSet<>();
                int finalCells = grid.getLiveCellsAmount(), steps = 0;
                do {
                    previousStates.add(grid);
                    regression.addData(steps, finalCells);
                    grid.nextGeneration();
                    if (isGenerationOnGraphic(p, j)) {
                        if (dimension == 2) {
                            outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                        } else {
                            outputWriter.println(Serializer.serialize3D(grid.getLiveCells()));
                        }
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