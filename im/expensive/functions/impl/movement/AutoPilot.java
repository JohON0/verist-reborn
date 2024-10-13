/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

@FunctionRegister(name="AutoUPClan", type=Category.Player)
public class AutoPilot
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate event) {
        if (event instanceof EventUpdate) {
            AutoPilot.mc.gameSettings.keyBindUseItem.setPressed(true);
            AutoPilot.mc.playerController.onPlayerDamageBlock(new BlockPos(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ()), Minecraft.player.getHorizontalFacing());
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            AutoPilot.mc.playerController.onPlayerDamageBlock(new BlockPos(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ()), Minecraft.player.getHorizontalFacing());
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            AutoPilot.mc.playerController.onPlayerDamageBlock(new BlockPos(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ()), Minecraft.player.getHorizontalFacing());
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            AutoPilot.mc.playerController.onPlayerDamageBlock(new BlockPos(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ()), Minecraft.player.getHorizontalFacing());
            Minecraft.player.swingArm(Hand.MAIN_HAND);
        }
    }

    @Override
    public void onDisable() {
        AutoPilot.mc.gameSettings.keyBindUseItem.setPressed(true);
        super.onDisable();
    }
}

