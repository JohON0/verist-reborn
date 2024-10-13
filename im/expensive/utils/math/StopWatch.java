/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.math;

public class StopWatch {
    public long lastMS = System.currentTimeMillis();

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean isReached(long time) {
        return System.currentTimeMillis() - this.lastMS > time;
    }

    public void setLastMS(long newValue) {
        this.lastMS = System.currentTimeMillis() + newValue;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public boolean isRunning() {
        return System.currentTimeMillis() - this.lastMS <= 0L;
    }

    public boolean hasTimeElapsed() {
        return this.lastMS < System.currentTimeMillis();
    }

    public void update() {
        this.lastMS = System.currentTimeMillis();
    }

    public long getLastMS() {
        return this.lastMS;
    }
}

