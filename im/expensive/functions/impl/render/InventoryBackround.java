/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.Setting;

@FunctionRegister(name="BlurInverntory", type=Category.Render)
public class InventoryBackround
extends Function {
    public static InventoryBackround inventoryBackround;
    public boolean state;

    public InventoryBackround() {
        this.toggle();
        this.addSettings(new Setting[0]);
    }
}

