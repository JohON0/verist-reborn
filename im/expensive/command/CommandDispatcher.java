/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command;

import im.expensive.command.impl.DispatchResult;

public interface CommandDispatcher {
    public DispatchResult dispatch(String var1);
}

