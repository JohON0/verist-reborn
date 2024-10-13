/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.drag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.api.Function;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.Vec2i;
import im.expensive.utils.math.MathUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.AbstractGui;

public class Dragging {
    @Expose
    @SerializedName(value="x")
    private float xPos;
    @Expose
    @SerializedName(value="y")
    private float yPos;
    public float initialXVal;
    public float initialYVal;
    private float startX;
    private float startY;
    private boolean dragging;
    private float width;
    private float height;
    private boolean showGrid = false;
    @Expose
    @SerializedName(value="name")
    private final String name;
    private final Function module;
    private final int gridRows = 5;
    private final int gridCols = 5;
    private final float cellWidth = 100.0f;
    private final float cellHeight = 100.0f;

    public Dragging(Function module, String name, float initialXVal, float initialYVal) {
        this.module = module;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }

    public Function getModule() {
        return this.module;
    }

    public String getName() {
        return this.name;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return this.xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return this.yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public final void onDraw(int mouseX, int mouseY, MainWindow res, MatrixStack matrixStack) {
        Vec2i fixed = ClientUtil.getMouse(mouseX, mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        if (this.dragging) {
            this.xPos = (float)mouseX - this.startX;
            this.yPos = (float)mouseY - this.startY;
            int cellWidth = 20;
            int cellHeight = 20;
            this.xPos = Math.round(this.xPos / (float)cellWidth) * cellWidth;
            this.yPos = Math.round(this.yPos / (float)cellHeight) * cellHeight;
            if (this.xPos + this.width > (float)res.getScaledWidth()) {
                this.xPos = (float)res.getScaledWidth() - this.width;
            }
            if (this.yPos + this.height > (float)res.getScaledHeight()) {
                this.yPos = (float)res.getScaledHeight() - this.height;
            }
            if (this.xPos < 0.0f) {
                this.xPos = 0.0f;
            }
            if (this.yPos < 0.0f) {
                this.yPos = 0.0f;
            }
            this.drawGrid(matrixStack, res);
            this.drawAlignmentLines(matrixStack, res);
        }
    }

    public final boolean onClick(double mouseX, double mouseY, int button) {
        Vec2i fixed = ClientUtil.getMouse((int)mouseX, (int)mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        if (button == 0 && MathUtil.isHovered((float)mouseX, (float)mouseY, this.xPos, this.yPos, this.width, this.height)) {
            this.dragging = true;
            this.showGrid = true;
            this.startX = (int)(mouseX - (double)this.xPos);
            this.startY = (int)(mouseY - (double)this.yPos);
            return true;
        }
        return false;
    }

    public final void onRelease(int button) {
        if (button == 0) {
            this.dragging = false;
            this.showGrid = false;
        }
    }

    public void drawGrid(MatrixStack matrixStack, MainWindow res) {
        if (!this.showGrid) {
            return;
        }
        int cellWidth = 20;
        int cellHeight = 20;
        int alpha = 0;
        int gridColor = alpha << 24 | 0xAAAAAA;
        for (int i = 0; i < res.getScaledWidth(); i += cellWidth) {
            AbstractGui.fill(matrixStack, i, 0, i + 1, res.getScaledHeight(), gridColor);
        }
        for (int j = 0; j < res.getScaledHeight(); j += cellHeight) {
            AbstractGui.fill(matrixStack, 0, j, res.getScaledWidth(), j + 1, gridColor);
        }
    }

    public void drawAlignmentLines(MatrixStack matrixStack, MainWindow res) {
        if (!this.showGrid) {
            return;
        }
        int alpha = 255;
        int lineColor = alpha << 24 | 0xAAAAAA;
        AbstractGui.fill(matrixStack, (int)this.xPos, 0, (int)this.xPos + 1, res.getScaledHeight(), lineColor);
        AbstractGui.fill(matrixStack, 0, (int)this.yPos, res.getScaledWidth(), (int)this.yPos + 1, lineColor);
    }
}

