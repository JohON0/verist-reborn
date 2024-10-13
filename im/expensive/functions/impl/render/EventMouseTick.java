/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import net.minecraftforge.eventbus.api.Event;

public class EventMouseTick
extends Event {
    private final int button;
    private final double mouseX;
    private final double mouseY;

    public EventMouseTick(int button, double mouseX, double mouseY) {
        this.button = button;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getButton() {
        return this.button;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }
}

