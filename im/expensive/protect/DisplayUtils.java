/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

import im.expensive.protect.NoStackTraceThrowable;

public class DisplayUtils {
    public static void Display() {
        throw new NoStackTraceThrowable("Verification was unsuccessful!");
    }
}

