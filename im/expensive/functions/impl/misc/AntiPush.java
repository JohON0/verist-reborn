/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;

@FunctionRegister(name="AntiPush", type=Category.Player)
public class AntiPush
extends Function {
    private final ModeListSetting modes = new ModeListSetting("\u0422\u0438\u043f", new BooleanSetting("\u0418\u0433\u0440\u043e\u043a\u0438", true), new BooleanSetting("\u0412\u043e\u0434\u0430", false), new BooleanSetting("\u0411\u043b\u043e\u043a\u0438", true));

    public AntiPush() {
        this.addSettings(this.modes);
    }

    public ModeListSetting getModes() {
        return this.modes;
    }
}

