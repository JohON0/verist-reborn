/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.Cursors;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

public class BooleanComponent
extends Component {
    private final BooleanSetting setting;
    private Animation animation = new Animation();
    private float textOffset = 0.0f;
    private float width;
    private float height;
    private boolean hovered = false;
    private boolean hoveredOnSquare = false;
    private final ResourceLocation booleansetting = new ResourceLocation("expensive/images/check.png");

    public BooleanComponent(BooleanSetting setting) {
        this.setting = setting;
        this.setHeight(16.0f);
        this.animation = this.animation.animate((Boolean)setting.get() != false ? 1.0 : 0.0, 0.2, Easings.CIRC_OUT);
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        super.render(stack, mouseX, mouseY);
        this.animation.update();
        if (this.hovered && !this.hoveredOnSquare) {
            this.textOffset += 0.8f;
            if (this.textOffset >= this.getWidth()) {
                this.textOffset = -Fonts.sfui.getWidth(this.setting.getName(), 8.0f);
            }
        } else if (!this.hovered) {
            this.textOffset = 0.0f;
        }
        float textX = this.getX() + 5.0f + this.textOffset;
        Fonts.sfui.drawText(stack, this.setting.getName(), textX, this.getY() + 4.0f + 1.7f, ColorUtils.rgba(210, 210, 210, 255), 7.5f, 0.05f);
        this.width = 15.0f;
        this.height = 7.0f;
        int color = ColorUtils.interpolate(ColorUtils.rgb(21, 21, 21), ColorUtils.getColor(10), 1.0f - (float)this.animation.getValue());
        int shadowcolor = ColorUtils.interpolate(ColorUtils.rgb(21, 21, 21), ColorUtils.getColor(10), 1.0f - (float)this.animation.getValue());
        int color1 = ColorUtils.interpolate(ColorUtils.rgb(31, 31, 31), ColorUtils.rgba(255, 255, 255, 255), 1.0f - (float)this.animation.getValue());
        DisplayUtils.drawShadow(this.getX() - 0.5f + this.getWidth() - this.width - 1.5f, this.getY() - 0.5f + this.getHeight() / 2.0f - this.height / 2.0f, this.width - 3.0f, this.height + 2.5f, 8, shadowcolor);
        DisplayUtils.drawRoundedRect(this.getX() - 1.0f + this.getWidth() - this.width - 1.5f, this.getY() - 1.5f + this.getHeight() / 2.0f - this.height / 2.0f, this.width - 2.0f, this.height + 4.2f, 2.5f, color);
        DisplayUtils.drawRoundedRect(this.getX() + this.getWidth() - this.width - 1.5f, this.getY() - 0.7f + this.getHeight() / 2.0f - this.height / 2.0f, this.width - 4.0f, this.height + 2.5f, 2.0f, ColorUtils.rgb(31, 31, 31));
        DisplayUtils.drawImage(this.booleansetting, this.getX() + 1.1f + this.getWidth() - this.width - 1.0f, this.getY() + 1.0f + this.getHeight() / 2.0f - this.height / 2.0f, 9.2f, 9.2f, color1);
        this.hoveredOnSquare = MathUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - this.width - 1.5f, this.getY() - 1.7f + this.getHeight() / 2.0f - this.height / 2.0f, this.width - 4.0f, this.height + 2.5f);
        if (this.isHovered(mouseX, mouseY)) {
            this.hovered = true;
            if (this.hoveredOnSquare) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.HAND);
            } else {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
            }
        } else {
            this.hovered = false;
            this.hoveredOnSquare = false;
        }
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int mouse) {
        if (MathUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - this.width - 1.5f, this.getY() - 1.7f + this.getHeight() / 2.0f - this.height / 2.0f, this.width - 4.0f, this.height + 2.5f)) {
            this.setting.set((Boolean)this.setting.get() == false);
            this.animation = this.animation.animate((Boolean)this.setting.get() != false ? 1.0 : 0.0, 0.2, Easings.CIRC_OUT);
        }
        super.mouseClick(mouseX, mouseY, mouse);
    }

    @Override
    public boolean isVisible() {
        return (Boolean)this.setting.visible.get();
    }
}

