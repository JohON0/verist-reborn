/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown.impl;

import im.expensive.ui.dropdown.Panel;
import im.expensive.ui.dropdown.impl.IBuilder;

public class Component
implements IBuilder {
    private float x;
    private float y;
    private float width;
    private float height;
    private Panel panel;
    private String string;

    public boolean isHovered(float mouseX, float mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    public boolean isHovered(float mouseX, float mouseY, float height) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + height;
    }

    public boolean isVisible() {
        return true;
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

    public Panel getPanel() {
        return this.panel;
    }

    public String getString() {
        return this.string;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public void setString(String string) {
        this.string = string;
    }
}

