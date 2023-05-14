import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollisionSystem {
    private final List<Ball> balls;
    private double xWall;
    private double yWall;
    private double t;

    private List<Ball> holes = new ArrayList<>();

    private final double dt;

    public CollisionSystem(List<Ball> balls, double dt, double xWall, double yWall) {
        this.balls = balls;
        this.dt = dt;
        this.xWall = xWall;
        this.yWall = yWall;
        for (Ball ball : balls) {
            if (ball.isHole()) {
                holes.add(ball);
            }
        }
    }

    // TODO: Stop condition when no balls are alive
    public void evolveSystem() {
        // predecir valores
        for (Ball ball : balls) {
            ball.predictValues(dt);
        }

        // predecir aceleraciones
        for (Ball ball : balls) {
            ball.predictAcceleration(balls, xWall, yWall);
        }
        
        // corregir valores
        for (Ball ball : balls) {
            ball.correctValues(dt);
        }
        
        t += dt;
    }

    public void evolveSystemWithHoles() {
        List<Ball> ballsToRemove = new ArrayList<>();
        // predecir valores
        for (Ball ball : balls) {
            ball.predictValues(dt);
        }

        // predecir aceleraciones
        for (Ball ball : balls) {
            ball.predictAcceleration(balls, xWall, yWall);
        }

        // corregir valores
        for (Ball ball : balls) {
            ball.correctValues(dt);
        }

        // remove balls that are in a hole
        for (Ball ball : balls) {
            if (ball.isHole()) {
                continue;
            }
            for (Ball hole : holes) {
                if (ball.isInHole(hole)) {
                    ballsToRemove.add(ball);
                }
            }
        }
        for (Ball ball : ballsToRemove) {
            balls.remove(ball);
        }

        t += dt;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public int getNumberOfBalls() {
        return balls.size();
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
            result.append(ball.getR().getX()).append(" ");
            result.append(ball.getR().getY()).append(" ");
            result.append(ball.getV().getX()).append(" ");
            result.append(ball.getV().getY()).append(" ");
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
