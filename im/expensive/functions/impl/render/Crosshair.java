/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;

@FunctionRegister(name="Crosshair", type=Category.Render)
public class Crosshair
extends Function {
    private final ModeSetting mode = new ModeSetting("\u0412\u0438\u0434", "\u041a\u0440\u0443\u0433", "\u041a\u0440\u0443\u0433", "\u041a\u043b\u0430\u0441\u0438\u0447\u0435\u0441\u043a\u0438\u0439");
    private final BooleanSetting staticCrosshair = new BooleanSetting("\u0421\u0442\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439", false);
    private float lastYaw;
    private float lastPitch;
    private float animatedYaw;
    private float animatedPitch;
    private float animation;
    private float animationSize;
    private final int outlineColor = Color.BLACK.getRGB();
    private final int entityColor = Color.RED.getRGB();

    public Crosshair() {
        this.addSettings(this.mode, this.staticCrosshair);
    }

    @Subscribe
    public void onDisplay(EventDisplay e) {
        block10: {
            block9: {
                if (Minecraft.player == null) break block9;
                if (Minecraft.world != null && e.getType() == EventDisplay.Type.POST) break block10;
            }
            return;
        }
        float x = (float)mc.getMainWindow().getScaledWidth() / 2.0f;
        float y = (float)mc.getMainWindow().getScaledHeight() / 2.0f;
        float padding = 5.0f;
        switch (this.mode.getIndex()) {
            case 0: {
                float size = 5.0f;
                int color = ColorUtils.interpolate(HUD.getColor(1), HUD.getColor(1), 1.0f - this.animation);
                if (!((Boolean)this.staticCrosshair.get()).booleanValue()) {
                    x += this.animatedYaw;
                    y += this.animatedPitch;
                }
                this.animationSize = MathUtil.fast(this.animationSize, (1.0f - Minecraft.player.getCooledAttackStrength(1.0f)) * 260.0f, 10.0f);
                float radius = 3.0f + ((Boolean)this.staticCrosshair.get() != false ? 0.0f : this.animationSize);
                if (Crosshair.mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON) break;
                DisplayUtils.drawCircle1(x, y, 0.0f, 360.0f, 5.5f, 2.1f, false, ColorUtils.getColor(90));
                DisplayUtils.drawCircle1(x, y, 0.0f, this.animationSize, 5.5f, 2.0f, false, ColorUtils.rgb(23, 21, 21));
                break;
            }
            case 1: {
                if (Crosshair.mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON) {
                    return;
                }
                float cooldown = 1.0f - Minecraft.player.getCooledAttackStrength(1.0f);
                float thickness = 1.0f;
                float length = 3.0f;
                float gap = 2.0f + 8.0f * cooldown;
                int color = Crosshair.mc.pointedEntity != null ? this.entityColor : -1;
                this.drawOutlined(x - thickness / 2.0f, y - gap - length, thickness, length, ColorUtils.getColor(90));
                this.drawOutlined(x - thickness / 2.0f, y + gap, thickness, length, ColorUtils.getColor(90));
                this.drawOutlined(x - gap - length, y - thickness / 2.0f, length, thickness, color);
                this.drawOutlined(x + gap, y - thickness / 2.0f, length, thickness, color);
            }
        }
    }

    private void drawOutlined(float x, float y, float w, float h, int hex) {
        DisplayUtils.drawRectW((double)x - 0.5, (double)y - 0.5, w + 1.0f, h + 1.0f, this.outlineColor);
        DisplayUtils.drawRectW(x, y, w, h, hex);
    }
}

