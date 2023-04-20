import helpers.Pair;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Table {

    private static final int height = 112;            // cm
    private static final int width = 224;           // cm
    private final double epsilonMax = 0.03;
    private double ballRadius = 2.85;     // cm

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
        this(initialBallYPos, 2.0, ballsEpsilon);
    }

    public Table(final double initialBallYPos, final Double initialBallXSpeed, final List<Pair<Double, Double>> ballsEpsilon) {
        int ballIndex = 0;
        balls.put(ballIndex, new Ball(ballIndex++, ballRadius, false, colors[ballIndex-1], new Pair<>(56.0, initialBallYPos), new Pair<>(initialBallXSpeed, 0.0)));

        double xyEpsilon = 2 * (epsilonMax + ballRadius);
        // Triangle balls
        for (int i=0 ; i < 5 ; i++) {
            double xPos = 168 + xyEpsilon * i;
            double initialYPos = 56 - (xyEpsilon * i) / 2;
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
