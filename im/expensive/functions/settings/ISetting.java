/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.settings;

import im.expensive.functions.settings.Setting;
import java.util.function.Supplier;

public interface ISetting {
    public Setting<?> setVisible(Supplier<Boolean> var1);
}

