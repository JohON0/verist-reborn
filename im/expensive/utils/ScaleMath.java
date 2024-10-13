/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils;

import im.expensive.utils.client.Vec2i;
import net.minecraft.client.Minecraft;

public class ScaleMath {
    public static Vec2i getMouse(int mouseX, int mouseY) {
        return new Vec2i((int)((double)mouseX * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2.0), (int)((double)mouseY * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2.0));
    }
}

