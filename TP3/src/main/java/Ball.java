import helpers.Pair;

public class Ball {
  
    private double mass = 165;        // gr
    private double radius = 2.35;     // cm


    private boolean isHole = false;
    private int number;

    private Pair<Double, Double> position;      // (x, y)
    private Pair<Double, Double> velocity;         // (x, y)

    public Ball(final int number, final double radius, final boolean isHole, final Pair<Double, Double> initialPosition) {
        this(number, radius, isHole, initialPosition, new Pair<>(0.0, 0.0));
    }


    public Ball(final int number, final double radius, final boolean isHole, final Pair<Double, Double> initialPosition, final Pair<Double, Double> initialVelocity) {
        this.radius = radius;
        if(isHole) {
            this.isHole = true;
            this.radius *= 2;         // Holes are simulated as balls, but double the width
        }
        this.number = number;
        this.position = initialPosition;
        this.velocity = initialVelocity;
    }

    public int getNumber() {
        return this.number;
    }

    public Pair<Double, Double> getPosition() {
        return this.position;
    }

    // return the duration of time until the invoking particle collides with a vertical wall
    public double collidesX() {
        if (velocity.getX() == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (velocity.getX() > 0) {
            return (Table.getWidth() - radius - position.getX()) / velocity.getX();
        }
        return (radius - position.getX()) / velocity.getX();
    }

     // return the duration of time until the invoking particle collides with a horizontal wall
    public double collidesY() {
        if (velocity.getY() == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (velocity.getY() > 0) {
            return (Table.getHeight() - radius - position.getY()) / velocity.getY();
        }
        return (radius - position.getY()) / velocity.getY();
    }

    /**
     * return the duration of time until the invoking particle collides with another particle
     * @param b the other particle
     * @return the duration of time until the invoking particle collides with another particle if it does, Double.POSITIVE_INFINITY otherwise
     */
    public double collides(Ball b) {
        double tc = Double.POSITIVE_INFINITY;

        double[] deltaR = {b.position.getX() - this.position.getX(), b.position.getY() - this.position.getY()};
        double[] deltaV = {b.velocity.getX() - this.velocity.getX(), b.velocity.getY() - this.velocity.getY()};

        double dotProduct = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];
        double deltaR2 = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];
        double deltaV2 = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];


        double d = dotProduct * dotProduct - deltaV2 * (deltaR2 - (this.radius + b.radius) * (this.radius + b.radius));

        return tc;
    }

}
