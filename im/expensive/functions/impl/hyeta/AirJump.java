/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import net.minecraft.client.Minecraft;

@FunctionRegister(name="AirJump", type=Category.Movement)
public class AirJump
extends Function {
    private ModeSetting mode = new ModeSetting("\u041e\u0431\u0445\u043e\u0434", "Matrix", "Default", "Matrix");

    public AirJump() {
        this.addSettings(this.mode);
    }

    @Subscribe
    public void onUpdate(EventMotion e) {
        if (this.mode.is("Default")) {
            Minecraft.player.isOnGround();
        }
        if (this.mode.is("Matrix")) {
            if (!Minecraft.world.getCollisionShapes(Minecraft.player, Minecraft.player.getBoundingBox().expand(0.5, 0.0, 0.5).offset(0.0, -1.0, 0.0)).toList().isEmpty()) {
                if (Minecraft.player.ticksExisted % 2 == 0) {
                    Minecraft.player.fallDistance = 0.0f;
                    Minecraft.player.jumpTicks = 0;
                    e.setOnGround(true);
                    Minecraft.player.isOnGround();
                }
            }
        }
    }
}

