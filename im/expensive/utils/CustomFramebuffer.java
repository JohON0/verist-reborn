/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;

public class CustomFramebuffer
extends Framebuffer {
    private boolean linear;

    public CustomFramebuffer(boolean useDepthIn) {
        super(1, 1, useDepthIn, Minecraft.IS_RUNNING_ON_MAC);
    }

    private static void resizeFramebuffer(Framebuffer framebuffer) {
        Minecraft mc = Minecraft.getInstance();
        if (framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()) {
            framebuffer.createBuffers(Math.max(mc.getMainWindow().getWidth(), 1), Math.max(mc.getMainWindow().getHeight(), 1), Minecraft.IS_RUNNING_ON_MAC);
        }
    }

    public static void drawTexture() {
        Minecraft mc = Minecraft.getInstance();
        MainWindow sr = mc.getMainWindow();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float width = sr.getScaledWidth();
        float height = sr.getScaledHeight();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(0.0, 0.0, 0.0).tex(0.0f, 1.0f).endVertex();
        bufferBuilder.pos(0.0, height, 0.0).tex(0.0f, 0.0f).endVertex();
        bufferBuilder.pos(width, height, 0.0).tex(1.0f, 0.0f).endVertex();
        bufferBuilder.pos(width, 0.0, 0.0).tex(1.0f, 1.0f).endVertex();
        tessellator.draw();
    }

    public static void drawTexture(int color) {
        Minecraft mc = Minecraft.getInstance();
        MainWindow sr = mc.getMainWindow();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float width = sr.getScaledWidth();
        float height = sr.getScaledHeight();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        bufferBuilder.pos(0.0, 0.0, 0.0).color(color).tex(0.0f, 1.0f).endVertex();
        bufferBuilder.pos(0.0, height, 0.0).color(color).tex(0.0f, 0.0f).endVertex();
        bufferBuilder.pos(width, height, 0.0).color(color).tex(1.0f, 0.0f).endVertex();
        bufferBuilder.pos(width, 0.0, 0.0).color(color).tex(1.0f, 1.0f).endVertex();
        tessellator.draw();
    }

    public CustomFramebuffer setLinear() {
        this.linear = true;
        return this;
    }

    @Override
    public void setFramebufferFilter(int framebufferFilterIn) {
        super.setFramebufferFilter(this.linear ? 9729 : framebufferFilterIn);
    }

    public void setup() {
        CustomFramebuffer.resizeFramebuffer(this);
        this.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
        this.bindFramebuffer(false);
    }

    public void draw() {
        this.bindFramebufferTexture();
        CustomFramebuffer.drawTexture();
    }

    public void draw(int color) {
        this.bindFramebufferTexture();
        CustomFramebuffer.drawTexture(color);
    }

    public void draw(Framebuffer bFramebuffer) {
        bFramebuffer.bindFramebufferTexture();
        CustomFramebuffer.drawTexture();
    }
}

