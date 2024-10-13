/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

public class NoStackTraceThrowable
extends RuntimeException {
    public NoStackTraceThrowable(String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "xz";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

