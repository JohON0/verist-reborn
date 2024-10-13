/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.events.EventDisplay;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.impl.TargetInfoRenderer;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class ArmorRenderer
implements ElementRenderer {
    private final Dragging dragging;
    private final ResourceLocation logo = new ResourceLocation("expensive/images/cros.png");
    private float width;
    private float height;

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float maxWidth = 30.0f;
        float localWidth = 30.0f;
        float localHeight = 15.0f;
        float height1 = 66.0f;
        float width1 = 22.0f;
        Scissor.push();
        Scissor.setFromComponentCoordinates(posX - 10.0f, posY - 30.0f, this.width + 50.0f, this.height + 100.0f);
        this.drawStyledRect(posX - 2.0f, posY - 1.3f, width1 + 3.6f, height1 + 3.0f, 6.0f, 220);
        float offsetX = posX + 4.0f;
        float offsetY = posY + 0.5f;
        for (int i = 3; i >= 0; --i) {
            ItemStack armorItem = Minecraft.player.inventory.armorInventory.get(i);
            if (armorItem.getItem() == Items.AIR) continue;
            float itemX = offsetX;
            float itemY = offsetY + 16.5f * (float)(3 - i);
            TargetInfoRenderer.drawItemStack(armorItem, itemX - 1.0f, itemY, true, true, 1.0f);
            float maxDurability = armorItem.getMaxDamage();
            float currentDurability = maxDurability - (float)armorItem.getDamage();
            int armorPercentage = (int)(currentDurability / maxDurability * 100.0f);
            String armorStatus = armorPercentage + "%";
            float textWidth = ArmorRenderer.mc.fontRenderer.getStringWidth(armorStatus);
            Objects.requireNonNull(ArmorRenderer.mc.fontRenderer);
            float textHeight = 9.0f;
            this.drawStyledRect(itemX + 20.0f, itemY + 4.0f - 2.0f, textWidth + 4.0f, textHeight + 4.0f, 1.0f, 220);
            ArmorRenderer.mc.fontRenderer.drawStringWithShadow(ms, armorStatus, itemX + 23.0f, itemY + 4.0f, Color.WHITE.getRGB());
            int lineColor = ColorUtils.rgb(255 - (int)((double)armorPercentage * 2.55), (int)((double)armorPercentage * 2.55), 0);
            ArmorRenderer.drawLine(itemX - 15.0f, itemY + 5.0f, itemX - 15.0f, itemY + 2.0f + 10.0f, lineColor);
        }
        Scissor.unset();
        Scissor.pop();
        this.width = Math.max(maxWidth, 20.0f);
        this.height = localHeight + 55.0f;
        this.dragging.setWidth(this.width);
        this.dragging.setHeight(this.height);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }

    public static void drawLine(float startX, float startY, float endX, float endY, int color) {
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.ordinal(), GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.ordinal());
        GlStateManager.color4f(red, green, blue, alpha);
        GlStateManager.lineWidth(5.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(endX, endY, 0.0).color(0.0f, 1.0f, 0.0f, 0.5f).endVertex();
        tessellator.draw();
        GlStateManager.lineWidth(3.0f);
        buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(startX, startY, 0.0).color(red, green, blue, alpha).endVertex();
        buffer.pos(endX, endY, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }

    public ArmorRenderer(Dragging dragging) {
        this.dragging = dragging;
    }
}

