import helpers.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuckyStrike {
    private static final double MIN_Y0_WHITE_BALL = 0.42;
    private static final double MAX_Y0_WHITE_BALL = 0.56;

    private static final String[] CSV_HEADERS = {"y0", "duration"};

    public static void main(String[] args) throws IOException {

        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("execution_data.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        // Lucky Strike Experiment
        double dt = 10^-4;     // s
        double vx0 = 1;         // m/s

        double dt2 = 1;

        FileWriter[] fileWriters = new FileWriter[20];

        double dy = (MAX_Y0_WHITE_BALL - MIN_Y0_WHITE_BALL) / 20;

        // 20 y0 values
        for (int i = 0; i< 20; i++) {
            // 20 files
            fileWriters[i] = new FileWriter("output" + (MIN_Y0_WHITE_BALL + dy * i) + ".txt");
            // 5 random epsilons values for each y0
            for (int j = 0; j < 5; j++) {
                List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
                // Triangle balls positions
                for (int k = 0; k < 15; k++) {
                    ballsEpsilon.add(new Pair<>(getRandomEpsilon() / 100, getRandomEpsilon() / 100));
                }
                Table gameTable = new Table(MIN_Y0_WHITE_BALL + dy * i, vx0, ballsEpsilon);
                List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());

                CollisionSystem collisionSystem = new CollisionSystem(balls, dt, Table.getWidth(), Table.getHeight());
                while (collisionSystem.getNumberOfBalls() > 9) {
                    collisionSystem.evolveSystemWithHoles();
                    if (j == 0 && collisionSystem.getTime() % dt2 == 0) {
                        fileWriters[i].write(collisionSystem.writeTable());
                    }

                }
                csvPrinter.printRecord(MIN_Y0_WHITE_BALL + dy * i, collisionSystem.getTime());
            }
            fileWriters[i].close();
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
