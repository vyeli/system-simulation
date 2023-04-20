import helpers.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class PoolGame {
    public static void main(String[] args) throws IOException {
        List<Pair<Double, Double>> ballsEpsilon = new ArrayList<>();

        for(int i=0 ; i < 15 ; i++) {
            ballsEpsilon.add(new Pair<>(getRandomEpsilon(), getRandomEpsilon()));
        }

        Table gameTable = new Table(56, 420.0, ballsEpsilon);
        for(Ball ball : gameTable.getBalls().values()) {
            // System.out.println("Ball #" + ball.getNumber() + ":");
            Pair<Double, Double> position = ball.getPosition();
            // System.out.println("- x: " + position.getX() + ", y: " + position.getY());
        }

        List<Ball> balls = new ArrayList<>(gameTable.getBalls().values());
        System.out.printf("Simulation started with %d balls %n", balls.size());
        CollisionSystem collisionSystem = new CollisionSystem(balls);
        while(collisionSystem.hasNextEvent()) {
            System.out.println("Simulation time: " + collisionSystem.getTime());
            collisionSystem.simulateNextEvent();
            try(FileWriter fileWriter = new FileWriter("output.txt", true)) {
                fileWriter.write(collisionSystem.writeEvent());
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        System.out.printf("Simulation ended after %f seconds %n", collisionSystem.getTime());
    }

    public static double getRandomEpsilon() {
        double randValue = Math.random();
        double epsilon = 0.02 + randValue * 0.01;
        if(randValue > 0.05) {
            epsilon = -epsilon;
        }
        return epsilon;
    }

}
