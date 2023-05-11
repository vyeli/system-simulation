import java.util.List;
import java.util.PriorityQueue;

public class CollisionSystem {
    private final List<Ball> balls;
    private double t;

    public CollisionSystem(List<Ball> balls, double tf) {
        this.balls = balls;
    }


    /**
     * Returns a string of the event with the format:
     * N
     * time
     * Number rx ry vx vy mass radius color
     * Number rx ry vx vy mass radius color
     * Number rx ry vx vy mass radius color
     * ...
     */
    public String writeTable() {
        StringBuilder result = new StringBuilder();

        result.append(balls.size()).append('\n');
        result.append(t).append('\n');
        for (Ball ball : balls) {
            result.append(ball.getNumber()).append(" ");
            result.append(ball.getPosition().getX()).append(" ");
            result.append(ball.getPosition().getY()).append(" ");
            result.append(ball.getVelocity().getX()).append(" ");
            result.append(ball.getVelocity().getY()).append(" ");
            result.append(ball.getMass()).append(" ");
            result.append(ball.getRadius()).append(" ");
            result.append(ball.getColor()).append(" ");
            result.append("\n");
        }
        result.append("\n");
        return result.toString();
    }

    public double getTime() {
        return t;
    }
}
