import helpers.Pair;
import helpers.Trio;

import java.util.Set;

public abstract class RandomGrid<T, W> {
    public abstract void generateRandomCells(double percentage);
    public abstract void nextGeneration();
    public abstract T getGrid();
    public abstract void setGrid(T grid);

    public abstract Set<W> getLiveCells();

    public abstract int getLiveCellsAmount();

    // Declare abstract equals() and hashCode() methods
    public abstract boolean equals(Object obj);
    public abstract int hashCode();

    public abstract boolean hasCellsOutside();
}
