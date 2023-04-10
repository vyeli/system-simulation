import helpers.Pair;

public class Collision implements Comparable<Collision>{
    private Pair<Ball, Ball> balls;
    private double timeToCollision;

    public Collision(final Pair<Ball, Ball> balls, final double timeToCollision) {
        this.balls = balls;
    }

    @Override
    public int compareTo(Collision other) {
        return Double.compare(timeToCollision, other.timeToCollision);
    }

}
