/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@FunctionRegister(name="Velocity", type=Category.Combat)
public class Velocity
extends Function {
    private final ModeSetting mode = new ModeSetting("Mode", "Cancel", "Cancel", "Grim Skip", "Grim Cancel", "Funtime");
    private int skip = 0;
    private boolean cancel;
    boolean damaged;
    BlockPos blockPos;

    public Velocity() {
        this.addSettings(this.mode);
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        if (Minecraft.player == null) {
            return;
        }
        if (e.isReceive()) {
            SEntityVelocityPacket p;
            IPacket<?> iPacket = e.getPacket();
            if (iPacket instanceof SEntityVelocityPacket) {
                p = (SEntityVelocityPacket)iPacket;
                if (p.getEntityID() != Minecraft.player.getEntityId()) {
                    return;
                }
            }
            switch (this.mode.getIndex()) {
                case 0: {
                    if (!(e.getPacket() instanceof SEntityVelocityPacket)) break;
                    e.cancel();
                    break;
                }
                case 1: {
                    if (e.getPacket() instanceof SEntityVelocityPacket) {
                        this.skip = 6;
                        e.cancel();
                    }
                    if (!(e.getPacket() instanceof CPlayerPacket) || this.skip <= 0) break;
                    --this.skip;
                    e.cancel();
                    break;
                }
                case 2: {
                    if (e.getPacket() instanceof SEntityVelocityPacket) {
                        e.cancel();
                        this.cancel = true;
                    }
                    if (e.getPacket() instanceof SPlayerPositionLookPacket) {
                        this.skip = 3;
                    }
                    if (!(e.getPacket() instanceof CPlayerPacket)) break;
                    --this.skip;
                    if (!this.cancel) break;
                    if (this.skip <= 0) {
                        BlockPos blockPos = new BlockPos(Minecraft.player.getPositionVec());
                        Minecraft.player.connection.sendPacket(new CPlayerPacket.PositionRotationPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ(), Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, Minecraft.player.isOnGround()));
                        Minecraft.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                    }
                    this.cancel = false;
                }
            }
            if (this.mode.is("Funtime")) {
                iPacket = e.getPacket();
                if (iPacket instanceof SEntityVelocityPacket) {
                    p = (SEntityVelocityPacket)iPacket;
                    if (this.skip >= 2) {
                        return;
                    }
                    if (p.getEntityID() != Minecraft.player.getEntityId()) {
                        return;
                    }
                    e.cancel();
                    this.damaged = true;
                }
                if (e.getPacket() instanceof SPlayerPositionLookPacket) {
                    this.skip = 3;
                }
            }
        }
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (this.mode.is("Funtime")) {
            --this.skip;
            if (this.damaged) {
                BlockPos blockPos = Minecraft.player.getPosition();
                Minecraft.player.connection.sendPacketWithoutEvent(new CPlayerPacket.PositionRotationPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ(), Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, Minecraft.player.isOnGround()));
                Minecraft.player.connection.sendPacketWithoutEvent(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                this.damaged = false;
            }
        }
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        this.skip = 0;
        this.cancel = false;
        this.damaged = false;
        return false;
    }
}

