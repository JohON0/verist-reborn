/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

class Easing {
    Easing() {
    }

    public static float easeOutQuad(float t) {
        return t * (2.0f - t);
    }
}

