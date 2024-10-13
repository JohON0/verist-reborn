/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;

@FunctionRegister(name="Display", type=Category.Misc)
public class BetterMinecraft
extends Function {
    public final BooleanSetting smoothCamera = new BooleanSetting("\u041f\u043b\u0430\u0432\u043d\u0430\u044f \u043a\u0430\u043c\u0435\u0440\u0430", true);
    public final BooleanSetting smoothTab = new BooleanSetting("\u041f\u043b\u0430\u0432\u043d\u044b\u0439 \u0442\u0430\u0431", true);
    public final BooleanSetting betterTab = new BooleanSetting("\u0423\u043b\u0443\u0447\u0448\u0435\u043d\u043d\u044b\u0439 \u0442\u0430\u0431", true);
    public final BooleanSetting RGBFog = new BooleanSetting("\u0420\u0430\u0434\u0443\u0436\u043d\u044b\u0439 \u0442\u0443\u043c\u0430\u043d", true);
    public final BooleanSetting RGBBlock = new BooleanSetting("\u0420\u0430\u0434\u0443\u0436\u043d\u044b\u0439 \u0431\u043b\u043e\u043a", true);
    public final BooleanSetting score = new BooleanSetting("\u041d\u0435\u0432\u0438\u0434\u0438\u043c\u044b\u0439 \u0431\u043e\u0430\u0440\u0434", false);

    public BetterMinecraft() {
        this.addSettings(this.smoothCamera, this.betterTab, this.RGBFog, this.RGBBlock, this.score);
    }
}

