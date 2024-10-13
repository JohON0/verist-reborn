/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Particle {
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private float scale;
    private boolean isSliding = false;
    private float alpha;
    private final ResourceLocation texture;
    private final int lifetime;
    private int age;
    private int fadeColor;
    private int color;

    public Particle(float x, float y, float velocityX, float velocityY, float scale, ResourceLocation texture, int lifetime, int color) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.scale = scale;
        this.texture = texture;
        this.lifetime = lifetime;
        this.alpha = 1.0f;
        this.age = 0;
        this.color = color;
    }

    public void setFadeColor(int color) {
        this.fadeColor = color;
    }

    public boolean isAlive() {
        return this.age < this.lifetime;
    }

    public void setSliding(boolean isSliding) {
        this.isSliding = isSliding;
    }

    public void update() {
        if (this.isSliding) {
            this.velocityX = (float)((double)this.velocityX * 0.98);
            this.velocityY = (float)((double)this.velocityY * 0.98);
        }
        this.x += this.velocityX;
        this.y += this.velocityY;
        ++this.age;
        this.alpha = Math.max(0.0f, 1.0f - (float)this.age / (float)this.lifetime);
    }

    public void render() {
        if (!this.isAlive()) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.x, this.y, 0.0f);
        GlStateManager.scalef(this.scale, this.scale, this.scale);
        Minecraft.getInstance().getTextureManager().bindTexture(this.texture);
        float red = (float)(this.color >> 16 & 0xFF) / 255.0f;
        float green = (float)(this.color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(this.color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, this.alpha);
        this.renderTexturedQuad(0, 0, 16, 16);
        GlStateManager.popMatrix();
    }

    private void renderTexturedQuad(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0).tex(0.0f, 1.0f).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 1.0f).endVertex();
        buffer.pos(x + width, y, 0.0).tex(1.0f, 0.0f).endVertex();
        buffer.pos(x, y, 0.0).tex(0.0f, 0.0f).endVertex();
        tessellator.draw();
    }
}

