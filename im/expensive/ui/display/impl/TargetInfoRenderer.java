/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.styles.Style;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.Direction;
import im.expensive.utils.animations.impl.EaseBackIn;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.font.Font;
import im.expensive.utils.render.font.Fonts;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.opengl.GL11;

public class TargetInfoRenderer
implements ElementRenderer {
    private final StopWatch stopWatch = new StopWatch();
    private final Dragging drag;
    private LivingEntity entity = null;
    private boolean allow;
    private final Animation animation = new EaseBackIn(400, 1.0, 1.0f);
    private float healthAnimation = 0.0f;
    private float absorptionAnimation = 0.0f;

    @Override
    public void render(EventDisplay eventDisplay) {
        this.entity = this.getTarget(this.entity);
        float rounding = 6.0f;
        boolean out = !this.allow || this.stopWatch.isReached(1000L);
        this.animation.setDuration(out ? 400 : 300);
        this.animation.setDirection(out ? Direction.BACKWARDS : Direction.FORWARDS);
        if (this.animation.getOutput() == 0.0) {
            this.entity = null;
        }
        if (this.entity != null) {
            Color currentColor;
            String header;
            String name = this.entity.getName().getString();
            float posX = this.drag.getX();
            float posY = this.drag.getY();
            float headSize = 33.0f;
            float spacing = 5.0f;
            float width = 114.666664f;
            float height = 39.333332f;
            this.drag.setWidth(width);
            this.drag.setHeight(height);
            Scoreboard var28 = Minecraft.world.getScoreboard();
            String var10001 = this.entity.getScoreboardName();
            Score score = var28.getOrCreateScore(var10001, Minecraft.world.getScoreboard().getObjectiveInDisplaySlot(2));
            float hp = this.entity.getHealth();
            float maxHp = this.entity.getMaxHealth();
            String string = header = Minecraft.getInstance().ingameGUI.getTabList().header == null ? " " : Minecraft.getInstance().ingameGUI.getTabList().header.getString().toLowerCase();
            if (Minecraft.getInstance().getCurrentServerData() != null && Minecraft.getInstance().getCurrentServerData().serverIP.contains("funtime") && (header.contains("\u0430\u043d\u0430\u0440\u0445\u0438\u044f") || header.contains("\u0433\u0440\u0438\u0444\u0435\u0440\u0441\u043a\u0438\u0439")) && this.entity instanceof PlayerEntity) {
                hp = score.getScorePoints();
                maxHp = 20.0f;
            }
            this.healthAnimation = MathUtil.fast(this.healthAnimation, MathHelper.clamp(hp / maxHp, 0.0f, 1.0f), 10.0f);
            this.absorptionAnimation = MathUtil.fast(this.absorptionAnimation, MathHelper.clamp(this.entity.getAbsorptionAmount() / maxHp, 0.0f, 1.0f), 10.0f);
            float animationValue = (float)this.animation.getOutput();
            float halfAnimationValueRest = (1.0f - animationValue) / 2.0f;
            float testX = posX + width * halfAnimationValueRest;
            float testY = posY + height * halfAnimationValueRest;
            float testW = width * animationValue;
            float testH = height * animationValue;
            GlStateManager.pushMatrix();
            Style style = Expensive.getInstance().getStyleManager().getCurrentStyle();
            TargetInfoRenderer.sizeAnimation(posX + width / 2.0f, posY + height / 2.0f, this.animation.getOutput());
            this.drawStyledRect(posX, posY, width, height, rounding, 220);
            DisplayUtils.drawRoundedRect(posX + spacing - 1.9f, posY + spacing - 1.5f, headSize, headSize, 6.0f, style.getSecondColor().getRGB());
            this.drawTargetHead(this.entity, posX + spacing - 1.9f, posY + spacing - 1.5f, headSize, headSize);
            Scissor.push();
            Scissor.setFromComponentCoordinates(testX, testY, testW - 6.0f, testH);
            Fonts.sfui.drawText(eventDisplay.getMatrixStack(), this.entity.getName().getString(), posX + headSize + spacing + spacing - 2.0f, posY + spacing + 1.0f, -1, 8.0f);
            Font var29 = Fonts.sfMedium;
            MatrixStack var30 = eventDisplay.getMatrixStack();
            int var31 = (int)hp;
            var29.drawText(var30, "HP: " + (var31 + (int)Minecraft.player.getAbsorptionAmount()), posX + headSize + spacing + spacing - 2.0f, posY + spacing + 1.0f + spacing + spacing, ColorUtils.rgb(200, 200, 200), 7.0f);
            Scissor.unset();
            Scissor.pop();
            Color greenColor = new Color(0, 153, 76);
            Color yellowColor = new Color(204, 204, 0);
            Color redColor = new Color(204, 0, 0);
            float healthPercentage = hp / maxHp;
            if (healthPercentage >= 0.5f) {
                float ratio = (healthPercentage - 0.5f) * 2.0f;
                currentColor = TargetInfoRenderer.blendColors(yellowColor, greenColor, ratio);
            } else {
                float ratio = healthPercentage * 2.0f;
                currentColor = TargetInfoRenderer.blendColors(redColor, yellowColor, ratio);
            }
            Vector4i vector4i = new Vector4i(currentColor.getRGB(), currentColor.getRGB(), currentColor.getRGB(), currentColor.getRGB());
            DisplayUtils.drawRoundedRect(posX + headSize + spacing + spacing - 3.0f, posY + height - spacing * 2.0f - 3.0f, width - 42.0f, 7.0f, new Vector4f(4.0f, 4.0f, 4.0f, 4.0f), ColorUtils.rgb(32, 32, 32));
            DisplayUtils.drawRoundedRect(posX + headSize + spacing + spacing - 3.0f, posY + height - spacing * 2.0f - 3.0f, (width - 42.0f) * this.healthAnimation, 7.0f, new Vector4f(4.0f, 4.0f, 4.0f, 4.0f), vector4i);
            GlStateManager.popMatrix();
        }
    }

    private LivingEntity getTarget(LivingEntity nullTarget) {
        LivingEntity auraTarget = Expensive.getInstance().getFunctionRegistry().getKillAura().getTarget();
        LivingEntity target = nullTarget;
        if (auraTarget != null) {
            this.stopWatch.reset();
            this.allow = true;
            target = auraTarget;
        } else if (TargetInfoRenderer.mc.currentScreen instanceof ChatScreen) {
            this.stopWatch.reset();
            this.allow = true;
            Minecraft var10000 = mc;
            target = Minecraft.player;
        } else {
            this.allow = false;
        }
        return target;
    }

    public void drawTargetHead(LivingEntity entity2, float x, float y, float width, float height) {
        if (entity2 != null) {
            EntityRenderer<LivingEntity> rendererManager = mc.getRenderManager().getRenderer(entity2);
            ResourceLocation texture = rendererManager.getEntityTexture(entity2);
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            mc.getTextureManager().bindTexture(texture);
            this.applyRoundedScissor(x, y, width, height, 4.0f);
            AbstractGui.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8.0f, 8.0f, width, height, 64.0f, 64.0f);
            Scissor.unset();
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }

    public void applyRoundedScissor(float x, float y, float width, float height, float radius) {
        Scissor.push();
        Scissor.setFromComponentCoordinates(x, y, width, height);
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgb(255, 255, 255));
        Scissor.pop();
    }

    public static void sizeAnimation(double width, double height, double scale) {
        GlStateManager.translated(width, height, 0.0);
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-width, -height, 0.0);
    }

    public void drawFace(ResourceLocation res, float d, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight, LivingEntity target) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        mc.getTextureManager().bindTexture(res);
        float hurtPercent = ((float)target.hurtTime - (target.hurtTime != 0 ? TargetInfoRenderer.mc.timer.renderPartialTicks : 0.0f)) / 10.0f;
        GL11.glColor4f(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
        AbstractGui.drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }

    public static void drawItemStack(ItemStack stack, float x, float y, boolean withoutOverlay, boolean scale, float scaleValue) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0.0f);
        if (scale) {
            GL11.glScaled(scaleValue, scaleValue, scaleValue);
        }
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (withoutOverlay) {
            mc.getItemRenderer().renderItemOverlays(TargetInfoRenderer.mc.fontRenderer, stack, 0, 0);
        }
        RenderSystem.popMatrix();
    }

    public static Color blendColors(Color color1, Color color2, float ratio) {
        float inverseRatio = 1.0f - ratio;
        float r = (float)color1.getRed() * inverseRatio + (float)color2.getRed() * ratio;
        float g = (float)color1.getGreen() * inverseRatio + (float)color2.getGreen() * ratio;
        float b = (float)color1.getBlue() * inverseRatio + (float)color2.getBlue() * ratio;
        return new Color((int)r, (int)g, (int)b);
    }

    public TargetInfoRenderer(Dragging drag) {
        this.drag = drag;
    }
}

