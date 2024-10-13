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

@FunctionRegister(name="Parkour", type=Category.Movement)
public class Parkour
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (MoveUtils.isBlockUnder(0.001f)) {
            if (Minecraft.player.isOnGround()) {
                Minecraft.player.jump();
            }
        }
    }
}

