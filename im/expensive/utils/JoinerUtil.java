/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils;

import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;

public class JoinerUtil
implements IMinecraft {
    public static void selectCompass() {
        int slot = InventoryUtil.getItemSlot(Items.COMPASS.getDefaultInstance());
        if (slot == -1) {
            return;
        }
        Minecraft.player.inventory.currentItem = slot;
        Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(slot));
    }
}

