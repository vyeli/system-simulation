import helpers.Pair;
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

    private static final String DATA_FOLDER_PATH = "./output/";
    private static final String OBSERVABLE_DATA_BASE_FILE_NAME = "2d_obs";
    private static final String CONFIGS_DATA_BASE_FILE_NAME = "2d_configs";

    private static final String[] OBS_CSV_HEADERS = {"porcentaje", "pendiente"};
    private static final String[] CONFIGS_CSV_HEADERS = {"porcentaje", "iteracion", "cant_celulas_vivas", "dist_al_centro"};

    public static void main(String[] args) {
        try {
            File folder = new File(DATA_FOLDER_PATH);
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
        // return (percentageCells == 15 || percentageCells == 45 || percentageCells == 75) && generation == 0;
        // TODO: Ask if all 6 percentages must be printed
        return generation == 0;
    }

    public static void random2D(Integer neighboursForRevive) throws IOException {
        int gridSize = 19;
        int domain = 11;

        // Archivo del observable
        BufferedWriter bwObs = Files.newBufferedWriter(Paths.get(DATA_FOLDER_PATH + OBSERVABLE_DATA_BASE_FILE_NAME + "_N" + (neighboursForRevive == null ? "" : neighboursForRevive) + ".csv"));
        CSVFormat csvFormatObs = CSVFormat.DEFAULT.builder().setHeader(OBS_CSV_HEADERS).build();
        final CSVPrinter printerObs = new CSVPrinter(bwObs, csvFormatObs);

        // Archivo de cant celulas vivas y radio al centro
        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get(DATA_FOLDER_PATH + CONFIGS_DATA_BASE_FILE_NAME + "_N" + (neighboursForRevive == null ? "" : neighboursForRevive) + ".csv"));
        CSVFormat csvFormatCOnfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter printerConfigs = new CSVPrinter(bwConfigs, csvFormatCOnfigs);

        Grid2D grid = new Grid2D(gridSize, domain, neighboursForRevive);
        SimpleRegression regression = new SimpleRegression();

        for (int j = 0; j < 10; j++) {
            List<Double> csvLineObs = new ArrayList<>();
            // List<Double> csvLineConfigs = new ArrayList<>();

            // System.out.println("Sistema con " + p + "%:");
            for (int p = 15; p < 100; p += 15) {
                double percentage = (double) p / 100;
                csvLineObs.add(percentage);

                // Set up initial pattern
                grid.setGrid(new int[gridSize][gridSize]);
                grid.generateRandomCells(percentage);
                PrintWriter outputWriter = null;

                if (isGenerationOnGraphic(p, j)) {
                    outputWriter = new PrintWriter("./output/2d_N" + (neighboursForRevive == null ? "" : neighboursForRevive) + "_P" + p + ".txt");
                    outputWriter.println(gridSize);
                    outputWriter.println(domain);
                    outputWriter.println();
                    outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));

                    Set<Pair<Integer, Integer>> liveCells = grid.getLiveCells();
                    outputWriter.println(Serializer.serialize2D(liveCells));
                    printerConfigs.printRecord(percentage, 0, liveCells.size(), grid.getCellsRadius());
                }

                // Set up control values
                Set<Grid2D> previousStates = new HashSet<>();
                int finalCells= grid.getLiveCellsAmount(), steps = 0;
                do {
                    previousStates.add(grid);
                    regression.addData(steps, finalCells);
                    grid.nextGeneration();
                    if (isGenerationOnGraphic(p, j)) {
                        Set<Pair<Integer, Integer>> liveCells = grid.getLiveCells();
                        outputWriter.println(Serializer.serialize2D(liveCells));
                        printerConfigs.printRecord(percentage, steps+1, liveCells.size(), grid.getCellsRadius());
                    }
                    finalCells = grid.getLiveCellsAmount();
                    steps++;
                } while (!grid.hasCellsOutside() && !previousStates.contains(grid) && grid.getLiveCellsAmount() > 0);

                if (outputWriter != null) {
                    outputWriter.close();
                }

                // System.out.println("- Iteración " + (j+1) + ": " + regression.getSlope());
                csvLineObs.add(regression.getSlope());
                printerObs.printRecord(csvLineObs);

                regression.clear();
                csvLineObs.clear();
            }
            
        }
        printerObs.flush();
        printerObs.close();

        printerConfigs.flush();
        printerConfigs.close();
    }
}