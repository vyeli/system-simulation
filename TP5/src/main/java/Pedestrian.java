import java.util.List;
import java.util.Random;

import helpers.Pair;

public class Pedestrian {
    
    private int id;
    private long neighbourAmount = 0;
    private double r, vdMax, rMin, rMax, beta;
    private Pair<Double, Double> v;
    private Pair<Double, Double> position;
    private Pair<Double, Double> vd;

    private Pair<Double, Double> e;
    private Pair<Double, Double> d;

    private boolean exited;

    private double secondTargetXStart = 8.5;
    private double secondTargetXEnd = 11.5;
    private double secondTargetY = -10.0;

    public Pedestrian(int id, double initialR, Pair<Double, Double> position, double rMin, double rMax, double vdMax, double initialTargetStart, double initialTargetEnd, double beta) {
        this.id = id;
        this.r = initialR;
        this.position = position;
        this.vdMax = vdMax;
        this.rMin = rMin;
        this.rMax = rMax;
        this.beta = beta;
        this.exited = false;
        calculateD(initialTargetStart, initialTargetEnd);
        calculateVd();
        this.v = vd;
    }

    public boolean overlapsWith(Pedestrian otherPedestrian) {
        double xDist = Math.abs(position.getX() - otherPedestrian.position.getX());
        double yDist = Math.abs(position.getY() - otherPedestrian.position.getY());
        return Math.hypot(xDist, yDist) < r + otherPedestrian.r;
        // return Math.sqrt(Math.pow(position.getX() - otherPedestrian.position.getX(), 2) + Math.pow(position.getY() - otherPedestrian.position.getY(), 2)) < r + otherPedestrian.r;
    }


    public double[] calculateEij(Pedestrian otherP) {
        double[] eij = {position.getX() - otherP.position.getX(), position.getY() - otherP.position.getY()};
        double norm = Math.sqrt(Math.pow(eij[0], 2) + Math.pow(eij[1], 2));
        eij[0] = eij[0] / norm;
        eij[1] = eij[1] / norm;

        return eij;
    }

    public void calculateD(double targetStart, double targetEnd) {
        double xVector = position.getX();
        double yVector = -position.getY();
        if (exited) {
            yVector = secondTargetY - position.getY(); 
        }
        if (position.getX() < targetStart || position.getX() > targetEnd) {
            xVector = randomizeTargetX(targetStart, targetEnd - targetStart);
        }
        d = new Pair<>(xVector - position.getX(), yVector);
    }

    public void calculateVd() {
        double vdMagnitud = vdMax * Math.pow((r - rMin) / (rMax - rMin), beta);
        double norm = Math.sqrt(Math.pow(d.getX(), 2) + Math.pow(d.getY(), 2));
        this.setVd(new Pair<>(d.getX() / norm * vdMagnitud, d.getY() / norm * vdMagnitud));
    }

    public double randomizeTargetX(double start, double width) {
        return start + Math.random() * width;
    }

    public void calculateNeighbourAmount(List<Pedestrian> possibleNeighbours, double neighbourRadius) {
        neighbourAmount = possibleNeighbours
                                .stream()
                                .filter(maybeNeighbour -> maybeNeighbour == this
                                        || Math.hypot(Math.abs(position.getX() - maybeNeighbour.position.getX()), Math.abs(position.getY() - maybeNeighbour.position.getY())) <= neighbourRadius + r + maybeNeighbour.r)
                                        .count();
    }

    public long getNeighbourAmount() {
        return this.neighbourAmount;
    }

    public int getId() {
        return id;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Pair<Double, Double> getV() {
        return v;
    }

    public void setV(Pair<Double, Double> v) {
        this.v = v;
    }

    public Pair<Double, Double> getPosition() {
        return position;
    }

    public void setPosition(Pair<Double, Double> position) {
        this.position = position;
    }

    public Pair<Double, Double> getE() {
        return e;
    }

    public void setE(Pair<Double, Double> e) {
        this.e = e;
    }

    public Pair<Double, Double> getD() {
        return d;
    }

    public void setD(Pair<Double, Double> d) {
        this.d = d;
    }

    public Pair<Double, Double> getVd() {
        return vd;
    }

    public void setVd(Pair<Double, Double> vd) {
        this.vd = vd;
    }

    public double getVdMax() {
        return vdMax;
    }

    public void setVdMax(double vdMax) {
        this.vdMax = vdMax;
    }

    public double getrMin() {
        return rMin;
    }

    public void setrMin(double rMin) {
        this.rMin = rMin;
    }

    public double getrMax() {
        return rMax;
    }

    public void setrMax(double rMax) {
        this.rMax = rMax;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pedestrian)) {
            return false;
        }
        Pedestrian other = (Pedestrian)o;
        return other.id == id;
    }
}
