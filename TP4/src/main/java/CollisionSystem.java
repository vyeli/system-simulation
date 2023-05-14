import java.util.List;

public class CollisionSystem {
    private final List<Ball> balls;
    private double xWall;
    private double yWall;
    private double t;

    private final double dt;

    private final double tf;

    public CollisionSystem(List<Ball> balls, double tf, double dt, double xWall, double yWall) {
        this.balls = balls;
        this.tf = tf;
        this.dt = dt;
        this.xWall = xWall;
        this.yWall = yWall;
    }

    // TODO: Stop condition when no balls are alive
    public void evolveSystem() {
        // evolucionar el sistema hasta t
        // for (Ball ball : balls) {
        //     ball.move(dt);
        // }

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
            result.append(ball.getRPred().getX()).append(" ");
            result.append(ball.getRPred().getY()).append(" ");
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
