package helpers;

import java.util.Objects;

public class Trio<X, Y, Z> {
    private X x;
    private Y y;
    private Z z;

    public Trio(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Z getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trio)) return false;
        Trio<?, ?, ?> trio = (Trio<?, ?, ?>) o;
        return Objects.equals(x, trio.x) &&
                Objects.equals(y, trio.y) &&
                Objects.equals(z, trio.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
