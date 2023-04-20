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
        return balls.size() > HOLES_AMOUNT;
    }

    /**
     * Simulates the collision system until there are no more events to simulate
     */
    public void simulateNextEvent() {
        calculatePosibleEvents();
        Event event = pq.poll();

        while(event.wasSuperveningEvent())
            event = pq.poll();

        // Move all balls to the time of the event
        for (Ball ball : balls) {
            if (ball.isHole()) {
                continue;
            }
            ball.move(event.getTimeToCollision());
        }

        // Process the event
        Ball a = event.getBall1();
        Ball b = event.getBall2();

        // Update the velocity of the balls or remove them
        if (a == null)
            b.bounceX();
        else if (b == null)
            a.bounceY();
        else if (a.isHole()) {
            balls.remove(b);
        } else if (b.isHole()) {
            balls.remove(a);
        } else {
            a.bounce(b);
        }

        time += event.getTimeToCollision();

    }

    // Calculates the minimun time to collision for each balls and add those events to the priority queue
    public void calculatePosibleEvents() {
        for (Ball ball : balls) {
            Event minEvent = null;
            if (ball.isHole()) {
                continue;
            }
            // Calculate all possible collisions between balls
            for (Ball otherBall : balls) {
                if (ball != otherBall) {
                    double time = ball.collides(otherBall);
                    if (time > 0 && (minEvent == null || time < minEvent.getTimeToCollision())) {
                        minEvent = new Event(ball, otherBall, time);
                    }
                }

                // Calculate all possible collisions between balls and walls
                double verticalTime = ball.collidesY();
                double horizontalTime = ball.collidesX();
                if (verticalTime > 0 && (minEvent == null || verticalTime < minEvent.getTimeToCollision())) {
                    minEvent = new Event(ball, null, verticalTime);
                }
                if (horizontalTime > 0 && (minEvent == null || horizontalTime < minEvent.getTimeToCollision())) {
                    minEvent = new Event(null, ball, horizontalTime);
                }
            }
            if (minEvent != null)
                pq.add(minEvent);
        }
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
    public String writeEvent() {
        StringBuilder result = new StringBuilder();

        result.append(balls.size()).append('\n');
        result.append(time).append('\n');
        for (Ball ball : balls) {
            result.append(ball.getNumber()).append(" ");
            result.append(ball.getPosition().getX()).append(" ");
            result.append(ball.getPosition().getY()).append(" ");
            result.append(ball.getVelocity().getX()).append(" ");
            result.append(ball.getVelocity().getY()).append(" ");
            result.append(ball.getMass()).append(" ");
            result.append(ball.getRadius()).append(" ");
            result.append(ball.getColor()).append(" ");
            result.append('\n');
        }
        return result.toString();
    }

    public double getTime() {
        return time;
    }
}
