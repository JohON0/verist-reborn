/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;

@FunctionRegister(name="AutoRespawn", type=Category.Misc)
public class AutoRespawn
extends Function {
    @Subscribe
    public void onUpdate(EventUpdate e) {
        block6: {
            block5: {
                if (Minecraft.player == null) break block5;
                if (Minecraft.world != null) break block6;
            }
            return;
        }
        if (AutoRespawn.mc.currentScreen instanceof DeathScreen) {
            if (Minecraft.player.deathTime > 5) {
                Minecraft.player.respawnPlayer();
                mc.displayGuiScreen(null);
            }
        }
    }
}

