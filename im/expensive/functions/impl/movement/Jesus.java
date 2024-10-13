/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;

@FunctionRegister(name="Jesus", type=Category.Movement)
public class Jesus
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate update) {
        if (Minecraft.player.isInWater()) {
            float moveSpeed = 0.1f;
            double moveX = Minecraft.player.getForward().x;
            double moveZ = Minecraft.player.getForward().z;
            Minecraft.player.motion.y = 0.0;
            if (MoveUtils.isMoving()) {
                double motionX = Minecraft.player.motion.x;
                double motionZ = Minecraft.player.motion.z;
                double length = Math.sqrt(motionX * motionX + motionZ * motionZ);
                if (length > 0.0) {
                    motionX /= length;
                    motionZ /= length;
                }
                Minecraft.player.motion.x = motionX * (double)moveSpeed;
                Minecraft.player.motion.z = motionZ * (double)moveSpeed;
            }
            if (Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motion.y = 0.2f;
            }
        }
    }
}

