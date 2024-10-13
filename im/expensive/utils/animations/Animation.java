/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.animations;

import im.expensive.utils.animations.Direction;
import im.expensive.utils.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public abstract class Animation {
    protected long startTime;
    public StopWatch timerUtil = new StopWatch();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public Animation(int ms, double endPoint) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }

    public void update() {
        if (!this.isDone()) {
            this.timerUtil.update();
        }
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public double getValue() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = Math.min(currentTime - this.startTime, (long)this.duration);
        double progress = (double)elapsedTime / (double)this.duration;
        return progress;
    }

    public static double deltaTime() {
        return Minecraft.getInstance().getDebugFPS() > 0 ? 1.0 / (double)Minecraft.getInstance().getDebugFPS() : 1.0;
    }

    public static float lerp(float end, float start, float multiple) {
        return (float)((double)end + (double)(start - end) * MathHelper.clamp(Animation.deltaTime() * (double)multiple, 0.0, 1.0));
    }

    public static double lerp(double end, double start, double multiple) {
        return end + (start - end) * MathHelper.clamp(Animation.deltaTime() * multiple, 0.0, 1.0);
    }

    public Animation(int ms, double endPoint, Direction direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean finished(Direction direction) {
        return this.isDone() && this.direction.equals((Object)direction);
    }

    public double getLinearOutput() {
        return 1.0 - (double)this.timerUtil.getTime() / (double)this.duration * this.endPoint;
    }

    public double getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public void reset() {
        this.timerUtil.reset();
    }

    public boolean isDone() {
        return this.timerUtil.isReached(this.duration);
    }

    public void changeDirection() {
        this.setDirection(this.direction.opposite());
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        if (this.direction == Direction.FORWARDS) {
            return this.isDone() ? this.endPoint : this.getEquation(this.timerUtil.getTime()) * this.endPoint;
        }
        if (this.isDone()) {
            return 0.0;
        }
        if (this.correctOutput()) {
            double revTime = Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
            return this.getEquation(revTime) * this.endPoint;
        }
        return (1.0 - this.getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    protected abstract double getEquation(double var1);
}

