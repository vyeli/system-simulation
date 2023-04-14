import helpers.Pair;

import java.util.Map;
import java.util.HashMap;

public class Table {
    private static final int height = 112;            // cm
    private static final int width = 224;           // cm

    private Map<Integer, Ball> balls = new HashMap<>();

    public Table(final double initialBallYPos) {
        this(initialBallYPos, 2.0);
    }

    public Table(final double initialBallYPos, final Double initialBallXSpeed) {
        int ballIndex = 0;
        balls.put(ballIndex, new Ball(ballIndex++, false, new Pair<>(56.0, initialBallYPos), new Pair<>(0.0, initialBallXSpeed)));
        balls.put(ballIndex, new Ball(ballIndex++, false, new Pair<>(168.0, 56.0)));
        for (int i=2 ; i < 16 ; i++) {
            balls.put(ballIndex, new Ball(ballIndex++, false, new Pair<>(0.0, 0.0)));
        }
        for (double xMult=0.0 ; xMult < 1.1 ; xMult += 0.5) {
            for(double yMult=0-0 ; yMult < 1.1 ; yMult += 0.5) {
                if (!(xMult > 0.4 && xMult < 0.6) || !(yMult > 0.4 && yMult < 0.6)) {
                    balls.put(ballIndex, new Ball(ballIndex++, true, new Pair<>(xMult * width, yMult * height)));
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
