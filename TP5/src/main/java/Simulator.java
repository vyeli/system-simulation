import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import helpers.Config;
import helpers.Pair;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Simulator {

    private static final String[] B_CONFIGS_CSV_HEADERS = {"iteration", "exited_pedestrians", "t", "dt"};
    private static final String[] C_CONFIGS_CSV_HEADERS = {"iteration", "d", "n", "exited_pedestrians", "t", "dt"};

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Config config = new Config();

        try (FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, Config.class);
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        }

        constantFlowAnalysis(config);
        meanFlowAnalysis(config);

    }

    private static void meanFlowAnalysis(Config config) throws IOException {
        int valueAmount = config.getParticles().length;
        int iterations = config.getIterationsPerValue();

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("mean_flow.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(C_CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvRemovedPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        for (int w=0 ; w < valueAmount ; w++) {
            double doorWidth = config.getDoorWidth()[w];
            double doorStart = (config.getBoxSize() - doorWidth) / 2;
            double doorEnd = (config.getBoxSize() + doorWidth) / 2;
            int particleAmount = config.getParticles()[w];

            FileWriter writer = new FileWriter("mean_flow_" + particleAmount + ".txt");

            for (int j=0 ; j < iterations ; j++) {
                List<Pedestrian> pedestrians = new ArrayList<>();
                // Generate pedestrians
                int pedestrianId = 0;
                do {
                    boolean hasOverlap = false;
                    double x = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
                    double y = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
                    Pedestrian newPedestrian = new Pedestrian(pedestrianId, config.getMaxR(), new Pair<>(x, y), config.getMinR(), config.getMaxR(), config.getVdMax(), doorStart + 0.1, doorEnd - 0.1, config.getBeta());
                    for (Pedestrian otherPedestrian : pedestrians) {
                        if (otherPedestrian.overlapsWith(newPedestrian)) {
                            hasOverlap = true;
                            break;
                        }
                    }
                    if (!hasOverlap) {
                        pedestrians.add(newPedestrian);
                        pedestrianId++;
                    }
                } while (pedestrians.size() < particleAmount);

                double dt = config.getMinR() / (2 * config.getVdMax());
                PedestrianSystem system = new PedestrianSystem(pedestrians, dt, config.getVdMax(), config.getMinR(), config.getMaxR(), config.getTau(), config.getBoxSize(), doorWidth, config.getNeighbourRadius(), j == 0);

                // Write initial state
                if (j == 0) {
                    writer.write(system.writePedestrians());
                    writer.write('\n');
                }

                int deltaTDecimals = Double.toString(dt).split("\\.")[1].length();
                double multiplier = Math.pow(10, deltaTDecimals);

                long i = 0L;
                int lastExitedPedestriansAmount = 0;

                while(system.hasPedestriansLeft()) {
                    system.evolveSystem();
                    if (lastExitedPedestriansAmount != system.getExitedPedestriansAmount()) {
                        lastExitedPedestriansAmount = system.getExitedPedestriansAmount();
                        if (lastExitedPedestriansAmount >= config.getLowerFlowLimit() && lastExitedPedestriansAmount <= config.getUpperFlowLimit()) {
                            csvRemovedPrinter.printRecord(j, doorWidth, particleAmount, lastExitedPedestriansAmount, Math.round((system.getTime() - dt) * multiplier) / multiplier, dt);
                        }
                    }
                    if (i % config.getKDeltaT() == 0 && j == 0) {
                        try {
                            writer.write(system.writePedestrians());
                            writer.write('\n');
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
                System.out.println("Finish iteration " + (j+1));

                
            }
            writer.close();
        }
        csvRemovedPrinter.close();
    }

    private static void constantFlowAnalysis(Config config) throws IOException {
        double doorStart = (config.getBoxSize() - config.getDoorWidth()[0]) / 2;
        double doorEnd = (config.getBoxSize() + config.getDoorWidth()[0]) / 2;
        int iterations = config.getIterationsPerValue();

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("constant_flow.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(B_CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvRemovedPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        FileWriter writer = new FileWriter("constant_flow.txt");

        for (int j=0 ; j < iterations ; j++) {
            List<Pedestrian> pedestrians = new ArrayList<>();

            // Generate pedestrians
            int pedestrianId = 0;
            do {
                boolean hasOverlap = false;
                double x = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
                double y = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
                Pedestrian newPedestrian = new Pedestrian(pedestrianId, config.getMaxR(), new Pair<>(x, y), config.getMinR(), config.getMaxR(), config.getVdMax(), doorStart + 0.1, doorEnd - 0.1, config.getBeta());
                for (Pedestrian otherPedestrian : pedestrians) {
                    if (otherPedestrian.overlapsWith(newPedestrian)) {
                        hasOverlap = true;
                        break;
                    }
                }
                if (!hasOverlap) {
                    pedestrians.add(newPedestrian);
                    pedestrianId++;
                }
            } while (pedestrians.size() < config.getParticles()[0]);

            double dt = config.getMinR() / (2 * config.getVdMax());
            PedestrianSystem system = new PedestrianSystem(pedestrians, dt, config.getVdMax(), config.getMinR(), config.getMaxR(), config.getTau(), config.getBoxSize(), config.getDoorWidth()[0], config.getNeighbourRadius(), j == 0);

            // Write initial state
            if (j == 0) {
                writer.write(system.writePedestrians());
                writer.write('\n');
            }

            int deltaTDecimals = Double.toString(dt).split("\\.")[1].length();
            double multiplier = Math.pow(10, deltaTDecimals);

            long i = 0L;
            int lastExitedPedestriansAmount = 0;
            csvRemovedPrinter.printRecord(j, lastExitedPedestriansAmount, 0, dt);

            while(system.hasPedestriansLeft()) {
                system.evolveSystem();
                if (lastExitedPedestriansAmount != system.getExitedPedestriansAmount()) {
                    lastExitedPedestriansAmount = system.getExitedPedestriansAmount();
                    csvRemovedPrinter.printRecord(j, lastExitedPedestriansAmount, Math.round((system.getTime() - dt) * multiplier) / multiplier, dt);
                }
                if (i % config.getKDeltaT() == 0 && j == 0) {
                    try {
                        writer.write(system.writePedestrians());
                        writer.write('\n');
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }
                i++;
            }
            System.out.println("Finish iteration " + (j+1));
        }
        writer.close();
        csvRemovedPrinter.close();
    }
}
