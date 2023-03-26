package interfaces;

public interface Grid<T> {
    void generateRandomCells(double percentage);
    void nextGeneration();
    T getGrid();
}