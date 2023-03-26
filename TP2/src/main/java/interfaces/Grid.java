package interfaces;

public interface Grid<T> {
    void generateRandomCells(double percentage);
    void nextGeneration();
    int countLiveNeighbors(T grid, int row, int col);
    T getGrid();
}