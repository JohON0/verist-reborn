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

@FunctionRegister(name="ClickGui", type=Category.Render)
public class ClickGui
extends Function {
    public static ClickGui clickGui;
    public boolean state;
    public final BooleanSetting Blur = new BooleanSetting("\u0420\u0430\u0437\u043c\u044b\u0442\u0438\u0435 \u0444\u043e\u043d\u0430", true);
    public final BooleanSetting Xoxo = new BooleanSetting("\u0410\u043d\u0438\u043c\u0430\u0446\u0438\u044f \u0447\u0430\u0441\u0442\u0438\u0446", true);

    public ClickGui() {
        this.addSettings(this.Blur, this.Xoxo);
    }

    @Subscribe
    public void onRender(EventDisplay e) {
    }
}

