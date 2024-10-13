/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BlurUtil {
    public static BufferedImage applyGaussianBlur(BufferedImage img) {
        float[] matrix = new float[]{0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f};
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, 1, null);
        return op.filter(img, null);
    }

    public static void main(String[] args2) {
        try {
            BufferedImage img = ImageIO.read(new File("expensive/images/glow1"));
            BufferedImage blurredImg = BlurUtil.applyGaussianBlur(img);
            ImageIO.write((RenderedImage)blurredImg, "png", new File("expensive/images/glow1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

