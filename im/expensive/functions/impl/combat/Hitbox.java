/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

@FunctionRegister(name="HitBox", type=Category.Combat)
public class Hitbox
extends Function {
    public final SliderSetting size = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440", 0.2f, 0.0f, 3.0f, 0.05f);
    public final BooleanSetting visible = new BooleanSetting("\u0412\u0438\u0434\u0438\u043c\u044b\u0435", false);

    public Hitbox() {
        this.addSettings(this.size, this.visible);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        block4: {
            block3: {
                if (!((Boolean)this.visible.get()).booleanValue()) break block3;
                if (Minecraft.player != null) break block4;
            }
            return;
        }
        float sizeMultiplier = ((Float)this.size.get()).floatValue() * 2.5f;
        for (PlayerEntity playerEntity : Minecraft.world.getPlayers()) {
            if (this.isNotValid(playerEntity)) continue;
            playerEntity.setBoundingBox(this.calculateBoundingBox(playerEntity, sizeMultiplier));
        }
    }

    private boolean isNotValid(PlayerEntity player) {
        return player == Minecraft.player || !player.isAlive();
    }

    private AxisAlignedBB calculateBoundingBox(Entity entity2, float size) {
        double minX = entity2.getPosX() - (double)size;
        double minY = entity2.getBoundingBox().minY;
        double minZ = entity2.getPosZ() - (double)size;
        double maxX = entity2.getPosX() + (double)size;
        double maxY = entity2.getBoundingBox().maxY;
        double maxZ = entity2.getPosZ() + (double)size;
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

