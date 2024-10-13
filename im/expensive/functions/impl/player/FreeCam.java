/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventLivingUpdate;
import im.expensive.events.EventMotion;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="FreeCam", type=Category.Movement)
public class FreeCam
extends Function {
    private final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043f\u043e XZ", 1.0f, 0.1f, 5.0f, 0.05f);
    private final SliderSetting motionY = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043f\u043e Y", 0.5f, 0.1f, 1.0f, 0.05f);
    private Vector3d clientPosition = null;
    private RemoteClientPlayerEntity fakePlayer;

    public FreeCam() {
        this.addSettings(this.speed, this.motionY);
    }

    @Subscribe
    public void onLivingUpdate(EventLivingUpdate e) {
        if (Minecraft.player != null) {
            Minecraft.player.noClip = true;
            Minecraft.player.setOnGround(false);
            MoveUtils.setMotion(((Float)this.speed.get()).floatValue());
            if (FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motion.y = ((Float)this.motionY.get()).floatValue();
            }
            if (FreeCam.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.player.motion.y = -((Float)this.motionY.get()).floatValue();
            }
            Minecraft.player.abilities.isFlying = true;
        }
    }

    @Subscribe
    public void onMotion(EventMotion e) {
        if (Minecraft.player != null) {
            e.cancel();
        }
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        block8: {
            IPacket<?> iPacket;
            block7: {
                if (Minecraft.player == null) break block7;
                if (Minecraft.world != null && !(FreeCam.mc.currentScreen instanceof DownloadTerrainScreen)) break block8;
            }
            if ((iPacket = e.getPacket()) instanceof SPlayerPositionLookPacket) {
                SPlayerPositionLookPacket packet = (SPlayerPositionLookPacket)iPacket;
                if (this.fakePlayer != null) {
                    this.fakePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
                }
                e.cancel();
            }
        }
        if (e.getPacket() instanceof SRespawnPacket) {
            Minecraft.player.abilities.isFlying = false;
            if (this.clientPosition != null) {
                Minecraft.player.setPositionAndRotation(this.clientPosition.x, this.clientPosition.y, this.clientPosition.z, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
            }
            this.removeFakePlayer();
            Minecraft.player.motion = Vector3d.ZERO;
        }
    }

    @Override
    public boolean onEnable() {
        if (Minecraft.player == null) {
            return false;
        }
        this.clientPosition = Minecraft.player.getPositionVec();
        this.spawnFakePlayer();
        super.onEnable();
        return false;
    }

    @Override
    public void onDisable() {
        if (Minecraft.player == null) {
            return;
        }
        Minecraft.player.abilities.isFlying = false;
        if (this.clientPosition != null) {
            Minecraft.player.setPositionAndRotation(this.clientPosition.x, this.clientPosition.y, this.clientPosition.z, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
        }
        this.removeFakePlayer();
        Minecraft.player.motion = Vector3d.ZERO;
        super.onDisable();
    }

    private void spawnFakePlayer() {
        this.fakePlayer = new RemoteClientPlayerEntity(Minecraft.world, Minecraft.player.getGameProfile());
        this.fakePlayer.copyLocationAndAnglesFrom(Minecraft.player);
        this.fakePlayer.rotationYawHead = Minecraft.player.rotationYawHead;
        this.fakePlayer.renderYawOffset = Minecraft.player.renderYawOffset;
        this.fakePlayer.rotationPitchHead = Minecraft.player.rotationPitchHead;
        this.fakePlayer.container = Minecraft.player.container;
        this.fakePlayer.inventory = Minecraft.player.inventory;
        Minecraft.world.addEntity(1337, this.fakePlayer);
    }

    private void removeFakePlayer() {
        Minecraft.world.removeEntityFromWorld(1337);
    }
}

