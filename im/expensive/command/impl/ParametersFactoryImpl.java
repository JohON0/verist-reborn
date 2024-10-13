/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl;

import im.expensive.command.Parameters;
import im.expensive.command.ParametersFactory;
import im.expensive.command.impl.ParametersImpl;

public class ParametersFactoryImpl
implements ParametersFactory {
    @Override
    public Parameters createParameters(String message, String delimiter) {
        return new ParametersImpl(message.split(delimiter));
    }
}

