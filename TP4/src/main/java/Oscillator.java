import java.lang.Math;
import helpers.EvolutionType;

public class Oscillator {
    private EvolutionType evolType;

    private double m;
    private double k;
    private double gamma;

    private double r;
    private double v;
    private double a;
    private double r3;
    private double r4;
    private double r5;

    private double rPrev;
    private double vPrev;
    private double aPrev;

    private double dt;
    private double t;

    public Oscillator(final EvolutionType evolType, final double m, final double k, final double gamma, final double dt, final double r0) {
        this.evolType = evolType;

        this.m = m;
        this.k = k;
        this.gamma = gamma;

        this.dt = dt;
        this.t = 0;

        this.r = r0;
        this.v = -r0 * gamma / (2 * m);
        this.a = calculateAcceleration(r, v);

        switch(evolType) {
            case VERLET:
                verletInit();
                break;
            case BEEMAN:
                beemanInit();
                break;
            case GEAR_PREDICTOR:
                gearPredInit();
                break;
        }
    }

    public void evolve() {
        switch(evolType) {
            case VERLET:
                verletEvolve();
                break;
            case BEEMAN:
                beemanEvolve();
                break;
            case GEAR_PREDICTOR:
                gearPredEvolve();
                break;
        }
    }

    private void verletInit() {
        // Predict -dt position and v using Euler Modified Method (R using dt v)
        this.vPrev = v - dt * a;
        this.rPrev = r - dt * vPrev + (dt * dt * a) / 2;
    }

    private void verletEvolve() {
        // Calculate next values using Verlet Method
        double rNext = 2 * r - rPrev + dt * dt * a;
        // this is the current v, not the next one
        double vNext = (rNext - rPrev) / (2 * dt);

        // Update values
        this.rPrev = this.r;
        this.vPrev = this.v;

        this.r = rNext;
        this.v = vNext;

        this.a = calculateAcceleration(r, v);

        this.t += dt;
    }

    private void beemanInit() {
        this.vPrev = this.v + dt * this.a;
        this.rPrev = this.r + dt * this.vPrev + (dt * dt * this.a) / 2;
        this.aPrev = calculateAcceleration(this.rPrev, this.vPrev);
    }

    private void beemanEvolve() {
        double rNext = this.r + dt * (this.v + (2.0/3) * dt * this.a - (1.0/6) * dt * this.aPrev);

        // Predicting next acceleration
        double nextPredV = this.v + dt * ((3.0/2) * this.a - (1.0/2) * this.aPrev);
        double aNext = calculateAcceleration(rNext, nextPredV);

        // Correcting for next v
        double vNext = this.v + dt * ((1.0/3) * aNext + (5.0/6) * this.a - (1.0/6) * this.aPrev);

        this.r = rNext;
        this.v = vNext;
        this.aPrev = this.a;
        this.a = aNext;
        this.t += dt;
    }

    private void gearPredInit() {
        this.r3 = -k/m * v;
        this.r4 = -k/m * a;
        this.r5 = -k/m * r3;
    }

    private void gearPredEvolve() {
        double factor2 = Math.pow(dt, 2) / 2;
        double factor3 = Math.pow(dt, 3) / 6;
        double factor4 = Math.pow(dt, 4) / 24;
        double factor5 = Math.pow(dt, 5) / 120;

        // Calculating predictions
        double rPred = r + dt * v + factor2 * a + factor3 * r3 + factor4 * r4 + factor5 * r5;
        double vPred = v + dt * a + factor2 * r3 + factor3 * r4 + factor4 * r5;
        double aPred = a + dt * r3 + factor2 * r4 + factor3 * r5;
        double r3Pred = r3 + dt * r4 + factor2 * r5;
        double r4Pred = r4 + dt * r5;
        double r5Pred = r5;

        // Evaluating acceleration
        double dR2 = ((calculateAcceleration(rPred, vPred) - aPred) * Math.pow(dt, 2)) / 2;

        // Correcting predictions
        r = rPred + 3.0/16 * dR2;
        v = vPred + ((251.0/360) * dR2) / dt;
        a = aPred + dR2 / factor2;
        r3 = r3Pred + ((11.0/18) * dR2) / factor3;
        r4 = r4Pred + ((1.0/6) * dR2) / factor4;
        r5 = r5Pred + ((1.0/60) * dR2) / factor5;

        t += dt;
    }

    // get acceleration at a given position and v
    private double calculateAcceleration(double pos, double vel) {
        return (-k * pos - gamma * vel) / m;
    }

    public void printOscillatorValues() {
        System.out.println("Time: " + t);
        System.out.println("Calculated\tr: " + this.r + "m, v: " + this.v + "m/s, a: " + this.a + "m/s^2)");
        System.out.println("Analytic\tr: " + analytic());
    }

    public double analytic() {
        // System.out.println((k/m) - (Math.pow(gamma, 2) / (4 * Math.pow(m, 2))));
        return Math.exp(-(gamma / (2 * m)) * t) * Math.cos(Math.sqrt((k/m) - (Math.pow(gamma, 2) / (4 * Math.pow(m, 2)))) * t);
    }

}
