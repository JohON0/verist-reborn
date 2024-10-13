/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventStartRiding;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Teleport", type=Category.Movement)
public class Teleport
extends Function {
    private final StopWatch timer = new StopWatch();
    private final Random random = new Random();
    private final Set<BlockPos> visitedPositions = new HashSet<BlockPos>();
    private final int range = 4;

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (this.timer.isReached(10L)) {
            this.processEventUpdate();
        }
    }

    @Subscribe
    public void onRiding(EventStartRiding e) {
        this.processEventStartRiding(e);
    }

    private void processEventUpdate() {
        Block randomBlock = this.findRandomNearbyBlock();
        if (randomBlock != null) {
            this.sitOnSlab(randomBlock);
        }
        this.timer.reset();
    }

    private void processEventStartRiding(EventStartRiding event) {
        Entity entity2 = event.e;
        this.visitedPositions.add(entity2.getPosition());
        System.out.println("1");
        new Thread(() -> {
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Minecraft.player.stopRiding();
        }).start();
    }

    private Block findRandomNearbyBlock() {
        List<BlockPos> potentialPositions = this.getPotentialPositions();
        if (!potentialPositions.isEmpty()) {
            BlockPos selectedPos = potentialPositions.get(this.random.nextInt(potentialPositions.size()));
            return Minecraft.world.getBlockState(selectedPos).getBlock();
        }
        return null;
    }

    private List<BlockPos> getPotentialPositions() {
        Vector3d playerPos = Minecraft.player.getPositionVec();
        ArrayList<BlockPos> potentialPositions = new ArrayList<BlockPos>();
        for (int x = -4; x <= 4; ++x) {
            for (int y = -4; y <= 4; ++y) {
                for (int z = -4; z <= 4; ++z) {
                    BlockPos pos = new BlockPos(playerPos.x + (double)x, playerPos.y + (double)y, playerPos.z + (double)z);
                    if (!this.isPositionEligible(pos)) continue;
                    potentialPositions.add(pos);
                }
            }
        }
        return potentialPositions;
    }

    public static double getDistanceOfEntityToBlock(Entity entity2, BlockPos blockPos) {
        return Minecraft.player.getDistance(blockPos);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isPositionEligible(BlockPos pos) {
        if (!this.visitedPositions.contains(pos)) {
            if (Minecraft.world.getBlockState(pos).getBlock() instanceof SlabBlock) return true;
        }
        if (!(Minecraft.world.getBlockState(pos).getBlock() instanceof StairsBlock)) return false;
        if (!Minecraft.world.isAirBlock(pos.up())) return false;
        if (!Minecraft.world.isAirBlock(pos.up(2))) return false;
        return true;
    }

    private void sitOnSlab(Block block2) {
        BlockPos pos = this.findSlabPosition(block2);
        if (pos != null && !this.visitedPositions.contains(pos)) {
            Vector3d hitVec = new Vector3d((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5);
            BlockRayTraceResult blockRayTraceResult = new BlockRayTraceResult(hitVec, Direction.UP, pos, false);
            Teleport.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, blockRayTraceResult);
        }
    }

    private BlockPos findSlabPosition(Block slab) {
        Vector3d playerPos = Minecraft.player.getPositionVec();
        BlockPos posr = null;
        for (int x = -4; x <= 4; ++x) {
            for (int y = -4; y <= 4; ++y) {
                for (int z = -4; z <= 4; ++z) {
                    BlockPos pos = new BlockPos(playerPos.x + (double)x, playerPos.y + (double)y, playerPos.z + (double)z);
                    Block block2 = Minecraft.world.getBlockState(pos).getBlock();
                    if (posr == null && block2 == slab) {
                        posr = pos;
                    }
                    if (block2 != slab) continue;
                    if (!(Minecraft.player.getDistance(posr) < Minecraft.player.getDistance(pos))) continue;
                    posr = pos;
                }
            }
        }
        return posr;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.visitedPositions.clear();
    }
}

