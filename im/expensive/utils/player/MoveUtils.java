/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import im.expensive.events.EventInput;
import im.expensive.events.MovingEvent;
import im.expensive.utils.client.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public final class MoveUtils
implements IMinecraft {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isMoving() {
        if (Minecraft.player.movementInput.moveForward != 0.0f) return true;
        if (Minecraft.player.movementInput.moveStrafe == 0.0f) return false;
        return true;
    }

    public static void fixMovement(EventInput event, float yaw) {
        float forward = event.getForward();
        float strafe = event.getStrafe();
        double angle = MathHelper.wrapDegrees(Math.toDegrees(MoveUtils.direction(Minecraft.player.isElytraFlying() ? yaw : Minecraft.player.rotationYaw, forward, strafe)));
        if (forward == 0.0f && strafe == 0.0f) {
            return;
        }
        float closestForward = 0.0f;
        float closestStrafe = 0.0f;
        float closestDifference = Float.MAX_VALUE;
        for (float predictedForward = -1.0f; predictedForward <= 1.0f; predictedForward += 1.0f) {
            for (float predictedStrafe = -1.0f; predictedStrafe <= 1.0f; predictedStrafe += 1.0f) {
                double predictedAngle;
                double difference;
                if (predictedStrafe == 0.0f && predictedForward == 0.0f || !((difference = Math.abs(angle - (predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(MoveUtils.direction(yaw, predictedForward, predictedStrafe)))))) < (double)closestDifference)) continue;
                closestDifference = (float)difference;
                closestForward = predictedForward;
                closestStrafe = predictedStrafe;
            }
        }
        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
        if (moveForward < 0.0) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0) {
            forward = -0.5f;
        } else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYaw -= 90.0f * forward;
        }
        if (moveStrafing < 0.0) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static double getMotion() {
        return Math.hypot(Minecraft.player.getMotion().x, Minecraft.player.getMotion().z);
    }

    public static void setMotion(double speed) {
        if (!MoveUtils.isMoving()) {
            return;
        }
        double yaw = MoveUtils.getDirection(true);
        Minecraft.player.setMotion(-Math.sin(yaw) * speed, Minecraft.player.motion.y, Math.cos(yaw) * speed);
    }

    public static boolean isBlockUnder(float under) {
        if (Minecraft.player.getPosY() < 0.0) {
            return false;
        }
        AxisAlignedBB aab = Minecraft.player.getBoundingBox().offset(0.0, -under, 0.0);
        return Minecraft.world.getCollisionShapes(Minecraft.player, aab).toList().isEmpty();
    }

    public static double getDirection(boolean toRadians) {
        float rotationYaw = Minecraft.player.rotationYaw;
        if (Minecraft.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Minecraft.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return toRadians ? Math.toRadians(rotationYaw) : (double)rotationYaw;
    }

    public static void setSpeed(double speed) {
        float f = Minecraft.player.movementInput.moveForward;
        float f1 = Minecraft.player.movementInput.moveStrafe;
        float f2 = Minecraft.player.rotationYaw;
        if (f == 0.0f && f1 == 0.0f) {
            Minecraft.player.motion.x = 0.0;
            Minecraft.player.motion.z = 0.0;
        } else if (f != 0.0f) {
            if (f1 >= 1.0f) {
                f2 += (float)(f > 0.0f ? -35 : 35);
                f1 = 0.0f;
            } else if (f1 <= -1.0f) {
                f2 += (float)(f > 0.0f ? 35 : -35);
                f1 = 0.0f;
            }
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d0 = Math.cos(Math.toRadians(f2 + 90.0f));
        double d1 = Math.sin(Math.toRadians(f2 + 90.0f));
        Minecraft.player.motion.x = (double)f * speed * d0 + (double)f1 * speed * d1;
        Minecraft.player.motion.z = (double)f * speed * d1 - (double)f1 * speed * d0;
    }

    private MoveUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class MoveEvent {
        public static void setMoveMotion(MovingEvent move, double motion) {
            double forward = Minecraft.player.movementInput.moveForward;
            double strafe = Minecraft.player.movementInput.moveStrafe;
            float yaw = Minecraft.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                move.getMotion().x = 0.0;
                move.getMotion().z = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                move.getMotion().x = forward * motion * (double)MathHelper.cos((float)Math.toRadians(yaw + 90.0f)) + strafe * motion * (double)MathHelper.sin((float)Math.toRadians(yaw + 90.0f));
                move.getMotion().z = forward * motion * (double)MathHelper.sin((float)Math.toRadians(yaw + 90.0f)) - strafe * motion * (double)MathHelper.cos((float)Math.toRadians(yaw + 90.0f));
            }
        }
    }
}

