/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.styles;

import im.expensive.ui.styles.Style;
import java.util.List;

public class StyleManager {
    private final List<Style> styleList;
    private Style currentStyle;

    public StyleManager(List<Style> styleList, Style currentStyle) {
        this.styleList = styleList;
        this.currentStyle = currentStyle;
    }

    public List<Style> getStyleList() {
        return this.styleList;
    }

    public Style getCurrentStyle() {
        return this.currentStyle;
    }

    public void setCurrentStyle(Style currentStyle) {
        this.currentStyle = currentStyle;
    }
}

