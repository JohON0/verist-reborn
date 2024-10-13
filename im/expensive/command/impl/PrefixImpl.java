/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl;

import im.expensive.command.Prefix;

public class PrefixImpl
implements Prefix {
    private final String prefix = ".";

    @Override
    public void set(String prefix) {
    }

    @Override
    public String get() {
        return ".";
    }
}

