/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Effect;

public class PotionEffect {
    private final Effect effect;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean visible;

    public PotionEffect(Effect effect, int duration, int amplifier, boolean ambient, boolean visible) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.visible = visible;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void applyToEntity(Entity entity2) {
    }
}

