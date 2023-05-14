import helpers.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuckyStrike {
    private static final double MIN_Y0_WHITE_BALL = 0.42;
    private static final double MAX_Y0_WHITE_BALL = 0.56;

    public static void main(String[] args) throws IOException {

        // Lucky Strike Experiment
        double dt = 10^-4;     // s
        double vx0 = 1;         // m/s

        double dt2 = 1;

        FileWriter fileWriter = null;

        double dy = (MAX_Y0_WHITE_BALL - MIN_Y0_WHITE_BALL) / 20;

        // 20 y0 values
        for (int i = 0; i< 20; i++) {
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
                    collisionSystem.evolveSystem();
                    if (j == 0 && collisionSystem.getTime() % dt2 == 0) {
                        fileWriter = new FileWriter("output" + MIN_Y0_WHITE_BALL + dy * i + ".txt");
                        fileWriter.write(collisionSystem.writeTable());
                    }

                }
                if (j == 0) {
                    fileWriter.close();
                }
            }
        }

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
