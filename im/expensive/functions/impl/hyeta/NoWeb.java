/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="NoWeb", type=Category.Movement)
public class NoWeb
extends Function {
    @Subscribe
    public static void onUpdate(EventUpdate event) {
        if (NoWeb.isInWeb()) {
            NoWeb.clearWebCollision();
        }
    }

    private static boolean isInWeb() {
        Vector3d playerPos = Minecraft.player.getPositionVec();
        int playerX = (int)playerPos.getX();
        int playerY = (int)playerPos.getY();
        int playerZ = (int)playerPos.getZ();
        for (int offsetX = -1; offsetX <= 1; ++offsetX) {
            for (int offsetY = -1; offsetY <= 1; ++offsetY) {
                for (int offsetZ = -1; offsetZ <= 1; ++offsetZ) {
                    BlockPos blockPos = new BlockPos(playerX + offsetX, playerY + offsetY, playerZ + offsetZ);
                    Block block2 = Minecraft.player.world.getBlockState(blockPos).getBlock();
                    if (block2 != Blocks.COBWEB) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static void clearWebCollision() {
        Vector3d playerPos = Minecraft.player.getPositionVec();
        int playerX = (int)playerPos.getX();
        int playerY = (int)playerPos.getY();
        int playerZ = (int)playerPos.getZ();
        for (int offsetX = -1; offsetX <= 1; ++offsetX) {
            for (int offsetY = -1; offsetY <= 1; ++offsetY) {
                for (int offsetZ = -1; offsetZ <= 1; ++offsetZ) {
                    BlockPos blockPos = new BlockPos(playerX + offsetX, playerY + offsetY, playerZ + offsetZ);
                    if (Minecraft.player.world.getBlockState(blockPos).getBlock() != Blocks.COBWEB) continue;
                    Minecraft.player.world.destroyBlock(blockPos, false);
                    Minecraft.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
                    Minecraft.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.DOWN));
                }
            }
        }
    }
}

