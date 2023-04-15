public class Event {
    private Ball ball1, ball2;
    private double timeToCollision;

    int b1CollitionCount = 0, b2CollitionCount = 0;

    /**
     * @param ball1
     * @param ball2
     * @param timeToCollision
     * Create a new event representing a collision between
     * particles a and b at time t. If neither a nor b is null, then it represents a pairwise collision between a and b; if both a
     * and b are null, it represents a redraw event; if only b is null, it represents a collision between a and a vertical wall; if
     * only a is null, it represents a collision between b and a horizontal wall.
     */
    public Event(Ball ball1, Ball ball2, double timeToCollision) {
        this.ball1 = ball1;
        this.ball2 = ball2;
        this.timeToCollision = timeToCollision;
        if (ball1 != null)
            this.b1CollitionCount = ball1.getCollisionCount();
        if (ball2 != null)
            this.b2CollitionCount = ball2.getCollisionCount();
    }

    /**
     * @return the first particle, possibly null.
     */
    public Ball getBall1() {
        return ball1;
    }

    /**
     * @return the second particle, possibly null.
     */
    public Ball getBall2() {
        return ball2;
    }

    /**
     * @return the time at which the event will occur.
     */
    public double getTimeToCollision() {
        return timeToCollision;
    }

    /**
     * compare the time associated with this event and other.
     * Return a positive number (greater), negative number (less), or zero (equal) accordingly
     */
    public int compareTo(Event other) {
        return Double.compare(timeToCollision, other.timeToCollision);
    }

    /**
     *  return true if the event has been invalidated since creation, and false otherwise
     *  In order to implement wasSuperveningEvent, the event data type should store the collision counts of two particles at the
     *  time the event was created. The event corresponds to a physical collision if the current collision counts of the particles are
     *  the same as when the event was created.
     */
    public boolean wasSuperveningEvent() {
        return (ball1 != null && ball1.getCollisionCount() != b1CollitionCount) || (ball2 != null && ball2.getCollisionCount() != b2CollitionCount);
    }

}
