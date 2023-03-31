import helpers.Serializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import helpers.Trio;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class GameOfLife {

    private static final String DATA_FOLDER_PATH = "./output/";

    private static final String CONFIGS_2D_DATA_BASE_FILE_NAME = "2d_configs";
    private static final String CONFIGS_3D_DATA_BASE_FILE_NAME = "3d_configs";

    private static final String OBSERVABLE_2D_DATA_BASE_FILE_NAME = "2d_obs";
    private static final String OBSERVABLE_3D_DATA_BASE_FILE_NAME = "3d_obs";

    private static final String[] OBS_CSV_HEADERS = {"porcentaje", "pendiente"};
    private static final String[] CONFIGS_CSV_HEADERS = {"porcentaje", "iteracion", "cant_celulas_vivas", "dist_al_centro"};
    // TODO: Get percentages info from config file
    private static final int GRID_SIZE = 59;
    private static final int DOMAIN = 19;

    public static void main(String[] args) {
        try {
            File folder = new File(DATA_FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // 2D
            random(2, 2);
            random(3, 2);
            random(4, 2);

            // 3D
            random(2, 3);
            random(3, 3);
            random(4, 3);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isGenerationOnGraphic(int percentageCells, int generation) {
        // return (percentageCells == 15 || percentageCells == 45 || percentageCells == 75) && generation == 0;
        return generation == 0;
    }
        
    public static void random(Integer neighboursForRevive, int dimension) throws IOException {
        BufferedWriter bwObs = Files.newBufferedWriter(Paths.get(DATA_FOLDER_PATH + (dimension == 2 ? OBSERVABLE_2D_DATA_BASE_FILE_NAME : OBSERVABLE_3D_DATA_BASE_FILE_NAME) + "_N" + (neighboursForRevive == null ? "" : neighboursForRevive) + ".csv"));
        CSVFormat csvFormatObs = CSVFormat.DEFAULT.builder().setHeader(OBS_CSV_HEADERS).build();
        final CSVPrinter printerObs = new CSVPrinter(bwObs, csvFormatObs);

        // Archivo de cant celulas vivas y radio al centro
        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get(DATA_FOLDER_PATH + (dimension == 2 ? CONFIGS_2D_DATA_BASE_FILE_NAME : CONFIGS_3D_DATA_BASE_FILE_NAME) + "_N" + (neighboursForRevive == null ? "" : neighboursForRevive) + ".csv"));
        CSVFormat csvFormatCOnfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter printerConfigs = new CSVPrinter(bwConfigs, csvFormatCOnfigs);

        SimpleRegression regression = new SimpleRegression();

        if (dimension != 2 && dimension != 3) {
            printerObs.close();
            printerConfigs.close();

            throw new IllegalArgumentException("Dimension must be 2 or 3");
        }

        RandomGrid grid = dimension == 2 ? new Grid2D(GRID_SIZE, DOMAIN, neighboursForRevive) : new Grid3D(GRID_SIZE, DOMAIN, neighboursForRevive);

        for (int j = 0; j < 10; j++) {
            List<Double> csvLineObs = new ArrayList<>();
            // List<Double> csvLineConfigs = new ArrayList<>();

            // System.out.println("Sistema con " + p + "%:");

            // To write into file the grid that are cycles with maxSteps of evolution of non-cycles
            List<Trio> cycles = new ArrayList<>();
            int maxSteps = -1;

            for (int p = 15; p < 100; p += 15) {
                double percentage = (double) p / 100;
                csvLineObs.add(percentage);

                // Set up initial pattern
                if (dimension == 2) {
                    grid.setGrid(new int[GRID_SIZE][GRID_SIZE]);
                } else {
                    grid.setGrid(new int[GRID_SIZE][GRID_SIZE][GRID_SIZE]);
                }
                grid.generateRandomCells(percentage);
                PrintWriter outputWriter = null;

                if (isGenerationOnGraphic(p, j)) {
                    outputWriter = new PrintWriter("./output/" + (dimension == 2 ? "2d_N" : "3d_N") + (neighboursForRevive == null ? "" : neighboursForRevive) + "_P" + p + ".txt");
                    outputWriter.println(GRID_SIZE);
                    outputWriter.println(DOMAIN);
                    outputWriter.println();

                    Set liveCells = grid.getLiveCells();
                    if (dimension == 2) {
                        outputWriter.println(Serializer.serialize2D(liveCells));
                    } else {
                        outputWriter.println(Serializer.serialize3D(liveCells));
                    }

                    printerConfigs.printRecord(percentage, 0, liveCells.size(), grid.getCellsRadius());
                }


                // Set up control values
                Set<RandomGrid> previousStates = new HashSet<>();
                int finalCells = grid.getLiveCellsAmount(), steps = 0;
                do {
                    previousStates.add(grid);
                    regression.addData(steps, finalCells);
                    grid.nextGeneration();
                    finalCells = grid.getLiveCellsAmount();
                    steps++;
                    if (isGenerationOnGraphic(p, j)) {
                        if (dimension == 2) {
                            outputWriter.println(Serializer.serialize2D(grid.getLiveCells()));
                        } else {
                            outputWriter.println(Serializer.serialize3D(grid.getLiveCells()));
                        }
                        printerConfigs.printRecord(percentage, steps+1, grid.getLiveCells().size(), grid.getCellsRadius());
                        if (maxSteps < steps) {
                            maxSteps = steps;
                        }
                    }
                } while (!grid.hasCellsOutside() && !previousStates.contains(grid) && grid.getLiveCellsAmount() > 0);

                // Add cycle iteration to comple later
                if (isGenerationOnGraphic(p, j) && previousStates.contains(grid)) {
                    cycles.add(new Trio<>(p, steps, grid.getLiveCells()));
                }

                if (outputWriter != null) {
                    outputWriter.close();
                }

                // System.out.println("- Iteración " + (j+1) + ": " + regression.getSlope());
                csvLineObs.add(regression.getSlope());
                printerObs.printRecord(csvLineObs);

                regression.clear();
                csvLineObs.clear();
            }

            // Complete cycles
            for (Trio<Integer, Integer, Set> cycle : cycles) {
                FileWriter fileWriter = new FileWriter("./output/" + (dimension == 2 ? "2d_N" : "3d_N") + (neighboursForRevive == null ? "" : neighboursForRevive) + "_P" + cycle.getX() + ".txt", true);
                PrintWriter outputWriter = new PrintWriter(fileWriter);
                RandomGrid cycleGrid = dimension == 2 ? new Grid2D(GRID_SIZE, DOMAIN, neighboursForRevive) : new Grid3D(GRID_SIZE, DOMAIN, neighboursForRevive);
                if (dimension == 2)
                    cycleGrid.setGrid(Serializer.deserialize2D(cycle.getZ(), dimension));
                else
                    cycleGrid.setGrid(Serializer.deserialize3D(cycle.getZ(), dimension));
                for (int i = cycle.getY(); i < maxSteps; i++) {
                    cycleGrid.nextGeneration();
                    if (dimension == 2) {
                        outputWriter.println(Serializer.serialize2D(cycleGrid.getLiveCells()));
                    } else {
                        outputWriter.println(Serializer.serialize3D(cycleGrid.getLiveCells()));
                    }
                    printerConfigs.printRecord(cycle.getX(), i+1, cycle.getZ().size(), cycleGrid.getCellsRadius());
                }
            }



        }
        printerObs.close();
        printerConfigs.close();
    }

}