/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.events.EventDisplay;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.impl.TargetInfoRenderer;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector4f;

public class InventoryRenderer
implements ElementRenderer {
    public static ClientPlayerEntity player;
    private final Dragging dragging;
    private float width;
    private float height;

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = this.dragging.getX() + 5.0f;
        float posY = this.dragging.getY();
        int headerHeight = 45;
        float width = 180.0f;
        float containerSize = 17.0f;
        float containerGap = 2.0f;
        float localWidth = 70.0f;
        float localHeight = 70.0f;
        float height = 70.0f;
        float maxWidth = 70.0f;
        Scissor.push();
        Scissor.setFromComponentCoordinates(posX, posY - 30.0f, width, height + 20.0f);
        float headerContainerWidth = 9.0f * (containerSize + containerGap);
        float headerContainerStartX = posX;
        float headerContainerStartY = posY;
        DisplayUtils.drawRoundedRect(headerContainerStartX, headerContainerStartY - 20.0f, headerContainerWidth - 3.0f, (float)(headerHeight - 30), new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), ColorUtils.rgb(14, 12, 12));
        float textX = headerContainerStartX + headerContainerWidth / 2.0f;
        float textY = headerContainerStartY + 18.0f;
        Fonts.sfbold.drawText(ms, "Main-Inventory", textX - 35.0f, textY - 35.0f, -1, 8.0f);
        for (int i = 9; i < 36; ++i) {
            int row = (i - 9) / 9;
            int col = (i - 9) % 9;
            float x = posX + (float)col * (containerSize + containerGap);
            float y = posY + (float)row * (containerSize + containerGap);
            DisplayUtils.drawRoundedRect(x, y, containerSize, containerSize, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), ColorUtils.rgba(0, 0, 0, 120));
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.isEmpty()) continue;
            TargetInfoRenderer.drawItemStack(itemStack, x, y, true, true, 1.0f);
        }
        if (localWidth > maxWidth) {
            maxWidth = localWidth;
        }
        Scissor.unset();
        Scissor.pop();
        width = Math.max(maxWidth, 180.0f);
        height = localHeight + 10.0f;
        this.dragging.setWidth(width);
        this.dragging.setHeight(height);
    }

    public InventoryRenderer(Dragging dragging) {
        this.dragging = dragging;
    }
}

