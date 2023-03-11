import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Particle {

    private int id;
    private final Set<Integer> neighbours;

    private Point point;
    private double radius;

    public Particle(int id, Point point, double radius) {
        this.id = id;
        this.point = point;
        this.radius = radius;
        this.neighbours = new HashSet<>();
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

    public void addNeighbour(int particleId) {
        neighbours.add(particleId);
    }

    public boolean isNeighbour(int maybeNeighbourId) {
        return neighbours.contains(maybeNeighbourId);
    }

    public Set<Integer> getNeighboursIds() {
        return neighbours;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || obj.getClass() != getClass())
            return false;

        Particle p = (Particle) obj;
        return p.id == id;
    }

}
