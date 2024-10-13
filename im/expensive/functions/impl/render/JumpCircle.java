/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.events.JumpEvent;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.render.ColorUtils;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

@FunctionRegister(name="JumpCircle", type=Category.Render)
public class JumpCircle
extends Function {
    private final CopyOnWriteArrayList<Circle> circles = new CopyOnWriteArrayList();
    private final SliderSetting radiusSlider = new SliderSetting("Radius", 1.0f, 1.0f, 4.0f, 1.0f);
    private final ResourceLocation circle = new ResourceLocation("expensive/images/circle.png");

    public JumpCircle() {
        this.addSettings(this.radiusSlider);
    }

    @Subscribe
    private void onJump(JumpEvent e) {
        this.circles.add(new Circle(Minecraft.player.getPositon(mc.getRenderPartialTicks()).add(0.0, 0.05, 0.0)));
    }

    @Subscribe
    private void onRender(WorldEvent e) {
        GlStateManager.pushMatrix();
        GlStateManager.shadeModel(7425);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableCull();
        GlStateManager.translated(-JumpCircle.mc.getRenderManager().info.getProjectedView().x, -JumpCircle.mc.getRenderManager().info.getProjectedView().y, -JumpCircle.mc.getRenderManager().info.getProjectedView().z);
        for (Circle c : this.circles) {
            mc.getTextureManager().bindTexture(this.circle);
            if (System.currentTimeMillis() - c.time > 2000L) {
                this.circles.remove(c);
            }
            if (System.currentTimeMillis() - c.time > 1500L && !c.isBack) {
                c.animation.animate(0.0, 0.5, Easings.CUBIC_OUT);
                c.isBack = true;
            }
            c.animation.update();
            float rad2 = (float)c.animation.getValue() * (float)((Float)this.radiusSlider.get()).intValue();
            Vector3d vector3d = c.vector3d;
            vector3d = vector3d.add(-rad2 / 2.0f, 0.0, -rad2 / 2.0f);
            buffer.begin(6, DefaultVertexFormats.POSITION_COLOR_TEX);
            int alpha = (int)(255.0f * MathHelper.clamp(rad2, 0.0f, 1.0f));
            buffer.pos(vector3d.x, vector3d.y, vector3d.z).color(ColorUtils.setAlpha(ColorUtils.getColor(5), alpha)).tex(0.0f, 0.0f).endVertex();
            buffer.pos(vector3d.x + (double)rad2, vector3d.y, vector3d.z).color(ColorUtils.setAlpha(ColorUtils.getColor(10), alpha)).tex(1.0f, 0.0f).endVertex();
            buffer.pos(vector3d.x + (double)rad2, vector3d.y, vector3d.z + (double)rad2).color(ColorUtils.setAlpha(ColorUtils.getColor(15), alpha)).tex(1.0f, 1.0f).endVertex();
            buffer.pos(vector3d.x, vector3d.y, vector3d.z + (double)rad2).color(ColorUtils.setAlpha(ColorUtils.getColor(20), alpha)).tex(0.0f, 1.0f).endVertex();
            tessellator.draw();
        }
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableAlphaTest();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private class Circle {
        private final Vector3d vector3d;
        private final long time;
        private final Animation animation = new Animation();
        private boolean isBack;

        public Circle(Vector3d vector3d) {
            this.vector3d = vector3d;
            this.time = System.currentTimeMillis();
            this.animation.animate(1.0, 0.5, Easings.SINE_OUT);
        }
    }
}

