import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import helpers.EvolutionType;

public class Simulator {

    private static final String[] CONFIGS_CSV_HEADERS = {"method", "dt", "t", "value"};

    private static final double m = 70;
    private static final double k = 10000;
    private static final double gamma = 100;
    private static final double r0 = 1;

    private static final double tf = 5;
    
    public static void main(String[] args) throws IOException {
        BufferedWriter bwConfigs = Files.newBufferedWriter(Paths.get("oscillator_methods.csv"));
        CSVFormat csvFormatConfigs = CSVFormat.DEFAULT.builder().setHeader(CONFIGS_CSV_HEADERS).build();
        final CSVPrinter csvPrinter = new CSVPrinter(bwConfigs, csvFormatConfigs);

        for (int i=2 ; i < 7 ; i++) {
            double dt = Math.pow(10, -i);
            int iterations = (int)(tf / dt);

            Oscillator verletOscillator = new Oscillator(EvolutionType.VERLET, m, k, gamma, dt, r0);
            Oscillator beemanOscillator = new Oscillator(EvolutionType.BEEMAN, m, k, gamma, dt, r0);
            Oscillator gearPredOscillator = new Oscillator(EvolutionType.GEAR_PREDICTOR, m, k, gamma, dt, r0);

            for (int j=0 ; j < iterations ; j++) {
                double t = j * dt;
                // Print current values
                csvPrinter.printRecord("verlet", dt, t, verletOscillator.getR());
                csvPrinter.printRecord("beeman", dt, t, beemanOscillator.getR());
                csvPrinter.printRecord("gear_pred", dt, t, gearPredOscillator.getR());
                csvPrinter.printRecord("analytic", dt, t, verletOscillator.analytic(t));
                // Evolve oscillators
                verletOscillator.evolve();
                beemanOscillator.evolve();
                gearPredOscillator.evolve();
            }
            
        }

        csvPrinter.close();
    }

}
