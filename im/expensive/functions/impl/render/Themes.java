/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.render.ColorUtils;
import java.awt.Color;

@FunctionRegister(name="Themes", type=Category.Render)
public class Themes
extends Function {
    public static final ModeSetting vibor = new ModeSetting("\u0422\u0435\u043c\u0430", "\u041a\u0440\u043e\u0432\u0430\u0432\u044b\u0439", "\u0422\u043e\u043a\u0441\u0438\u0447\u043d\u044b\u0439", "\u041a\u043e\u043d\u0444\u0435\u0442\u043d\u044b\u0439", "\u041e\u0433\u043d\u0435\u043d\u043d\u044b\u0439", "\u041c\u043e\u0440\u0441\u043a\u043e\u0439", "\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u0438\u0441\u0442\u0438\u0447\u043d\u044b\u0439", "\u041a\u0440\u043e\u0432\u0430\u0432\u044b\u0439", "\u041b\u0430\u0432\u0430\u043d\u0434\u043e\u0432\u044b\u0439", "\u041b\u0443\u043d\u0430\u0434\u0430", "\u041b\u0435\u0441 \u0432 \u0441\u0443\u043c\u0435\u0440\u043a\u0430\u0445");
    public static final SliderSetting speedis = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 5.0f, 5.0f, 40.0f, 2.5f);
    public static final SliderSetting transparency = new SliderSetting("\u041f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u043e\u0441\u0442\u044c", 255.0f, 0.0f, 255.0f, 1.0f);

    public Themes() {
        this.addSettings(vibor, speedis, transparency);
    }

    public static int viborTEM(int index) {
        int speed = ((Float)speedis.get()).intValue();
        int alpha = ((Float)transparency.get()).intValue();
        if (vibor.is("\u041a\u043e\u043d\u0444\u0435\u0442\u043d\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(168, 230, 207, alpha), ColorUtils.rgba(220, 237, 193, alpha), ColorUtils.rgba(255, 211, 182, alpha), ColorUtils.rgba(255, 170, 165, alpha), index, speed);
        }
        if (vibor.is("\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u0438\u0441\u0442\u0438\u0447\u043d\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(40, 39, 39, alpha), ColorUtils.rgba(178, 178, 178, alpha), ColorUtils.rgba(100, 100, 100, alpha), ColorUtils.rgba(220, 220, 220, alpha), index, speed);
        }
        if (vibor.is("\u041c\u043e\u0440\u0441\u043a\u043e\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(5, 63, 111, alpha), ColorUtils.rgba(133, 183, 246, alpha), ColorUtils.rgba(0, 105, 148, alpha), ColorUtils.rgba(3, 169, 244, alpha), index, speed);
        }
        if (vibor.is("\u0422\u043e\u043a\u0441\u0438\u0447\u043d\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(52, 235, 31, alpha), ColorUtils.rgba(0, 255, 68, alpha), ColorUtils.rgba(173, 255, 47, alpha), ColorUtils.rgba(0, 128, 0, alpha), index, speed);
        }
        if (vibor.is("\u041e\u0433\u043d\u0435\u043d\u043d\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(194, 21, 0, alpha), ColorUtils.rgba(255, 197, 0, alpha), ColorUtils.rgba(255, 69, 0, alpha), ColorUtils.rgba(255, 140, 0, alpha), index, speed);
        }
        if (vibor.is("\u041a\u0440\u043e\u0432\u0430\u0432\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(197, 55, 109, alpha), ColorUtils.rgba(106, 27, 29, alpha), ColorUtils.rgba(139, 0, 0, alpha), ColorUtils.rgba(255, 0, 0, alpha), index, speed);
        }
        if (vibor.is("\u041b\u0430\u0432\u0430\u043d\u0434\u043e\u0432\u044b\u0439")) {
            return Themes.gradient4(ColorUtils.rgba(163, 187, 250, alpha), ColorUtils.rgba(66, 57, 155, alpha), ColorUtils.rgba(186, 85, 211, alpha), ColorUtils.rgba(138, 43, 226, alpha), index, speed);
        }
        if (vibor.is("\u041b\u0443\u043d\u0430\u0434\u0430")) {
            return Themes.gradient4(ColorUtils.rgba(88, 62, 252, alpha), ColorUtils.rgba(162, 246, 205, alpha), ColorUtils.rgba(72, 61, 139, alpha), ColorUtils.rgba(173, 216, 230, alpha), index, speed);
        }
        return vibor.is("\u041b\u0435\u0441 \u0432 \u0441\u0443\u043c\u0435\u0440\u043a\u0430\u0445") ? Themes.gradient4(ColorUtils.rgba(30, 48, 30, alpha), ColorUtils.rgba(58, 83, 53, alpha), ColorUtils.rgba(85, 107, 47, alpha), ColorUtils.rgba(114, 144, 85, alpha), index, speed) : index * 16;
    }

    public static int gradient4(int color1, int color2, int color3, int color4, int index, int duration) {
        int endColor;
        int startColor;
        int cycle = 20 * duration;
        long currentTime = System.nanoTime();
        int position = (int)((currentTime / 1000000L / 30L + (long)index) % (long)cycle);
        float segment = (float)cycle / 4.0f;
        float segmentPosition = (float)position % segment;
        float phase = segmentPosition / segment;
        float localRatio = Themes.hermiteInterpolation(0.0f, 1.0f, phase);
        int angle = (int)((System.currentTimeMillis() / (long)duration + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        if ((float)position < segment) {
            startColor = color1;
            endColor = color2;
        } else if ((float)position < segment * 2.0f) {
            startColor = color2;
            endColor = color3;
        } else if ((float)position < segment * 3.0f) {
            startColor = color3;
            endColor = color4;
        } else {
            startColor = color4;
            endColor = color1;
        }
        int interpolatedColor = Themes.interpolate7(startColor, endColor, localRatio);
        float[] hs = Themes.rgba(interpolatedColor);
        float[] hsb = Color.RGBtoHSB((int)(hs[0] * 255.0f), (int)(hs[1] * 255.0f), (int)(hs[2] * 255.0f), null);
        hsb[1] = hsb[1] * 1.5f;
        hsb[1] = Math.min(hsb[1], 1.0f);
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    private static int interpolate7(int colorA, int colorB, float ratio) {
        int a = colorA >> 24 & 0xFF;
        int r = colorA >> 16 & 0xFF;
        int g = colorA >> 8 & 0xFF;
        int b = colorA & 0xFF;
        int a2 = colorB >> 24 & 0xFF;
        int r2 = colorB >> 16 & 0xFF;
        int g2 = colorB >> 8 & 0xFF;
        int b2 = colorB & 0xFF;
        int newA = (int)((float)a + (float)(a2 - a) * ratio);
        int newR = (int)((float)r + (float)(r2 - r) * ratio);
        int newG = (int)((float)g + (float)(g2 - g) * ratio);
        int newB = (int)((float)b + (float)(b2 - b) * ratio);
        return newA << 24 | newR << 16 | newG << 8 | newB;
    }

    public static float hermiteInterpolation(float edge0, float edge1, float x) {
        x = Themes.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return x * x * (3.0f - 2.0f * x);
    }

    public static float clamp(float value, float min2, float max2) {
        return Math.max(min2, Math.min(max2, value));
    }

    public static float[] rgba(int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        return new float[]{r, g, b, a};
    }
}

