import helpers.Pair;

import java.util.List;
import java.util.ArrayList;

public class PoolGame {
    public static void main(String[] args) {
        List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();

        for(int i=0 ; i < 15 ; i++) {
            ballsEpsilon.add(new Pair<Double,Double>(getRandomEpsilon(), getRandomEpsilon()));
        }

        Table gameTable = new Table(56, ballsEpsilon);
        for(Ball ball : gameTable.getBalls().values()) {
            System.out.println("Ball #" + ball.getNumber() + ":");
            Pair<Double, Double> position = ball.getPosition();
            System.out.println("- x: " + position.getX() + ", y: " + position.getY());
        }

        System.out.println("Working!!");
    }

    public static double getRandomEpsilon() {
        double randValue = Math.random();
        double epsilon = 0.02 + randValue * 0.01;
        if(randValue > 0.05) {
            randValue = -randValue;
        }
        return epsilon;
    }

}
