import helpers.Pair;

public class PoolGame {
    public static void main(String[] args) {
        Table gameTable = new Table(56);
        for(Ball ball : gameTable.getBalls().values()) {
            System.out.println("Ball #" + ball.getNumber() + ":");
            Pair<Double, Double> position = ball.getPosition();
            System.out.println("- x: " + position.getX() + ", y: " + position.getY());
        }
        System.out.println("Working!!");
    }
}
