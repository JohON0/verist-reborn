/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

class Animation1 {
    private float value;
    private float target;
    private long startTime;
    private long duration;

    public Animation1(float target, long duration) {
        this.target = target;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.value = 0.0f;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        float progress = (float)(currentTime - this.startTime) / (float)this.duration;
        this.value = Math.min(1.0f, progress);
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

