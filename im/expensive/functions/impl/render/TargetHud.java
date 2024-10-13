/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;

@FunctionRegister(name="TargetHUD", type=Category.Render)
public class TargetHud
extends Function {
    public final BooleanSetting Target = new BooleanSetting("\u0412\u043a\u043b. \u041f\u0440\u0435\u0434\u043c\u0435\u0442\u044b", false);
    public final BooleanSetting Target1 = new BooleanSetting("\u0427\u0430\u0441\u0442\u0438\u0446\u044b \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", false);
    public static TargetHud targetHud;
    public boolean state;

    public TargetHud() {
        this.addSettings(this.Target, this.Target1);
    }

    @Subscribe
    public void onRender(EventDisplay e) {
    }
}

