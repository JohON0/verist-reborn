/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

public class GradientColor {
    public static void main(String[] args2) {
        int hp = 15;
        int color = 20;
        Color[] colors = new Color[]{new Color(2, 255, 2, 255), new Color(117, 148, 74, 255), new Color(148, 142, 74, 255), new Color(148, 118, 74, 255), new Color(148, 105, 74, 255), new Color(164, 92, 61, 255), new Color(159, 79, 50, 255), new Color(159, 66, 50, 255), new Color(185, 55, 40, 255), new Color(255, 26, 0, 255)};
        Color startColor = Color.BLACK;
        Color endColor = colors[0];
        GradientPaint gradient = new GradientPaint(new Point2D.Double(0.0, 0.0), startColor, new Point2D.Double(1.0, 0.0), endColor);
        if (hp >= 20) {
            color = colors[0].getRGB();
        } else if (hp >= 18) {
            color = colors[1].getRGB();
        } else if (hp >= 16) {
            color = colors[2].getRGB();
        } else if (hp >= 14) {
            color = colors[3].getRGB();
        } else if (hp >= 12) {
            color = colors[4].getRGB();
        } else if (hp >= 10) {
            color = colors[5].getRGB();
        } else if (hp >= 8) {
            color = colors[6].getRGB();
        } else if (hp >= 6) {
            color = colors[7].getRGB();
        } else if (hp >= 4) {
            color = colors[8].getRGB();
        } else if (hp >= 2) {
            color = colors[9].getRGB();
        }
        System.out.println("Color: " + color);
    }
}

