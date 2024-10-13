/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import java.util.regex.Pattern;

public final class PlayerUtils {
    private static final Pattern NAME_REGEX = Pattern.compile("^[A-z\u0410-\u044f0-9_]{3,16}$");

    public static boolean isNameValid(String name) {
        return NAME_REGEX.matcher(name).matches();
    }

    private PlayerUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

