/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.module.impl.combat;

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

@FunctionRegister(name="CriticalsFT", type=Category.Misc)
public class Criticals
extends Function {
    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public boolean onEnable() {
        super.onEnable();
        return true;
    }

    @Subscribe
    public void onPlayerUpdate(EventUpdate var1) {
        Minecraft var10000 = this.mc;
        if (Minecraft.player != null) {
            var10000 = this.mc;
            if (Minecraft.world != null) {
                return;
            }
        }
    }

    @Subscribe
    public void onPlayerAttack(EventMotion var1) {
        Minecraft var10000 = this.mc;
        if (Minecraft.player != null) {
            var10000 = this.mc;
            if (Minecraft.world != null) {
                var10000 = this.mc;
                if (Minecraft.player.isOnGround()) {
                    var10000 = this.mc;
                    if (Minecraft.player.fallDistance == 0.0f) {
                        var10000 = this.mc;
                        if (!Minecraft.player.isInWater()) {
                            var10000 = this.mc;
                            ClientPlayNetHandler var2 = Minecraft.player.connection;
                            Minecraft var10003 = this.mc;
                            double var3 = Minecraft.player.getPosX();
                            Minecraft var10004 = this.mc;
                            double var4 = Minecraft.player.getPosY() + 0.1;
                            Minecraft var10005 = this.mc;
                            var2.sendPacket(new CPlayerPacket.PositionPacket(var3, var4, Minecraft.player.getPosZ(), false));
                            var10000 = this.mc;
                            var2 = Minecraft.player.connection;
                            var10003 = this.mc;
                            var3 = Minecraft.player.getPosX();
                            var10004 = this.mc;
                            var4 = Minecraft.player.getPosY();
                            var10005 = this.mc;
                            var2.sendPacket(new CPlayerPacket.PositionPacket(var3, var4, Minecraft.player.getPosZ(), false));
                            var10000 = this.mc;
                            var2 = Minecraft.player.connection;
                            var10003 = this.mc;
                            var3 = Minecraft.player.getPosX();
                            var10004 = this.mc;
                            var4 = Minecraft.player.getPosY() + 0.01;
                            var10005 = this.mc;
                            var2.sendPacket(new CPlayerPacket.PositionPacket(var3, var4, Minecraft.player.getPosZ(), false));
                            var10000 = this.mc;
                            var2 = Minecraft.player.connection;
                            var10003 = this.mc;
                            var3 = Minecraft.player.getPosX();
                            var10004 = this.mc;
                            var4 = Minecraft.player.getPosY();
                            var10005 = this.mc;
                            var2.sendPacket(new CPlayerPacket.PositionPacket(var3, var4, Minecraft.player.getPosZ(), false));
                            var10000 = this.mc;
                            Minecraft.player.swingArm(Hand.MAIN_HAND);
                        }
                    }
                }
                return;
            }
        }
    }
}

