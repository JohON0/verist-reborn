/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.events.EventDisplay;
import im.expensive.functions.impl.render.HUD;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.ResourceLocation;

public class CoordsRenderer
implements ElementRenderer {
    @Subscribe
    public void tick() {
    }

    @Override
    public void render(EventDisplay eventDisplay) {
        float offset = 2.0f;
        float fontSize = 6.5f;
        float fontHeight = Fonts.sfbold.getHeight(6.5f);
        float posX = 2.0f;
        float y = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float stringWidth = Fonts.sfbold.getWidth("XYZ: ", 6.5f);
        float bgWidth = stringWidth + Fonts.sfbold.getWidth((int)Minecraft.player.getPosX() + ", " + (int)Minecraft.player.getPosY() + ", " + (int)Minecraft.player.getPosZ(), 6.5f) + 4.0f;
        float bgHeight = fontHeight + 5.0f;
        DisplayUtils.drawRoundedRect(0.0f, y - 6.0f, bgWidth + 17.0f, bgHeight + 3.0f, 2.0f, ColorUtils.rgb(14, 12, 12));
        ResourceLocation logo = new ResourceLocation("expensive/images/grand.png");
        Vector4i vector4i1 = new Vector4i(HUD.getColor(5), HUD.getColor(10), HUD.getColor(15), HUD.getColor(20));
        DisplayUtils.drawImage(logo, -18.0f + stringWidth, y - 4.3f, 12.0f, 12.0f, -1);
        Fonts.sfbold.drawTextWithOutline(eventDisplay.getMatrixStack(), "XYZ: ", 15.0f, y - 2.0f, -1, 6.5f, 0.05f);
        Fonts.sfbold.drawTextWithOutline(eventDisplay.getMatrixStack(), (int)Minecraft.player.getPosX() + " | " + (int)Minecraft.player.getPosY() + " | " + (int)Minecraft.player.getPosZ(), 2.0f + stringWidth + 13.0f, y - 2.0f, ColorUtils.rgb(158, 255, 185), 6.5f, 0.05f);
        this.renderFps(eventDisplay.getMatrixStack(), 2.0f, y, 6.5f);
        this.renderBPS(eventDisplay.getMatrixStack(), 2.0f, y, 6.5f);
    }

    private void renderCurrentTime(MatrixStack ms, float posX, float posY, float fontSize) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTimeString = sdf.format(new Date());
        float timeTextWidth = Fonts.sfbold.getWidth("Time: " + currentTimeString, fontSize);
        float timeTextHeight = Fonts.sfbold.getHeight(fontSize);
        float bgWidth = timeTextWidth + 13.0f;
        float bgHeight = timeTextHeight + 5.0f;
        float bottomY = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float backgroundPosY = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float backgroundPosX = (float)window.getScaledWidth() - bgWidth - 2.0f;
        float textPosX = backgroundPosX + 5.0f;
        float textPosY = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        DisplayUtils.drawRoundedRect(backgroundPosX, backgroundPosY - 3.0f, bgWidth, bgHeight, 2.0f, ColorUtils.rgb(27, 25, 25));
        Fonts.sfbold.drawTextWithOutline(ms, "Time: " + currentTimeString, textPosX, textPosY, -1, fontSize, 0.05f);
    }

    private void renderFps(MatrixStack matrixStack, float posX1, float y1, float fontSize1) {
        float offset = 2.0f;
        float fontSize = 6.5f;
        float fontHeight = Fonts.sfbold.getHeight(6.5f);
        float posX = 2.0f;
        float y = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float stringWidth = Fonts.sfbold.getWidth("XYZ: ", 6.5f);
        float bgWidth = stringWidth + Fonts.sfbold.getWidth(String.valueOf(CoordsRenderer.mc.debugFPS), 6.5f) + 4.0f;
        float bgHeight = fontHeight + 5.0f;
        float backgroundPosX = (float)window.getScaledWidth() - bgWidth - 2.0f;
        float textPosY = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float textPosX = backgroundPosX + 5.0f;
        DisplayUtils.drawRoundedRect(backgroundPosX - 10.0f, textPosY - 6.0f, bgWidth + 7.0f, bgHeight + 3.0f, 2.0f, ColorUtils.rgb(14, 12, 12));
        Minecraft mc = Minecraft.getInstance();
        Fonts.sfbold.drawTextWithOutline(matrixStack, "FPS: ", textPosX - 10.0f, textPosY - 2.0f, -1, 6.5f, 0.05f);
        Fonts.sfbold.drawTextWithOutline(matrixStack, String.valueOf(mc.getDebugFPS()), textPosX + 10.0f, y - 2.0f, ColorUtils.rgb(158, 255, 185), 6.5f, 0.05f);
    }

    private void renderBPS(MatrixStack matrixStack, float posX1, float y1, float fontSize1) {
        float offset = 2.0f;
        float fontSize = 6.5f;
        float fontHeight = Fonts.sfbold.getHeight(6.5f);
        float posX = 2.0f;
        float y = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float stringWidth = Fonts.sfbold.getWidth("BPS: ", 6.5f);
        Object[] objectArray = new Object[1];
        objectArray[0] = Math.hypot(Minecraft.player.prevPosX - Minecraft.player.getPosX(), Minecraft.player.prevPosZ - Minecraft.player.getPosZ()) * 20.0;
        float bgWidth = stringWidth + Fonts.sfbold.getWidth(String.format("%.2f", objectArray), 6.5f) + 4.0f;
        float bgHeight = fontHeight + 5.0f;
        float backgroundPosX = (float)window.getScaledWidth() - bgWidth - 2.0f;
        float textPosY = (float)window.getScaledHeight() - Fonts.sfbold.getHeight(9.0f) - (float)(CoordsRenderer.mc.currentScreen instanceof ChatScreen ? 8 * CoordsRenderer.mc.gameSettings.guiScale : 2);
        float textPosX = backgroundPosX + 5.0f;
        DisplayUtils.drawRoundedRect(backgroundPosX - 60.0f, textPosY - 6.0f, bgWidth + 7.0f, bgHeight + 3.0f, 2.0f, ColorUtils.rgb(14, 12, 12));
        Minecraft mc = Minecraft.getInstance();
        Fonts.sfbold.drawTextWithOutline(matrixStack, "BPS: ", textPosX - 60.0f, textPosY - 2.0f, -1, 6.5f, 0.05f);
        Object[] objectArray2 = new Object[1];
        objectArray2[0] = Math.hypot(Minecraft.player.prevPosX - Minecraft.player.getPosX(), Minecraft.player.prevPosZ - Minecraft.player.getPosZ()) * 20.0;
        Fonts.sfui.drawTextWithOutline(matrixStack, String.format("%.2f", objectArray2), textPosX - 41.0f, textPosY - 2.0f, ColorUtils.rgb(158, 255, 185), 6.5f, 0.05f);
    }
}

