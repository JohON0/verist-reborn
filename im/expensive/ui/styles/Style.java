/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.styles;

import java.awt.Color;

public class Style {
    private String styleName;
    private Color firstColor;
    private Color secondColor;

    public Style(String styleName, Color firstColor, Color secondColor) {
        this.styleName = styleName;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    public String getStyleName() {
        return this.styleName;
    }

    public Color getFirstColor() {
        return this.firstColor;
    }

    public Color getSecondColor() {
        return this.secondColor;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }

    public String toString() {
        return "Style(styleName=" + this.getStyleName() + ", firstColor=" + this.getFirstColor() + ", secondColor=" + this.getSecondColor() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Style)) {
            return false;
        }
        Style other = (Style)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$styleName = this.getStyleName();
        String other$styleName = other.getStyleName();
        if (this$styleName == null ? other$styleName != null : !this$styleName.equals(other$styleName)) {
            return false;
        }
        Color this$firstColor = this.getFirstColor();
        Color other$firstColor = other.getFirstColor();
        if (this$firstColor == null ? other$firstColor != null : !((Object)this$firstColor).equals(other$firstColor)) {
            return false;
        }
        Color this$secondColor = this.getSecondColor();
        Color other$secondColor = other.getSecondColor();
        return !(this$secondColor == null ? other$secondColor != null : !((Object)this$secondColor).equals(other$secondColor));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Style;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $styleName = this.getStyleName();
        result = result * 59 + ($styleName == null ? 43 : $styleName.hashCode());
        Color $firstColor = this.getFirstColor();
        result = result * 59 + ($firstColor == null ? 43 : ((Object)$firstColor).hashCode());
        Color $secondColor = this.getSecondColor();
        result = result * 59 + ($secondColor == null ? 43 : ((Object)$secondColor).hashCode());
        return result;
    }
}

