/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;

@FunctionRegister(name="AntiAFK", type=Category.Player)
public class AntiAFK
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (Minecraft.player.ticksExisted % 200 != 0) {
            return;
        }
        if (Minecraft.player.isOnGround()) {
            Minecraft.player.jump();
        }
        Minecraft.player.rotationYaw += ThreadLocalRandom.current().nextFloat(-10.0f, 10.0f);
    }
}

