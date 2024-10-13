/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;

public class SearchField {
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private boolean isFocused;
    private boolean typing;
    private final String placeholder;

    public SearchField(int x, int y, int width, int height, String placeholder) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
        this.text = "";
        this.isFocused = false;
        this.typing = false;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.drawStyledRect(this.x, this.y, this.width, this.height, 5.0f, 220);
        String textToDraw = this.text.isEmpty() && !this.typing ? this.placeholder : this.text;
        String cursor = this.typing && System.currentTimeMillis() % 1000L > 500L ? "_" : "";
        Fonts.montserrat.drawText(matrixStack, textToDraw + cursor, (float)(this.x + 5), (float)(this.y + (this.height - 8) / 2 + 1), ColorUtils.rgb(255, 255, 255), 8.0f);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(32, 32, 32, alpha));
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (this.isFocused) {
            this.text = this.text + codePoint;
            return true;
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isFocused && keyCode == 259 && !this.text.isEmpty()) {
            this.text = this.text.substring(0, this.text.length() - 1);
            return true;
        }
        if (keyCode == 257 || keyCode == 256) {
            this.typing = false;
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.typing = this.isFocused = MathUtil.isHovered((float)mouseX, (float)mouseY, this.x, this.y, this.width, this.height);
        return this.isFocused;
    }

    public boolean isEmpty() {
        return this.text.isEmpty();
    }

    public void setFocused(boolean focused) {
        this.isFocused = focused;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getText() {
        return this.text;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public boolean isTyping() {
        return this.typing;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }
}

