import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Set;

public class Cycle {
    private double percentage;
    private int steps;
    private Set liveCells;
    private SimpleRegression regression;

    public Cycle(double percentage, int steps, Set liveCells, SimpleRegression regression) {
        this.percentage = percentage;
        this.steps = steps;
        this.liveCells = liveCells;
        this.regression = regression;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getSteps() {
        return steps;
    }

    public Set getLiveCells() {
        return liveCells;
    }

    public SimpleRegression getRegression() {
        return regression;
    }
}