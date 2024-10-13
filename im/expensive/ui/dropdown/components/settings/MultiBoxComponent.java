/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.Cursors;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.glfw.GLFW;

public class MultiBoxComponent
extends Component {
    final ModeListSetting setting;
    float width = 0.0f;
    float heightPadding = 0.0f;
    final float horizontalSpacing = 2.0f;

    public MultiBoxComponent(ModeListSetting setting) {
        this.setting = setting;
        this.setHeight(22.0f);
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        super.render(stack, mouseX, mouseY);
        Fonts.montserrat.drawText(stack, this.setting.getName(), this.getX() + 5.0f, this.getY() + 2.0f, -1, 5.5f, 0.15f);
        float offset = 0.0f;
        float heightoff = 0.0f;
        boolean plused = false;
        boolean anyHovered = false;
        for (BooleanSetting text : (List)this.setting.get()) {
            float componentWidth = Fonts.sfMedium.getWidth(text.getName(), 6.5f, 0.015f) + 7.0f;
            if (offset + componentWidth >= this.getWidth() - 10.0f) {
                offset = 0.0f;
                heightoff += 12.0f;
                plused = true;
            }
            if (MathUtil.isHovered(mouseX, mouseY, this.getX() + 8.0f + offset, this.getY() + 11.5f + heightoff, componentWidth, Fonts.montserrat.getHeight(6.5f) + 1.0f)) {
                anyHovered = true;
            }
            if (((Boolean)text.get()).booleanValue()) {
                DisplayUtils.drawRoundedRect(this.getX() + 6.0f + offset, this.getY() + 10.0f + heightoff, componentWidth, 10.0f, new Vector4f(3.0f, 3.0f, 3.0f, 3.0f), new Vector4i(ColorUtils.setAlpha(ColorUtils.getColor(0), 120), ColorUtils.setAlpha(ColorUtils.getColor(90), 120), ColorUtils.setAlpha(ColorUtils.getColor(180), 120), ColorUtils.setAlpha(ColorUtils.getColor(270), 120)));
                Fonts.sfMedium.drawText(stack, text.getName(), this.getX() + 8.0f + offset, this.getY() + 11.5f + heightoff, ColorUtils.rgba(255, 255, 255, 255), 7.5f, 0.015f);
            } else {
                DisplayUtils.drawRoundedRect(this.getX() + 6.0f + offset, this.getY() + 10.0f + heightoff, componentWidth, 10.0f, 2.0f, ColorUtils.rgb(47, 45, 45));
                Fonts.sfMedium.drawText(stack, text.getName(), this.getX() + 8.0f + offset, this.getY() + 11.5f + heightoff, ColorUtils.rgb(255, 255, 255), 7.5f, 0.015f);
            }
            offset += componentWidth + 2.0f;
        }
        if (this.isHovered(mouseX, mouseY)) {
            if (anyHovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.HAND);
            } else {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
            }
        }
        this.width = plused ? this.getWidth() - 15.0f : offset;
        this.setHeight(22.0f + heightoff);
        this.heightPadding = heightoff;
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int mouse) {
        float offset = 0.0f;
        float heightoff = 0.0f;
        for (BooleanSetting text : (List)this.setting.get()) {
            float componentWidth = Fonts.montserrat.getWidth(text.getName(), 6.5f, 0.015f) + 5.0f;
            if (offset + componentWidth >= this.getWidth() - 10.0f) {
                offset = 0.0f;
                heightoff += 12.0f;
            }
            if (MathUtil.isHovered(mouseX, mouseY, this.getX() + 8.0f + offset, this.getY() + 11.5f + heightoff, componentWidth, Fonts.montserrat.getHeight(5.5f) + 1.0f)) {
                text.set((Boolean)text.get() == false);
            }
            offset += componentWidth + 2.0f;
        }
        super.mouseClick(mouseX, mouseY, mouse);
    }

    @Override
    public boolean isVisible() {
        return (Boolean)this.setting.visible.get();
    }
}

