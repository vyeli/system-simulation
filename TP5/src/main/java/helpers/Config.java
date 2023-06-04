package helpers;

public class Config {
    private double boxSize;
    private double minR, maxR, vdMax;
    private double beta;
    private double tau;

    private double[] doorWidth;
    private int[] particles;

    private int iterationsPerValue;
    private int lowerFlowLimit;
    private int upperFlowLimit;

    public void setBoxSize(double boxSize) {
        this.boxSize = boxSize;
    }

    public void setDoorWidth(double[] doorWidth) {
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

    public double[] getDoorWidth() {
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

    public int[] getParticles() {
        return particles;
    }

    public void setParticles(int[] particles) {
        this.particles = particles;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public int getIterationsPerValue() {
        return iterationsPerValue;
    }

    public void setIterationsPerValue(int iterationsPerValue) {
        this.iterationsPerValue = iterationsPerValue;
    }

    public int getLowerFlowLimit() {
        return lowerFlowLimit;
    }

    public void setLowerFlowLimit(int lowerFlowLimit) {
        this.lowerFlowLimit = lowerFlowLimit;
    }

    public int getUpperFlowLimit() {
        return upperFlowLimit;
    }

    public void setUpperFlowLimit(int upperFlowLimit) {
        this.upperFlowLimit = upperFlowLimit;
    }

}
