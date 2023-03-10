import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        long start = System.currentTimeMillis();
        double L = 5, rc = 1, r = 0.25;
        int M = 9, N = 200;
        int i = 0;

        PrintWriter outputWriter = new PrintWriter("particle-coordinates.txt");
        Grid grid = new Grid(L, M, rc, true, N);
        grid.fillCells(L/M, r);

        try {
            outputWriter = new PrintWriter("cell-neighbours.txt");

            for(Map.Entry<Integer, List<Integer>> particle : grid.getNeighbours().entrySet()) {
                outputWriter.print(particle.getKey() + "\t");
                PrintWriter finalOutputWriter = outputWriter;
                particle.getValue().forEach(neighbour -> finalOutputWriter.print(neighbour + " "));
                outputWriter.println();
            }

            // Getting neighbours
            /*
            for(Integer cellId : cellMap.keySet()) {            // TODO: Change to entry set
                List<Particle> cellParticles = cellMap.get(cellId).getParticles();
                for(Particle p : cellParticles) {
                    outputWriter.print(p.getId() + "\t");
                    PrintWriter finalOutputWriter = outputWriter;
                    grid.getNeighbours(cellId, p).forEach((neighbour) -> finalOutputWriter.print(neighbour.getId() + " "));
                    outputWriter.println();
                }
            }
            */

            outputWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}