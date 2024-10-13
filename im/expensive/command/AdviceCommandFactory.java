/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command;

import im.expensive.command.CommandProvider;
import im.expensive.command.impl.AdviceCommand;

public interface AdviceCommandFactory {
    public AdviceCommand adviceCommand(CommandProvider var1);
}

