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

@FunctionRegister(name="DragonFly", type=Category.Movement)
public class DragonFly
extends Function {
    @Subscribe
    public void onUpdate(EventUpdate event) {
        if (event instanceof EventUpdate) {
            if (Minecraft.player.abilities.isFlying) {
                MoveUtils.setMotion(1.0);
                Minecraft.player.motion.y = 0.0;
                if (DragonFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.motion.y = 0.25;
                    if (Minecraft.player.moveForward == 0.0f && !DragonFly.mc.gameSettings.keyBindLeft.isKeyDown() && !DragonFly.mc.gameSettings.keyBindRight.isKeyDown()) {
                        Minecraft.player.motion.y = 0.5;
                    }
                }
                if (DragonFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.player.motion.y = -0.25;
                    if (Minecraft.player.moveForward == 0.0f && !DragonFly.mc.gameSettings.keyBindLeft.isKeyDown() && !DragonFly.mc.gameSettings.keyBindRight.isKeyDown()) {
                        Minecraft.player.motion.y = -0.5;
                    }
                }
            }
        }
    }
}

