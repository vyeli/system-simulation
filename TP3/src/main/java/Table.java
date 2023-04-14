import helpers.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Table {

    private static final int height = 112;            // cm
    private static final int width = 224;           // cm
    private final double epsilonMax = 0.03;
    private double ballRadius = 2.85;     // cm

    private Map<Integer, Ball> balls = new HashMap<>();

    public Table(final double initialBallYPos, final List<Pair<Double, Double>> ballsEpsilon) {
        this(initialBallYPos, 2.0, ballsEpsilon);
    }

    public Table(final double initialBallYPos, final Double initialBallXSpeed, final List<Pair<Double, Double>> ballsEpsilon) {
        int ballIndex = 0;
        balls.put(ballIndex, new Ball(ballIndex++, ballRadius, false, new Pair<>(56.0, initialBallYPos), new Pair<>(0.0, initialBallXSpeed)));

        double xyEpsilon = 2 * (epsilonMax + ballRadius);
        // Triangle balls
        for (int i=0 ; i < 5 ; i++) {
            double xPos = 168 + xyEpsilon * i;
            double initialYPos = 56 - (xyEpsilon * i) / 2;
            for (int j=0 ; j <= i ; j++) {
                Pair<Double, Double> ballEpsilon = ballsEpsilon.get(ballIndex - 1);
                balls.put(ballIndex, new Ball(ballIndex++, ballRadius, false, new Pair<>(xPos + ballEpsilon.getX(), initialYPos + j * xyEpsilon + ballEpsilon.getY())));
            }
        }

        // Corners
        for (double xMult=0.0 ; xMult < 1.1 ; xMult += 0.5) {
            for(double yMult=0.0 ; yMult < 1.1 ; yMult += 0.5) {
                if (!(xMult > 0.4 && xMult < 0.6) || !(yMult > 0.4 && yMult < 0.6)) {
                    balls.put(ballIndex, new Ball(ballIndex++, ballRadius, true, new Pair<>(xMult * width, yMult * height)));
                }
            }
        }

    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public Map<Integer, Ball> getBalls() {
        return this.balls;
    }

}
