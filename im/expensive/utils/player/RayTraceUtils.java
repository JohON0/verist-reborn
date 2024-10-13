/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import im.expensive.utils.client.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public final class RayTraceUtils
implements IMinecraft {
    public static boolean rayTraceSingleEntity(float yaw, float pitch, double distance, Entity entity2) {
        Vector3d eyeVec = Minecraft.player.getEyePosition(1.0f);
        Vector3d lookVec = Minecraft.player.getVectorForRotation(pitch, yaw);
        Vector3d extendedVec = eyeVec.add(lookVec.scale(distance));
        AxisAlignedBB AABB = entity2.getBoundingBox();
        return AABB.contains(eyeVec) || AABB.rayTrace(eyeVec, extendedVec).isPresent();
    }

    public static boolean isHitBoxNotVisible(Vector3d vec3d) {
        RayTraceContext rayTraceContext = new RayTraceContext(Minecraft.player.getEyePosition(1.0f), vec3d, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, Minecraft.player);
        BlockRayTraceResult blockHitResult = Minecraft.world.rayTraceBlocks(rayTraceContext);
        return blockHitResult.getType() == RayTraceResult.Type.MISS;
    }

    private RayTraceUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

