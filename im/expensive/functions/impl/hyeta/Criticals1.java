/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.Hand;

@FunctionRegister(name="Hotbar", type=Category.Combat)
public class Criticals1
extends Function {
    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public boolean onEnable() {
        super.onEnable();
        return true;
    }

    @Subscribe
    public void onPlayerUpdate(EventUpdate event) {
        if (Minecraft.player != null) {
            if (Minecraft.world != null) {
                // empty if block
            }
        }
    }

    @Subscribe
    public void onPlayerAttack(EventMotion event) {
        if (Minecraft.player != null) {
            if (Minecraft.world != null) {
                if (Minecraft.player.isOnGround()) {
                    if (Minecraft.player.fallDistance == 0.0f) {
                        if (!Minecraft.player.isInWater()) {
                            ClientPlayNetHandler connection = Minecraft.player.connection;
                            double posX = Minecraft.player.getPosX();
                            double posY = Minecraft.player.getPosY();
                            double posZ = Minecraft.player.getPosZ();
                            connection.sendPacket(new CPlayerPacket.PositionPacket(posX, posY + 0.1, posZ, false));
                            connection.sendPacket(new CPlayerPacket.PositionPacket(posX, posY, posZ, false));
                            connection.sendPacket(new CPlayerPacket.PositionPacket(posX, posY + 0.01, posZ, false));
                            connection.sendPacket(new CPlayerPacket.PositionPacket(posX, posY, posZ, false));
                            Minecraft.player.swingArm(Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }
}

