/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;

@FunctionRegister(name="NoJumpDelay", type=Category.Player)
public class NoJumpDelay
extends Function {
    @Subscribe
    public void onUpdate(EventUpdate e) {
        Minecraft.player.jumpTicks = 0;
    }
}

