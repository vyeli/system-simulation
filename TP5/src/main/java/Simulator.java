import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import helpers.Config;
import helpers.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    public static void main(String[] args) {
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

        double dt = config.getMinR() / 2 * config.getVdMax();
        PedestrianSystem system = new PedestrianSystem(pedestrians, dt, config.getVdMax(), config.getMinR(), config.getMaxR(), config.getBeta(), config.getTau(), config.getBoxSize(), config.getDoorWidth());

        int i = 0;
        while(system.hasPedestriansLeft() && i < 50) {
            System.out.println("Iteracion #" + i + ":");
            Pedestrian current = system.getPedestrians().get(0);
            System.out.println("\t-x: " + current.getPosition().getX() + ", y: " + current.getPosition().getY());
            i++;
            system.evolveSystem();
        }

    }
}
