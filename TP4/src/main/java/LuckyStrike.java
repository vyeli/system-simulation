import helpers.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LuckyStrike {
    private static final double MIN_Y0_WHITE_BALL = 0.42;
    private static final double MAX_Y0_WHITE_BALL = 0.56;

    private static final String[] CSV_HEADERS = {"y0", "duration"};

    public static void main(String[] args) throws IOException {

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("execution_data_full.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        // Lucky Strike Experiment
        double dt = 0.0001;     // s
        double vx0 = 1;         // m/s

        Long iteration = 0L;
        Long snapshot = 1000L;

        // FileWriter[] fileWriters = new FileWriter[20];

        double dy = (MAX_Y0_WHITE_BALL - MIN_Y0_WHITE_BALL) / 19;

        // 20 y0 values
        for (int i = 0; i< 20; i++) {
            // 20 files
            // fileWriters[i] = new FileWriter("output-" + (MIN_Y0_WHITE_BALL + dy * i) * 100 + ".txt");
            // 5 random epsilons values for each y0
            for (int j = 0; j < 10; j++) {
                List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
                // Triangle balls positions
                for (int k = 0; k < 15; k++) {
                    ballsEpsilon.add(new Pair<>(getRandomEpsilon() / 100, getRandomEpsilon() / 100));
                }
                Table gameTable = new Table(MIN_Y0_WHITE_BALL + dy * i, vx0, ballsEpsilon);
                List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());

                // List<Ball> holes = balls.stream().filter(ball -> !ball.isHole()).collect(Collectors.toList());
                // balls.removeAll(holes);

                CollisionSystem collisionSystem = new CollisionSystem(balls, dt, Table.getWidth(), Table.getHeight());
                do {
                    if (j == 0 && iteration % snapshot == 0 ) {
                        // fileWriters[i].write(collisionSystem.writeTable());
                    }
                    iteration++;
                    collisionSystem.evolveSystemWithHoles();
                } while (collisionSystem.getNumberOfBalls() > 0);
                if (j == 0) {
                    // fileWriters[i].write(collisionSystem.writeTable());
                }
                csvPrinter.printRecord(MIN_Y0_WHITE_BALL + dy * i, collisionSystem.getTime());
            }
            // fileWriters[i].close();
            iteration = 0L;
        }
        csvPrinter.close();
        // End of Lucky Strike Experiment
    }


    public static double getRandomEpsilon() {
        double randValue = Math.random();
        double epsilon = 0.02 + randValue * 0.01;
        if(randValue > 0.5) {
            epsilon = -epsilon;
        }
        return epsilon;
    }
}
