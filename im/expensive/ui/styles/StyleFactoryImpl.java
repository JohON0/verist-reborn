/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.styles;

import im.expensive.ui.styles.Style;
import im.expensive.ui.styles.StyleFactory;
import java.awt.Color;

public class StyleFactoryImpl
implements StyleFactory {
    @Override
    public Style createStyle(String name, Color firstColor, Color secondColor) {
        return new Style(name, firstColor, secondColor);
    }
}

