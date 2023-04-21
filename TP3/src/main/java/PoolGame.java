import helpers.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;

public class PoolGame {

    private static final String[] CONFIGS_CSV_HEADERS = {"generation", "y0", "vx0", "event_time", "timestamp"};

    private static final double MIN_Y0_WHITE_BALL = 42;
    private static final double MAX_Y0_WHITE_BALL = 56;
    private static final double ITERATIONS_PER_POSITION = 20;

    public static void main(String[] args) throws IOException {
        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("execution_data.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);
        FileWriter fileWriter = new FileWriter("output.txt");

        // Change white ball y0 position
        for (int i=0; i < 10; i++) {
            for (int w=0 ; w < ITERATIONS_PER_POSITION ; w++) {
                double y0 = MIN_Y0_WHITE_BALL + i * (MAX_Y0_WHITE_BALL - MIN_Y0_WHITE_BALL) / 9;
                double vx0 = 200;

                List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
                for (int j = 0; j < 15; j++) {
                    ballsEpsilon.add(new Pair<>(getRandomEpsilon(), getRandomEpsilon()));
                }

                Table gameTable = new Table(y0, vx0, ballsEpsilon);
                List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());
                // System.out.printf("Simulation started with %d balls %n", balls.size());
                CollisionSystem collisionSystem = new CollisionSystem(balls);

                try {
                    while (collisionSystem.hasNextEvent()) {
                        if (i == 9 && w == 0) {
                            fileWriter.write(collisionSystem.writeEvent());
                        }
                        double pastEventTime = collisionSystem.getTime();
                        collisionSystem.simulateNextEvent();
                        csvPrinter.printRecord(i, y0, vx0, collisionSystem.getTime() - pastEventTime, collisionSystem.getTime());
                    }
                    if (i == 9 && w == 0) {
                        fileWriter.write(collisionSystem.writeEvent());
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                System.out.printf("Simulation ended after %f seconds %n", collisionSystem.getTime());
            }
        }
        fileWriter.close();
        csvPrinter.close();
    }

    public static double getRandomEpsilon() {
        double randValue = Math.random();
        double epsilon = 0.02 + randValue * 0.01;
        if(randValue > 0.05) {
            epsilon = -epsilon;
        }
        return epsilon;
    }

}
