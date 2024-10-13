/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;

@FunctionRegister(name="AutoEat", type=Category.Misc)
public class AutoEat
extends Function {
    private final StopWatch delay = new StopWatch();
    private final int hungerThreshold = 19;

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (Minecraft.player.getFoodStats().getFoodLevel() < 19 && this.delay.isReached(600L)) {
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.OFF_HAND));
            this.delay.reset();
        }
    }
}

