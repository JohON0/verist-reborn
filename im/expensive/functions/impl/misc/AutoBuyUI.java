/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;

@FunctionRegister(name="AutoBuyUI", type=Category.Misc)
public class AutoBuyUI
extends Function {
    public BindSetting setting = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u043e\u0442\u043a\u0440\u044b\u0442\u0438\u044f", -1);

    public AutoBuyUI() {
        this.addSettings(this.setting);
    }
}

