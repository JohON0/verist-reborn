/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;

@FunctionRegister(name="ItemSwapFix", type=Category.Misc)
public class ItemSwapFix
extends Function {
    @Subscribe
    private void onPacket(EventPacket e) {
        if (Minecraft.player == null) {
            return;
        }
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SHeldItemChangePacket) {
            SHeldItemChangePacket wrapper2 = (SHeldItemChangePacket)iPacket;
            int serverSlot = wrapper2.getHeldItemHotbarIndex();
            if (serverSlot != Minecraft.player.inventory.currentItem) {
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Math.max(Minecraft.player.inventory.currentItem % 8 + 1, 0)));
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
                e.cancel();
            }
        }
    }
}

