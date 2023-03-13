import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Simulator {

    /**
     *
     * @param args
     * args[0] = staticInput.txt
     * args[1] = M
     * args[2] = rc
     * args[3] = periodic (any other thing will be consider not periodic)
     *
     */
    public static void main(String[] args) throws IOException {
        double L, rc;
        String periodic;
        int M, N;

        if (args.length != 4) {
            System.out.print("Debe ingresar todos los argumentos");
            return;
        }

        Parser parser = new Parser(args[0]);
        L = parser.getL();
        N = parser.getN();

        M = Integer.parseInt(args[1]);
        rc = Double.parseDouble(args[2]);
        periodic = args[3].toLowerCase(Locale.ROOT);

        Grid grid = new Grid(L, M, rc, periodic.equals("periodic"), N);
        grid.fillCells(L/M, parser.getParticles());

        try {
            final PrintWriter outputWriter = new PrintWriter("cell-neighbours.txt");

            Instant start = Instant.now();
            Map<Integer, List<Integer>> particleNeighbours = grid.getNeighbours();
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end));

            grid.writeParticleCoordinates();

            for (Map.Entry<Integer, List<Integer>> particle : particleNeighbours.entrySet()) {
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