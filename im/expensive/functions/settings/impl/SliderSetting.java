/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.settings.impl;

import im.expensive.functions.settings.Setting;
import java.util.function.Supplier;

public class SliderSetting
extends Setting<Float> {
    public float min;
    public float max;
    public float increment;

    public SliderSetting(String name, float defaultVal, float min2, float max2, float increment) {
        super(name, Float.valueOf(defaultVal));
        this.min = min2;
        this.max = max2;
        this.increment = increment;
    }

    public SliderSetting setVisible(Supplier<Boolean> bool) {
        return (SliderSetting)super.setVisible(bool);
    }

    public Number getValue() {
        return null;
    }
}

