/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;

@FunctionRegister(name="KTLeave", type=Category.Player)
public class KTLeave
extends Function {
    @Override
    public boolean onEnable() {
        super.onEnable();
        this.setState(false, false);
        return false;
    }
}

