/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.util.math.vector.Matrix4f;

public class EventRender {
    public float partialTicks;
    public MainWindow scaledResolution;
    public Type type;
    public MatrixStack matrixStack;
    public Matrix4f matrix;

    public EventRender(float partialTicks, MatrixStack stack, MainWindow scaledResolution, Type type, Matrix4f matrix) {
        this.partialTicks = partialTicks;
        this.scaledResolution = scaledResolution;
        this.matrixStack = stack;
        this.type = type;
        this.matrix = matrix;
    }

    public boolean isRender3D() {
        return this.type == Type.RENDER3D;
    }

    public boolean isRender2D() {
        return this.type == Type.RENDER2D;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public MainWindow getScaledResolution() {
        return this.scaledResolution;
    }

    public Type getType() {
        return this.type;
    }

    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }

    public Matrix4f getMatrix() {
        return this.matrix;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setScaledResolution(MainWindow scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public void setMatrix(Matrix4f matrix) {
        this.matrix = matrix;
    }

    public static enum Type {
        RENDER3D,
        RENDER2D;

    }
}

