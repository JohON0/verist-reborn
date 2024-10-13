/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.api.Function;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.functions.settings.impl.StringSetting;
import im.expensive.ui.dropdown.components.settings.BindComponent;
import im.expensive.ui.dropdown.components.settings.BooleanComponent;
import im.expensive.ui.dropdown.components.settings.ModeComponent;
import im.expensive.ui.dropdown.components.settings.MultiBoxComponent;
import im.expensive.ui.dropdown.components.settings.SliderComponent;
import im.expensive.ui.dropdown.components.settings.StringComponent;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.utils.client.KeyStorage;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.Cursors;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Stencil;
import im.expensive.utils.render.font.Fonts;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.glfw.GLFW;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

public class ModuleComponent
extends Component {
    private final Vector4f ROUNDING_VECTOR = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    private final Vector4i BORDER_COLOR = new Vector4i(ColorUtils.rgb(45, 46, 53), ColorUtils.rgb(25, 26, 31), ColorUtils.rgb(45, 46, 53), ColorUtils.rgb(25, 26, 31));
    private final Function function;
    public Animation animation = new Animation();
    public boolean open;
    private boolean bind;
    private float currentAlpha = 0.33f;
    private boolean increasing = true;
    boolean sorted = false;
    boolean state;
    private boolean isRightClicked = false;
    private final ObjectArrayList<Component> components = new ObjectArrayList();
    private boolean hovered = false;

    public ModuleComponent(Function function) {
        this.function = function;
        for (Setting<?> setting : function.getSettings()) {
            Setting mode;
            if (setting instanceof BooleanSetting) {
                BooleanSetting bool = (BooleanSetting)setting;
                this.components.add(new BooleanComponent(bool));
            }
            if (setting instanceof SliderSetting) {
                SliderSetting slider = (SliderSetting)setting;
                this.components.add(new SliderComponent(slider));
            }
            if (setting instanceof BindSetting) {
                BindSetting bind = (BindSetting)setting;
                this.components.add(new BindComponent(bind));
            }
            if (setting instanceof ModeSetting) {
                mode = (ModeSetting)setting;
                this.components.add(new ModeComponent((ModeSetting)mode));
            }
            if (setting instanceof ModeListSetting) {
                mode = (ModeListSetting)setting;
                this.components.add(new MultiBoxComponent((ModeListSetting)mode));
            }
            if (!(setting instanceof StringSetting)) continue;
            StringSetting string = (StringSetting)setting;
            this.components.add(new StringComponent(string));
        }
        this.animation = this.animation.animate(this.open ? 1.0 : 0.0, 0.3);
    }

    public void drawComponents(MatrixStack stack, float mouseX, float mouseY) {
        if (this.animation.getValue() > 0.0) {
            if (this.animation.getValue() > 0.1 && this.components.stream().filter(Component::isVisible).count() >= 1L) {
                DisplayUtils.drawRectVerticalW(this.getX() + 5.0f, this.getY() + 20.0f, this.getWidth() - 10.0f, 0.5, ColorUtils.getColor(90), ColorUtils.getColor(90));
            }
            Stencil.initStencilToWrite();
            DisplayUtils.drawRoundedRect(this.getX() + 0.5f, this.getY() + 0.5f, this.getWidth() - 1.0f, this.getHeight() - 1.0f, this.ROUNDING_VECTOR, ColorUtils.rgba(23, 23, 23, 84));
            Stencil.readStencilBuffer(1);
            float y = this.getY() + 20.0f;
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.setX(this.getX());
                component.setY(y);
                component.setWidth(this.getWidth());
                component.render(stack, mouseX, mouseY);
                y += component.getHeight();
            }
            Stencil.uninitStencilBuffer();
        }
    }

    @Override
    public void mouseRelease(float mouseX, float mouseY, int mouse) {
        for (Component component : this.components) {
            component.mouseRelease(mouseX, mouseY, mouse);
        }
        super.mouseRelease(mouseX, mouseY, mouse);
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        int color = ColorUtils.interpolate(ColorUtils.getColor(900), -1, (float)this.function.getAnimation().getValue());
        this.function.getAnimation().update();
        super.render(stack, mouseX, mouseY);
        this.drawOutlinedRect(mouseX, mouseY, color, stack);
        this.drawText(stack, color);
        this.drawComponents(stack, mouseX, mouseY);
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int button) {
        if (this.isHovered(mouseX, mouseY, 20.0f)) {
            if (button == 0) {
                this.function.toggle();
            }
            if (button == 1) {
                this.open = !this.open;
                this.animation = this.animation.animate(this.open ? 1.0 : 0.0, 0.5, Easings.SINE_BOTH);
            }
            if (button == 2) {
                boolean bl = this.bind = !this.bind;
            }
        }
        if (this.isHovered(mouseX, mouseY) && this.open) {
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.mouseClick(mouseX, mouseY, button);
            }
        }
        if (button == 1) {
            this.handleRightClick(mouseX, mouseY);
        }
        super.mouseClick(mouseX, mouseY, button);
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        for (Component component : this.components) {
            if (!component.isVisible()) continue;
            component.charTyped(codePoint, modifiers);
        }
        super.charTyped(codePoint, modifiers);
    }

    @Override
    public void keyPressed(int key, int scanCode, int modifiers) {
        for (Component component : this.components) {
            if (!component.isVisible()) continue;
            component.keyPressed(key, scanCode, modifiers);
        }
        if (this.bind) {
            if (key == 261) {
                this.function.setBind(0);
            } else {
                this.function.setBind(key);
            }
            this.bind = false;
        }
        super.keyPressed(key, scanCode, modifiers);
    }

    private void drawOutlinedRect(float mouseX, float mouseY, int color, MatrixStack stack) {
        float delta = 0.01f;
        if (this.increasing) {
            this.currentAlpha += delta;
            if (this.currentAlpha >= 0.66f) {
                this.currentAlpha = 0.66f;
                this.increasing = false;
            }
        } else {
            this.currentAlpha -= delta;
            if (this.currentAlpha <= 0.33f) {
                this.currentAlpha = 0.33f;
                this.increasing = true;
            }
        }
        Stencil.initStencilToWrite();
        DisplayUtils.drawRoundedRect(this.getX() + 0.4f, this.getY() + 0.5f, this.getWidth() - 1.0f, this.getHeight() - 1.0f, 3.0f, ColorUtils.rgba(23, 23, 23, 84));
        Stencil.readStencilBuffer(0);
        Stencil.uninitStencilBuffer();
        int startColor = ColorUtils.rgba(14, 12, 12, 84);
        int endColor = ColorUtils.rgb(20, 18, 18);
        if (this.function.isState()) {
            DisplayUtils.drawRoundedRect(this.getX() + 2.0f, this.getY(), this.getWidth() - 4.0f, this.getHeight(), 3.0f, startColor);
        } else {
            DisplayUtils.drawRoundedRect(this.getX() + 2.0f, this.getY(), this.getWidth() - 4.0f, this.getHeight(), 3.0f, startColor);
        }
        if (MathUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), 20.0f)) {
            DisplayUtils.drawRoundedRect(this.getX() + 2.0f, this.getY(), this.getWidth() - 4.0f, this.getHeight(), 3.0f, ColorUtils.rgba(220, 220, 220, (int)(255.0f * this.currentAlpha)));
            if (!this.hovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.HAND);
                this.hovered = true;
            }
        } else {
            if (this.function.isState()) {
                DisplayUtils.drawRoundedRect(this.getX() + 2.0f, this.getY(), this.getWidth() - 4.0f, this.getHeight(), 3.0f, startColor);
            } else {
                DisplayUtils.drawRoundedRect(this.getX() + 2.0f, this.getY(), this.getWidth() - 4.0f, this.getHeight(), 3.0f, startColor);
            }
            if (this.hovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
                this.hovered = false;
            }
        }
        if (!this.isRightClicked || this.canBeOpened()) {
            // empty if block
        }
    }

    private void handleRightClick(float mouseX, float mouseY) {
        if (MathUtil.isHovered(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight()) && this.canBeOpened()) {
            this.isRightClicked = !this.isRightClicked;
        }
    }

    private void resetRightClick() {
        this.isRightClicked = false;
    }

    private boolean canBeOpened() {
        return this.components.stream().anyMatch(Component::isVisible);
    }

    private void drawText(MatrixStack stack, int color) {
        int[] gradientColors = new int[]{ColorUtils.getColor(90), ColorUtils.getColor(180), ColorUtils.getColor(270), ColorUtils.getColor(360)};
        String text = this.function.getName();
        int textLength = text.length();
        float totalTextWidth = 0.0f;
        for (int i = 0; i < textLength; ++i) {
            char c = text.charAt(i);
            String charStr = String.valueOf(c);
            totalTextWidth += Fonts.sfMedium.getWidth(charStr, 9.0f, 0.05f) * 0.8f;
        }
        float x = this.getX() + (this.getWidth() - totalTextWidth) / 2.0f - 50.0f + 50.0f;
        for (int i = 0; i < textLength; ++i) {
            int colorToUse;
            if (this.function.isState()) {
                float relativePosition = (float)i / (float)textLength;
                colorToUse = this.interpolateColor(gradientColors, relativePosition);
            } else {
                colorToUse = ColorUtils.rgb(255, 255, 255);
            }
            char c = text.charAt(i);
            String charStr = String.valueOf(c);
            Fonts.sfMedium.drawText(stack, charStr, x, this.getY() + 6.5f, colorToUse, 9.0f, 0.05f);
            x += Fonts.sfMedium.getWidth(charStr, 9.0f, 0.05f) * 0.8f;
        }
        if (this.components.stream().filter(Component::isVisible).count() >= 1L) {
            if (this.bind) {
                Fonts.montserrat.drawText(stack, this.function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(this.function.getBind()), this.getX() + this.getWidth() - 6.0f - Fonts.montserrat.getWidth(this.function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(this.function.getBind()), 6.0f, 0.1f), this.getY() + Fonts.icons.getHeight(6.0f) + 1.0f, ColorUtils.rgb(161, 164, 177), 6.0f, 0.1f);
            } else {
                Fonts.montserrat.drawText(stack, "...", this.getX() + this.getWidth() - 6.0f - Fonts.montserrat.getWidth("...", 7.0f, 0.1f), this.getY() + Fonts.icons.getHeight(7.0f) - 1.0f, ColorUtils.rgb(161, 164, 177), 7.0f, 0.1f);
            }
        } else if (this.bind) {
            Fonts.montserrat.drawCenteredText(stack, this.function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(this.function.getBind()), this.getX() + this.getWidth() - 6.0f - Fonts.montserrat.getWidth(this.function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(this.function.getBind()), 6.0f, 0.1f), this.getY() + Fonts.icons.getHeight(6.0f) + 1.0f, ColorUtils.rgb(161, 164, 177), 6.0f, 0.1f);
            this.sorted = true;
        }
    }

    private int interpolateColor(int[] colors, float position) {
        int numColors = colors.length;
        float segmentLength = 1.0f / (float)(numColors - 1);
        int segment = (int)(position / segmentLength);
        float segmentPosition = position % segmentLength / segmentLength;
        int startColor = colors[segment];
        int endColor = colors[Math.min(segment + 1, numColors - 1)];
        return this.interpolateColors(startColor, endColor, segmentPosition);
    }

    private int interpolateColors(int colorA, int colorB, float ratio) {
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

    public Vector4f getROUNDING_VECTOR() {
        return this.ROUNDING_VECTOR;
    }

    public Vector4i getBORDER_COLOR() {
        return this.BORDER_COLOR;
    }

    public Function getFunction() {
        return this.function;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean isBind() {
        return this.bind;
    }

    public float getCurrentAlpha() {
        return this.currentAlpha;
    }

    public boolean isIncreasing() {
        return this.increasing;
    }

    public boolean isSorted() {
        return this.sorted;
    }

    public boolean isState() {
        return this.state;
    }

    public boolean isRightClicked() {
        return this.isRightClicked;
    }

    public ObjectArrayList<Component> getComponents() {
        return this.components;
    }

    public boolean isHovered() {
        return this.hovered;
    }
}

