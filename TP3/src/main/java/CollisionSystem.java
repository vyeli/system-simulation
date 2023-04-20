import java.util.List;
import java.util.PriorityQueue;

public class CollisionSystem {
    private final PriorityQueue<Event> pq;
    private final List<Ball> balls;
    private double time;

    public CollisionSystem(List<Ball> balls) {
        pq = new PriorityQueue<>();
        this.balls = balls;
    }

    public boolean hasNextEvent() {
        int HOLES_AMOUNT = 6;
        return pq.size() > HOLES_AMOUNT;
    }

    /**
     * Simulates the collision system until there are no more events to simulate
     */
    public void simulate() {
        calculatePosibleEvents();
        while (hasNextEvent()) {
            Event event = pq.poll();
            assert event != null;

            if (event.wasSuperveningEvent()) {
                continue;
            }
            // Move all balls to the time of the event
            for (Ball ball : balls) {
                if (ball.isHole()) {
                    continue;
                }
                ball.move(event.getTimeToCollision() - time);
            }
            time = event.getTimeToCollision();

            // Process the event
            Ball a = event.getBall1();
            Ball b = event.getBall2();

            // If the event is a collision between two balls and are not holes
            if (a != null && b != null && !a.isHole() && !b.isHole()) {
                a.bounce(b);
            }
            // If the event is a collision between a ball and a vertical wall
            else if (a != null && b == null && !a.isHole()) {
                a.bounceX();
            }
            // If the event is a collision between a ball and a horizontal wall
            else if (a == null && b != null && !b.isHole()) {
                b.bounceY();
            }
            // If the event is a collision between a ball and a hole
            else if (a != null && b != null && !a.isHole() && b.isHole()) {
                a.setCollisionCount(a.getCollisionCount()+1);
                balls.remove(a);
            }
            // If the event is a collision between a ball and a hole
            else if (a != null && b != null && a.isHole() && !b.isHole()) {
                b.setCollisionCount(b.getCollisionCount()+1);
                balls.remove(b);
            }
            // If the event is a redraw
            else if (a == null && b == null) {
                continue;
            }
            calculatePosibleEvents();
        }
    }

    // Calculates the minimun time to collision for each balls and add those events to the priority queue
    public void calculatePosibleEvents() {
        Event minEvent = null;
        double minTime = Double.POSITIVE_INFINITY;

        // Calculate all possible collisions between balls
        for (Ball ball : balls) {
            for (Ball otherBall : balls) {
                if (ball != otherBall) {
                    double time = ball.collides(otherBall);
                    if (time > 0 && (time < minTime || minEvent == null)) {
                        minTime = time;
                        minEvent = new Event(ball, otherBall, time);
                    }
                }
            }

            // Calculate all possible collisions between balls and walls
            double verticalTime = ball.collidesY();
            double horizontalTime = ball.collidesX();
            if (verticalTime > 0 && (verticalTime < minTime || minEvent == null)) {
                minTime = verticalTime;
                minEvent = new Event(ball, null, verticalTime);
            }
            if (horizontalTime > 0 && (horizontalTime < minTime || minEvent == null)) {
                minTime = horizontalTime;
                minEvent = new Event(null, ball, horizontalTime);
            }
        }
        pq.add(minEvent);
    }

}