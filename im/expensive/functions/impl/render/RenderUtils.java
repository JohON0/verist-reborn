/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void drawOutlinedBoundingBox(AxisAlignedBB bb, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        double x = bb.minX;
        double y = bb.minY;
        double z = bb.minZ;
        double x1 = bb.maxX;
        double y1 = bb.maxY;
        double z1 = bb.maxZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5f);
        buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y1, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y1, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y1, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y1, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y1, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y1, z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x1, y1, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        buffer.pos(x, y1, z1).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
}

