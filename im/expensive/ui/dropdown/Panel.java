/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.Expensive;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.ui.dropdown.DropDown;
import im.expensive.ui.dropdown.SortCriteria;
import im.expensive.ui.dropdown.components.ModuleComponent;
import im.expensive.ui.dropdown.impl.Component;
import im.expensive.ui.dropdown.impl.IBuilder;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.impl.EaseBackIn;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.font.Fonts;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;

public class Panel
implements IBuilder {
    private final Category category;
    protected float x;
    protected float y;
    protected final float width = 125.0f;
    protected final float height = 320.0f;
    private final Animation textAnimation = new EaseBackIn(400, 1.0, 1.0f);
    private List<ModuleComponent> modules = new ArrayList<ModuleComponent>();
    private float scroll;
    private float animatedScroll;
    private SortCriteria sortCriteria = SortCriteria.NAME_LENGTH_DESC;
    float max = 0.0f;

    public Panel(Category category) {
        this.category = category;
        for (Function function : Expensive.getInstance().getFunctionRegistry().getFunctions()) {
            if (function.getCategory() != category) continue;
            ModuleComponent component = new ModuleComponent(function);
            component.setPanel(this);
            this.modules.add(component);
        }
        this.sortModules();
        this.textAnimation.start();
    }

    private void sortModules() {
        switch (this.sortCriteria) {
            case NAME_LENGTH_DESC: {
                this.modules.sort((o1, o2) -> Integer.compare(o2.getFunction().getName().length(), o1.getFunction().getName().length()));
                break;
            }
            case NAME_LENGTH_ASC: {
                this.modules.sort((o1, o2) -> Integer.compare(o1.getFunction().getName().length(), o2.getFunction().getName().length()));
                break;
            }
            case NAME_ASC: {
                this.modules.sort(Comparator.comparing(o -> o.getFunction().getName()));
                break;
            }
            case NAME_DESC: {
                this.modules.sort((o1, o2) -> o2.getFunction().getName().compareTo(o1.getFunction().getName()));
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + this.sortCriteria);
            }
        }
    }

    public void setSortCriteria(SortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
        this.sortModules();
    }

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        this.animatedScroll = MathUtil.fast(this.animatedScroll, this.scroll, 10.0f);
        float header = 20.0f;
        float headerFont = 9.0f;
        float textX = this.x + 62.5f;
        this.drawStyledRect(this.x + 1.0f, this.y + 5.0f, 123.0f, 315.0f, 7.0f, 220);
        Fonts.montserrat.drawCenteredText(stack, this.category.name(), textX, this.y + header / 2.0f - Fonts.montserrat.getHeight(headerFont) / 2.0f - 1.0f + 6.0f, ColorUtils.rgba(255, 255, 255, 255), headerFont, 0.1f);
        DisplayUtils.drawRoundedRect(this.x, this.y + 320.0f - header, 125.0f, header, new Vector4f(0.0f, 7.0f, 0.0f, 7.0f), new Vector4i(3, ColorUtils.rgba(23, 23, 23, 127), 3, ColorUtils.rgba(23, 23, 23, 127)));
        this.drawComponents(stack, mouseX, mouseY);
        if (this.max > 320.0f - header - 10.0f) {
            this.drawScrollbar(stack);
        }
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(32, 32, 32, alpha));
    }

    private void drawScrollbar(MatrixStack stack) {
        float header = 20.0f;
        float scrollbarHeight = 280.0f;
        float scrollbarPosition = this.y + 25.0f;
        float maxScroll = this.max - (320.0f - header - 10.0f);
        float scrollbarThumbHeight = Math.max(20.0f, scrollbarHeight * (320.0f - header - 10.0f) / this.max);
        float scrollbarY = scrollbarPosition + -this.animatedScroll / maxScroll * (scrollbarHeight - scrollbarThumbHeight);
        DisplayUtils.drawRoundedRect(this.x + 125.0f - 5.0f, scrollbarY, 2.0f, scrollbarThumbHeight, new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), ColorUtils.rgba(170, 170, 170, 255));
    }

    private void drawComponents(MatrixStack stack, float mouseX, float mouseY) {
        float animationValue = (float)DropDown.getAnimation().getValue() * DropDown.scale;
        float halfAnimationValueRest = (1.0f - animationValue) / 2.0f;
        float height = this.getHeight();
        float testX = this.getX() + this.getWidth() * halfAnimationValueRest;
        float testY = this.getY() + 20.0f + height * halfAnimationValueRest;
        float testW = this.getWidth() * animationValue;
        float testH = height * animationValue;
        testX = testX * animationValue + ((float)Minecraft.getInstance().getMainWindow().getScaledWidth() - testW) * halfAnimationValueRest;
        Scissor.push();
        Scissor.setFromComponentCoordinates(testX, testY, testW, testH);
        float offset = -1.0f;
        float header = 20.0f;
        if (this.max > height - header - 10.0f) {
            this.scroll = MathHelper.clamp(this.scroll, -this.max + height - header - 10.0f, 0.0f);
            this.animatedScroll = MathHelper.clamp(this.animatedScroll, -this.max + height - header - 10.0f, 0.0f);
        } else {
            this.scroll = 0.0f;
            this.animatedScroll = 0.0f;
        }
        Iterator<ModuleComponent> var13 = this.modules.iterator();
        while (true) {
            if (!var13.hasNext()) {
                this.max = offset;
                Scissor.unset();
                Scissor.pop();
                return;
            }
            ModuleComponent component = var13.next();
            if (Expensive.getInstance().getDropDown().searchCheck(component.getFunction().getName())) continue;
            component.setX(this.getX() + 5.0f);
            component.setY(this.getY() + header + offset + 6.0f + this.animatedScroll);
            component.setWidth(this.getWidth() - 10.0f);
            component.setHeight(20.0f);
            component.animation.update();
            if (component.animation.getValue() > 0.0) {
                float componentOffset = 0.0f;
                for (Component component2 : component.getComponents()) {
                    if (!component2.isVisible()) continue;
                    componentOffset += component2.getHeight();
                }
                componentOffset = (float)((double)componentOffset * component.animation.getValue());
                component.setHeight(component.getHeight() + componentOffset);
            }
            component.render(stack, mouseX, mouseY);
            offset += component.getHeight() + 1.0f;
        }
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int button) {
        float header = 20.0f;
        float footerHeight = 10.0f;
        float topBound = this.y + header;
        float bottomBound = this.y + 320.0f - footerHeight;
        if (!(mouseY < topBound) && !(mouseY > bottomBound)) {
            for (ModuleComponent component : this.modules) {
                if (Expensive.getInstance().getDropDown().searchCheck(component.getFunction().getName())) continue;
                component.mouseClick(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyPressed(int key, int scanCode, int modifiers) {
        for (ModuleComponent component : this.modules) {
            component.keyPressed(key, scanCode, modifiers);
        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        for (ModuleComponent component : this.modules) {
            component.charTyped(codePoint, modifiers);
        }
    }

    @Override
    public void mouseRelease(float mouseX, float mouseY, int button) {
        float header = 20.0f;
        float footerHeight = 40.0f;
        float topBound = this.y + header;
        float bottomBound = this.y + 320.0f - footerHeight;
        if (!(mouseY < topBound) && !(mouseY > bottomBound)) {
            for (ModuleComponent component : this.modules) {
                component.mouseRelease(mouseX, mouseY, button);
            }
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Animation getTextAnimation() {
        return this.textAnimation;
    }

    public List<ModuleComponent> getModules() {
        return this.modules;
    }

    public float getScroll() {
        return this.scroll;
    }

    public float getAnimatedScroll() {
        return this.animatedScroll;
    }

    public SortCriteria getSortCriteria() {
        return this.sortCriteria;
    }

    public float getMax() {
        return this.max;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setModules(List<ModuleComponent> modules) {
        this.modules = modules;
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    public void setAnimatedScroll(float animatedScroll) {
        this.animatedScroll = animatedScroll;
    }

    public void setMax(float max2) {
        this.max = max2;
    }
}

