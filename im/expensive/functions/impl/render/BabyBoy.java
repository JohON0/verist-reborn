/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;

@FunctionRegister(name="BabyBoy", type=Category.Render)
public class BabyBoy
extends Function {
    private static boolean enabled = false;

    @Override
    public boolean onEnable() {
        enabled = true;
        this.print("Ready!");
        super.onEnable();
        return false;
    }

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }

    public static boolean isEnabled() {
        return enabled;
    }
}

