/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Function;
import im.expensive.functions.impl.render.HUD;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.styles.Style;
import im.expensive.utils.client.KeyStorage;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import im.expensive.utils.text.GradientUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class KeyBindRenderer
implements ElementRenderer {
    private final Dragging dragging;
    private float animation;
    private float width;
    private float height;
    final ResourceLocation bind1 = new ResourceLocation("expensive/xz/bind1.png");
    final float iconSize = 10.0f;

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float fontSize = 6.5f;
        float padding = 5.0f;
        StringTextComponent name = GradientUtil.gradient("Hotkeys");
        float maxWidth = Fonts.sfbold.getWidth(name, 6.5f) + 10.0f;
        Style style = Expensive.getInstance().getStyleManager().getCurrentStyle();
        this.drawStyledRect(posX, posY + 1.0f, this.animation, 18.0f, 4.0f, 220);
        Fonts.sfui.drawText(ms, "HotKeys", posX + 20.0f, posY + 5.0f + 1.5f, ColorUtils.rgb(255, 255, 255), 6.5f);
        Vector4i colors = new Vector4i(HUD.getColor(0, 1.0f), HUD.getColor(90, 1.0f), HUD.getColor(180, 1.0f), HUD.getColor(270, 1.0f));
        DisplayUtils.drawImage(this.bind1, posX + 5.0f, posY + 5.0f, 10.0f, 10.0f, ColorUtils.rgb(255, 255, 255));
        DisplayUtils.drawRectVerticalW(posX + 18.0f, posY + 3.0f, this.width - 1.0f, 14.0, 3, ColorUtils.rgba(255, 255, 255, 191));
        posY += 16.5f;
        float localHeight = 16.5f;
        posY += 3.0f;
        for (Function f : Expensive.getInstance().getFunctionRegistry().getFunctions()) {
            f.getAnimation().update();
            if (!(f.getAnimation().getValue() > 0.0) || f.getBind() == 0) continue;
            String nameText = f.getName();
            float nameWidth = Fonts.sfbold.getWidth(nameText, 6.5f);
            String bindText = "[" + KeyStorage.getKey(f.getBind()) + "]";
            float bindWidth = Fonts.sfbold.getWidth(bindText, 6.5f);
            float localWidth = nameWidth + bindWidth + 15.0f;
            this.drawStyledRect(posX, posY, this.animation, 12.0f, 3.0f, ColorUtils.rgba(220, 220, 220, (int)(220.0 * f.getAnimation().getValue())));
            Fonts.sfui.drawText(ms, nameText, posX + 4.0f, posY + 2.5f, ColorUtils.rgba(255, 255, 255, (int)(255.0 * f.getAnimation().getValue())), 6.5f);
            Fonts.sfui.drawText(ms, bindText, posX + this.animation - 4.0f - bindWidth, posY + 2.5f, ColorUtils.rgba(255, 255, 255, (int)(255.0 * f.getAnimation().getValue())), 6.5f);
            float lineXPosition = posX + this.animation - bindWidth - 8.0f;
            DisplayUtils.drawRectVerticalW(lineXPosition, posY + 2.0f, this.width - 1.0f, 8.0, 3, ColorUtils.rgba(255, 255, 255, 191));
            if (localWidth > maxWidth) {
                maxWidth = localWidth;
            }
            posY += (float)(12.5 * f.getAnimation().getValue());
            localHeight += (float)(11.5 * f.getAnimation().getValue());
        }
        this.animation = MathUtil.lerp(this.animation, Math.max(maxWidth, 80.0f), 10.0f);
        this.height = localHeight + 2.5f;
        this.dragging.setWidth(this.animation);
        this.dragging.setHeight(this.height);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }

    public static void sizeAnimation(double width, double height, double scale) {
        GlStateManager.translated(width, height, 0.0);
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-width, -height, 0.0);
    }

    public KeyBindRenderer(Dragging dragging) {
        this.dragging = dragging;
    }
}

