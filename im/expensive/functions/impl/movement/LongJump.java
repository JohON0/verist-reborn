/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.events.MovingEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import im.expensive.utils.player.MouseUtil;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Pose;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

@FunctionRegister(name="SpeedBoost", type=Category.Movement)
public class LongJump
extends Function {
    boolean placed;
    int counter;
    public ModeSetting mod = new ModeSetting("\u041c\u043e\u0434", "Slap", "Slap");
    StopWatch stopWatch = new StopWatch();

    public LongJump() {
        this.addSettings(this.mod);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (!this.mod.is("Slap")) return;
        if (Minecraft.player.isInWater()) return;
        int slot = InventoryUtil.getSlotInInventoryOrHotbar();
        if (slot == -1) {
            this.print("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0442 \u043f\u043e\u043b\u0443\u0431\u043b\u043e\u043a\u043e\u0432 \u0432 \u0445\u043e\u0442\u0431\u0430\u0440\u0435!");
            this.toggle();
            return;
        }
        int old = Minecraft.player.inventory.currentItem;
        RayTraceResult rayTraceResult = MouseUtil.rayTraceResult(2.0, Minecraft.player.rotationYaw, 90.0f, Minecraft.player);
        if (!(rayTraceResult instanceof BlockRayTraceResult)) {
            if (!Minecraft.player.isOnGround()) return;
            if (LongJump.mc.gameSettings.keyBindJump.pressed) return;
            Minecraft.player.jump();
            this.placed = false;
            return;
        }
        BlockRayTraceResult result = (BlockRayTraceResult)rayTraceResult;
        if (!MoveUtils.isMoving()) return;
        if ((double)Minecraft.player.fallDistance >= 0.8) {
            if (Minecraft.world.getBlockState(Minecraft.player.getPosition()).isAir()) {
                if (!Minecraft.world.getBlockState(result.getPos()).isAir()) {
                    if (Minecraft.world.getBlockState(result.getPos()).isSolid()) {
                        if (!(Minecraft.world.getBlockState(result.getPos()).getBlock() instanceof SlabBlock)) {
                            if (!(Minecraft.world.getBlockState(result.getPos()).getBlock() instanceof StairsBlock)) {
                                Minecraft.player.inventory.currentItem = slot;
                                this.placed = true;
                                LongJump.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, result);
                                Minecraft.player.inventory.currentItem = old;
                                Minecraft.player.fallDistance = 0.0f;
                            }
                        }
                    }
                }
            }
        }
        LongJump.mc.gameSettings.keyBindJump.pressed = false;
        if (Minecraft.player.isOnGround() && !LongJump.mc.gameSettings.keyBindJump.pressed && this.placed) {
            if (Minecraft.world.getBlockState(Minecraft.player.getPosition()).isAir()) {
                if (!Minecraft.world.getBlockState(result.getPos()).isAir()) {
                    if (Minecraft.world.getBlockState(result.getPos()).isSolid()) {
                        if (!(Minecraft.world.getBlockState(result.getPos()).getBlock() instanceof SlabBlock) && this.stopWatch.isReached(750L)) {
                            Minecraft.player.setPose(Pose.STANDING);
                            this.stopWatch.reset();
                            this.placed = false;
                            return;
                        }
                    }
                }
            }
        }
        if (!Minecraft.player.isOnGround()) return;
        if (LongJump.mc.gameSettings.keyBindJump.pressed) return;
        Minecraft.player.jump();
        this.placed = false;
    }

    @Subscribe
    public void onMoving(MovingEvent e) {
    }

    @Subscribe
    public void onFlag(EventPacket e) {
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SPlayerPositionLookPacket) {
            SPlayerPositionLookPacket p = (SPlayerPositionLookPacket)iPacket;
            this.placed = false;
            this.counter = 0;
            Minecraft.player.setPose(Pose.STANDING);
        }
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        this.counter = 0;
        this.placed = false;
        return false;
    }
}

