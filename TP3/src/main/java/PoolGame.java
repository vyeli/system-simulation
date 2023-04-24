import helpers.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class PoolGame {

    private static final String[] CONFIGS_CSV_HEADERS = {"generation", "y0", "vx0", "event_time", "timestamp"};

    private static final double MIN_Y0_WHITE_BALL = 42;
    private static final double MAX_Y0_WHITE_BALL = 56;
    private static final double ITERATIONS = 50;

    public static void main(String[] args) throws IOException {
        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("execution_data.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);
        FileWriter fileWriter = new FileWriter("output.txt");

        // Change white ball y0 position
        for (int i=0; i < 10; i++) {
            for (int w=0 ; w < ITERATIONS ; w++) {
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
                        csvPrinter.printRecord(w, y0, vx0, collisionSystem.getTime() - pastEventTime, collisionSystem.getTime());
                    }
                    if (i == 9 && w == 0) {
                        fileWriter.write(collisionSystem.writeEvent());
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
        fileWriter.close();
        csvPrinter.close();

        // change white ball vx0 velocity
        BufferedWriter bwConfigs2 = Files.newBufferedWriter(Paths.get("execution_data_vx0.csv"));
        final CSVPrinter csvPrinter2 = new CSVPrinter(bwConfigs2, csvFormatConfigs);
        for (int i=0; i < 10; i++) {
            for (int w=0 ; w < ITERATIONS ; w++) {
                double y0 = 56;
                double vx0 = 100 + i * 100;

                List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
                for (int j = 0; j < 15; j++) {
                    ballsEpsilon.add(new Pair<>(getRandomEpsilon(), getRandomEpsilon()));
                }

                Table gameTable = new Table(y0, vx0, ballsEpsilon);
                List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());
                CollisionSystem collisionSystem = new CollisionSystem(balls);

                try {
                    while (collisionSystem.hasNextEvent()) {
                        double pastEventTime = collisionSystem.getTime();
                        collisionSystem.simulateNextEvent();
                        csvPrinter2.printRecord(w, y0, vx0, collisionSystem.getTime() - pastEventTime, collisionSystem.getTime());
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
        csvPrinter2.close();

    }

    public static double getRandomEpsilon() {
        double randValue = Math.random();
        double epsilon = 0.02 + randValue * 0.01;
        if(randValue > 0.5) {
            epsilon = -epsilon;
        }
        return epsilon;
    }

    public static double getDeterministicDoubleEpsilons(int epsilonIdx) {
        double epsilons[] = {
            -0.025375633720082617,
            0.023195390183247143,
            0.023467172503365472,
            0.024568862163597948,
            0.021235567161039016,
            0.024321684724795026,
            0.023379759201919707,
            0.022069884891683465,
            0.02175431925530433,
            -0.028101254186428375,
            0.024613681688628403,
            -0.0280714576052831,
            -0.026644394141343335,
            0.023695177579638935,
            -0.02920082021216556,
            -0.02657502617910734,
            0.021138162507242198,
            -0.02983145381017637,
            0.022729002282930455,
            -0.028238472313370958,
            -0.026021777559192476,
            0.02206438517219644,
            0.022474028054153813,
            -0.029101719056499338,
            0.023248323576678342,
            -0.028690357554693235,
            0.02482934476508576,
            -0.027542859614355632,
            -0.025897592220895486,
            0.020427744555701916
        };
        if (epsilonIdx < 0 || epsilonIdx >= epsilons.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return epsilons[epsilonIdx];
    }

    public static float getDeterministicFloatEpsilons(int epsilonIdx) {
        float epsilons[] = {
            -0.028792826f,
            -0.028979674f,
            -0.029060215f,
            0.02105733f,  
            0.023895605f, 
            0.02095217f,  
            -0.02641131f, 
            0.02440638f,  
            0.023331495f, 
            -0.026705587f,
            0.020751735f, 
            -0.029478062f,
            -0.027588578f,
            -0.02726412f, 
            0.02020634f,  
            0.022366522f, 
            0.024202934f, 
            0.024454853f, 
            -0.029077772f,
            0.02182581f,  
            0.024283746f,
            -0.027173134f,
            -0.026929239f,
            0.021658776f,
            0.02074862f,
            -0.02536989f,
            -0.026660318f,
            -0.029669588f,
            -0.028696656f,
            0.021185035f,
        };
        if (epsilonIdx < 0 || epsilonIdx >= epsilons.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return epsilons[epsilonIdx];
    }

}
