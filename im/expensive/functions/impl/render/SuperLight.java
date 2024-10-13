/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;

@FunctionRegister(name="FullBright", type=Category.Render)
public class SuperLight
extends Function {
    private double previousGamma;

    @Override
    public boolean onEnable() {
        this.previousGamma = SuperLight.mc.gameSettings.gamma;
        SuperLight.mc.gameSettings.gamma = 1000.0;
        return false;
    }

    @Override
    public void onDisable() {
        SuperLight.mc.gameSettings.gamma = this.previousGamma * 0.5;
    }
}

