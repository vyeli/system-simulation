import helpers.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoolGame {

    private static final double MIN_Y0_WHITE_BALL = 0.42;
    private static final double MAX_Y0_WHITE_BALL = 0.56;

    private static final String[] CSV_HEADERS = {"k", "t", "x", "y"};

    public static void main(String[] args) throws IOException {

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("phit.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);


        // Parallel universes Experiment
        double tf = 20;         // s
        //double dt = 0.001;     // s

        double[] dts = {0.001, 0.0001, 0.00001};

        double y0 = 0.56;       // m
        double vx0 = 1;         // m/s

        Map<Integer, Map<Double, List<Pair<Double, Double>>>> ballsPositions = new HashMap<>();

        // FileWriter fileWriter = new FileWriter("output.txt");

        List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
        // Triangle balls positions
        for (int j = 0; j < 15; j++) {
            ballsEpsilon.add(new Pair<>(getDeterministicDoubleEpsilons(2*j) / 100, getDeterministicDoubleEpsilons(2*j+1) / 100));
        }
        Table gameTable = new Table(y0, vx0, ballsEpsilon);
        gameTable.removeCornerBall();
        List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());

        int k = 3;
        for (Double dt : dts) {
            int iterations = (int) (tf / dt);
            CollisionSystem collisionSystem = new CollisionSystem(balls, dt, Table.getWidth(), Table.getHeight());
            for (int i = 0; i < iterations; i++) {
                collisionSystem.evolveSystem();
                switch (k) {
                    case 3:
                        for (Ball ball : balls) {
                            try {
                                csvPrinter.printRecord(k, collisionSystem.getTime(), ball.getR().getX(), ball.getR().getY());
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                        }
                    case 4:
                        if (i % 10 == 0) {
                            for (Ball ball : balls) {
                                try {
                                    csvPrinter.printRecord(k, collisionSystem.getTime(), ball.getR().getX(), ball.getR().getY());
                                } catch (IOException e) {
                                    System.out.println("An error occurred.");
                                    e.printStackTrace();
                                }
                            }
                        }
                    case 5:
                        if (i % 100 == 0) {
                            for (Ball ball : balls) {
                                try {
                                    csvPrinter.printRecord(k, collisionSystem.getTime(), ball.getR().getX(), ball.getR().getY());
                                } catch (IOException e) {
                                    System.out.println("An error occurred.");
                                    e.printStackTrace();
                                }
                            }
                        }
                }
            }
            k++;
        }


//        for (int i = 0; i < iterations; i++) {
//            int j = 500;
//            collisionSystem.evolveSystem();
//            if (i % j == 0) {
//                try {
//                    fileWriter.write(collisionSystem.writeTable());
//                } catch (IOException e) {
//                    System.out.println("An error occurred.");
//                    e.printStackTrace();
//                }
//            }
//        }

        //fileWriter.close();
        // End of Parallel universes Experiment

    }


    public static double getDeterministicDoubleEpsilons(int epsilonIdx) {
        double[] epsilons = {
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
}
