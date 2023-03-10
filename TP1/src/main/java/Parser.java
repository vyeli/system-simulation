import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * class parser to read static and dynamic input file that describe the problem
 */
public class Parser {
    int L, M, N;
    double rc;

    List<Particle> particles;
    public Parser(String staticFileLocation) throws FileNotFoundException, IOException {
        /*
        *   Create buffered reader for static and dynamic file since they are huge files
        */
        BufferedReader staticFile;
        try {
            staticFile = new BufferedReader(new FileReader(staticFileLocation));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            throw e;
        }

        /*
         * Read static file (L M N rc)
         * L: length of the square
         * M: number of cells per side
         * N: number of particles
         * rc: radius of the interaction
         *
         * Read N lines with the radius of each particle
         *
         */
        try {
            L = Integer.parseInt(staticFile.readLine());
            M = Integer.parseInt(staticFile.readLine());
            N = Integer.parseInt(staticFile.readLine());
            rc = Double.parseDouble(staticFile.readLine());

            for (int i = 0; i < N; i++) {
                String[] line = staticFile.readLine().split(" ");
                double r = Double.parseDouble(line[0]);
                particles.add(new Particle(i, null, r));
            }
            staticFile.close();
        } catch (IOException e) {
            System.out.println("Error reading static file");
            throw e;
        }
    }

}
