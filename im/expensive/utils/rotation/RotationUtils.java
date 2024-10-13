/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.rotation;

import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.math.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public final class RotationUtils
implements IMinecraft {
    public static Vector3d getClosestVec(Entity entity2) {
        Vector3d eyePosVec = Minecraft.player.getEyePosition(1.0f);
        return VectorUtils.getClosestVec(eyePosVec, entity2).subtract(eyePosVec);
    }

    public static double getStrictDistance(Entity entity2) {
        return RotationUtils.getClosestVec(entity2).length();
    }

    private RotationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

