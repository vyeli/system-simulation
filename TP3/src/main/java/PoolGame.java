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
        FileWriter fileWriter = null;

        // Change white ball y0 position
        for (int i=0; i < 10; i++) {
            for (int w=0 ; w < ITERATIONS ; w++) {
                double y0 = MIN_Y0_WHITE_BALL + i * (MAX_Y0_WHITE_BALL - MIN_Y0_WHITE_BALL) / 9;
                double vx0 = 200;
                fileWriter = new FileWriter("result_" + i + "_" + w + ".txt");
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
                        fileWriter.write(collisionSystem.writeEvent());
                        double pastEventTime = collisionSystem.getTime();
                        collisionSystem.simulateNextEvent();
                        csvPrinter.printRecord(w, y0, vx0, collisionSystem.getTime() - pastEventTime, collisionSystem.getTime());
                    }
                        fileWriter.write(collisionSystem.writeEvent());
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                System.out.printf("Simulation ended after %f seconds %n", collisionSystem.getTime());
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
                System.out.printf("Simulation ended after %f seconds %n", collisionSystem.getTime());
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
            -0.025596315f,
            0.024343103f,
            -0.02838714f,
            -0.029417235f,
            0.022747952f,
            -0.028205177f,
            -0.026237985f,
            -0.027319811f,
            0.023575434f,
            -0.02582139f,
            0.021362368f,
            0.022162775f,
            -0.028338164f,
            0.020601558f,
            -0.028244011f,
            -0.026999932f,
            -0.02950929f,
            0.020943455f,
            -0.028236402f,
            -0.029102143f,
            0.020404557f,
            0.023038285f,
            -0.028032625f,
            -0.028947951f,
            -0.027090013f,
            0.021726482f,
            0.022232033f,
            -0.027297636f,
            0.02262886f,
            -0.025273262f
        };
        if (epsilonIdx < 0 || epsilonIdx >= epsilons.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return epsilons[epsilonIdx];
    }

}
