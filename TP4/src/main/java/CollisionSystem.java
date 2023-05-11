import java.util.List;

public class CollisionSystem {
    private final List<Ball> balls;
    private double t;

    private final double dt;

    private final double tf;

    public CollisionSystem(List<Ball> balls, double tf, double dt) {
        this.balls = balls;
        this.tf = tf;
        this.dt = dt;
    }

    public void evolveSystem() {
        // evolucionar sistema hasta el dt actual
        for (Ball ball : balls) {
            ball.move(dt);
        }
        // actualizar aceleración de cada bola
        for (Ball ball : balls) {
            ball.calculateAcceleration(balls);
        }
        // predecir y corregir valores
        for (Ball ball : balls) {
            ball.gearPredEvolve(dt);
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
