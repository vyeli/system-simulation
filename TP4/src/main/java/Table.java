import helpers.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

    private static final double height = 1.12;            // m
    private static final double width = 2.24;           // m
    private final double epsilonMax = 0.0003;
    private double ballRadius = 0.0285;     // cm

    // Color of the 16 balls in the pool 51
    private final String[] colors = {
            /* 1. White */ "#FFFFFF",
            /* 2. Yellow */ "#FFFF00",
            /* 3. Blue */ "#0000FF",
            /* 4. Red */ "#FF0000",
            /* 5. Purple */ "#800080",
            /* 6. Orange */ "#FFA500",
            /* 7. Green */ "#008000",
            /* 8. Maroon */ "#800000",
            /* 9. Black */ "#000000",
            /* 10. Yellow */ "#FFFF00",
            /* 11. Blue */ "#0000FF",
            /* 12. Red */ "#FF0000",
            /* 13. Purple */ "#800080",
            /* 14. Orange */ "#FFA500",
            /* 15. Green */ "#008000",
            /* 16. Maroon */ "#800000",

    };


    private Map<Integer, Ball> balls = new HashMap<>();

    public Table(final double initialBallYPos, final List<Pair<Double, Double>> ballsEpsilon) {
        this(initialBallYPos, 1.0, ballsEpsilon);
    }

    public Table(final double initialBallYPos, final Double initialBallXSpeed, final List<Pair<Double, Double>> ballsEpsilon) {
        int ballIndex = 0;
        balls.put(ballIndex, new Ball(ballIndex++, ballRadius, false, colors[ballIndex-1], new Pair<>(0.56, initialBallYPos), new Pair<>(initialBallXSpeed, 0.0)));

        double xyEpsilon = 2 * (epsilonMax + ballRadius);
        // Triangle balls
        for (int i=0 ; i < 5 ; i++) {
            double xPos = 1.68 + xyEpsilon * i;
            double initialYPos = 0.56 - (xyEpsilon * i) / 2;
            for (int j=0 ; j <= i ; j++) {
                Pair<Double, Double> ballEpsilon = ballsEpsilon.get(ballIndex - 1);
                balls.put(ballIndex, new Ball(ballIndex++, ballRadius, false, new Pair<>(xPos + ballEpsilon.getX(), initialYPos + j * xyEpsilon + ballEpsilon.getY()), colors[ballIndex-1]));
            }
        }

        // Corners
        for (double xMult=0.0 ; xMult < 1.1 ; xMult += 0.5) {
            for (double yMult=0.0 ; yMult < 1.1 ; yMult += 1) {
                balls.put(ballIndex, new Ball(ballIndex++, ballRadius, true, new Pair<>(xMult * width, yMult * height), "#000000"));
            }
        }

    }

    // Remove the 6 corner balls
    public void removeCornerBall() {
        for (int i=16 ; i < 22 ; i++) {
            balls.remove(i);
        }
    }


    public static double getHeight() {
        return height;
    }

    public static double getWidth() {
        return width;
    }

    public Map<Integer, Ball> getBalls() {
        return this.balls;
    }

}
