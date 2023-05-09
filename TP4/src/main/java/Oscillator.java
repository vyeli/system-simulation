import java.lang.Math;

public class Oscillator {
    private double m;
    private double k;
    private double gamma;

    private double position;
    private double velocity;
    private double acceleration;

    private double prevPosition;
    private double prevVelocity;
    private double prevAcceleration;

    private double deltaT;
    private double t;

    public Oscillator(final double m, final double k, final double gamma, final double deltaT, final double initialPos) {
        this.m = m;
        this.k = k;
        this.gamma = gamma;

        this.deltaT = deltaT;
        this.t = 0;

        this.position = initialPos;
        this.velocity = -initialPos * gamma / (2 * m);
        this.acceleration = 0;
    }

    public void verletInit() {
        this.prevVelocity = this.velocity - deltaT * acceleration;
        this.prevPosition = this.position - deltaT * prevVelocity + (Math.pow(deltaT, 2) * acceleration) / 2;
        this.acceleration = currentAcceleration();
        // this.prevPosition = position;
    }

    public void verletEvolve() {
        // Calculate next values
        double deltaTSquared = Math.pow(deltaT, 2);
        double nextPosition = 2 * position - prevPosition + deltaTSquared * acceleration;
        this.acceleration = (nextPosition - 2 * position + prevPosition) / deltaTSquared;

        this.prevPosition = position;
        this.position = nextPosition;
        // this.prevAcceleration = this.acceleration;
        this.t += deltaT;
    }

    public void beemanEvolve() {
        
    }

    public void gearPredEvolve() {
        
    }

    public double estimateVelocity(double t) {
        return 0;           // TODO: Add estimator
    }

    public double currentAcceleration() {
        return (-k * position - gamma * velocity) / m;
    }

    public void printOscillatorValues() {
        System.out.println("Time: " + t);
        System.out.println("Calculated\tx: " + position + "m, v: " + velocity + "m/s, a: " + acceleration + "m/s^2)");
        System.out.println("Analytic\tx: " + analytic());
    }

    public double analytic() {
        // System.out.println((k/m) - (Math.pow(gamma, 2) / (4 * Math.pow(m, 2))));
        return Math.exp(-(gamma / (2 * m)) * t) * Math.cos(Math.sqrt((k/m) - (Math.pow(gamma, 2) / (4 * Math.pow(m, 2)))) * t);
    }

}
