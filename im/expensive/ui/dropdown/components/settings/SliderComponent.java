/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.Cursors;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.glfw.GLFW;

public class SliderComponent
extends Component {
    private final SliderSetting setting;
    private float anim;
    private boolean drag;
    private boolean hovered = false;

    public SliderComponent(SliderSetting setting) {
        this.setting = setting;
        this.setHeight(18.0f);
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        super.render(stack, mouseX, mouseY);
        DisplayUtils.drawRoundedRect(this.getX() + this.getWidth() - 5.0f - Fonts.montserrat.getWidth(String.valueOf(this.setting.get()), 6.5f), this.getY() + 1.0f, Fonts.montserrat.getWidth(String.valueOf(this.setting.get()) + "2", 5.5f), 8.0f, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector4i(ColorUtils.setAlpha(ColorUtils.getColor(0), 90), ColorUtils.setAlpha(ColorUtils.getColor(90), 90), ColorUtils.setAlpha(ColorUtils.getColor(180), 90), ColorUtils.setAlpha(ColorUtils.getColor(270), 90)));
        Fonts.montserrat.drawText(stack, this.setting.getName(), this.getX() + 5.0f, this.getY() + 2.25f - 0.5f, ColorUtils.rgb(160, 163, 175), 6.5f, 0.05f);
        Fonts.montserrat.drawText(stack, String.valueOf(this.setting.get()), this.getX() + this.getWidth() - 7.0f - Fonts.montserrat.getWidth(String.valueOf(this.setting.get()), 5.5f), this.getY() + 1.0f + 1.0f, -1, 6.5f, 0.1f);
        DisplayUtils.drawRoundedRect(this.getX() + 5.0f, this.getY() + 11.0f, this.getWidth() - 10.0f, 2.0f, 0.6f, ColorUtils.rgb(28, 28, 31));
        float sliderWidth = this.anim = MathUtil.fast(this.anim, (this.getWidth() - 10.0f) * (((Float)this.setting.get()).floatValue() - this.setting.min) / (this.setting.max - this.setting.min), 20.0f);
        DisplayUtils.drawRoundedRect(this.getX() + 5.0f, this.getY() + 10.0f, sliderWidth, 4.0f, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector4i(ColorUtils.setAlpha(ColorUtils.getColor(0), 90), ColorUtils.setAlpha(ColorUtils.getColor(90), 90), ColorUtils.setAlpha(ColorUtils.getColor(180), 90), ColorUtils.setAlpha(ColorUtils.getColor(270), 90)));
        DisplayUtils.drawCircle(this.getX() + 5.0f + sliderWidth, this.getY() + 12.0f, 7.0f, ColorUtils.rgb(255, 255, 255));
        DisplayUtils.drawShadowCircle(this.getX() + 5.0f + sliderWidth, this.getY() + 12.0f, 6.0f, ColorUtils.rgba(128, 132, 150, 64));
        if (this.drag) {
            GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.glfwCreateStandardCursor(221189));
            this.setting.set(Float.valueOf((float)MathHelper.clamp(MathUtil.round((mouseX - this.getX() - 7.0f) / (this.getWidth() - 10.0f) * (this.setting.max - this.setting.min) + this.setting.min, this.setting.increment), (double)this.setting.min, (double)this.setting.max)));
        }
        if (this.isHovered(mouseX, mouseY)) {
            if (MathUtil.isHovered(mouseX, mouseY, this.getX() + 5.0f, this.getY() + 11.0f, this.getWidth() - 10.0f, 8.0f)) {
                if (!this.hovered) {
                    GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.RESIZEH);
                    this.hovered = true;
                }
            } else if (this.hovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
                this.hovered = false;
            }
        }
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int mouse) {
        if ((mouse == 0 || mouse == 1) && MathUtil.isHovered(mouseX, mouseY, this.getX() + 5.0f, this.getY() + 10.0f, this.getWidth() - 10.0f, 3.0f)) {
            this.drag = true;
        }
        super.mouseClick(mouseX, mouseY, mouse);
    }

    @Override
    public void mouseRelease(float mouseX, float mouseY, int mouse) {
        if (mouse == 0 || mouse == 1) {
            this.drag = false;
            GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
        }
        super.mouseRelease(mouseX, mouseY, mouse);
    }

    @Override
    public boolean isVisible() {
        return (Boolean)this.setting.visible.get();
    }
}

