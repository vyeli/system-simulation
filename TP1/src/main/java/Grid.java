import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Grid {

    private final Cell[][] cells;
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

    /**
     * For each cell, get the neighbors of each particle inside the cell
     * @return Map<Particle id, List<Neighbours ids>>
     */
    public Map<Integer, List<Integer>> getNeighbours() {
        Map<Integer, List<Integer>> neighbours = new HashMap<>();

        for (int row=0 ; row < M ; row++) {
            for (int col=0 ; col < M ; col++) {                          // looping cells
                if (cells[row][col] != null) {
                    for (int i=-1 ; i <= 1 ; i++) {                      // looping possible neighbours
                        for (int j=-1 ; j <= 1 ; j++) {
                            int neighbourRow = Math.floorMod(row + i, M);
                            int neighbourCol = Math.floorMod(col + j, M);

                            if (!periodic) {
                                neighbourRow = row + i;
                                neighbourCol = col + j;
                                if (neighbourRow < 0 || neighbourRow >= M) continue;
                                if (neighbourCol < 0 || neighbourCol >= M) continue;
                            }

                            Cell neighbour = cells[neighbourRow][neighbourCol];
                            //System.out.println("ROW=" + neighbourRow + ", COL=" + neighbourCol);

                            if (neighbour != null) {
                                for (Particle particle : cells[row][col].getParticles()) {
                                    for (Particle maybeNeighbour : neighbour.getParticles()) {
                                        if (particle != maybeNeighbour &&
                                                (particle.isNeighbour(maybeNeighbour.getId()) || particleIsDistanceNeighbour(particle, maybeNeighbour,
                                                        Math.abs(neighbourRow - row) <= 1, Math.abs(neighbourCol - col) <= 1))) {
                                            neighbours.putIfAbsent(particle.getId(), new ArrayList<>());
                                            neighbours.get(particle.getId()).add(maybeNeighbour.getId());
                                            maybeNeighbour.addNeighbour(particle.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // TODO: Check if this is necessary
                    // cells[row][col].checkCell();
                }
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
    private boolean particleIsDistanceNeighbour(Particle p1, Particle p2, boolean hasContinuousRows, boolean hasContinuousCols) {
        Point p1Point = new Point(p1.getPoint());
        Point p2Point = new Point(p2.getPoint());

        if (!hasContinuousRows) {
            if (p1Point.getY() > p2Point.getY()) {
                p1Point.setY(p1Point.getY() - L);
            } else {
                p2Point.setY(p2Point.getY() - L);
            }
        }

        if (!hasContinuousCols) {
            if (p1Point.getX() > p2Point.getX()) {
                p1Point.setX(p1Point.getX() - L);
            } else {
                p2Point.setX(p2Point.getX() - L);
            }
        }

        return (Point.distance(p1Point, p2Point) - p1.getRadius() - p2.getRadius()) < RC;
    }

    public void fillCells(double cellWidth, List<Particle> particles) {

        for (Particle p : particles) {
            int row = (int) (p.getPoint().getY() / cellWidth);
            int col = (int) (p.getPoint().getX() / cellWidth);

            if(cells[row][col] == null)
                cells[row][col] = new Cell();
            cells[row][col].addParticle(p);

            // TODO: Add particle without collision
        }
    }

}
