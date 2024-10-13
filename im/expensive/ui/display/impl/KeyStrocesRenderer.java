/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.util.math.vector.Vector4f;

public class KeyStrocesRenderer
implements ElementRenderer {
    private final Dragging dragging;
    private float width = 20.0f;
    private float height = 20.0f;

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        int firstColor = ColorUtils.getColor(90);
        int secondColor = ColorUtils.getColor(180);
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float fontSize = 9.5f;
        float padding = 8.0f;
        float width = 20.0f;
        float height = 20.0f;
        float maxWidth = 15.0f;
        float localWidth = 15.0f;
        float localHeight = 15.0f;
        Scissor.push();
        String w = "W";
        String a = "A";
        String s = "S";
        String d = "D";
        String inv = "-  ";
        DisplayUtils.drawRoundedRect(posX, posY, width, height, 4.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfMedium.drawText(ms, w, posX + 4.0f, posY + 6.0f, -1, fontSize);
        DisplayUtils.drawRoundedRect(posX, posY + 23.0f, width, height, 4.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfMedium.drawText(ms, s, posX + 6.0f, posY + 28.0f, -1, fontSize);
        DisplayUtils.drawRoundedRect(posX - 23.0f, posY + 23.0f, width, height, 4.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfMedium.drawText(ms, a, posX - 18.0f, posY + 28.0f, -1, fontSize + 1.0f);
        DisplayUtils.drawRoundedRect(posX + 23.0f, posY + 23.0f, width, height, 4.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfMedium.drawText(ms, d, posX + 29.0f, posY + 28.0f, -1, fontSize);
        DisplayUtils.drawRoundedRect(posX - 23.0f, posY + 45.0f, width + 47.0f, height, 4.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfMedium.drawText(ms, inv, posX - 12.0f, posY + 10.0f, -1, fontSize + 60.0f);
        if (KeyStrocesRenderer.mc.gameSettings.keyBindForward.isKeyDown()) {
            DisplayUtils.drawRoundedRect(posX, posY, width, height, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(800), ColorUtils.getColor(1600), ColorUtils.getColor(2700)));
            Fonts.sfMedium.drawText(ms, w, posX + 4.0f, posY + 6.0f, ColorUtils.rgba(255, 255, 255, 255), fontSize);
            DisplayUtils.drawRoundedRect(posX, posY, width, height, 4.0f, ColorUtils.setAlpha(ColorUtils.rgba(255, 255, 255, 255), 34));
        }
        if (KeyStrocesRenderer.mc.gameSettings.keyBindBack.isKeyDown()) {
            DisplayUtils.drawRoundedRect(posX, posY + 23.0f, width, height, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(800), ColorUtils.getColor(1600), ColorUtils.getColor(2700)));
            Fonts.sfMedium.drawText(ms, s, posX + 6.0f, posY + 28.0f, ColorUtils.rgba(255, 255, 255, 255), fontSize);
        }
        if (KeyStrocesRenderer.mc.gameSettings.keyBindLeft.isKeyDown()) {
            DisplayUtils.drawRoundedRect(posX - 23.0f, posY + 23.0f, width, height, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(800), ColorUtils.getColor(1600), ColorUtils.getColor(2700)));
            Fonts.sfMedium.drawText(ms, a, posX - 18.0f, posY + 28.0f, ColorUtils.rgba(255, 255, 255, 255), fontSize + 1.0f);
        }
        if (KeyStrocesRenderer.mc.gameSettings.keyBindRight.isKeyDown()) {
            DisplayUtils.drawRoundedRect(posX + 23.0f, posY + 23.0f, width, height, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(800), ColorUtils.getColor(1600), ColorUtils.getColor(2700)));
            Fonts.sfMedium.drawText(ms, d, posX + 29.0f, posY + 28.0f, ColorUtils.rgba(255, 255, 255, 255), fontSize);
        }
        if (KeyStrocesRenderer.mc.gameSettings.keyBindJump.isKeyDown()) {
            DisplayUtils.drawRoundedRect(posX - 23.0f, posY + 45.0f, width + 47.0f, height, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(800), ColorUtils.getColor(1600), ColorUtils.getColor(2700)));
            Fonts.sfMedium.drawText(ms, inv, posX - 12.0f, posY + 10.0f, ColorUtils.rgba(255, 255, 255, 255), fontSize + 60.0f);
        }
        if (localWidth > maxWidth) {
            maxWidth = localWidth;
        }
        Scissor.unset();
        Scissor.pop();
        width = Math.max(maxWidth, 50.0f);
        height = localHeight + 40.5f;
        this.dragging.setWidth(width);
        this.dragging.setHeight(height);
    }

    public void update(EventUpdate e) {
    }

    public KeyStrocesRenderer(Dragging dragging) {
        this.dragging = dragging;
    }
}

