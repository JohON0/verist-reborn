/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl;

import im.expensive.command.AdviceCommandFactory;
import im.expensive.command.CommandProvider;
import im.expensive.command.Logger;
import im.expensive.command.impl.AdviceCommand;

public class AdviceCommandFactoryImpl
implements AdviceCommandFactory {
    private final Logger logger;

    @Override
    public AdviceCommand adviceCommand(CommandProvider commandProvider) {
        return new AdviceCommand(commandProvider, this.logger);
    }

    public AdviceCommandFactoryImpl(Logger logger) {
        this.logger = logger;
    }
}

