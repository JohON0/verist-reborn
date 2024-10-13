/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.render.ColorUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@FunctionRegister(name="GlowESP", type=Category.Render)
public class GlowESP
extends Function {
    private Map<Integer, Boolean> glowingPlayers = new HashMap<Integer, Boolean>();

    @Subscribe
    public void onUpdate(EventUpdate eventUpdate) {
        for (Entity entity2 : Minecraft.world.getPlayers()) {
            if (entity2 == null) continue;
            this.applyGlow(entity2);
        }
    }

    @Override
    public boolean onEnable() {
        for (Entity entity2 : Minecraft.world.getPlayers()) {
            if (entity2 == null) continue;
            this.applyGlow(entity2);
        }
        return true;
    }

    @Override
    public void onDisable() {
        for (Entity entity2 : Minecraft.world.getPlayers()) {
            if (entity2 == null) continue;
            entity2.setGlowing(false);
        }
        this.glowingPlayers.clear();
    }

    private void applyGlow(Entity player) {
        player.setGlowing(true);
        this.setEntityGlowingColor(player, ColorUtils.getColor(0));
        this.glowingPlayers.put(player.getEntityId(), true);
    }

    private void setEntityGlowingColor(Entity entity2, int color) {
        entity2.setGlowing(true);
    }
}

