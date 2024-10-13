/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.events.EventDisplay;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import im.expensive.utils.text.GradientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class PotionRenderer
implements ElementRenderer {
    private final Dragging dragging;
    private float width;
    private float height;
    private float animation;
    final ResourceLocation potion = new ResourceLocation("expensive/xz/potion1.png");
    final float iconSize = 10.0f;

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float fontSize = 6.5f;
        float padding = 5.0f;
        StringTextComponent name = GradientUtil.gradient("Elixirs");
        this.drawStyledRect(posX, posY + 1.0f, this.animation, 18.0f, 4.0f, 220);
        Fonts.sfui.drawText(ms, "Active Potions", posX + 20.0f, posY + 5.0f + 1.5f, ColorUtils.rgb(255, 255, 255), 6.5f);
        DisplayUtils.drawImage(this.potion, posX + 5.0f, posY + 5.0f, 10.0f, 10.0f, ColorUtils.rgb(255, 255, 255));
        DisplayUtils.drawRectVerticalW(posX + 18.0f, posY + 3.0f, this.width - 1.0f, 14.0, 3, ColorUtils.rgba(255, 255, 255, 191));
        posY += 16.5f;
        float maxWidth = Fonts.sfui.getWidth(name, 6.5f) + 10.0f;
        float localHeight = 16.5f;
        posY += 3.0f;
        for (EffectInstance ef : Minecraft.player.getActivePotionEffects()) {
            int amp = ef.getAmplifier();
            Object ampStr = "";
            if (amp >= 1 && amp <= 9) {
                ampStr = " " + I18n.format("enchantment.level." + (amp + 1), new Object[0]);
            }
            String nameText = I18n.format(ef.getEffectName(), new Object[0]) + (String)ampStr;
            float nameWidth = Fonts.sfui.getWidth(nameText, 6.5f);
            String bindText = EffectUtils.getPotionDurationString(ef, 1.0f);
            float bindWidth = Fonts.sfui.getWidth(bindText, 6.5f);
            float localWidth = nameWidth + bindWidth + 15.0f;
            this.drawStyledRect(posX, posY, this.animation, 12.0f, 3.0f, 220);
            Fonts.sfui.drawText(ms, nameText, posX + 4.0f, posY + 2.5f, ColorUtils.rgba(255, 255, 255, 255), 6.5f);
            Fonts.sfui.drawText(ms, bindText, posX + this.animation - 4.0f - bindWidth, posY + 2.5f, ColorUtils.rgba(255, 255, 255, 255), 6.5f);
            float lineXPosition = posX + this.animation - bindWidth - 8.0f;
            DisplayUtils.drawRectVerticalW(lineXPosition, posY + 2.0f, this.width - 1.0f, 8.0, 3, ColorUtils.rgba(255, 255, 255, 191));
            if (localWidth > maxWidth) {
                maxWidth = localWidth;
            }
            posY += 12.5f;
            localHeight += 11.5f;
        }
        this.animation = MathUtil.lerp(this.animation, Math.max(maxWidth, 80.0f), 10.0f);
        this.height = localHeight + 2.5f;
        this.dragging.setWidth(this.animation);
        this.dragging.setHeight(this.height);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }

    public PotionRenderer(Dragging dragging) {
        this.dragging = dragging;
    }
}

