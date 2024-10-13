/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.render;

import com.jhlabs.image.GaussianFilter;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.ui.styles.Style;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.shader.ShaderHandler;
import im.expensive.utils.shader.ShaderUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.optifine.util.TextureUtils;
import org.lwjgl.opengl.GL11;

public class DisplayUtils
implements IMinecraft {
    private static final HashMap<Integer, Integer> shadowCache = new HashMap();
    public static ShaderHandler shaderMainMenu = new ShaderHandler("shaderMainMenu");
    private static Framebuffer whiteCache = new Framebuffer(1, 1, false, true);
    private static Framebuffer contrastCache = new Framebuffer(1, 1, false, true);

    public static void quads(float x, float y, float width, float height, int glQuads, int color) {
        buffer.begin(glQuads, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(x, y, 0.0).tex(0.0f, 0.0f).color(color).endVertex();
        buffer.pos(x, y + height, 0.0).tex(0.0f, 1.0f).color(color).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 1.0f).color(color).endVertex();
        buffer.pos(x + width, y, 0.0).tex(1.0f, 0.0f).color(color).endVertex();
        tessellator.draw();
    }

    public static void drawImageAlpha(ResourceLocation resourceLocation, float x, float y, float width, float height, Vector4i color) {
        RenderSystem.pushMatrix();
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(770, 1, 0, 1);
        mc.getTextureManager().bindTexture(resourceLocation);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(x, y, 0.0).tex(0.0f, 0.99f).lightmap(0, 240).color(color.x).endVertex();
        buffer.pos(x, y + height, 0.0).tex(1.0f, 0.99f).lightmap(0, 240).color(color.y).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 0.0f).lightmap(0, 240).color(color.z).endVertex();
        buffer.pos(x + width, y, 0.0).tex(0.0f, 0.0f).lightmap(0, 240).color(color.w).endVertex();
        tessellator.draw();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableAlphaTest();
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
    }

    public static void drawImageAlpha1(ResourceLocation resourceLocation, float x, float y, float width, float height, Vector4i color) {
        RenderSystem.pushMatrix();
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(770, 1, 0, 1);
        mc.getTextureManager().bindTexture(resourceLocation);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(x, y, 0.0).tex(0.0f, 0.0f).lightmap(0, 120).color(color.x).endVertex();
        buffer.pos(x, y + height, 0.0).tex(0.0f, 0.99f).lightmap(0, 120).color(color.y).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 0.99f).lightmap(0, 120).color(color.z).endVertex();
        buffer.pos(x + width, y, 0.0).tex(1.0f, 0.0f).lightmap(0, 120).color(color.w).endVertex();
        tessellator.draw();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableAlphaTest();
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
    }

    public static boolean isInRegion(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static boolean isInRegion(double mouseX, double mouseY, float x, float y, float width, float height) {
        return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
    }

    public static boolean isInRegion(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
    }

    public static void drawtargetespimage(MatrixStack stack, ResourceLocation image, double x, double y, double z, double width, double height, int color1, int color2, int color3, int color4) {
        Minecraft minecraft = Minecraft.getInstance();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        GL11.glShadeModel(7425);
        GL11.glAlphaFunc(516, 0.0f);
        minecraft.getTextureManager().bindTexture(image);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        bufferBuilder.pos(stack.getLast().getMatrix(), (float)x, (float)(y + height), (float)z).color(color1 >> 16 & 0xFF, color1 >> 8 & 0xFF, color1 & 0xFF, color1 >>> 24).tex(0.0f, 0.99f).lightmap(0, 240).endVertex();
        bufferBuilder.pos(stack.getLast().getMatrix(), (float)(x + width), (float)(y + height), (float)z).color(color2 >> 16 & 0xFF, color2 >> 8 & 0xFF, color2 & 0xFF, color2 >>> 24).tex(1.0f, 0.99f).lightmap(0, 240).endVertex();
        bufferBuilder.pos(stack.getLast().getMatrix(), (float)(x + width), (float)y, (float)z).color(color3 >> 16 & 0xFF, color3 >> 8 & 0xFF, color3 & 0xFF, color3 >>> 24).tex(1.0f, 0.0f).lightmap(0, 240).endVertex();
        bufferBuilder.pos(stack.getLast().getMatrix(), (float)x, (float)y, (float)z).color(color4 >> 16 & 0xFF, color4 >> 8 & 0xFF, color4 & 0xFF, color4 >>> 24).tex(0.0f, 0.0f).lightmap(0, 240).endVertex();
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void drawGradientRoundedRect(float x, float y, float width, float height, float radius, int startColor, int endColor) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glBegin(6);
        DisplayUtils.setColor(startColor);
        GL11.glVertex2f(x + radius, y + radius);
        DisplayUtils.setColor(endColor);
        GL11.glVertex2f(x + radius, y + height - radius);
        DisplayUtils.setColor(startColor);
        GL11.glVertex2f(x + width - radius, y + height - radius);
        DisplayUtils.setColor(endColor);
        GL11.glVertex2f(x + width - radius, y + radius);
        GL11.glEnd();
        DisplayUtils.drawRoundedCorner(x + radius, y + radius, radius, startColor, endColor, true, true);
        DisplayUtils.drawRoundedCorner(x + width - radius, y + radius, radius, startColor, endColor, false, true);
        DisplayUtils.drawRoundedCorner(x + width - radius, y + height - radius, radius, startColor, endColor, false, false);
        DisplayUtils.drawRoundedCorner(x + radius, y + height - radius, radius, startColor, endColor, true, false);
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    private static void setColor(int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    private static void drawRoundedCorner(float cx, float cy, float radius, int startColor, int endColor, boolean left, boolean top) {
        GL11.glBegin(6);
        DisplayUtils.setColor(startColor);
        GL11.glVertex2f(cx, cy);
        for (int i = 0; i <= 90; i += 5) {
            double angle = Math.toRadians(i);
            float dx = (float)(Math.cos(angle) * (double)radius);
            float dy = (float)(Math.sin(angle) * (double)radius);
            if (!left) {
                dx = -dx;
            }
            if (!top) {
                dy = -dy;
            }
            DisplayUtils.setColor(endColor);
            GL11.glVertex2f(cx + dx, cy + dy);
        }
        GL11.glEnd();
    }

    public static void scissor(double x, double y, double width, double height) {
        double scale = mc.getMainWindow().getGuiScaleFactor();
        y = (double)mc.getMainWindow().getScaledHeight() - y;
        GL11.glScissor((int)(x *= scale), (int)((y *= scale) - (height *= scale)), (int)(width *= scale), (int)height);
    }

    public static void drawCircle1(float x, float y, float start, float end, float radius, float width, boolean filled, int color) {
        float sin2;
        float cos2;
        float i;
        if (start > end) {
            float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        for (i = end; i >= start; i -= 1.0f) {
            ColorUtils.setColor(color);
            cos2 = MathHelper.cos((float)((double)i * Math.PI / 180.0)) * radius;
            sin2 = MathHelper.sin((float)((double)i * Math.PI / 180.0)) * radius;
            GL11.glVertex2f(x + cos2, y + sin2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        if (filled) {
            GL11.glBegin(6);
            for (i = end; i >= start; i -= 1.0f) {
                ColorUtils.setColor1(color);
                cos2 = MathHelper.cos((float)((double)i * Math.PI / 180.0)) * radius;
                sin2 = MathHelper.sin((float)((double)i * Math.PI / 180.0)) * radius;
                GL11.glVertex2f(x + cos2, y + sin2);
            }
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }

    public static void drawCircle1(float x, float y, float start, float end, float radius, float width, boolean filled, Style s) {
        float sin2;
        float cos2;
        int color;
        float i;
        if (start > end) {
            float endOffset = end;
            end = start;
            start = endOffset;
        }
        int color1 = ColorUtils.getColor(0);
        int color2 = ColorUtils.getColor(90);
        int color3 = ColorUtils.getColor(180);
        int color4 = ColorUtils.getColor(270);
        GlStateManager.enableBlend();
        RenderSystem.disableAlphaTest();
        GL11.glDisable(3553);
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.shadeModel(7425);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        for (i = end; i >= start; i -= 1.0f) {
            color = DisplayUtils.interpolateColor(i, start, end, color1, color2, color3, color4);
            ColorUtils.setColor(color);
            cos2 = MathHelper.cos((float)((double)i * Math.PI / 180.0)) * radius;
            sin2 = MathHelper.sin((float)((double)i * Math.PI / 180.0)) * radius;
            GL11.glVertex2f(x + cos2, y + sin2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        if (filled) {
            GL11.glBegin(6);
            for (i = end; i >= start; i -= 1.0f) {
                color = DisplayUtils.interpolateColor(i, start, end, color1, color2, color3, color4);
                ColorUtils.setColor(color);
                cos2 = MathHelper.cos((float)((double)i * Math.PI / 180.0)) * radius;
                sin2 = MathHelper.sin((float)((double)i * Math.PI / 180.0)) * radius;
                GL11.glVertex2f(x + cos2, y + sin2);
            }
            GL11.glEnd();
        }
        RenderSystem.enableAlphaTest();
        RenderSystem.shadeModel(7424);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
    }

    private static int interpolateColor(float angle, float start, float end, int color1, int color2, int color3, int color4) {
        float range = end - start;
        float normalizedAngle = (angle - start) / range;
        if (normalizedAngle <= 0.33f) {
            return DisplayUtils.interpolate(color1, color2, normalizedAngle / 0.33f);
        }
        if (normalizedAngle <= 0.66f) {
            return DisplayUtils.interpolate(color2, color3, (normalizedAngle - 0.33f) / 0.33f);
        }
        return DisplayUtils.interpolate(color3, color4, (normalizedAngle - 0.66f) / 0.34f);
    }

    private static int interpolate(int colorA, int colorB, float fraction) {
        int redA = colorA >> 16 & 0xFF;
        int greenA = colorA >> 8 & 0xFF;
        int blueA = colorA & 0xFF;
        int redB = colorB >> 16 & 0xFF;
        int greenB = colorB >> 8 & 0xFF;
        int blueB = colorB & 0xFF;
        int red = (int)((float)redA + (float)(redB - redA) * fraction);
        int green = (int)((float)greenA + (float)(greenB - greenA) * fraction);
        int blue = (int)((float)blueA + (float)(blueB - blueA) * fraction);
        return red << 16 | green << 8 | blue;
    }

    public static void drawShadow(float x, float y, float width, float height, int radius, int color, int i) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.01f);
        GlStateManager.disableAlphaTest();
        GL11.glShadeModel(7425);
        x -= (float)radius;
        y -= (float)radius;
        x -= 0.25f;
        y += 0.25f;
        int identifier = Objects.hash(Float.valueOf(width += (float)(radius * 2)), Float.valueOf(height += (float)(radius * 2)), radius);
        if (shadowCache.containsKey(identifier)) {
            int textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            BufferedImage originalImage = new BufferedImage((int)width, (int)height, 3);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int)(width - (float)(radius * 2)), (int)(height - (float)(radius * 2)));
            graphics.dispose();
            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            int textureId = texture.getGlTextureId();
            shadowCache.put(identifier, textureId);
        }
        float[] startColorComponents = ColorUtils.rgba(color);
        float[] i1 = ColorUtils.rgba(i);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(0.0f, 0.0f).endVertex();
        buffer.pos(x, y + (float)((int)height), 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(0.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y + (float)((int)height), 0.0).color(i1[0], i1[1], i1[2], i1[3]).tex(1.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y, 0.0).color(i1[0], i1[1], i1[2], i1[3]).tex(1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.enableAlphaTest();
        GL11.glShadeModel(7424);
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void drawShadowVertical(float x, float y, float width, float height, int radius, int color, int i) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.01f);
        GlStateManager.disableAlphaTest();
        GL11.glShadeModel(7425);
        x -= (float)radius;
        y -= (float)radius;
        x -= 0.25f;
        y += 0.25f;
        int identifier = Objects.hash(Float.valueOf(width += (float)(radius * 2)), Float.valueOf(height += (float)(radius * 2)), radius);
        if (shadowCache.containsKey(identifier)) {
            int textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            int textureId;
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            BufferedImage originalImage = new BufferedImage((int)width, (int)height, 3);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int)(width - (float)(radius * 2)), (int)(height - (float)(radius * 2)));
            graphics.dispose();
            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }
        float[] startColorComponents = ColorUtils.rgba(color);
        float[] i1 = ColorUtils.rgba(i);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(0.0f, 0.0f).endVertex();
        buffer.pos(x, y + (float)((int)height), 0.0).color(i1[0], i1[1], i1[2], i1[3]).tex(0.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y + (float)((int)height), 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(1.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y, 0.0).color(i1[0], i1[1], i1[2], i1[3]).tex(1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.enableAlphaTest();
        GL11.glShadeModel(7424);
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void drawShadow(float x, float y, float width, float height, int radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.01f);
        GlStateManager.disableAlphaTest();
        x -= (float)radius;
        y -= (float)radius;
        x -= 0.25f;
        y += 0.25f;
        int identifier = Objects.hash(Float.valueOf(width += (float)(radius * 2)), Float.valueOf(height += (float)(radius * 2)), radius);
        if (shadowCache.containsKey(identifier)) {
            int textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            int textureId;
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            BufferedImage originalImage = new BufferedImage((int)width, (int)height, 3);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int)(width - (float)(radius * 2)), (int)(height - (float)(radius * 2)));
            graphics.dispose();
            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }
        float[] startColorComponents = ColorUtils.rgba(color);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(0.0f, 0.0f).endVertex();
        buffer.pos(x, y + (float)((int)height), 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(0.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y + (float)((int)height), 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(1.0f, 1.0f).endVertex();
        buffer.pos(x + (float)((int)width), y, 0.0).color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3]).tex(1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.enableAlphaTest();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, int color) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        mc.getTextureManager().bindTexture(resourceLocation);
        DisplayUtils.quads(x, y, width, height, 7, color);
        RenderSystem.shadeModel(7424);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.popMatrix();
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Vector4i color) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        mc.getTextureManager().bindTexture(resourceLocation);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(x, y, 0.0).tex(0.0f, 0.0f).color(color.x).endVertex();
        buffer.pos(x, y + height, 0.0).tex(0.0f, 1.0f).color(color.y).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 1.0f).color(color.z).endVertex();
        buffer.pos(x + width, y, 0.0).tex(1.0f, 0.0f).color(color.w).endVertex();
        tessellator.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.popMatrix();
    }

    public static void drawRectWBuilding(double left, double top, double right, double bottom, int color) {
        right += left;
        bottom += top;
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.pos(left, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f, f1, f2, f3).endVertex();
    }

    public static void drawRectBuilding(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.pos(left, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f, f1, f2, f3).endVertex();
    }

    public static void drawMCVerticalBuilding(double x, double y, double width, double height, int start, int end) {
        float f = (float)(start >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(start >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(start >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(start & 0xFF) / 255.0f;
        float f4 = (float)(end >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(end >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(end >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(end & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.pos(x, height, 0.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(width, height, 0.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(width, y, 0.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(f5, f6, f7, f4).endVertex();
    }

    public static void drawMCHorizontalBuilding(double x, double y, double width, double height, int start, int end) {
        float f = (float)(start >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(start >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(start >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(start & 0xFF) / 255.0f;
        float f4 = (float)(end >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(end >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(end >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(end & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.pos(x, height, 0.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(width, height, 0.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(width, y, 0.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(f1, f2, f3, f).endVertex();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRectW(double x, double y, double w, double h, int color) {
        w = x + w;
        h = y + h;
        if (x < w) {
            double i = x;
            x = w;
            w = i;
        }
        if (y < h) {
            double j = y;
            y = h;
            h = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(w, h, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(w, y, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRectHorizontalW(double x, double y, double w, double h, int color, int color1) {
        w = x + w;
        h = y + h;
        if (x < w) {
            double i = x;
            x = w;
            w = i;
        }
        if (y < h) {
            double j = y;
            y = h;
            h = j;
        }
        float[] colorOne = ColorUtils.rgba(color);
        float[] colorTwo = ColorUtils.rgba(color1);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.shadeModel(7425);
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0).color(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).endVertex();
        bufferbuilder.pos(w, h, 0.0).color(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).endVertex();
        bufferbuilder.pos(w, y, 0.0).color(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.shadeModel(7424);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawRectVerticalW(double x, double y, double w, double h, int color, int color1) {
        w = x + w;
        h = y + h;
        if (x < w) {
            double i = x;
            x = w;
            w = i;
        }
        if (y < h) {
            double j = y;
            y = h;
            h = j;
        }
        float[] colorOne = ColorUtils.rgba(color);
        float[] colorTwo = ColorUtils.rgba(color1);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0).color(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).endVertex();
        bufferbuilder.pos(w, h, 0.0).color(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).endVertex();
        bufferbuilder.pos(w, y, 0.0).color(colorTwo[0], colorTwo[1], colorTwo[2], colorTwo[3]).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(colorOne[0], colorOne[1], colorOne[2], colorOne[3]).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
    }

    public static void drawRoundedRect(float x, float y, float width, float height, Vector4f vector4f, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.rounded.attach();
        ShaderUtil.rounded.setUniform("size", width * 2.0f, height * 2.0f);
        ShaderUtil.rounded.setUniform("round", vector4f.x * 2.0f, vector4f.y * 2.0f, vector4f.z * 2.0f, vector4f.w * 2.0f);
        ShaderUtil.rounded.setUniform("smoothness", 0.0f, 1.5f);
        ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.rounded.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRoundedRect1(float x, float y, float width, float height, Vector4f vector4f, int color, int color1) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.rounded.attach();
        ShaderUtil.rounded.setUniform("size", width * 2.0f, height * 2.0f);
        ShaderUtil.rounded.setUniform("round", vector4f.x * 2.0f, vector4f.y * 2.0f, vector4f.z * 2.0f, vector4f.w * 2.0f);
        ShaderUtil.rounded.setUniform("smoothness", 0.0f, 1.5f);
        ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color));
        ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.rounded.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 771);
        ShaderUtil.rounded.attach();
        ShaderUtil.rounded.setUniform(String.valueOf(x), y, width, height, radius, width * 2.0f, -1.0f);
        ShaderUtil.rounded.setUniform("color1", IntColor.rgb(bottomLeft));
        ShaderUtil.rounded.setUniform("color2", IntColor.rgb(topLeft));
        ShaderUtil.rounded.setUniform("color3", IntColor.rgb(bottomRight));
        ShaderUtil.rounded.setUniform("color4", IntColor.rgb(topRight));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.rounded.detach();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedRect(float x, float y, float width, float height, Vector4f vector4f, Vector4i color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.rounded.attach();
        ShaderUtil.rounded.setUniform("size", width * 2.0f, height * 2.0f);
        ShaderUtil.rounded.setUniform("round", vector4f.x * 2.0f, vector4f.y * 2.0f, vector4f.z * 2.0f, vector4f.w * 2.0f);
        ShaderUtil.rounded.setUniform("smoothness", 0.0f, 1.5f);
        ShaderUtil.rounded.setUniform("color1", ColorUtils.rgba(color.getX()));
        ShaderUtil.rounded.setUniform("color2", ColorUtils.rgba(color.getY()));
        ShaderUtil.rounded.setUniform("color3", ColorUtils.rgba(color.getZ()));
        ShaderUtil.rounded.setUniform("color4", ColorUtils.rgba(color.getW()));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.rounded.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float outline, int color1, Vector4f vector4f, Vector4i color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.roundedout.attach();
        ShaderUtil.roundedout.setUniform("size", width * 2.0f, height * 2.0f);
        ShaderUtil.roundedout.setUniform("round", vector4f.x * 2.0f, vector4f.y * 2.0f, vector4f.z * 2.0f, vector4f.w * 2.0f);
        ShaderUtil.roundedout.setUniform("smoothness", 0.0f, 1.5f);
        ShaderUtil.roundedout.setUniform("outlineColor", ColorUtils.rgba(color.getX()));
        ShaderUtil.roundedout.setUniform("outlineColor1", ColorUtils.rgba(color.getY()));
        ShaderUtil.roundedout.setUniform("outlineColor2", ColorUtils.rgba(color.getZ()));
        ShaderUtil.roundedout.setUniform("outlineColor3", ColorUtils.rgba(color.getW()));
        ShaderUtil.roundedout.setUniform("color", ColorUtils.rgba(color1));
        ShaderUtil.roundedout.setUniform("outline", outline);
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.rounded.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawContrast(float state) {
        state = MathHelper.clamp(state, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.glBlendFuncSeparate(770, 771, 1, 0);
        contrastCache = ShaderUtil.createFrameBuffer(contrastCache);
        contrastCache.framebufferClear(false);
        contrastCache.bindFramebuffer(true);
        ShaderUtil.contrast.attach();
        ShaderUtil.contrast.setUniform("texture", 0);
        ShaderUtil.contrast.setUniformf("contrast", state);
        GlStateManager.bindTexture(DisplayUtils.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        contrastCache.unbindFramebuffer();
        ShaderUtil.contrast.detach();
        mc.getFramebuffer().bindFramebuffer(true);
        ShaderUtil.contrast.attach();
        ShaderUtil.contrast.setUniform("texture", 0);
        ShaderUtil.contrast.setUniformf("contrast", state);
        GlStateManager.bindTexture(DisplayUtils.contrastCache.framebufferTexture);
        ShaderUtil.drawQuads();
        ShaderUtil.contrast.detach();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.bindTexture(0);
    }

    public static void drawWhite(float state) {
        state = MathHelper.clamp(state, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.glBlendFuncSeparate(770, 771, 1, 0);
        whiteCache = ShaderUtil.createFrameBuffer(whiteCache);
        whiteCache.framebufferClear(false);
        whiteCache.bindFramebuffer(true);
        ShaderUtil.white.attach();
        ShaderUtil.white.setUniform("texture", 0);
        ShaderUtil.white.setUniformf("state", state);
        GlStateManager.bindTexture(DisplayUtils.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        whiteCache.unbindFramebuffer();
        ShaderUtil.white.detach();
        mc.getFramebuffer().bindFramebuffer(true);
        ShaderUtil.white.attach();
        ShaderUtil.white.setUniform("texture", 0);
        ShaderUtil.white.setUniformf("state", state);
        GlStateManager.bindTexture(DisplayUtils.whiteCache.framebufferTexture);
        ShaderUtil.drawQuads();
        ShaderUtil.white.detach();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.bindTexture(0);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.smooth.attach();
        ShaderUtil.smooth.setUniformf("location", (float)((double)x * mc.getMainWindow().getGuiScaleFactor()), (float)((double)mc.getMainWindow().getHeight() - (double)height * mc.getMainWindow().getGuiScaleFactor() - (double)y * mc.getMainWindow().getGuiScaleFactor()));
        ShaderUtil.smooth.setUniformf("rectSize", (double)width * mc.getMainWindow().getGuiScaleFactor(), (double)height * mc.getMainWindow().getGuiScaleFactor());
        ShaderUtil.smooth.setUniformf("radius", (double)radius * mc.getMainWindow().getGuiScaleFactor());
        ShaderUtil.smooth.setUniform("blur", 0);
        ShaderUtil.smooth.setUniform("color", ColorUtils.rgba(color));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.smooth.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRoundedRect1(float x, float y, float width, float height, float radius, int color, int gradientAlpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        ShaderUtil.smooth.attach();
        ShaderUtil.smooth.setUniformf("location", (float)((double)x * mc.getMainWindow().getGuiScaleFactor()), (float)((double)mc.getMainWindow().getHeight() - (double)height * mc.getMainWindow().getGuiScaleFactor() - (double)y * mc.getMainWindow().getGuiScaleFactor()));
        ShaderUtil.smooth.setUniformf("rectSize", (double)width * mc.getMainWindow().getGuiScaleFactor(), (double)height * mc.getMainWindow().getGuiScaleFactor());
        ShaderUtil.smooth.setUniformf("radius", (double)radius * mc.getMainWindow().getGuiScaleFactor());
        ShaderUtil.smooth.setUniform("blur", 0);
        ShaderUtil.smooth.setUniform("color", ColorUtils.rgba(color));
        DisplayUtils.drawQuads(x, y, width, height, 7);
        ShaderUtil.smooth.detach();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        DisplayUtils.drawRoundedRect(x - radius / 2.0f, y - radius / 2.0f, radius, radius, radius / 2.0f, color);
    }

    public static void drawShadowCircle(float x, float y, float radius, int color) {
        DisplayUtils.drawShadow(x - radius / 2.0f, y - radius / 2.0f, radius, radius, (int)radius, color);
    }

    public static void drawQuads(float x, float y, float width, float height, int glQuads) {
        buffer.begin(glQuads, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y, 0.0).tex(0.0f, 0.0f).endVertex();
        buffer.pos(x, y + height, 0.0).tex(0.0f, 1.0f).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex(1.0f, 1.0f).endVertex();
        buffer.pos(x + width, y, 0.0).tex(1.0f, 0.0f).endVertex();
        Tessellator.getInstance().draw();
    }

    public static void drawBox(double x, double y, double width, double height, double size, int color) {
        DisplayUtils.drawRectBuilding(x + size, y, width - size, y + size, color);
        DisplayUtils.drawRectBuilding(x, y, x + size, height, color);
        DisplayUtils.drawRectBuilding(width - size, y, width, height, color);
        DisplayUtils.drawRectBuilding(x + size, height - size, width - size, height, color);
    }

    public static void drawBoxTest(double x, double y, double width, double height, double size, Vector4i colors) {
        DisplayUtils.drawMCHorizontalBuilding(x + size, y, width - size, y + size, colors.x, colors.z);
        DisplayUtils.drawMCVerticalBuilding(x, y, x + size, height, colors.z, colors.x);
        DisplayUtils.drawMCVerticalBuilding(width - size, y, width, height, colors.x, colors.z);
        DisplayUtils.drawMCHorizontalBuilding(x + size, height - size, width - size, height, colors.z, colors.x);
    }

    public static class IntColor {
        public static float[] rgb(int color) {
            return new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, (float)(color >> 24 & 0xFF) / 255.0f};
        }

        public static int rgba(int r, int g, int b, int a) {
            return a << 24 | r << 16 | g << 8 | b;
        }

        public static int getRed(int hex) {
            return hex >> 16 & 0xFF;
        }

        public static int getGreen(int hex) {
            return hex >> 8 & 0xFF;
        }

        public static int getBlue(int hex) {
            return hex & 0xFF;
        }

        public static int getAlpha(int hex) {
            return hex >> 24 & 0xFF;
        }
    }
}

