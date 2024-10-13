/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import im.expensive.ui.display.impl.Particle;
import java.util.ArrayList;
import java.util.List;

public class ParticleManager {
    private final List<Particle> particles = new ArrayList<Particle>();

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }

    public void update() {
        this.particles.removeIf(p -> !p.isAlive());
        for (Particle particle : this.particles) {
            particle.update();
        }
    }

    public void render() {
        for (Particle particle : this.particles) {
            particle.render();
        }
    }
}

