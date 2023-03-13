import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * class parser to read static and dynamic input file that describe the problem
 */
public class Parser {
    int L, N;

    List<Particle> particles;
    public Parser(String staticFileLocation, String dynamicFileLocation) throws FileNotFoundException, IOException {
        /*
        *   Create buffered reader for static and dynamic file since they are huge files
        */
        BufferedReader staticFile, dynamicFile;
        try {
            staticFile = new BufferedReader(new FileReader(staticFileLocation));
            dynamicFile = new BufferedReader(new FileReader(dynamicFileLocation));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            throw e;
        }

        /*
         * Read static file (N L)
         * N: number of particles
         * L: length of the square
         *
         * Read N lines with the radius of each particle
         *
         */
        try {
            N = Integer.parseInt(staticFile.readLine());
            L = Integer.parseInt(staticFile.readLine());
            particles = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                String[] staticFileLine = staticFile.readLine().split(" ");
                String[] dynamicFileLine = dynamicFile.readLine().split(" ");
                double r = Double.parseDouble(staticFileLine[0]);
                double x = Double.parseDouble(dynamicFileLine[1]);
                double y = Double.parseDouble(dynamicFileLine[2]);
                particles.add(new Particle(i, new Point(x, y), r));
            }
            staticFile.close();
        } catch (IOException e) {
            System.out.println("Error reading static file");
            throw e;
        }
    }

    public int getL() {
        return L;
    }

    public int getN() {
        return N;
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
