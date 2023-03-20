package interfaces;

public interface Grid<T> {
    void generateRandomCells(double percentage);
    T getNextGeneration(T grid);
    int countLiveNeighbors(T grid, int row, int col);
    T getGrid();
}