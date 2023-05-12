import helpers.Pair;

import java.util.List;

public class Ball {
    private double mass = 0.165;        // kg
    private double radius = 0.0285;     // m
    private final String color;
    private boolean isHole = false;
    private int number;
    private int collisionCount;

    private Pair<Double, Double> r;         // (x, y)
    private Pair<Double, Double> v;         // (vx, vy)
    private Pair<Double, Double> a = new Pair<>(0.0, 0.0);         // (ax, ay)
    private Pair<Double, Double> r3 = new Pair<>(0.0, 0.0);
    private Pair<Double, Double> r4 = new Pair<>(0.0, 0.0);
    private Pair<Double, Double> r5 = new Pair<>(0.0, 0.0);

    private Pair<Double, Double> aCalc;         // (ax, ay)

    private final double k = 10000;        // N/m

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
        this.r = initialPosition;
        this.v = initialVelocity;
    }

    public int getNumber() {
        return this.number;
    }

    public Pair<Double, Double> getR() {
        return this.r;
    }

    public boolean collideX() {
        if (v.getX() == 0) {
            return false;
        }
        return v.getX() > 0 ? Table.getWidth() - radius - r.getX() <= 0 : radius - r.getX() <= 0;
    }

    public boolean collideY() {
        if (v.getY() == 0) {
            return false;
        }
        return v.getY() > 0 ? Table.getHeight() - radius - r.getY() <= 0 : radius - r.getY() <= 0;
    }

    public boolean collide(Ball b) {
        double norm = Math.sqrt(Math.pow(b.r.getX() - this.r.getX(), 2) + Math.pow(b.r.getY() - this.r.getY(), 2));
        return norm <= this.radius + b.radius;
    }

    public void predictAcceleration(List<Ball> otherBalls, double xWall, double yWall) {
        Pair<Double, Double> totalForce = new Pair<>(0.0, 0.0);
        for (Ball otherBall : otherBalls) {
            if (otherBall == this) {
                continue;
            }
            Double[] addedForce = getForce(otherBall);
            totalForce.setX(totalForce.getX() + addedForce[0]);
            totalForce.setY(totalForce.getY() + addedForce[1]);
        }
        if (r.getX() - radius <= 0) {
            // totalForce.setX(totalForce.getX() - k * r.getX());
            totalForce.setX(totalForce.getX() + k * radius);
        }
        if (r.getX() + radius >= xWall) {
            totalForce.setX(totalForce.getX() - k * radius);
            // totalForce.setX(totalForce.getX() - k * (xWall - r.getX()));
        }
        if (r.getY() - radius <= 0) {
            totalForce.setY(totalForce.getY() + k * radius);
            // totalForce.setY(totalForce.getY() - k * r.getY());
        }
        if (r.getY() + radius >= yWall) {
            totalForce.setY(totalForce.getY() - k * radius);
            // totalForce.setY(totalForce.getY() - k * (yWall - r.getY()));
        }
        // System.out.println("Force on ball #" + number + ": (" + totalForce.getX() + ", " + totalForce.getY() + ")");
        this.aCalc = new Pair<>(totalForce.getX() / mass, totalForce.getY() / mass);
    }

    public void predictValues(double dt) {
        double factor2 = Math.pow(dt, 2) / 2;
        double factor3 = Math.pow(dt, 3) / 6;
        double factor4 = Math.pow(dt, 4) / 24;
        double factor5 = Math.pow(dt, 5) / 120;

        // Calculating predictions
        r.setX(r.getX() + dt * v.getX() + factor2 * a.getX() + factor3 * r3.getX() + factor4 * r4.getX() + factor5 * r5.getX());
        r.setY(r.getY() + dt * v.getY() + factor2 * a.getY() + factor3 * r3.getY() + factor4 * r4.getY() + factor5 * r5.getY());

        v.setX(v.getX() + dt * a.getX() + factor2 * r3.getX() + factor3 * r4.getX() + factor4 * r5.getX());
        v.setY(v.getY() + dt * a.getY() + factor2 * r3.getY() + factor3 * r4.getY() + factor4 * r5.getY());

        a.setX(a.getX() + dt * r3.getX() + factor2 * r4.getX() + factor3 * r5.getX());
        a.setY(a.getY() + dt * r3.getY() + factor2 * r4.getY() + factor3 * r5.getY());

        r3.setX(r3.getX() + dt * r4.getX() + factor2 * r5.getX());
        r3.setY(r3.getY() + dt * r4.getY() + factor2 * r5.getY());

        r4.setX(r4.getX() + dt * r5.getX());
        r4.setY(r4.getY() + dt * r5.getY());

        // Useless, but part of gear pred
        // r5.setX(r5.getX());
        // r5.setY(r5.getY());
    }

    public void correctValues(double dt) {
        double factor2 = Math.pow(dt, 2) / 2;
        double factor3 = Math.pow(dt, 3) / 6;
        double factor4 = Math.pow(dt, 4) / 24;
        double factor5 = Math.pow(dt, 5) / 120;

        Pair<Double, Double> dR2 = new Pair<Double,Double>((aCalc.getX() - a.getX()) * factor2, (aCalc.getY() - a.getY()) * factor2);

        r.setX(r.getX() + 3.0/20 * dR2.getX());
        r.setY(r.getY() + 3.0/20 * dR2.getY());

        v.setX(v.getX() + (251.0/360 * dR2.getX()) / dt);
        v.setY(v.getY() + (251.0/360 * dR2.getY()) / dt);

        a.setX(a.getX() + dR2.getX() / factor2);
        a.setY(a.getY() + dR2.getY() / factor2);

        r3.setX(r3.getX() + (11.0/18 * dR2.getX()) / factor3);
        r3.setY(r3.getY() + (11.0/18 * dR2.getY()) / factor3);

        r4.setX(r4.getX() + (1.0/6 * dR2.getX()) / factor4);
        r4.setY(r4.getY() + (1.0/6 * dR2.getY()) / factor4);

        r5.setX(r5.getX() + (1.0/60 * dR2.getX()) / factor5);
        r5.setY(r5.getY() + (1.0/60 * dR2.getY()) / factor5);
    }


    /**
     * Get the force between two balls, if they are colliding
     */
    public Double[] getForce(Ball b) {
        double norm = Math.sqrt(Math.pow(b.r.getX() - this.r.getX(), 2) + Math.pow(b.r.getY() - this.r.getY(), 2));
        Double[] rvector = {(b.r.getX() - this.r.getX())/norm, (b.r.getY() - this.r.getY())/norm};
        Double[] force;
        // If the balls are not colliding, return 0 force
        if (norm > this.radius + b.radius) {
            force = new Double[]{0.0, 0.0};
            return force;
        }
        force = new Double[]{k * (norm - (this.radius + b.radius)) * rvector[0], k * (norm - (this.radius + b.radius)) * rvector[1]};
        this.collisionCount++;
        return force;
    }

    /**
     * update the invoking particle to simulate it bouncing off a vertical wall
     */
    public void bounceX() {
        double currentXSpeed = v.getX();
        v.setX(-currentXSpeed);
        collisionCount++;
    }

    /**
     * update the invoking particle to simulate it bouncing off a horizontal wall
     */
    public void bounceY() {
        double currentYSpeed = v.getY();
        v.setY(-currentYSpeed);
        collisionCount++;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    private double[] deltaR(Ball b) {
        return new double[]{b.r.getX() - this.r.getX(), b.r.getY() - this.r.getY()};
    }

    private double[] deltaV(Ball b) {
        return new double[]{b.v.getX() - this.v.getX(), b.v.getY() - this.v.getY()};
    }

    private double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }

    public void move(double time) {
        r.setX(r.getX() + v.getX() * time);
        r.setY(r.getY() + v.getY() * time);
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

    public void setR(Pair<Double, Double> r) {
        this.r = r;
    }

    public Pair<Double, Double> getV() {
        return v;
    }

    public void setV(Pair<Double, Double> v) {
        this.v = v;
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
