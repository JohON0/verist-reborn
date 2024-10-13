/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="Predictions", type=Category.Render)
public class Predictions
extends Function {
    @Subscribe
    public void onRender(WorldEvent event) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        Vector3d renderOffset = Predictions.mc.getRenderManager().info.getProjectedView();
        GL11.glTranslated(-renderOffset.x, -renderOffset.y, -renderOffset.z);
        GL11.glLineWidth(3.0f);
        buffer.begin(1, DefaultVertexFormats.POSITION);
        block0: for (Entity entity2 : Minecraft.world.getAllEntities()) {
            if (!(entity2 instanceof EnderPearlEntity) && !(entity2 instanceof TridentEntity) && !(entity2 instanceof ArrowEntity)) continue;
            Vector3d motion = entity2.getMotion();
            Vector3d pos = entity2.getPositionVec();
            boolean drawLine = true;
            for (int i = 0; i < 150; ++i) {
                Vector3d prevPos = pos;
                pos = pos.add(motion);
                motion = this.getNextMotion(entity2, motion);
                if (drawLine) {
                    boolean isLast;
                    ColorUtils.setColor(HUD.getColor(i));
                    buffer.pos(prevPos.x, prevPos.y, prevPos.z).endVertex();
                    RayTraceContext rayTraceContext = new RayTraceContext(prevPos, pos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity2);
                    BlockRayTraceResult blockHitResult = Minecraft.world.rayTraceBlocks(rayTraceContext);
                    boolean bl = isLast = blockHitResult.getType() == RayTraceResult.Type.BLOCK;
                    if (isLast) {
                        pos = blockHitResult.getHitVec();
                    }
                    buffer.pos(pos.x, pos.y, pos.z).endVertex();
                    if (isLast || pos.y < 0.0) continue block0;
                }
                drawLine = !drawLine;
            }
        }
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    private Vector3d getNextMotion(Entity entity2, Vector3d motion) {
        motion = entity2.isInWater() ? motion.scale(0.8) : motion.scale(0.99);
        if (!entity2.hasNoGravity()) {
            motion = new Vector3d(motion.x, motion.y - this.getGravity(entity2), motion.z);
        }
        return motion;
    }

    private double getGravity(Entity entity2) {
        if (entity2 instanceof ArrowEntity) {
            return 0.05;
        }
        if (entity2 instanceof TridentEntity) {
            return 0.03;
        }
        if (entity2 instanceof ThrowableEntity) {
            return ((ThrowableEntity)entity2).getGravityVelocity();
        }
        return 0.03;
    }
}

