/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CCloseWindowPacket;

@FunctionRegister(name="xCarry", type=Category.Misc)
public class xCarry
extends Function {
    @Subscribe
    public void onPacket(EventPacket e) {
        if (Minecraft.player == null) {
            return;
        }
        if (e.getPacket() instanceof CCloseWindowPacket) {
            e.cancel();
        }
    }
}

