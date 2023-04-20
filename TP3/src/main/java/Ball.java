import helpers.Pair;

public class Ball {
    private double mass = 165;        // gr
    private double radius = 2.35;     // cm
    private final String color;
    private boolean isHole = false;
    private int number;
    private int collisionCount;
    private Pair<Double, Double> position;      // (x, y)
    private Pair<Double, Double> velocity;         // (x, y)

    private final Double NEGATIVE_TIME = -1.0;

    public Ball(final int number, final double radius, final boolean isHole, final Pair<Double, Double> initialPosition, String color) {
        this(number, radius, isHole, color, initialPosition, new Pair<>(0.0, 0.0));
    }


    public Ball(final int number, final double radius, final boolean isHole, String color, final Pair<Double, Double> initialPosition, final Pair<Double, Double> initialVelocity) {
        this.radius = radius;
        this.color = color;
        if(isHole) {
            this.isHole = true;
            this.radius *= 2;         // Holes are simulated as balls, but double the width
        }
        this.number = number;
        this.position = initialPosition;
        this.velocity = initialVelocity;
        this.collisionCount = 0;
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
            return NEGATIVE_TIME;
        }
        if (velocity.getX() > 0) {
            return (Table.getWidth() - radius - position.getX()) / velocity.getX();
        }
        return (radius - position.getX()) / velocity.getX();
    }

     // return the duration of time until the invoking particle collides with a horizontal wall
    public double collidesY() {
        if (velocity.getY() == 0) {
            return NEGATIVE_TIME;
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
        double tc = NEGATIVE_TIME;

        double[] deltaR = deltaR(b);
        double[] deltaV = deltaV(b);

        double dotProduct = dotProduct(deltaR, deltaV);
        double RSquared = dotProduct(deltaR, deltaR);
        double VSquared = dotProduct(deltaV, deltaV);

        double sigma = this.radius + b.radius;

        double d = dotProduct * dotProduct - VSquared * (RSquared - sigma * sigma);

        if (d >= 0 && dotProduct < 0) {
            tc = -(dotProduct + Math.sqrt(d)) / VSquared;
        }

        return tc;
    }
    /**
     * update the invoking particle to simulate it bouncing off a vertical wall
     */
    public void bounceX() {
        double currentXSpeed = velocity.getX();
        velocity.setX(-currentXSpeed);
        collisionCount++;
    }

    /**
     * update the invoking particle to simulate it bouncing off a horizontal wall
     */
    public void bounceY() {
        double currentYSpeed = velocity.getY();
        velocity.setY(-currentYSpeed);
        collisionCount++;
    }

    /**
     * update both particles to simulate them bouncing off each other
     * @param b the other particle
     */
    public void bounce(Ball b) {
        double[] deltaR = deltaR(b);
        double[] deltaV = deltaV(b);

        double RSquared = dotProduct(deltaR, deltaR);
        double dotProduct = dotProduct(deltaR, deltaV);

        double sigma = this.radius + b.radius;

        double j = 2 * this.mass * b.mass * dotProduct / ((this.mass + b.mass) * sigma);

        double[] jxy = {j * deltaR[0] / RSquared, j * deltaR[1] / sigma};

        this.velocity.setX(this.velocity.getX() + jxy[0] / this.mass);
        this.velocity.setY(this.velocity.getY() + jxy[1] / this.mass);

        b.velocity.setX(b.velocity.getX() - jxy[0] / b.mass);
        b.velocity.setY(b.velocity.getY() - jxy[1] / b.mass);

        collisionCount++;

    }

    public int getCollisionCount() {
        return collisionCount;
    }

    private double[] deltaR(Ball b) {
        return new double[]{b.position.getX() - this.position.getX(), b.position.getY() - this.position.getY()};
    }

    private double[] deltaV(Ball b) {
        return new double[]{b.velocity.getX() - this.velocity.getX(), b.velocity.getY() - this.velocity.getY()};
    }

    private double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }

    public void move(double time) {
        position.setX(position.getX() + velocity.getX() * time);
        position.setY(position.getY() + velocity.getY() * time);
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getColor() {
        return color;
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean hole) {
        isHole = hole;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCollisionCount(int collisionCount) {
        this.collisionCount = collisionCount;
    }

    public void setPosition(Pair<Double, Double> position) {
        this.position = position;
    }

    public Pair<Double, Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(Pair<Double, Double> velocity) {
        this.velocity = velocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ball ball = (Ball) o;

        return number == ball.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
