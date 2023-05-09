public class Simulator {
    
    public static void main(String[] args) {
        double finishTime = 5;
        double deltaT = 0.1;
        int iterations = (int)(finishTime / deltaT);

        Oscillator oscillator = new Oscillator(70, 10000, 100, deltaT, 1);
        
        // Verlet
        oscillator.verletInit();

        for(int i=0 ; i < iterations ; i++) {
            oscillator.verletEvolve();
            oscillator.printOscillatorValues();
        }

        // Beeman
        oscillator.verletInit();

        for(int i=0 ; i < iterations ; i++) {
            oscillator.verletEvolve();
            oscillator.printOscillatorValues();
        }


    }

}
