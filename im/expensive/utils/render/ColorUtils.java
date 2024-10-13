/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.functions.impl.render.HUD;
import im.expensive.utils.math.MathUtil;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;

public final class ColorUtils {
    public static final int green = new Color(64, 255, 64).getRGB();
    public static final int yellow = new Color(255, 255, 64).getRGB();
    public static final int orange = new Color(255, 128, 32).getRGB();
    public static final int red = new Color(255, 64, 64).getRGB();

    public static int rgb(int r, int g, int b) {
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public static int rgba(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int setAlphaColor(int color, float alpha) {
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        RenderSystem.color4f(red, green, blue, alpha);
        return color;
    }

    public static int getColor(int index) {
        return HUD.getColor(index);
    }

    public static void setColor(int color) {
        ColorUtils.setAlphaColor(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static void setColor1(int color) {
        ColorUtils.setAlphaColor(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static int toColor(String hexColor) {
        int argb = Integer.parseInt(hexColor.substring(1), 16);
        return ColorUtils.setAlpha(argb, 255);
    }

    public static int setAlpha(int color, int alpha) {
        return color & 0xFFFFFF | alpha << 24;
    }

    public static float[] rgba(int color) {
        return new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, (float)(color >> 24 & 0xFF) / 255.0f};
    }

    public static int gradient(int start, int end, int index, int speed) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int color = ColorUtils.interpolate(start, end, MathHelper.clamp((float)angle / 180.0f - 1.0f, 0.0f, 1.0f));
        float[] hs = ColorUtils.rgba(color);
        float[] hsb = Color.RGBtoHSB((int)(hs[0] * 255.0f), (int)(hs[1] * 255.0f), (int)(hs[2] * 255.0f), null);
        hsb[1] = hsb[1] * 1.5f;
        hsb[1] = Math.min(hsb[1], 1.0f);
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    public static int interpolate(int start, int end, float value) {
        float[] startColor = ColorUtils.rgba(start);
        float[] endColor = ColorUtils.rgba(end);
        return ColorUtils.rgba((int)MathUtil.interpolate(startColor[0] * 255.0f, endColor[0] * 255.0f, (double)value), (int)MathUtil.interpolate(startColor[1] * 255.0f, endColor[1] * 255.0f, (double)value), (int)MathUtil.interpolate(startColor[2] * 255.0f, endColor[2] * 255.0f, (double)value), (int)MathUtil.interpolate(startColor[3] * 255.0f, endColor[3] * 255.0f, (double)value));
    }

    public static Object gradient(String w, int color, int color1) {
        return null;
    }

    private ColorUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

