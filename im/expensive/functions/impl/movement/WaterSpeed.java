/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

@FunctionRegister(name="WaterSpeed", type=Category.Movement)
public class WaterSpeed
extends Function {
    ModeSetting type = new ModeSetting("\u0422\u0438\u043f", "\u041e\u0431\u044b\u0447\u043d\u044b\u0435", "Funtime", "Holyworld", "\u041e\u0431\u044b\u0447\u043d\u044b\u0435");

    public WaterSpeed() {
        this.addSettings(this.type);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        String selectedType = (String)this.type.get();
        if (selectedType.equals("\u041e\u0431\u044b\u0447\u043d\u044b\u0435")) {
            this.WATER_DEF();
        } else if (selectedType.equals("Funtime")) {
            this.WATER_FT();
        } else if (selectedType.equals("Holyworld")) {
            this.WATER_HOLY();
        }
    }

    private void WATER_DEF() {
        Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.player;
        if (player != null && player.isAlive() && player.isInWater()) {
            player.setMotion(player.getMotion().x * 1.1, player.getMotion().y, player.getMotion().z * 1.1);
        }
    }

    private void WATER_FT() {
        Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.player;
        if (player != null && player.isAlive() && player.isInWater()) {
            player.setMotion(player.getMotion().x * 1.0505, player.getMotion().y, player.getMotion().z * 1.0505);
        }
    }

    private void WATER_HOLY() {
        Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.player;
        if (player != null && player.isAlive() && player.isInWater()) {
            player.setMotion(player.getMotion().x * 1.03, player.getMotion().y, player.getMotion().z * 1.03);
        }
    }
}

