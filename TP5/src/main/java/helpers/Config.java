package helpers;

public class Config {
    private int iterations;
    private double boxSize;
    private double doorWidth;
    private double minR, maxR, vdMax;
    private double beta;
    private int particles;
    private double tau;

    public void setBoxSize(double boxSize) {
        this.boxSize = boxSize;
    }

    public void setDoorWidth(double doorWidth) {
        this.doorWidth = doorWidth;
    }

    public void setMinR(double minR) {
        this.minR = minR;
    }

    public void setMaxR(double maxR) {
        this.maxR = maxR;
    }

    public void setVdMax(double vdMax) {
        this.vdMax = vdMax;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getBoxSize() {
        return boxSize;
    }

    public double getDoorWidth() {
        return doorWidth;
    }

    public double getMinR() {
        return minR;
    }

    public double getMaxR() {
        return maxR;
    }

    public double getVdMax() {
        return vdMax;
    }

    public double getBeta() {
        return beta;
    }

    public int getParticles() {
        return particles;
    }

    public void setParticles(int particles) {
        this.particles = particles;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

}
