import java.util.ArrayList;
import java.util.List;

public class Cell {

    private List<Particle> particles;
    private boolean isChecked = false;

    public Cell() {
        this.particles = new ArrayList<>();
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public void removeParticle(Particle particle) {
        particles.remove(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public void checkCell() {
        this.isChecked = true;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
