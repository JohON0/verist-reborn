/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

@FunctionRegister(name="SeeInvisibles", type=Category.Render)
public class SeeInvisibles
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate e) {
        for (PlayerEntity playerEntity : Minecraft.world.getPlayers()) {
            if (playerEntity == Minecraft.player || !playerEntity.isInvisible()) continue;
            playerEntity.setInvisible(false);
        }
    }
}

