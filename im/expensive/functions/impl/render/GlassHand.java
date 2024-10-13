/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.CustomFramebuffer;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.KawaseBlur;
import im.expensive.utils.shader.impl.Outline;
import net.minecraft.client.settings.PointOfView;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="Glass Hand", type=Category.Render)
public class GlassHand
extends Function {
    public CustomFramebuffer hands = new CustomFramebuffer(false).setLinear();
    public CustomFramebuffer mask = new CustomFramebuffer(false).setLinear();

    @Subscribe
    public void onRender(EventDisplay e) {
        if (e.getType() != EventDisplay.Type.HIGH) {
            return;
        }
        if (GlassHand.mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
            KawaseBlur.blur.updateBlur(3.0f, 4);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.enableAlphaTest();
            ColorUtils.setColor(ColorUtils.getColor(90));
            KawaseBlur.blur.render(() -> this.hands.draw());
            Outline.registerRenderCall(() -> this.hands.draw());
            GlStateManager.disableAlphaTest();
            GlStateManager.popMatrix();
        }
    }

    public static void setSaturation(float saturation) {
        float[] saturationMatrix = new float[]{0.3086f * (1.0f - saturation) + saturation, 0.6094f * (1.0f - saturation), 0.082f * (1.0f - saturation), 0.0f, 0.0f, 0.3086f * (1.0f - saturation), 0.6094f * (1.0f - saturation) + saturation, 0.082f * (1.0f - saturation), 0.0f, 0.0f, 0.3086f * (1.0f - saturation), 0.6094f * (1.0f - saturation), 0.082f * (1.0f - saturation) + saturation, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        GL11.glLoadMatrixf(saturationMatrix);
    }
}

