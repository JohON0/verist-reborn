/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.render.ShaderUtil;
import im.expensive.utils.render.StencilUtil;
import java.nio.FloatBuffer;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;

public class GaussinBlur {
    private static final ShaderUtil gaussianBlur = new ShaderUtil("blur");
    private static Framebuffer framebuffer1 = new Framebuffer(1, 1, false, false);
    private static Framebuffer framebuffer2 = new Framebuffer(1, 1, false, false);
    private static final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
    private static boolean initialized = false;

    private static void initializeWeightBuffer() {
        if (!initialized) {
            for (int i = 0; i <= 128; ++i) {
                weightBuffer.put(GaussinBlur.calculateGaussianValue(i, 64.0f));
            }
            weightBuffer.rewind();
            initialized = true;
        }
    }

    private static void setupUniforms(float dir1, float dir2, float radius) {
        gaussianBlur.setUniform("textureIn", 0);
        float texelWidth = 1.0f / (float)IMinecraft.mc.getMainWindow().getWidth();
        float texelHeight = 1.0f / (float)IMinecraft.mc.getMainWindow().getHeight();
        gaussianBlur.setUniformf("texelSize", texelWidth, texelHeight);
        gaussianBlur.setUniformf("direction", dir1, dir2);
        gaussianBlur.setUniformf("radius", radius);
        RenderSystem.glUniform1(gaussianBlur.getUniform("weights"), weightBuffer);
    }

    public static void startBlur() {
        StencilUtil.initStencilToWrite();
    }

    public static void endBlur(float radius, float compression) {
        StencilUtil.readStencilBuffer(1);
        GaussinBlur.performBlur(radius, compression);
        StencilUtil.uninitStencilBuffer();
    }

    public static void blur(float radius, float compression) {
        GaussinBlur.performBlur(radius, compression);
    }

    private static void performBlur(float radius, float compression) {
        framebuffer1 = GaussinBlur.ensureFramebufferSize(framebuffer1);
        framebuffer2 = GaussinBlur.ensureFramebufferSize(framebuffer2);
        framebuffer1.framebufferClear(false);
        framebuffer1.bindFramebuffer(false);
        gaussianBlur.attach();
        GaussinBlur.setupUniforms(compression, 0.0f, radius);
        GlStateManager.bindTexture(IMinecraft.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer1.unbindFramebuffer();
        framebuffer2.framebufferClear(false);
        framebuffer2.bindFramebuffer(false);
        GaussinBlur.setupUniforms(0.0f, compression, radius);
        GlStateManager.bindTexture(GaussinBlur.framebuffer1.framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer2.unbindFramebuffer();
        gaussianBlur.detach();
        IMinecraft.mc.getFramebuffer().bindFramebuffer(false);
        GlStateManager.bindTexture(GaussinBlur.framebuffer2.framebufferTexture);
        ShaderUtil.drawQuads();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.bindTexture(0);
    }

    private static Framebuffer ensureFramebufferSize(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != IMinecraft.mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != IMinecraft.mc.getMainWindow().getHeight()) {
            framebuffer.deleteFramebuffer();
            framebuffer = new Framebuffer(IMinecraft.mc.getMainWindow().getWidth(), IMinecraft.mc.getMainWindow().getHeight(), false, false);
        }
        return framebuffer;
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double output = 1.0 / Math.sqrt(Math.PI * 2 * (double)(sigma * sigma));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
    }

    static {
        GaussinBlur.initializeWeightBuffer();
    }
}

