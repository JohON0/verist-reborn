/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Test", type=Category.Render)
public class block
extends Function {
    @Subscribe
    public void onDisplay(EventDisplay event) {
        MatrixStack ms = event.getMatrixStack();
        Minecraft mc = Minecraft.getInstance();
        RayTraceResult objectMouseOver = mc.objectMouseOver;
        if (objectMouseOver instanceof BlockRayTraceResult) {
            BlockPos pos = ((BlockRayTraceResult)objectMouseOver).getPos();
            block.renderBlockOutline(mc, pos, event.getPartialTicks(), ms);
        }
    }

    private static void renderBlockOutline(Minecraft mc, BlockPos pos, float partialTicks, MatrixStack matrixStack) {
        ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
        Vector3d viewPosition = renderInfo.getProjectedView();
        double x = (double)pos.getX() - viewPosition.x;
        double y = (double)pos.getY() - viewPosition.y;
        double z = (double)pos.getZ() - viewPosition.z;
        AxisAlignedBB box = Minecraft.world.getBlockState(pos).getShape(Minecraft.world, pos).getBoundingBox().offset(x, y, z);
        IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
        float red = 1.0f;
        float green = 1.0f;
        float blue = 1.0f;
        float alpha = 0.5f;
        RenderType outlineRenderType = RenderType.getLines();
        IVertexBuilder vertexBuilder = buffer.getBuffer(outlineRenderType);
        block.drawBoundingBox(matrixStack, vertexBuilder, box, red, green, blue, alpha);
        buffer.finish(outlineRenderType);
    }

    private static void drawBoundingBox(MatrixStack matrixStack, IVertexBuilder buffer, AxisAlignedBB bb, float red, float green, float blue, float alpha) {
        MatrixStack.Entry entry = matrixStack.getLast();
        Matrix4f matrix4f = entry.getMatrix();
        block.addLine(buffer, matrix4f, bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.minZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.minY, bb.minZ, bb.minX, bb.maxY, bb.minZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.minY, bb.minZ, bb.minX, bb.minY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.maxY, bb.minZ, bb.minX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.maxX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.minY, bb.maxZ, bb.maxX, bb.minY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.maxX, bb.minY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ, red, green, blue, alpha);
        block.addLine(buffer, matrix4f, bb.minX, bb.maxY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ, red, green, blue, alpha);
    }

    private static void addLine(IVertexBuilder buffer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        buffer.pos(matrix, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(matrix, (float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).endVertex();
    }

    public static Stream<BlockPos> getAllInBox(MutableBoundingBox box) {
        return (Stream)((Object)block.getAllInBox(Math.min(box.minX, box.maxX), Math.min(box.minY, box.maxY), Math.min(box.minZ, box.maxZ), Math.max(box.minX, box.maxX), Math.max(box.minY, box.maxY), Math.max(box.minZ, box.maxZ)));
    }

    public static Iterable<BlockPos> getAllInBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return BlockPos.getAllInBoxMutable(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

