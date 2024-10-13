/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.vector.Vector4f;

public class ColorComponent
extends Component {
    final ModeSetting setting;
    private List<Color> selectedColors;
    private boolean colorPickerVisible;
    private int activeColorIndex;
    private final Color[] colorPickerColors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.GRAY, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, Color.WHITE, Color.DARK_GRAY, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.GRAY, Color.LIGHT_GRAY, Color.WHITE};

    public ColorComponent(ModeSetting setting) {
        this.setting = setting;
        this.setHeight(22.0f);
        this.selectedColors = new ArrayList<Color>();
        for (int i = 0; i < 4; ++i) {
            this.selectedColors.add(Color.WHITE);
        }
        this.colorPickerVisible = false;
        this.activeColorIndex = -1;
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        super.render(stack, mouseX, mouseY);
        float titleWidth = Fonts.montserrat.getWidth("\u0412\u044b\u0431\u043e\u0440 \u0420\u0435\u0436\u0438\u043c\u0430", 7.5f, 0.1f);
        float titleXPos = this.getX() + (this.getWidth() - titleWidth) / 2.0f;
        float titleYPos = this.getY() + 2.0f;
        Fonts.montserrat.drawText(stack, "\u0412\u044b\u0431\u043e\u0440 \u0420\u0435\u0436\u0438\u043c\u0430", titleXPos - 18.0f, titleYPos + 3.0f, ColorUtils.rgb(255, 255, 255), 6.5f, 0.1f);
        float yPos = titleYPos + Fonts.montserrat.getHeight(7.5f) + 8.0f;
        float squareSize = 15.0f;
        float spacing = 5.0f;
        float squareX = this.getX() + 10.0f;
        float squareY = yPos;
        for (int i = 0; i < this.selectedColors.size(); ++i) {
            Color color = this.selectedColors.get(i);
            DisplayUtils.drawRoundedRect(squareX + (squareSize + spacing) * (float)i, squareY, squareSize, squareSize, 2.0f, color.getRGB());
        }
        yPos += squareSize + spacing;
        for (String mod : this.setting.strings) {
            boolean isActive = ((String)this.setting.get()).equals(mod);
            float textWidth = Fonts.montserrat.getWidth(mod, 6.5f, 0.15f);
            float xPos = this.getX() + 8.0f;
            float textHeight = Fonts.montserrat.getHeight(6.5f);
            if (isActive) {
                DisplayUtils.drawRoundedRect(xPos - 1.5f, yPos - 1.0f, textWidth + 6.0f, textHeight + 3.0f, new Vector4f(3.0f, 3.0f, 3.0f, 3.0f), new Vector4i(ColorUtils.setAlpha(ColorUtils.getColor(0), 120), ColorUtils.setAlpha(ColorUtils.getColor(90), 120), ColorUtils.setAlpha(ColorUtils.getColor(180), 120), ColorUtils.setAlpha(ColorUtils.getColor(270), 120)));
            } else {
                int backgroundColor = ColorUtils.rgb(47, 45, 45);
                DisplayUtils.drawRoundedRect(xPos - 1.5f, yPos - 1.0f, textWidth + 6.0f, textHeight + 3.0f, 1.0f, backgroundColor);
            }
            Fonts.montserrat.drawText(stack, mod, xPos, yPos, ColorUtils.rgb(255, 255, 255), 7.5f, 0.1f);
            yPos += textHeight + 4.0f;
        }
        if (this.colorPickerVisible) {
            float pickerX = this.getX() + this.getWidth() / 2.0f - 50.0f;
            float pickerY = this.getY() + this.getHeight() + 10.0f;
            this.renderColorPicker(stack, pickerX, pickerY);
        }
        this.setHeight(yPos - this.getY());
    }

    private void renderColorPicker(MatrixStack stack, float x, float y) {
        float cellSize = 20.0f;
        float padding = 2.0f;
        for (int i = 0; i < this.colorPickerColors.length; ++i) {
            int row = i / 5;
            int col = i % 5;
            float cellX = x + (cellSize + padding) * (float)col;
            float cellY = y + (cellSize + padding) * (float)row;
            DisplayUtils.drawRoundedRect(cellX, cellY, cellSize, cellSize, 2.0f, this.colorPickerColors[i].getRGB());
        }
        this.setHeight(this.getHeight() + (cellSize + padding) * 5.0f + padding);
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int mouse) {
        super.mouseClick(mouseX, mouseY, mouse);
        float yPos = this.getY() + 10.0f;
        float squareSize = 15.0f;
        float spacing = 5.0f;
        float squareX = this.getX() + 10.0f;
        float squareY = yPos;
        for (int i = 0; i < this.selectedColors.size(); ++i) {
            if (!MathUtil.isHovered(mouseX, mouseY, squareX + (squareSize + spacing) * (float)i, squareY, squareSize, squareSize)) continue;
            this.colorPickerVisible = true;
            this.activeColorIndex = i;
            return;
        }
        if (this.colorPickerVisible) {
            float pickerX = this.getX() + this.getWidth() / 2.0f - 50.0f;
            float pickerY = this.getY() + this.getHeight() + 10.0f;
            float cellSize = 20.0f;
            float padding = 2.0f;
            for (int i = 0; i < this.colorPickerColors.length; ++i) {
                int col = i % 5;
                float cellX = pickerX + (cellSize + padding) * (float)col;
                int row = i / 5;
                float cellY = pickerY + (cellSize + padding) * (float)row;
                if (!MathUtil.isHovered(mouseX, mouseY, cellX, cellY, cellSize, cellSize)) continue;
                this.selectedColors.set(this.activeColorIndex, this.colorPickerColors[i]);
                this.colorPickerVisible = false;
                this.activeColorIndex = -1;
                return;
            }
        }
        for (String mod : this.setting.strings) {
            float textHeight;
            float textWidth = Fonts.montserrat.getWidth(mod, 6.5f, 0.15f);
            float xPos = this.getX() + (this.getWidth() - textWidth) / 2.0f;
            if (MathUtil.isHovered(mouseX, mouseY, xPos - 48.0f, yPos + 7.0f, textWidth + 30.0f, (textHeight = Fonts.montserrat.getHeight(6.5f)) + 2.0f)) {
                this.setting.set(mod);
                break;
            }
            yPos += textHeight + 3.0f;
        }
    }

    @Override
    public boolean isVisible() {
        return (Boolean)this.setting.visible.get();
    }
}

