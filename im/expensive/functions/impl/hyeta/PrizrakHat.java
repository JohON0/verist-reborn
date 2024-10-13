/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.events.EventUpdate;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.impl.DecelerateAnimation;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="PrizrakHat", type=Category.Render)
public class PrizrakHat
extends Function {
    private final Animation alpha = new DecelerateAnimation(1000, 255.0);
    private LivingEntity currentTarget;
    private double speed;
    private long lastTime = System.currentTimeMillis();
    private LivingEntity target;

    @Subscribe
    private void onUpdate(EventUpdate eventUpdate) {
    }

    @Subscribe
    private void onWorldEvent(WorldEvent e) {
        MatrixStack stack = new MatrixStack();
        EntityRendererManager rm = mc.getRenderManager();
        float c = (float)((double)((float)(System.currentTimeMillis() - this.lastTime) / 2000.0f) + Math.sin((float)(System.currentTimeMillis() - this.lastTime) / 1500.0f) / 10.0);
        double x = MathUtil.interpolate(mc.getRenderPartialTicks(), Minecraft.player.lastTickPosX, Minecraft.player.getPosX());
        double y = MathUtil.interpolate(mc.getRenderPartialTicks(), Minecraft.player.lastTickPosY, Minecraft.player.getPosY()) + (double)(Minecraft.player.getHeight() / 1.35f);
        double z = MathUtil.interpolate(mc.getRenderPartialTicks(), Minecraft.player.lastTickPosZ, Minecraft.player.getPosZ());
        x -= rm.info.getProjectedView().getX();
        y -= rm.info.getProjectedView().getY();
        z -= rm.info.getProjectedView().getZ();
        float alpha = Shaders.shaderPackLoaded ? 1.0f : 0.5f;
        alpha = (float)((double)alpha * 0.3);
        float pl = 0.0f;
        boolean fa = true;
        for (int b = 0; b < 2; ++b) {
            for (float i = c * 360.0f; i < c * 360.0f + 70.0f; i += 4.0f) {
                float cur = i;
                float min2 = c * 360.0f;
                float max2 = c * 360.0f + 140.0f;
                float dc = MathHelper.normalize(cur, c * 360.0f - 70.0f, max2);
                float degrees = i;
                int color = HUD.getColor(0);
                int color2 = HUD.getColor(90);
                if (PrizrakHat.mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                    color -= ColorUtils.rgb(0, 0, 0);
                    color2 -= ColorUtils.rgb(0, 0, 0);
                }
                float rf = 0.4f;
                double radians = Math.toRadians(degrees);
                double plY = (double)pl + Math.sin(radians * 1.0) * (double)0.07f;
                stack.push();
                stack.translate(x, y, z);
                stack.rotate(PrizrakHat.mc.getRenderManager().info.getRotation());
                GlStateManager.depthMask(false);
                float q = (!fa ? 0.15f : 0.15f) * (Math.max(fa ? 0.15f : 0.15f, fa ? dc : (1.0f - -(0.4f - dc)) / 2.0f) + 0.45f);
                float w = q * (1.7f + (0.5f - alpha) * 2.0f);
                DisplayUtils.drawtargetespimage(stack, new ResourceLocation("expensive/images/glow.png"), Math.cos(radians) * (double)rf - (double)(w / 2.0f), plY + 0.3, Math.sin(radians) * (double)rf - (double)(w / 2.0f), w, w, color, color2, color2, color);
                GL11.glEnable(2929);
                GlStateManager.depthMask(true);
                stack.pop();
            }
            c *= -1.0f;
            fa = !fa;
            pl += 0.0f;
        }
    }
}

