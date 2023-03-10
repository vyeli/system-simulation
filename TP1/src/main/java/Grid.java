import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Grid {

    private Map<Integer, Cell> cells = new HashMap<>();
    private final double L;
    private final int M;
    private final double RC;
    private final int N;

    private final boolean periodic;

    public Grid( double l, int m, double rc, boolean periodic, int n) {
        L = l;
        M = m;
        this.periodic = periodic;
        RC = rc;
        N = n;
    }

    public Map<Integer, Cell> getCells() {
        return cells;
    }

    public double getL() {
        return L;
    }

    public int getM() {
        return M;
    }

    /*
    *   6   7   8
    *   3   4   5
    *   0   1   2
    */

    /*
    * 0 1 0
    * 2 3 2
    * 0 1 0
    * */

    /**
     * For each cell, get the neighbors of each particle inside the cell
     * @return Map<Particle id, List<Neighbours ids>>
     */
    public Map<Integer, List<Integer>> getNeighbours() {
        Map<Integer, List<Integer>> neighbours = new HashMap<>();

        for(Map.Entry<Integer, Cell> cellData : cells.entrySet()) {
            if(cellData.getValue() != null) {
                for (int i = -M; i <= M; i += M) {             // Select row
                    for (int j = -1; j <= 1; j++) {             // Select column
                        int neighbourId = (cellData.getKey() + M + j) % (M * M);
                        Cell neighbourCell = cells.get(neighbourId);
                        if (neighbourCell != null && !neighbourCell.isChecked()) {                 // Algorithm has not passed cell yet
                            for (Particle particle : cellData.getValue().getParticles()) {
                                for (Particle maybeNeighbour : cells.get(neighbourId).getParticles()) {
                                    if (particle.isNeighbour(maybeNeighbour.getId()) || particleIsNeighbor(particle, maybeNeighbour)) {
                                        neighbours.putIfAbsent(particle.getId(), new ArrayList<>());
                                        neighbours.get(particle.getId()).add(maybeNeighbour.getId());
                                        maybeNeighbour.addNeighbour(particle.getId());
                                    }
                                }
                            }
                        }
                    }
                }
                cellData.getValue().checkCell();
            }
        }

        return neighbours;
    }

    /**
     * Given two particle return true if their distance with border to border is in rc range
     * @param p1
     * @param p2
     * @return boolean true if two particles are neighbor, false if not
     */
    private boolean particleIsNeighbor(Particle p1, Particle p2) {
        return (Point.distance(p1.getPoint(), p2.getPoint()) - p1.getRadius() - p2.getRadius()) < RC;
    }

    public void fillCells(double cellWidth, double r) {
        Random coordinateGenerator = new Random();
        // TODO: Upper bound is exclusive, make inclusive
        Iterator<Double> xPoints = coordinateGenerator.doubles(0, L).iterator();
        Iterator<Double> yPoints = coordinateGenerator.doubles(0, L).iterator();
        int i = 0;
        while (i < N) {
            double y = yPoints.next();
            double x = xPoints.next();
            Integer cellNumber = ((int) (y / cellWidth)) * M + ((int) (x / cellWidth));
            cells.putIfAbsent(cellNumber, new Cell());
            cells.get(cellNumber).addParticle(new Particle(i, new Point(x, y), r));
            // TODO: Increment only when particle is valid
            i++;
        }
    }

    public void writeParticleCoordinates() {
        try {
            PrintWriter outputWriter = new PrintWriter("particle-coordinates.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
