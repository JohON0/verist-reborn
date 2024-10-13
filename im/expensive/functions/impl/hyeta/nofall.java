/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

@FunctionRegister(name="NoFall", type=Category.Movement)
public class nofall
extends Function {
    @Subscribe
    public void onUpdate(EventUpdate var1) {
        if (Minecraft.player.fallDistance > 2.0f && !Minecraft.player.isOnGround()) {
            Minecraft.player.motion.y = 0.0035f;
            Minecraft.player.rotationYaw = 0.0f;
        }
    }

    protected float[] rotations(PlayerEntity var1) {
        return new float[0];
    }
}

