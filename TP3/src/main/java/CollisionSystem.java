import java.util.List;
import java.util.PriorityQueue;

public class CollisionSystem {
    private final PriorityQueue<Event> pq;
    private final List<Ball> balls;
    private final int HOLES_AMOUNT = 6;
    private double time;

    public CollisionSystem(List<Ball> balls) {
        pq = new PriorityQueue<>();
        this.balls = balls;
    }

    public boolean hasNextEvent() {
        return pq.size() > HOLES_AMOUNT;
    }

    public void simulate() {
        calculatePosibleEvents();
        while (hasNextEvent()) {
            Event event = pq.poll();
            if (!event.wasSuperveningEvent()) {
                continue;
            }
            for (Ball ball : balls) {
                ball.move(event.getTimeToCollision() - time);
            }
            time = event.getTimeToCollision();
            event.resolve();
            calculatePosibleEvents();
        }
    }

    // TODO AVOID HOLES
    public void calculatePosibleEvents() {
        for (Ball ball : balls) {
            for (Ball otherBall : balls) {
                if (ball != otherBall) {
                    double time = ball.collides(otherBall);
                    if (time > 0) {
                        pq.add(new Event(ball, otherBall, time));
                    }
                }
            }

            double horizontalTime = ball.collidesX();
            double verticalTime = ball.collidesY();
            if (horizontalTime > 0) {
                pq.add(new Event(ball, null, horizontalTime));
            }
            if (verticalTime > 0) {
                pq.add(new Event(ball, null, verticalTime));
            }
        }
    }







}