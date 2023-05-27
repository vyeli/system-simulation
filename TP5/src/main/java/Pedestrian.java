import helpers.Pair;

public class Pedestrian {
    
    private double r, vdMax, rMin, rMax, beta;
    private Pair<Double, Double> v;
    private Pair<Double, Double> position;
    private Pair<Double, Double> e;
    private Pair<Double, Double> vd;
    private double doorStart;
    private double doorEnd;

    public Pedestrian(double initialR, Pair<Double, Double> position, double rMin, double rMax, double vdMax, double doorStart, double doorEnd, double beta) {
        this.r = initialR;
        this.position = position;
        this.vdMax = vdMax;
        this.rMin = rMin;
        this.rMax = rMax;
        this.doorStart = doorStart;
        this.doorEnd = doorEnd;
        this.beta = beta;
        calculateVd();
        this.v = vd;
    }

    public boolean overlapsWith(Pedestrian otherPedestrian) {
        return Math.sqrt(Math.pow(position.getX() - otherPedestrian.position.getX(), 2) + Math.pow(position.getY() - otherPedestrian.position.getY(), 2)) < r + otherPedestrian.r;
    }


    public double[] calculateEij(Pedestrian otherP) {
        double[] eij = {position.getX() - otherP.position.getX(), position.getY() - otherP.position.getY()};
        double norm = Math.sqrt(Math.pow(eij[0], 2) + Math.pow(eij[1], 2));
        eij[0] = eij[0] / norm;
        eij[1] = eij[1] / norm;

        return eij;
    }

    public void calculateVd() {
        double vdMagnitud = vdMax * Math.pow((r - rMin) / (rMax - rMin), beta);
        if (position.getX() < doorStart) {
            double[] dij = {doorStart - position.getX(), 0 - position.getY()};
            double norm = Math.sqrt(Math.pow(dij[0], 2) + Math.pow(dij[1], 2));

            double[] vd = {dij[0] / norm * vdMagnitud, dij[1] / norm * vdMagnitud};
            this.setVd(new Pair<>(vd[0], vd[1]));

        } else if (position.getX() > doorEnd) {
            double[] dij = {doorEnd - position.getX(), 0 - position.getY()};
            double norm = Math.sqrt(Math.pow(dij[0], 2) + Math.pow(dij[1], 2));

            double[] vd = {dij[0] / norm * vdMagnitud, dij[1] / norm * vdMagnitud};
            this.setVd(new Pair<>(vd[0], vd[1]));
        } else {
            double[] dij = {0, 0 - position.getY()};
            double norm = Math.sqrt(Math.pow(dij[0], 2) + Math.pow(dij[1], 2));

            double[] vd = {dij[0] / norm * vdMagnitud, dij[1] / norm * vdMagnitud};
            this.setVd(new Pair<>(vd[0], vd[1]));
        }
    }

    public void updateVelocity(boolean isColliding, double[] eij) {
        if (isColliding) {

        }
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
}
