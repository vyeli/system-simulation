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

    private int exitedPedestriansAmount = 0;

    private final double doorTargetStart;
    private final double doorTargetEnd;

    private double secondTargetXStart = 8.5;
    private double secondTargetXEnd = 11.5;
    private double secondTargetY = -5;

    public PedestrianSystem(List<Pedestrian> pedestrians, double dt, double vdMax, double rMin, double rMax, double beta, double tau, double boxSize, double doorWidth) {
        this.pedestrians = pedestrians;
        this.dt = dt;
        this.vdMax = vdMax;
        this.rMin = rMin;
        this.rMax = rMax;
        this.beta = beta;
        this.tau = tau;
        this.boxSize = boxSize;
        this.doorTargetStart = (boxSize - doorWidth) / 2 + 0.1;
        this.doorTargetEnd = (boxSize + doorWidth) / 2 - 0.1;
    }

    public List<Pedestrian> evolveSystem() {

        // First iteration (Calculate Ve)
        for (Pedestrian current : pedestrians) {
            double[] eijAcum = {0d, 0d};
            for (Pedestrian other : pedestrians) {
                if (current == other || !current.overlapsWith(other)) {
                    continue;
                }
                double[] eij = current.calculateEij(other);
                eijAcum[0] += eij[0];
                eijAcum[1] += eij[1];
            }

            if (current.isExited() && current.getPosition().getY() - current.getR() > 0) {
                current.setExited(false);
                exitedPedestriansAmount--;
                current.setD(null);         // If exited, need to recalculate target
            }

            if (!current.isExited()) {
                // Collision with walls
                double[] wallsEij = {0d, 0d};

                if (current.getPosition().getX() - current.getR() <= 0) {           // Left wall
                    wallsEij[0] += current.getPosition().getX();
                }
                if (current.getPosition().getX() + current.getR() >= boxSize) {     // Right wall
                    wallsEij[0] += current.getPosition().getX() - boxSize;
                }
                if (current.getPosition().getY() - current.getR() <= 0) {           // Bottom wall
                    double yVector = 1;
                    if (current.getPosition().getX() >= doorTargetStart && current.getPosition().getX() <= doorTargetEnd) {
                        current.setExited(true);
                        exitedPedestriansAmount++;
                        current.setD(null);         // If exited, need to recalculate target
                        yVector = 0;
                    }
                    wallsEij[1] += yVector;
                }
                if (current.getPosition().getY() + current.getR() >= boxSize) {     // Top wall
                    wallsEij[1] += current.getPosition().getY() - boxSize;
                }

                double wallsNorm = Math.sqrt(Math.pow(wallsEij[0], 2) + Math.pow(wallsEij[1], 2));
                eijAcum[0] += wallsEij[0] / wallsNorm;
                eijAcum[1] += wallsEij[1] / wallsNorm;
            }

            double acumNorm = Math.sqrt(Math.pow(eijAcum[0], 2) + Math.pow(eijAcum[1], 2));
            if (acumNorm > 0) {
                current.setE(new Pair<>(eijAcum[0] / acumNorm, eijAcum[1] / acumNorm));
                current.setD(null);
            } else {
                current.setE(null);
                if (current.getD() == null) {
                    if (current.isExited()) {
                        current.calculateD(secondTargetXStart, secondTargetXEnd);
                    } else {
                        current.calculateD(doorTargetStart, doorTargetEnd);
                    }
                }
            }
        }

        // Second iteration (Update Rii)
        for (Pedestrian current : pedestrians) {
            if (current.getE() != null) {
                current.setR(rMin);
            }
            else if (current.getR() < rMax) {
                current.setR(current.getR() + rMax / (this.tau / this.dt));
                if (current.getR() > rMax) {
                    current.setR(rMax);
                }
            }
        }

        // Third iteration (Update velocities and positions)
        for (Pedestrian current : pedestrians) {
            if (current.getE() == null) {
                current.calculateVd();
                current.setV(new Pair<>(current.getVd().getX(), current.getVd().getY()));
            } else {
                current.setV(new Pair<>(vdMax * current.getE().getX(), vdMax * current.getE().getY()));
            }
            current.getPosition().setX(current.getPosition().getX() + current.getV().getX() * this.dt);
            current.getPosition().setY(current.getPosition().getY() + current.getV().getY() * this.dt);
        }

        // Check if there are pedestrians that have left the box
        List<Pedestrian> pedestriansToRemove = new ArrayList<>();
        for (Pedestrian current : pedestrians) {
            if (current.isExited() && current.getPosition().getY() - current.getR() <= secondTargetY &&
                    current.getPosition().getX() >= secondTargetXStart &&
                    current.getPosition().getX() <= secondTargetXEnd) {
                pedestriansToRemove.add(current);
            }
        }
        pedestrians.removeAll(pedestriansToRemove);

        this.time += this.dt;

        return pedestriansToRemove;
    }

    public int getExitedPedestriansAmount() {
        return exitedPedestriansAmount;
    }

    public boolean hasPedestriansLeft() {
        return pedestrians.size() > 0;
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public double getDeltaT() {
        return this.dt;
    }

    public double getTime() {
        return this.time;
    }

    /**
     * Returns a string of the event with the format:
     * N
     * time
     * rx ry vx vy radius
     * rx ry vx vy radius
     * rx ry vx vy radius
     * ...
     */
    public String writePedestrians() {
        StringBuilder sb = new StringBuilder();
        sb.append(pedestrians.size()).append("\n");
        sb.append(time).append("\n");

        for (Pedestrian current : pedestrians) {
            sb.append(current.getId()).append(" ");
            sb.append(current.getPosition().getX()).append(" ").append(current.getPosition().getY()).append(" ");
            sb.append(current.getV().getX()).append(" ").append(current.getV().getY()).append(" ");
            sb.append(current.getR()).append("\n");
        }
        return sb.toString();
    }
}
