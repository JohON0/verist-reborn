/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.events;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public class MovingEvent {
    private Vector3d from;
    private Vector3d to;
    private Vector3d motion;
    private boolean toGround;
    private AxisAlignedBB aabbFrom;
    private boolean ignoreHorizontal;
    private boolean ignoreVertical;
    private boolean collidedHorizontal;
    private boolean collidedVertical;

    public MovingEvent(Vector3d from, Vector3d to, Vector3d motion, boolean toGround, boolean isCollidedHorizontal, boolean isCollidedVertical, AxisAlignedBB aabbFrom) {
        this.from = from;
        this.to = to;
        this.motion = motion;
        this.toGround = toGround;
        this.collidedHorizontal = isCollidedHorizontal;
        this.collidedVertical = isCollidedVertical;
        this.aabbFrom = aabbFrom;
    }

    public Vector3d getFrom() {
        return this.from;
    }

    public Vector3d getTo() {
        return this.to;
    }

    public Vector3d getMotion() {
        return this.motion;
    }

    public boolean isToGround() {
        return this.toGround;
    }

    public AxisAlignedBB getAabbFrom() {
        return this.aabbFrom;
    }

    public boolean isIgnoreHorizontal() {
        return this.ignoreHorizontal;
    }

    public boolean isIgnoreVertical() {
        return this.ignoreVertical;
    }

    public boolean isCollidedHorizontal() {
        return this.collidedHorizontal;
    }

    public boolean isCollidedVertical() {
        return this.collidedVertical;
    }

    public void setFrom(Vector3d from) {
        this.from = from;
    }

    public void setTo(Vector3d to) {
        this.to = to;
    }

    public void setMotion(Vector3d motion) {
        this.motion = motion;
    }

    public void setToGround(boolean toGround) {
        this.toGround = toGround;
    }

    public void setAabbFrom(AxisAlignedBB aabbFrom) {
        this.aabbFrom = aabbFrom;
    }

    public void setIgnoreHorizontal(boolean ignoreHorizontal) {
        this.ignoreHorizontal = ignoreHorizontal;
    }

    public void setIgnoreVertical(boolean ignoreVertical) {
        this.ignoreVertical = ignoreVertical;
    }

    public void setCollidedHorizontal(boolean collidedHorizontal) {
        this.collidedHorizontal = collidedHorizontal;
    }

    public void setCollidedVertical(boolean collidedVertical) {
        this.collidedVertical = collidedVertical;
    }
}

