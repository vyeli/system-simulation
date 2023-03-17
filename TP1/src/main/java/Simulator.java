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
     * args[0] = static.txt
     * args[1] = dynamic.txt
     * args[2] = M
     * args[3] = rc
     * args[4] = periodic (any other thing will be consider not periodic)
     *
     */
    public static void main(String[] args) throws IOException {
        double L, rc;
        String periodic;
        int M, N;

        if (args.length != 5) {
            System.out.print("Debe ingresar todos los argumentos");
            return;
        }

        Parser parser = new Parser(args[0], args[1]);
        L = parser.getL();
        N = parser.getN();

        M = Integer.parseInt(args[2]);
        rc = Double.parseDouble(args[3]);
        periodic = args[4].toLowerCase(Locale.ROOT);

        Grid grid = new Grid(L, M, rc, periodic.equals("periodic"), N);
        grid.fillCells(L/M, parser.getParticles());

        try {
            final PrintWriter outputWriter = new PrintWriter("cell-neighbours.txt");

            Instant start = Instant.now();
            Map<Integer, List<Integer>> particleNeighbours = grid.getNeighbours();
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end));

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