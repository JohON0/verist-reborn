/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;

@FunctionRegister(name="CapeVisible", type=Category.Render)
public class Cape
extends Function {
    Vector4i friendColors = new Vector4i(HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 0, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 90, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 180, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 270, 1.0f));
    public static Cape cape;
    public boolean state;
}

