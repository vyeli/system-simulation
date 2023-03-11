import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Simulator {

    /*
     * Read static file (L M N rc)
     * L: length of the square
     * M: number of cells per side
     * N: number of particles
     * rc: radius of the interaction
     *
     */

    public static void main(String[] args) throws FileNotFoundException {
        double L = 20, rc = 1, r = 0.25;
        int M = 4, N = 50;

        Grid grid = new Grid(L, M, rc, true, N);
        grid.fillCells(L/M, r);

        try {
            final PrintWriter outputWriter = new PrintWriter("cell-neighbours.txt");

            Instant start = Instant.now();
            Map<Integer, List<Integer>> particleNeighbours = grid.getNeighbours();
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end));

            grid.writeParticleCoordinates();

            for(Map.Entry<Integer, List<Integer>> particle : particleNeighbours.entrySet()) {
                outputWriter.print(particle.getKey() + "\t");
                particle.getValue().forEach(neighbour -> outputWriter.print(neighbour + " "));
                outputWriter.println();
            }
            outputWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }
    }

}