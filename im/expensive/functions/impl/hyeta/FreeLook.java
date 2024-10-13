/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.Expensive;
import im.expensive.events.EventMotion;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;

@FunctionRegister(name="FreeLook", type=Category.Misc)
public class FreeLook
extends Function {
    public BooleanSetting free = new BooleanSetting("\u0421\u0432\u043e\u0431\u043e\u0434\u043d\u0430\u044f \u043a\u0430\u043c\u0435\u0440\u0430", true);
    private float startYaw;
    private float startPitch;

    @Override
    public boolean onEnable() {
        if (this.isFree()) {
            this.startYaw = Minecraft.player.rotationYaw;
            this.startPitch = Minecraft.player.rotationPitch;
        }
        super.onEnable();
        return false;
    }

    @Override
    public void onDisable() {
        if (this.isFree()) {
            Minecraft.player.rotationYawOffset = -2.14748365E9f;
            FreeLook.mc.gameSettings.setPointOfView(PointOfView.FIRST_PERSON);
            Minecraft.player.rotationYaw = this.startYaw;
            Minecraft.player.rotationPitch = this.startPitch;
        }
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        KillAura aura = Expensive.getInstance().getFunctionRegistry().getKillAura();
        if (((Boolean)this.free.get()).booleanValue() && !aura.isState() && aura.getTarget() == null) {
            FreeLook.mc.gameSettings.setPointOfView(PointOfView.THIRD_PERSON_BACK);
            Minecraft.player.rotationYawOffset = this.startYaw;
        }
    }

    @Subscribe
    public void onMotion(EventMotion e) {
        if (((Boolean)this.free.get()).booleanValue()) {
            e.setYaw(this.startYaw);
            e.setPitch(this.startPitch);
            e.setOnGround(Minecraft.player.isOnGround());
            Minecraft.player.rotationYawHead = Minecraft.player.rotationYawOffset;
            Minecraft.player.renderYawOffset = Minecraft.player.rotationYawOffset;
            Minecraft.player.rotationPitchHead = this.startPitch;
        }
    }

    public boolean isFree() {
        return (Boolean)this.free.get();
    }
}

