/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.utils.client.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class PingUtil
implements IMinecraft {
    public static String calculateBPS() {
        Object[] objectArray = new Object[1];
        objectArray[0] = Math.hypot(Minecraft.player.getPosX() - Minecraft.player.prevPosX, Minecraft.player.getPosZ() - Minecraft.player.prevPosZ) * (double)PingUtil.mc.timer.timerSpeed * 20.0;
        return String.format("%.2f", objectArray);
    }

    public static void drawItemStack(ItemStack stack, float x, float y, boolean withoutOverlay, boolean scale, float scaleValue) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0.0f);
        if (scale) {
            GL11.glScaled(scaleValue, scaleValue, scaleValue);
        }
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
        if (withoutOverlay) {
            mc.getItemRenderer().renderItemOverlays(PingUtil.mc.fontRenderer, stack, 0, 0);
        }
        RenderSystem.popMatrix();
    }

    public static int calculatePing() {
        return Minecraft.player.connection.getPlayerInfo(Minecraft.player.getUniqueID()) != null ? Minecraft.player.connection.getPlayerInfo(Minecraft.player.getUniqueID()).getResponseTime() : 0;
    }

    public static String serverIP() {
        return mc.getCurrentServerData() != null && PingUtil.mc.getCurrentServerData().serverIP != null && !mc.isSingleplayer() ? PingUtil.mc.getCurrentServerData().serverIP : "";
    }
}

