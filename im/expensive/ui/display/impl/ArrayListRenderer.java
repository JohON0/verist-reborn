/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.ElementUpdater;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.List;
import net.minecraft.util.math.vector.Vector4f;
import ru.hogoshi.Animation;

public class ArrayListRenderer
implements ElementRenderer,
ElementUpdater {
    private int lastIndex;
    List<Function> list;
    StopWatch stopWatch = new StopWatch();

    @Override
    public void update(EventUpdate e) {
        if (this.stopWatch.isReached(1000L)) {
            this.list = Expensive.getInstance().getFunctionRegistry().getSorted(Fonts.sfui, 7.5f).stream().filter(m -> m.getCategory() != Category.Render).filter(m -> m.getCategory() != Category.Misc).toList();
            this.stopWatch.reset();
        }
    }

    @Override
    public void render(EventDisplay eventDisplay) {
        float value;
        Animation anim;
        float fontSize;
        MatrixStack ms = eventDisplay.getMatrixStack();
        float rounding = 4.0f;
        float padding = 3.5f;
        float screenWidth = 1905.0f;
        float posX = window.getScaledWidth() + 3;
        float posY = 3.0f;
        int index = 0;
        if (this.list == null) {
            return;
        }
        for (Function f : this.list) {
            fontSize = 6.5f;
            anim = f.getAnimation();
            value = (float)anim.getValue();
            String text = f.getName();
            float textWidth = Fonts.sfui.getWidth(text, fontSize);
            if (value == 0.0f) continue;
            float localFontSize = fontSize * value;
            float localTextWidth = textWidth * value;
            DisplayUtils.drawShadow(posX - localTextWidth - padding + 15.0f, posY, localTextWidth + padding * 2.0f, localFontSize + padding * 0.3f, 35, ColorUtils.getColor(90));
            posY += (fontSize + padding * 2.0f) * value;
            ++index;
        }
        index = 0;
        posY = -20.0f;
        for (Function f : this.list) {
            fontSize = 6.5f;
            anim = f.getAnimation();
            anim.update();
            value = (float)anim.getValue();
            int color1 = ColorUtils.getColor(0);
            int color2 = ColorUtils.getColor(0);
            int color3 = ColorUtils.getColor(0);
            int color4 = ColorUtils.getColor(0);
            String text = f.getName();
            float textWidth = Fonts.sfui.getWidth(text, fontSize);
            if (value == 0.0f) continue;
            float localFontSize = fontSize * value;
            float localTextWidth = textWidth * value;
            boolean isFirst = index == 0;
            boolean isLast = index == this.lastIndex;
            float localRounding = rounding;
            for (Function f2 : this.list.subList(this.list.indexOf(f) + 1, this.list.size())) {
                if (f2.getAnimation().getValue() == 0.0) continue;
                localRounding = isLast ? rounding : Math.min(textWidth - Fonts.sfui.getWidth(f2.getName(), fontSize), rounding);
                break;
            }
            Vector4f rectVec = new Vector4f(isFirst ? rounding : 0.0f, isLast ? rounding : 0.0f, isFirst ? rounding : 0.0f, isLast ? rounding : localRounding);
            DisplayUtils.drawRoundedRect(posX - localTextWidth - padding - 5.0f, posY + 19.0f, localTextWidth + padding * 2.0f, localFontSize + padding * 2.0f, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), ColorUtils.rgba(0, 0, 0, 170));
            int gradientColor = this.getInterpolatedColor(posY + 3.0f, 0.0f, 100.0f, color1, color2, color3, color4);
            Fonts.sfui.drawText(ms, f.getName(), posX - localTextWidth - padding - 3.0f, posY + padding + 19.0f, gradientColor, localFontSize + 1.0f);
            posY += (fontSize + padding * 2.0f) * value;
            ++index;
        }
        this.lastIndex = index - 1;
    }

    private int getInterpolatedColor(float position, float minPos, float maxPos, int color1, int color2, int color3, int color4) {
        float range = maxPos - minPos;
        float normalizedPos = (position - minPos) / range;
        if (normalizedPos <= 0.33f) {
            return ColorUtils.interpolate(color1, color2, normalizedPos / 0.33f);
        }
        if (normalizedPos <= 0.66f) {
            return ColorUtils.interpolate(color2, color3, (normalizedPos - 0.33f) / 0.33f);
        }
        return ColorUtils.interpolate(color3, color4, (normalizedPos - 0.66f) / 0.34f);
    }
}

