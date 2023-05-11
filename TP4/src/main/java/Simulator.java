import helpers.EvolutionType;

public class Simulator {
    
    public static void main(String[] args) {
        double finishTime = 5;
        double deltaT = 0.001;
        int iterations = (int)(finishTime / deltaT);

        // Verlet
        Oscillator verletOscillator = new Oscillator(EvolutionType.VERLET, 70, 10000, 100, deltaT, 1);

        for(int i=0 ; i < iterations ; i++) {
            verletOscillator.evolve();
        }

        System.out.println("Verlet:");
        verletOscillator.printOscillatorValues();

        // Beeman
        Oscillator beemanOscillator = new Oscillator(EvolutionType.BEEMAN, 70, 10000, 100, deltaT, 1);

        for(int i=0 ; i < iterations ; i++) {
            beemanOscillator.evolve();
        }

        System.out.println("Beeman:");
        beemanOscillator.printOscillatorValues();

        // GearPred
        Oscillator gearPredOscillator = new Oscillator(EvolutionType.GEAR_PREDICTOR, 70, 10000, 100, deltaT, 1);

        for(int i=0 ; i < iterations ; i++) {
            gearPredOscillator.evolve();
        }

        System.out.println("Gear Pred:");
        gearPredOscillator.printOscillatorValues();

    }

}
