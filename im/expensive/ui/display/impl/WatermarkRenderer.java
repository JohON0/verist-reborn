/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.styles.Style;
import im.expensive.utils.PingUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class WatermarkRenderer
implements ElementRenderer {
    private final Minecraft mc = Minecraft.getInstance();
    private final ResourceLocation logo = new ResourceLocation("expensive/xz/logo.png");
    private final ResourceLocation fpsIcon = new ResourceLocation("expensive/xz/fps.png");
    private final ResourceLocation timeIcon = new ResourceLocation("expensive/xz/time.png");
    private final ResourceLocation userIcon = new ResourceLocation("expensive/xz/user.png");
    private final ResourceLocation compassIcon = new ResourceLocation("expensive/xz/compass.png");
    private final ResourceLocation internetIcon = new ResourceLocation("expensive/xz/internet.png");
    private final ResourceLocation bpsIcon = new ResourceLocation("expensive/xz/bps.png");

    @Override
    public void render(EventDisplay var1) {
        MatrixStack var2 = var1.getMatrixStack();
        float var3 = 4.0f;
        float var4 = 4.0f;
        float var5 = 5.0f;
        float var6 = 6.5f;
        float var7 = 10.0f;
        float var8 = 80.0f;
        Style var9 = Expensive.getInstance().getStyleManager().getCurrentStyle();
        String var10 = "Verist Client";
        float var11 = Fonts.sfui.getWidth(var10, var6);
        DisplayUtils.drawShadow(var3, var4, var7 + var5 * 2.5f + var11, var7 + var5 * 2.0f, 15, ColorUtils.rgba(21, 24, 40, 165));
        this.drawStyledRect(var3, var4, var7 + var5 * 2.5f + var11, var7 + var5 * 2.0f, 4.0f, 220);
        DisplayUtils.drawImage(this.logo, var3 + var5, var4 + var5 - 1.0f, var7, var7, ColorUtils.getColor(180));
        Fonts.sfui.drawText(var2, var10, var3 + var7 + var5 * 1.5f, var4 + var7 / 2.0f + 1.5f, ColorUtils.getColor(5), var6);
        String var20 = Minecraft.player.getName().getString();
        float var21 = Fonts.sfui.getWidth(var20, var6);
        float var22 = var3 + var7 + var5 * 2.5f + var11 + var5;
        DisplayUtils.drawShadow(var22, var4, var7 + var5 * 2.5f + var21, var7 + var5 * 2.0f, 15, ColorUtils.rgba(21, 24, 40, 165));
        this.drawStyledRect(var22, var4, var7 + var5 * 2.5f + var21, var7 + var5 * 2.0f, 4.0f, 220);
        DisplayUtils.drawImage(this.userIcon, var22 + var5, var4 + var5, var7, var7, ColorUtils.getColor(5));
        Fonts.sfui.drawText(var2, var20, var22 + var5 * 1.5f + var7, var4 + var7 / 2.0f + 1.5f, ColorUtils.rgb(255, 255, 255), var6);
        int var12 = this.mc.getDebugFPS();
        String var13 = String.valueOf(var12) + " Fps";
        float var14 = Fonts.sfui.getWidth(var13, var6);
        float var15 = var22 + var7 + var5 * 2.5f + var21 + var5;
        DisplayUtils.drawShadow(var15, var4, var7 + var5 * 2.5f + var14, var7 + var5 * 2.0f, 15, ColorUtils.rgba(21, 24, 40, 165));
        this.drawStyledRect(var15, var4, var7 + var5 * 2.5f + var14, var7 + var5 * 2.0f, 4.0f, 220);
        DisplayUtils.drawImage(this.fpsIcon, var15 + var5, var4 + var5, var7, var7, ColorUtils.getColor(5));
        Fonts.sfui.drawText(var2, var13, var15 + var7 + var5 * 1.5f, var4 + var7 / 2.0f + 1.5f, ColorUtils.rgb(255, 255, 255), var6);
        float var24 = var4 + var7 + var5 * 2.0f + var5;
        int var10000 = (int)Minecraft.player.getPosX();
        String var25 = var10000 + " " + (int)Minecraft.player.getPosY() + " " + (int)Minecraft.player.getPosZ();
        float var26 = Fonts.sfui.getWidth(var25, var6);
        DisplayUtils.drawShadow(var3, var24, var7 + var5 * 2.5f + var26, var7 + var5 * 2.0f, 15, ColorUtils.rgba(21, 24, 40, 165));
        this.drawStyledRect(var3, var24, var7 + var5 * 2.5f + var26, var7 + var5 * 2.0f, 4.0f, 220);
        DisplayUtils.drawImage(this.compassIcon, var3 + var5, var24 + var5, var7, var7, ColorUtils.getColor(5));
        Fonts.sfui.drawText(var2, var25, var3 + var5 * 1.5f + var7, var24 + var7 / 2.0f + 1.5f, ColorUtils.rgb(255, 255, 255), var6);
        String var29 = String.valueOf(PingUtil.calculatePing()) + " ms";
        float var30 = Fonts.sfui.getWidth(var29, var6);
        float var31 = var3 + var7 + var5 * 2.5f + var26 + var5;
        DisplayUtils.drawShadow(var31, var24, var7 + var5 * 2.5f + var30, var7 + var5 * 2.0f, 15, ColorUtils.rgba(21, 24, 40, 165));
        this.drawStyledRect(var31, var24, var7 + var5 * 2.5f + var30, var7 + var5 * 2.0f, 4.0f, 220);
        DisplayUtils.drawImage(this.internetIcon, var31 + var5, var24 + var5, var7, var7, ColorUtils.getColor(5));
        Fonts.sfui.drawText(var2, var29, var31 + var5 * 1.5f + var7, var24 + var7 / 2.0f + 1.5f, ColorUtils.rgb(255, 255, 255), var6);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }
}

