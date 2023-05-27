import helpers.Pair;

import java.util.ArrayList;
import java.util.List;

public class PedestrianSystem {

    private double time;
    private final List<Pedestrian> pedestrians;
    private final double dt;
    private final double vdMax;
    private final double rMin;
    private final double rMax;
    private final double beta;
    private final double tau;
    private final double boxSize;
    private final double doorStart;
    private final double doorEnd;

    public PedestrianSystem(List<Pedestrian> pedestrians, double dt, double vdMax, double rMin, double rMax, double beta, double tau, double boxSize, double doorWidth) {
        this.pedestrians = pedestrians;
        this.dt = dt;
        this.vdMax = vdMax;
        this.rMin = rMin;
        this.rMax = rMax;
        this.beta = beta;
        this.tau = tau;
        this.boxSize = boxSize;
        this.doorStart = (boxSize - doorWidth) / 2 - 0.1;
        this.doorEnd = (boxSize + doorWidth) / 2 + 0.1;
    }

    public void evolveSystem() {

        // First iteration (Calculate Ve)
        List<Pedestrian> toRemove = new ArrayList<>();
        for (Pedestrian current : pedestrians) {
            double[] eijAcum = {0d, 0d};
            for (Pedestrian other : pedestrians) {
                if (current == other) {
                    continue;
                }
                if (current.overlapsWith(other)) {
                    double[] eij = current.calculateEij(other);
                    eijAcum[0] += eij[0];
                    eijAcum[1] += eij[1];
                }
            }

            // TODO: Check if this is correct
            // Collision with door -> Remove ball
            if (current.getPosition().getY() - current.getR() <= 0 && current.getPosition().getX() >= doorStart && current.getPosition().getX() <= doorEnd) {
                toRemove.add(current);
                continue;
            }

            if (current.getPosition().getX() - current.getR() <= 0 || current.getPosition().getX() + current.getR() >= boxSize) {
                // wall on the left -> add eij to the right (1, 0)
                eijAcum[0] -= current.getE().getX();
            }

            if (current.getPosition().getY() - current.getR() <= 0 || current.getPosition().getY() + current.getR() >= boxSize) {
                eijAcum[0] -= current.getE().getY();
            }

            double norm = Math.sqrt(Math.pow(eijAcum[0], 2) + Math.pow(eijAcum[1], 2));
            if (Double.compare(norm, 0) != 0) {
                current.setE(new Pair<>(eijAcum[0] / norm, eijAcum[1] / norm));
            } else {
                current.setE(new Pair<>(0d, 0d));
            }
        }

        pedestrians.removeAll(toRemove);

        // System.out.println(pedestrians.get(0).getE());

        // Second iteration (Update Rii)
        for (Pedestrian current : pedestrians) {
            if (current.getE().getX() != 0 || current.getE().getY() != 0) {
                current.setR(rMin);
            } else {
                current.setR(current.getR() + rMax / (this.tau / this.dt));
            }
        }

        // Third iteration (Calculate Vd)
        for (Pedestrian current : pedestrians) {
            // if the particle is not colliding with any other particle, calculate Vd
            if (Double.compare(current.getR(), rMin) != 0) {
                current.calculateVd();
            }
        }

        // Fourth iteration (Update Velocity and Position)
        for (Pedestrian current : pedestrians) {
            if (Double.compare(current.getR(), rMin) == 0) {
                current.setV(new Pair<>(vdMax * current.getE().getX(), vdMax * current.getE().getY()));
            }
            else {
                current.setV(new Pair<>(current.getVd().getX(), current.getVd().getY()));
            }
            current.getPosition().setX(current.getPosition().getX() + current.getV().getX() * this.dt);
            current.getPosition().setY(current.getPosition().getY() + current.getV().getY() * this.dt);
        }

        this.time += this.dt;
    }

    public boolean hasPedestriansLeft() {
        return pedestrians.size() > 0;
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }
}
