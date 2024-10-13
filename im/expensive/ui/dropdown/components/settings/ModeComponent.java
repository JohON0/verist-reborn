/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.util.math.vector.Vector4f;

public class ModeComponent
extends Component {
    final ModeSetting setting;
    private static final float MARGIN_X = 9.0f;
    private static final float MARGIN_Y = 4.0f;

    public ModeComponent(ModeSetting setting) {
        this.setting = setting;
        this.setHeight(22.0f);
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        super.render(stack, mouseX, mouseY);
        float titleWidth = Fonts.montserrat.getWidth("\u0412\u044b\u0431\u043e\u0440 \u0420\u0435\u0436\u0438\u043c\u0430", 7.5f, 0.1f);
        float titleXPos = this.getX() + (this.getWidth() - titleWidth) / 2.0f;
        float titleYPos = this.getY() + 2.0f;
        Fonts.montserrat.drawText(stack, "\u0412\u044b\u0431\u043e\u0440 \u0420\u0435\u0436\u0438\u043c\u0430", titleXPos - 19.0f, titleYPos + 3.0f, ColorUtils.rgb(255, 255, 255), 6.5f, 0.1f);
        float xPos = this.getX() + 9.0f - 2.0f;
        float yPos = titleYPos + Fonts.montserrat.getHeight(7.5f) + 5.0f;
        float maxWidth = this.getWidth() - 18.0f + 8.0f;
        float lineHeight = 0.0f;
        for (String mod : this.setting.strings) {
            boolean isActive = ((String)this.setting.get()).equals(mod);
            float textWidth = Fonts.montserrat.getWidth(mod, 6.5f, 0.15f);
            float textHeight = Fonts.montserrat.getHeight(6.5f);
            if (xPos + textWidth > this.getX() + maxWidth + 3.0f) {
                xPos = this.getX() + 9.0f - 2.0f;
                yPos += lineHeight + 4.0f - 3.0f;
                lineHeight = 0.0f;
            }
            if (isActive) {
                DisplayUtils.drawRoundedRect(xPos - 1.5f, yPos - 1.0f, textWidth + 6.0f, textHeight + 3.0f, new Vector4f(3.0f, 3.0f, 3.0f, 3.0f), new Vector4i(ColorUtils.setAlpha(ColorUtils.getColor(0), 120), ColorUtils.setAlpha(ColorUtils.getColor(90), 120), ColorUtils.setAlpha(ColorUtils.getColor(180), 120), ColorUtils.setAlpha(ColorUtils.getColor(270), 120)));
            } else {
                int backgroundColor = ColorUtils.rgb(47, 45, 45);
                DisplayUtils.drawRoundedRect(xPos - 1.5f, yPos - 1.0f, textWidth + 6.0f, textHeight + 3.0f, 1.0f, backgroundColor);
            }
            Fonts.montserrat.drawText(stack, mod, xPos, yPos, ColorUtils.rgb(255, 255, 255), 7.5f, 0.1f);
            lineHeight = Math.max(lineHeight, textHeight + 3.0f);
            xPos += textWidth + 9.0f - 1.0f;
        }
        this.setHeight(yPos + lineHeight - this.getY() + 5.0f);
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int mouse) {
        super.mouseClick(mouseX, mouseY, mouse);
        float componentX = this.getX() + 9.0f - 2.0f;
        float componentY = this.getY() + 17.0f;
        float componentWidth = this.getWidth() - 18.0f;
        float componentHeight = this.getHeight();
        if (!(mouseX < componentX || mouseX > componentX + componentWidth || mouseY < componentY || mouseY > componentY + componentHeight)) {
            float xPos = componentX;
            float yPos = componentY;
            for (String mod : this.setting.strings) {
                float textWidth = Fonts.montserrat.getWidth(mod, 6.5f, 0.15f);
                float textHeight = Fonts.montserrat.getHeight(6.5f);
                if (xPos + textWidth > componentX + componentWidth) {
                    xPos = componentX;
                    yPos += textHeight + 4.0f - 2.0f;
                }
                float itemX = xPos - 1.5f;
                float itemY = yPos - 3.0f;
                float itemWidth = textWidth + 6.0f;
                float itemHeight = textHeight + 5.0f;
                if (mouseX >= itemX && mouseX <= itemX + itemWidth && mouseY >= itemY && mouseY <= itemY + itemHeight) {
                    this.setting.set(mod);
                    break;
                }
                xPos += textWidth + 9.0f - 3.0f;
            }
        }
    }

    @Override
    public boolean isVisible() {
        return (Boolean)this.setting.visible.get();
    }
}

