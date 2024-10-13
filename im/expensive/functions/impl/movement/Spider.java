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
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;

@FunctionRegister(name="AirPlace", type=Category.Movement)
public class Spider
extends Function {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastPlacementTime = 0L;
    private final long placementDelay = 312L;

    @Subscribe
    private void onUpdate(EventUpdate update) {
        if (System.currentTimeMillis() - this.lastPlacementTime < 312L) {
            return;
        }
        if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)this.mc.objectMouseOver;
            BlockPos blockPos = blockRayTraceResult.getPos();
            Direction face = blockRayTraceResult.getFace();
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, blockRayTraceResult);
            this.lastPlacementTime = System.currentTimeMillis();
        }
    }
}

