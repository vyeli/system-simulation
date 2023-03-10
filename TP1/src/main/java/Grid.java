import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Grid {

    private Cell[][] cells;
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
        this.cells = new Cell[m][m];
    }

    public Cell[][] getCells() {
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

        int mod = M;
        if(!periodic)
            mod = 1;

        for (int row=0 ; row < M ; row++) {
            for (int col=0 ; col < M ; col++) {
                if (cells[row][col] != null) {
                    for (int i=-1 ; i <= 1 ; i++) {
                        for (int j=-1 ; j <= 1 ; j++) {
                            Cell neighbour = cells[(row + i + M) % mod][(col + j + M) % mod];
                            System.out.println("ROW=" + (row + i + M) % mod + ", COL=" + (col + j + M) % mod);
                            if (neighbour != null) {                 // Algorithm has not passed cell yet
                                for (Particle particle : cells[row][col].getParticles()) {
                                    for (Particle maybeNeighbour : neighbour.getParticles()) {
                                        if (particle != maybeNeighbour &&
                                                (particle.isNeighbour(maybeNeighbour.getId()) || particleIsDistanceNeighbour(particle, maybeNeighbour))) {
                                            neighbours.putIfAbsent(particle.getId(), new ArrayList<>());
                                            neighbours.get(particle.getId()).add(maybeNeighbour.getId());
                                            maybeNeighbour.addNeighbour(particle.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    cells[row][col].checkCell();
                }
            }
        }

        /*
        for(Map.Entry<Integer, Cell> cellData : cells.entrySet()) {
            if(cellData.getValue() != null) {
                for (int i = -M; i <= M; i += M) {             // Select row
                    for (int j = -1; j <= 1; j++) {             // Select column
                        int neighbourId = (cellData.getKey() + i + j + M*M) % (M * M);
                        Cell neighbourCell = cells.get(neighbourId);
                        System.out.println("VEO LA CELL " + cellData.getKey() + " CON SU VECINA " + neighbourId + " SI I=" + i + ", J=" + j);
                        if (neighbourCell != null && !neighbourCell.isChecked()) {                 // Algorithm has not passed cell yet
                            for (Particle particle : cellData.getValue().getParticles()) {
                                for (Particle maybeNeighbour : neighbourCell.getParticles()) {
                                    if (particle != maybeNeighbour &&
                                            (particle.isNeighbour(maybeNeighbour.getId()) || particleIsDistanceNeighbour(particle, maybeNeighbour))) {
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

         */

        return neighbours;
    }

    /**
     * Given two particle return true if their distance with border to border is in rc range
     * @param p1
     * @param p2
     * @return boolean true if two particles are neighbor, false if not
     */
    private boolean particleIsDistanceNeighbour(Particle p1, Particle p2) {
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

            int row = (int) (y / cellWidth);
            int col = (int) (x / cellWidth);

            if(cells[row][col] == null)
                cells[row][col] = new Cell();
            cells[row][col].addParticle(new Particle(i, new Point(x, y), r));

            // TODO: Increment only when particle is valid
            i++;
        }
    }

    public void writeParticleCoordinates() {
        try {
            PrintWriter outputWriter = new PrintWriter("particle-coordinates.txt");
            for(int i = 0; i < M; i++) {
                for(int j = 0; j< M; j++) {
                    if (cells[i][j] != null) {
                        for (Particle p : cells[i][j].getParticles()) {
                            outputWriter.println(p.getId() + " " + p.getPoint().getX() + " " + p.getPoint().getY());
                        }
                    }
                }
            }
            outputWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
