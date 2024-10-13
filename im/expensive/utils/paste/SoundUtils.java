/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.paste;

import im.expensive.utils.client.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvents;

public class SoundUtils
implements IMinecraft {
    public static void playSound(float pitch, float volume) {
        if (Minecraft.player == null) {
            return;
        }
        Minecraft.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, volume, pitch);
    }
}

