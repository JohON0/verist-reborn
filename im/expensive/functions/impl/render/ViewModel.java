/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;

@FunctionRegister(name="ViewModel", type=Category.Render)
public class ViewModel
extends Function {
    public final SliderSetting right_x = new SliderSetting("RightX", 0.0f, -2.0f, 2.0f, 0.1f);
    public final SliderSetting right_y = new SliderSetting("RightY", 0.0f, -2.0f, 2.0f, 0.1f);
    public final SliderSetting right_z = new SliderSetting("RightZ", 0.0f, -2.0f, 2.0f, 0.1f);
    public final SliderSetting left_x = new SliderSetting("LeftX", 0.0f, -2.0f, 2.0f, 0.1f);
    public final SliderSetting left_y = new SliderSetting("LeftY", 0.0f, -2.0f, 2.0f, 0.1f);
    public final SliderSetting left_z = new SliderSetting("LeftZ", 0.0f, -2.0f, 2.0f, 0.1f);

    public ViewModel() {
        this.addSettings(this.right_x, this.right_y, this.right_z, this.left_x, this.left_y, this.left_z);
    }
}

