/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.animations.impl;

import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.Direction;

public class EaseInOut
extends Animation {
    public EaseInOut(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseInOut(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        if ((x /= (double)(this.duration / 2)) < 1.0) {
            return 0.5 * x * x * x;
        }
        return 0.5 * ((x -= 2.0) * x * x + 2.0);
    }
}

