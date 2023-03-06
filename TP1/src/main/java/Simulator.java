import java.util.*;

public class Simulator {

    public static void main(String[] args) {
        double L = 20, rc = 1, r = 0.25;
        int M = 15, N = 200;

        // TODO: Throw error when L/M <= rc
        double cellWidth = L / M;
        
        Random coordinateGenerator = new Random();

        // TODO: Upper bound is exclusive, make inclusive
        Iterator<Double> xPoints = coordinateGenerator.doubles(N, 0, L).iterator();
        Iterator<Double> yPoints = coordinateGenerator.doubles(N, 0, L).iterator();

        Map<Integer, List<Particle>> cellMap = new HashMap<>();
        for(int i=0 ; i < N ; i++) {
            double y = yPoints.next();
            double x = xPoints.next();
            Integer cellNumber = ((int)(y / cellWidth)) * M + ((int)(x / cellWidth));
            cellMap.putIfAbsent(cellNumber, new ArrayList());
            cellMap.get(cellNumber).add(new Particle(x, y, 0));
            if(i % N == 50)
                System.out.println("METO LA PARTÍCULA " + i + " EN LA CELDA " + cellNumber + " CON COORDENADAS\n -X: " + x + "\n -Y: " + y);
        }
    }

}