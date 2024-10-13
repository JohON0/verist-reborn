/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;

@FunctionRegister(name="AspectRatio", type=Category.Misc)
public class AspectRatio
extends Function {
    public SliderSetting width = new SliderSetting("\u0428\u0438\u0440\u0438\u043d\u0430", 1.0f, 1.0f, 3.0f, 1.0f);

    public AspectRatio() {
        this.addSettings(this.width);
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

