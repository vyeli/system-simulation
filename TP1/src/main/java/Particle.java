import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Particle {

    private int id;

    private Point point;
    private double radius;

    public Particle(int id, Point point, double radius) {
        this.id = id;
        this.point = point;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
