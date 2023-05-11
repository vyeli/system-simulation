package helpers;

public enum EvolutionType {
    VERLET("Original Verlet"),
    BEEMAN("Beeman"),
    GEAR_PREDICTOR("Gear Predictor Order 5");

    private String name;

    private EvolutionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
