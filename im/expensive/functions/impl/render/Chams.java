/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;

@FunctionRegister(name="Chams", type=Category.Render)
public class Chams
extends Function {
    public static Chams chams;
    public boolean state;

    @Subscribe
    public void onRender(EventDisplay e) {
    }
}

