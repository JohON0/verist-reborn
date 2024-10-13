/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

@FunctionRegister(name="FakePlayer", type=Category.Misc)
public class FakePlayer
extends Function {
    private boolean spawn = false;
    private RemoteClientPlayerEntity fakePlayer;

    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.spawn = true;
    }

    private void spawnFakePlayer() {
        UUID var1 = UUID.nameUUIDFromBytes("1337".getBytes());
        this.fakePlayer = new RemoteClientPlayerEntity(Minecraft.world, new GameProfile(var1, "Nursultan_User9481"));
        this.fakePlayer.copyLocationAndAnglesFrom(Minecraft.player);
        this.fakePlayer.rotationYawHead = Minecraft.player.rotationYawHead;
        this.fakePlayer.renderYawOffset = Minecraft.player.renderYawOffset;
        this.fakePlayer.rotationPitchHead = Minecraft.player.rotationPitchHead;
        this.fakePlayer.container = Minecraft.player.container;
        this.fakePlayer.inventory = Minecraft.player.inventory;
        this.fakePlayer.setHealth(1337.0f);
        Minecraft.world.addEntity(1337, this.fakePlayer);
    }

    @Override
    public void onDisable() {
        this.removeFakePlayer();
        this.spawn = false;
        super.onDisable();
    }

    @Override
    public boolean onEnable() {
        this.spawnFakePlayer();
        super.onEnable();
        return false;
    }

    protected float[] rotations(PlayerEntity player) {
        return new float[0];
    }

    private void removeFakePlayer() {
        Minecraft.world.removeEntityFromWorld(1337);
    }
}

