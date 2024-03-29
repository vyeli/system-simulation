import helpers.Pair;

import java.util.List;

public class Ball {
    private double mass = 0.165;        // kg
    private double radius = 0.0285;     // m
    private final String color;
    private boolean isHole = false;
    private int number;

    private Pair<Double, Double> r;         // (x, y)
    private Pair<Double, Double> v;         // (vx, vy)
    private Pair<Double, Double> a = new Pair<>(0.0, 0.0);         // (ax, ay)
    private Pair<Double, Double> r3 = new Pair<>(0.0, 0.0);
    private Pair<Double, Double> r4 = new Pair<>(0.0, 0.0);
    private Pair<Double, Double> r5 = new Pair<>(0.0, 0.0);

    private Pair<Double, Double> rPred = new Pair<>(0.0, 0.0);

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
        this.rPred = new Pair<>(this.r.getX(), this.r.getY());
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
            // totalForce.setX(totalForce.getX() + k * radius);
            totalForce.setX(totalForce.getX() + k * (radius - r.getX()));
        }
        if (r.getX() + radius >= xWall) {
            // totalForce.setX(totalForce.getX() - k * radius);
            totalForce.setX(totalForce.getX() - k * (r.getX() + radius - xWall));
        }
        if (r.getY() - radius <= 0) {
            // totalForce.setY(totalForce.getY() + k * radius);
            totalForce.setY(totalForce.getY() + k * (radius - r.getY()));
        }
        if (r.getY() + radius >= yWall) {
            // totalForce.setY(totalForce.getY() - k * radius);
            totalForce.setY(totalForce.getY() - k * (r.getY() + radius - yWall));
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

        rPred.setX(r.getX());
        rPred.setY(r.getY());

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
        double distance = Math.sqrt(Math.pow(b.r.getX() - this.r.getX(), 2) + Math.pow(b.r.getY() - this.r.getY(), 2));
        Double[] rvector = {(b.r.getX() - this.r.getX())/distance, (b.r.getY() - this.r.getY())/distance};
        Double[] force;
        // If the balls are not colliding, return 0 force
        if (distance > this.radius + b.radius) {
            force = new Double[]{0.0, 0.0};
        } else {
            double forceFactor = k * (distance - (this.radius + b.radius));
            force = new Double[]{forceFactor * rvector[0], forceFactor * rvector[1]};
            // if (this.number == 6 && b.number == 10 && !Double.isNaN(force[0]) && !Double.isNaN(force[1])) {
            //     System.out.println("Positions: ");
            //     System.out.println("- 10: (" + b.getR().getX() + ", " + b.getR().getY() + ")");
            //     System.out.println("- 6: (" + getR().getX() + ", " + getR().getY() + ")");
            //     System.out.println("\tForce on contact (from 10 to 6): (" + force[0] + ", " + force[1] + ")");
            // }
        }
        return force;
    }

    public boolean isInHole(Ball hole) {
        double distance = Math.sqrt(Math.pow(hole.r.getX() - this.r.getX(), 2) + Math.pow(hole.r.getY() - this.r.getY(), 2));
        return distance <= hole.radius + this.radius ;
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

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Pair<Double, Double> getR() {
        return this.r;
    }

    public void setR(Pair<Double, Double> r) {
        this.r = r;
    }

    public Pair<Double, Double> getRPred() {
        return this.rPred;
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
