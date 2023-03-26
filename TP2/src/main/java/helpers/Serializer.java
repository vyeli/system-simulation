package helpers;

import java.util.Set;

public class Serializer {
    public static String serialize2D(Set<Pair<Integer, Integer>> liveCells, int size, int domain) {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        sb.append(domain).append("\n");
        for (Pair<Integer, Integer> cell : liveCells) {
            sb.append(cell.getX());
            sb.append(" ");
            sb.append(cell.getY());
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String serialize3D(Set<Trio<Integer, Integer, Integer>> liveCells, int size, int domain) {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        sb.append(domain).append("\n");
        for (Trio<Integer, Integer, Integer> cell : liveCells) {
            sb.append(cell.getX());
            sb.append(" ");
            sb.append(cell.getY());
            sb.append(" ");
            sb.append(cell.getZ());
            sb.append("\n");
        }
        return sb.toString();
    }

}
