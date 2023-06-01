import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import helpers.Config;
import helpers.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Config config = new Config();

        try (FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, Config.class);
            System.out.println(config.getBeta());
            System.out.println(config.getDoorWidth());
            System.out.println(config.getMinR());
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        }

        double doorStart = (config.getBoxSize() - config.getDoorWidth()) / 2 - 0.1;
        double doorEnd = (config.getBoxSize() - config.getDoorWidth()) / 2 - 0.1;
        List<Pedestrian> pedestrians = new ArrayList<>();

        // Generate pedestrians
        do {
            boolean hasOverlap = false;
            double x = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
            double y = Math.random() * (config.getBoxSize() - 2 * config.getMaxR());
            Pedestrian newPedestrian = new Pedestrian(config.getMaxR(), new Pair<>(x, y), config.getMinR(), config.getMaxR(), config.getVdMax(), doorStart, doorEnd, config.getBeta());
            for (Pedestrian otherPedestrian : pedestrians) {
                if (otherPedestrian.overlapsWith(newPedestrian)) {
                    hasOverlap = true;
                    break;
                }
            }
            if (!hasOverlap) {
                pedestrians.add(newPedestrian);
            }
        } while (pedestrians.size() < config.getParticles());

        FileWriter writer = new FileWriter("output.txt");


        double dt = config.getMinR() / 2 * config.getVdMax();
        PedestrianSystem system = new PedestrianSystem(pedestrians, dt, config.getVdMax(), config.getMinR(), config.getMaxR(), config.getBeta(), config.getTau(), config.getBoxSize(), config.getDoorWidth());

        // Write initial state
        writer.write(system.writePedestrians());
        writer.write('\n');

        while(system.hasPedestriansLeft()) {
            system.evolveSystem();
            try {
                writer.write(system.writePedestrians());
                writer.write('\n');
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        writer.close();

    }
}
