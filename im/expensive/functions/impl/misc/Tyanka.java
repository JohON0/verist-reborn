/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;

@FunctionRegister(name="\u0422\u044f\u043d\u043a\u0430", type=Category.Render)
public class Tyanka
extends Function {
    public final ModeSetting type33 = new ModeSetting("\u0412\u044b\u0431\u043e\u0440", "\u041f\u0435\u0440\u0432\u043e\u0435", "\u041f\u0435\u0440\u0432\u043e\u0435", "\u0412\u0442\u043e\u0440\u043e\u0435", "\u0422\u0440\u0435\u0442\u044c\u0435");
    public static Tyanka tyanka;
    public boolean state;

    public Tyanka() {
        this.toggle();
        this.addSettings(this.type33);
    }

    @Subscribe
    public void onRender(EventDisplay e) {
    }
}

