/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.math;

import im.expensive.utils.client.IMinecraft;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public final class MathUtil
implements IMinecraft {
    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public static Vector2f rotationToVec(Vector3d vec) {
        double diffX;
        Vector3d eyesPos = Minecraft.player.getEyePosition(1.0f);
        double d = diffX = vec != null ? vec.x - eyesPos.x : 0.0;
        double diffY = vec != null ? vec.y - (Minecraft.player.getPosY() + (double)Minecraft.player.getEyeHeight() + 0.5) : 0.0;
        double diffZ = vec != null ? vec.z - eyesPos.z : 0.0;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        yaw = Minecraft.player.rotationYaw + MathHelper.wrapDegrees(yaw - Minecraft.player.rotationYaw);
        pitch = Minecraft.player.rotationPitch + MathHelper.wrapDegrees(pitch - Minecraft.player.rotationPitch);
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new Vector2f(yaw, pitch);
    }

    public static Vector2f rotationToEntity(Entity target) {
        Vector3d vector3d = target.getPositionVec();
        Minecraft.getInstance();
        Vector3d vector3d2 = vector3d.subtract(Minecraft.player.getPositionVec());
        double magnitude = Math.hypot(vector3d2.x, vector3d2.z);
        return new Vector2f((float)Math.toDegrees(Math.atan2(vector3d2.z, vector3d2.x)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vector3d2.y, magnitude))));
    }

    public static Vector2f rotationToVec(Vector2f rotationVector, Vector3d target) {
        double x = target.x - Minecraft.player.getPosX();
        double y = target.y - Minecraft.player.getEyePosition((float)1.0f).y;
        double z = target.z - Minecraft.player.getPosZ();
        double dst = Math.sqrt(Math.pow(x, 2.0) + Math.pow(z, 2.0));
        float yaw = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = MathHelper.wrapDegrees(yaw - rotationVector.x);
        float pitchDelta = pitch - rotationVector.y;
        if (Math.abs(yawDelta) > 180.0f) {
            yawDelta -= Math.signum(yawDelta) * 360.0f;
        }
        return new Vector2f(yawDelta, pitchDelta);
    }

    public static double round(double num, double increment) {
        double v = (double)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(x * x + y * y);
    }

    public static double deltaTime() {
        return MathUtil.mc.debugFPS > 0 ? 1.0 / (double)MathUtil.mc.debugFPS : 1.0;
    }

    public static float fast(float end, float start, float multiple) {
        return (1.0f - MathHelper.clamp((float)(MathUtil.deltaTime() * (double)multiple), 0.0f, 1.0f)) * end + MathHelper.clamp((float)(MathUtil.deltaTime() * (double)multiple), 0.0f, 1.0f) * start;
    }

    public static Vector3d interpolate(Vector3d end, Vector3d start, float multiple) {
        return new Vector3d(MathUtil.interpolate(end.getX(), start.getX(), (double)multiple), MathUtil.interpolate(end.getY(), start.getY(), (double)multiple), MathUtil.interpolate(end.getZ(), start.getZ(), (double)multiple));
    }

    public static Vector3d fast(Vector3d end, Vector3d start, float multiple) {
        return new Vector3d(MathUtil.fast((float)end.getX(), (float)start.getX(), multiple), MathUtil.fast((float)end.getY(), (float)start.getY(), multiple), MathUtil.fast((float)end.getZ(), (float)start.getZ(), multiple));
    }

    public static float lerp(float end, float start, float multiple) {
        return (float)((double)end + (double)(start - end) * MathHelper.clamp(MathUtil.deltaTime() * (double)multiple, 0.0, 1.0));
    }

    public static double lerp(double end, double start, double multiple) {
        return end + (start - end) * MathHelper.clamp(MathUtil.deltaTime() * multiple, 0.0, 1.0);
    }

    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

