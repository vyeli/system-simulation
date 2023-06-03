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

    private static final String[] CONFIGS_CSV_HEADERS = {"iteration", "dt", "removed_pedestrians", "t"};

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Config config = new Config();

        try (FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, Config.class);
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        }

        double doorStart = (config.getBoxSize() - config.getDoorWidth()) / 2;
        double doorEnd = (config.getBoxSize() + config.getDoorWidth()) / 2;
        int iterations = config.getIterations();

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("removedPedestrians.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvRemovedPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        for (int j=0 ; j < iterations ; j++) {
            List<Pedestrian> pedestrians = new ArrayList<>();
            int removedAmount = 0;

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
            } while (pedestrians.size() < config.getParticles());

            FileWriter writer = new FileWriter("output.txt");

            double dt = config.getMinR() / (2 * config.getVdMax());
            PedestrianSystem system = new PedestrianSystem(pedestrians, dt, config.getVdMax(), config.getMinR(), config.getMaxR(), config.getBeta(), config.getTau(), config.getBoxSize(), config.getDoorWidth());

            // Write initial state
            writer.write(system.writePedestrians());
            writer.write('\n');

            int deltaTDecimals = Double.toString(dt).split("\\.")[1].length();
            double multiplier = Math.pow(10, deltaTDecimals);

            long i = 0L;
            while(system.hasPedestriansLeft()) {
                List<Pedestrian> removed = system.evolveSystem();
                if (removed.size() != 0) {
                    removedAmount += removed.size();
                    csvRemovedPrinter.printRecord(j, dt, removedAmount, Math.round((system.getTime() - dt) * multiplier) / multiplier);
                }
                if (i % 5 == 0) {
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

            writer.close();
        }
        csvRemovedPrinter.close();
    }
}
