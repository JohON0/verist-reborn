/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command;

import im.expensive.command.Parameters;

public interface Command {
    public void execute(Parameters var1);

    public String name();

    public String description();
}

