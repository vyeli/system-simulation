import helpers.Pair;

import java.util.ArrayList;
import java.util.List;

public class PoolGame {

    private static final double MIN_Y0_WHITE_BALL = 42;
    private static final double MAX_Y0_WHITE_BALL = 56;
    private static final double ITERATIONS = 50;


    public static void main(String[] args) {

        // Parallel universes Experiment
        double tf = 100; //s
        double dt = 0.01; //s

        double y0 = 56;
        double vx0 = 100; //cm/s

        List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();
        // Triangle balls positions
        for (int j = 0; j < 15; j++) {
            ballsEpsilon.add(new Pair<>(getDeterministicDoubleEpsilons(j), getDeterministicDoubleEpsilons(j+1)));
        }
        Table gameTable = new Table(y0, vx0, ballsEpsilon);

        int iterations = (int) (tf / dt);
        for (int i = 0; i < iterations; i++) {

        }


    }


    public static double getDeterministicDoubleEpsilons(int epsilonIdx) {
        double[] epsilons = {
                -0.025375633720082617,
                0.023195390183247143,
                0.023467172503365472,
                0.024568862163597948,
                0.021235567161039016,
                0.024321684724795026,
                0.023379759201919707,
                0.022069884891683465,
                0.02175431925530433,
                -0.028101254186428375,
                0.024613681688628403,
                -0.0280714576052831,
                -0.026644394141343335,
                0.023695177579638935,
                -0.02920082021216556,
                -0.02657502617910734,
                0.021138162507242198,
                -0.02983145381017637,
                0.022729002282930455,
                -0.028238472313370958,
                -0.026021777559192476,
                0.02206438517219644,
                0.022474028054153813,
                -0.029101719056499338,
                0.023248323576678342,
                -0.028690357554693235,
                0.02482934476508576,
                -0.027542859614355632,
                -0.025897592220895486,
                0.020427744555701916
        };
        if (epsilonIdx < 0 || epsilonIdx >= epsilons.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return epsilons[epsilonIdx];
    }
}