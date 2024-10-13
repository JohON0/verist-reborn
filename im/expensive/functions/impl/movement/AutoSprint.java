/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;

@FunctionRegister(name="AutoSprint", type=Category.Movement)
public class AutoSprint
extends Function {
    public BooleanSetting saveSprint = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0442\u044c \u0441\u043f\u0440\u0438\u043d\u0442", true);

    public AutoSprint() {
        this.addSettings(this.saveSprint);
    }
}

