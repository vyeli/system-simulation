import helpers.Pair;

import java.util.List;
import java.util.LinkedList;

public class Ball {
    private double weight = 165.0;        // gr
    private double diameter;     // cm

    private boolean isHole = false;
    private int number;

    private Pair<Double, Double> position;      // (x, y)
    private Pair<Double, Double> speed;         // (x, y)

    private List<Collision> futureCollisions;

    public Ball(final int number, final double diameter, final boolean isHole, final Pair<Double, Double> initialPosition) {
        this(number, diameter, isHole, initialPosition, new Pair<>(0.0, 0.0));
    }

    public Ball(final int number, final double diameter, final boolean isHole, final Pair<Double, Double> initialPosition, final Pair<Double, Double> initialSpeed) {
        this.diameter = diameter;
        if(isHole) {
            this.isHole = true;
            this.diameter *= 2;         // Holes are simulated as balls, but double the width
        }
        this.number = number;
        this.position = initialPosition;
        this.speed = initialSpeed;
        this.futureCollisions = new LinkedList<>();
    }

    public int getNumber() {
        return this.number;
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

}
