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

@FunctionRegister(name="Phase", type=Category.Movement)
public class Phase
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (!this.collisionPredict() && Phase.mc.gameSettings.keyBindJump.pressed) {
            Minecraft.player.setOnGround(true);
        }
    }

    public boolean collisionPredict() {
        boolean prevCollision = Minecraft.world.getCollisionShapes(Minecraft.player, Minecraft.player.getBoundingBox().shrink(0.0625)).toList().isEmpty();
        return Minecraft.world.getCollisionShapes(Minecraft.player, Minecraft.player.getBoundingBox().shrink(0.0625)).toList().isEmpty() && prevCollision;
    }
}

