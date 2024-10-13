/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CUseEntityPacket;

@FunctionRegister(name="Hotbar", type=Category.Render)
public class Hotbar
extends Function {
    public static Hotbar hotbar;
    public boolean state;
    private final Minecraft mc = Minecraft.getInstance();

    @Subscribe
    public void onRender(EventDisplay e) {
    }

    public void performCriticalHit(Entity target) {
        block3: {
            block2: {
                if (Minecraft.player == null) break block2;
                if (!Minecraft.player.isElytraFlying()) break block3;
            }
            return;
        }
        Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_SPRINTING));
        Minecraft.player.connection.sendPacket(new CPlayerPacket.PositionPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY() + 0.1, Minecraft.player.getPosZ(), false));
        Minecraft.player.connection.sendPacket(new CPlayerPacket.PositionPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ(), true));
        Minecraft.player.connection.sendPacket(new CUseEntityPacket());
        Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.STOP_SPRINTING));
    }
}

