/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.Hand;

@FunctionRegister(name="AutoFish", type=Category.Misc)
public class AutoFish
extends Function {
    private final StopWatch delay = new StopWatch();
    private boolean isHooked = false;
    private boolean needToHook = false;

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (this.delay.isReached(600L) && this.isHooked) {
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            this.isHooked = false;
            this.needToHook = true;
            this.delay.reset();
        }
        if (this.delay.isReached(300L) && this.needToHook) {
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            this.needToHook = false;
            this.delay.reset();
        }
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        SPlaySoundEffectPacket p;
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SPlaySoundEffectPacket && (p = (SPlaySoundEffectPacket)iPacket).getSound().getName().getPath().equals("entity.fishing_bobber.splash")) {
            this.isHooked = true;
            this.delay.reset();
        }
    }
}

