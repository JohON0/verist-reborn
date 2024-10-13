/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="ItemRadius", type=Category.Render)
public class ItemRadius
extends Function {
    private final Minecraft mc = Minecraft.getInstance();

    @Subscribe
    private void onRender(WorldEvent event) {
        ClientPlayerEntity player = Minecraft.player;
        ItemStack mainHandItem = player.getHeldItemMainhand();
        ItemStack offHandItem = Minecraft.player.getHeldItemOffhand();
        if ((player == null || mainHandItem.isEmpty() || mainHandItem.getItem() != Items.ENDER_EYE) && (offHandItem.isEmpty() || offHandItem.getItem() != Items.ENDER_EYE)) {
            if (player != null && !mainHandItem.isEmpty() && mainHandItem.getItem() == Items.SUGAR || !offHandItem.isEmpty() && offHandItem.getItem() == Items.SUGAR) {
                float xOffset;
                int z;
                float radius = 10.0f;
                GlStateManager.pushMatrix();
                RenderSystem.translated(-this.mc.getRenderManager().info.getProjectedView().x, -this.mc.getRenderManager().info.getProjectedView().y, -this.mc.getRenderManager().info.getProjectedView().z);
                Vector3d position = MathUtil.interpolate(Minecraft.player.getPositionVec(), new Vector3d(Minecraft.player.lastTickPosX, Minecraft.player.lastTickPosY, Minecraft.player.lastTickPosZ), event.getPartialTicks());
                position = position.add(0.0, -1.4, 0.0);
                RenderSystem.translated(position.x, position.y + (double)Minecraft.player.getHeight(), position.z);
                double pitch = this.mc.getRenderManager().info.getPitch();
                double yaw = this.mc.getRenderManager().info.getYaw();
                GL11.glRotatef((float)(-yaw), 0.0f, 1.0f, 0.0f);
                RenderSystem.translated(-position.x, -(position.y + (double)Minecraft.player.getHeight()), -position.z);
                RenderSystem.enableBlend();
                RenderSystem.depthMask(false);
                RenderSystem.disableTexture();
                RenderSystem.disableCull();
                RenderSystem.blendFunc(770, 771);
                RenderSystem.shadeModel(7425);
                RenderSystem.lineWidth(3.0f);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int x = 0x22FFFFFF;
                buffer.pos(position.x, position.y + (double)Minecraft.player.getHeight(), position.z).color(x).endVertex();
                for (z = 0; z <= 360; ++z) {
                    float angle = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(z)) * radius));
                    xOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(z)) * radius));
                    buffer.pos(angle, position.y + (double)Minecraft.player.getHeight(), xOffset).color(x).endVertex();
                }
                tessellator.draw();
                buffer.begin(2, DefaultVertexFormats.POSITION_COLOR);
                z = -1996488705;
                for (int loopIndex = 0; loopIndex <= 360; ++loopIndex) {
                    xOffset = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(loopIndex)) * radius));
                    float zOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(loopIndex)) * radius));
                    buffer.pos(xOffset, position.y + (double)Minecraft.player.getHeight(), zOffset).color(z).endVertex();
                }
                tessellator.draw();
                GL11.glHint(3154, 4352);
                GL11.glDisable(2848);
                RenderSystem.enableTexture();
                RenderSystem.disableBlend();
                RenderSystem.enableCull();
                RenderSystem.depthMask(true);
                RenderSystem.shadeModel(7424);
                GlStateManager.popMatrix();
            } else if ((player == null || mainHandItem.isEmpty() || mainHandItem.getItem() != Items.FIRE_CHARGE) && (offHandItem.isEmpty() || offHandItem.getItem() != Items.FIRE_CHARGE)) {
                if (player != null && !mainHandItem.isEmpty() && mainHandItem.getItem() == Items.PHANTOM_MEMBRANE || !offHandItem.isEmpty() && offHandItem.getItem() == Items.PHANTOM_MEMBRANE) {
                    float xOffset;
                    int z;
                    float radius = 2.0f;
                    GlStateManager.pushMatrix();
                    RenderSystem.translated(-this.mc.getRenderManager().info.getProjectedView().x, -this.mc.getRenderManager().info.getProjectedView().y, -this.mc.getRenderManager().info.getProjectedView().z);
                    Vector3d position = MathUtil.interpolate(Minecraft.player.getPositionVec(), new Vector3d(Minecraft.player.lastTickPosX, Minecraft.player.lastTickPosY, Minecraft.player.lastTickPosZ), event.getPartialTicks());
                    position = position.add(0.0, -1.4, 0.0);
                    RenderSystem.translated(position.x, position.y + (double)Minecraft.player.getHeight(), position.z);
                    double pitch = this.mc.getRenderManager().info.getPitch();
                    double yaw = this.mc.getRenderManager().info.getYaw();
                    GL11.glRotatef((float)(-yaw), 0.0f, 1.0f, 0.0f);
                    RenderSystem.translated(-position.x, -(position.y + (double)Minecraft.player.getHeight()), -position.z);
                    RenderSystem.enableBlend();
                    RenderSystem.depthMask(false);
                    RenderSystem.disableTexture();
                    RenderSystem.disableCull();
                    RenderSystem.blendFunc(770, 771);
                    RenderSystem.shadeModel(7425);
                    RenderSystem.lineWidth(3.0f);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                    int x = 0x220000FF;
                    buffer.pos(position.x, position.y + (double)Minecraft.player.getHeight(), position.z).color(x).endVertex();
                    for (z = 0; z <= 360; ++z) {
                        float angle = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(z)) * radius));
                        xOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(z)) * radius));
                        buffer.pos(angle, position.y + (double)Minecraft.player.getHeight(), xOffset).color(x).endVertex();
                    }
                    tessellator.draw();
                    buffer.begin(2, DefaultVertexFormats.POSITION_COLOR);
                    z = -2013265665;
                    for (int loopIndex = 0; loopIndex <= 360; ++loopIndex) {
                        xOffset = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(loopIndex)) * radius));
                        float zOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(loopIndex)) * radius));
                        buffer.pos(xOffset, position.y + (double)Minecraft.player.getHeight(), zOffset).color(z).endVertex();
                    }
                    tessellator.draw();
                    GL11.glHint(3154, 4352);
                    GL11.glDisable(2848);
                    RenderSystem.enableTexture();
                    RenderSystem.disableBlend();
                    RenderSystem.enableCull();
                    RenderSystem.depthMask(true);
                    RenderSystem.shadeModel(7424);
                    GlStateManager.popMatrix();
                }
            } else {
                float xOffset;
                int z;
                float radius = 10.0f;
                GlStateManager.pushMatrix();
                RenderSystem.translated(-this.mc.getRenderManager().info.getProjectedView().x, -this.mc.getRenderManager().info.getProjectedView().y, -this.mc.getRenderManager().info.getProjectedView().z);
                Vector3d position = MathUtil.interpolate(Minecraft.player.getPositionVec(), new Vector3d(Minecraft.player.lastTickPosX, Minecraft.player.lastTickPosY, Minecraft.player.lastTickPosZ), event.getPartialTicks());
                position = position.add(0.0, -1.4, 0.0);
                RenderSystem.translated(position.x, position.y + (double)Minecraft.player.getHeight(), position.z);
                double pitch = this.mc.getRenderManager().info.getPitch();
                double yaw = this.mc.getRenderManager().info.getYaw();
                GL11.glRotatef((float)(-yaw), 0.0f, 1.0f, 0.0f);
                RenderSystem.translated(-position.x, -(position.y + (double)Minecraft.player.getHeight()), -position.z);
                RenderSystem.enableBlend();
                RenderSystem.depthMask(false);
                RenderSystem.disableTexture();
                RenderSystem.disableCull();
                RenderSystem.blendFunc(770, 771);
                RenderSystem.shadeModel(7425);
                RenderSystem.lineWidth(3.0f);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int x = 587169536;
                buffer.pos(position.x, position.y + (double)Minecraft.player.getHeight(), position.z).color(x).endVertex();
                for (z = 0; z <= 360; ++z) {
                    float angle = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(z)) * radius));
                    xOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(z)) * radius));
                    buffer.pos(angle, position.y + (double)Minecraft.player.getHeight(), xOffset).color(x).endVertex();
                }
                tessellator.draw();
                buffer.begin(2, DefaultVertexFormats.POSITION_COLOR);
                z = -1996521728;
                for (int loopIndex = 0; loopIndex <= 360; ++loopIndex) {
                    xOffset = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(loopIndex)) * radius));
                    float zOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(loopIndex)) * radius));
                    buffer.pos(xOffset, position.y + (double)Minecraft.player.getHeight(), zOffset).color(z).endVertex();
                }
                tessellator.draw();
                GL11.glHint(3154, 4352);
                GL11.glDisable(2848);
                RenderSystem.enableTexture();
                RenderSystem.disableBlend();
                RenderSystem.enableCull();
                RenderSystem.depthMask(true);
                RenderSystem.shadeModel(7424);
                GlStateManager.popMatrix();
            }
        } else {
            float xOffset;
            int z;
            float radius = 10.0f;
            GlStateManager.pushMatrix();
            RenderSystem.translated(-this.mc.getRenderManager().info.getProjectedView().x, -this.mc.getRenderManager().info.getProjectedView().y, -this.mc.getRenderManager().info.getProjectedView().z);
            Vector3d position = MathUtil.interpolate(Minecraft.player.getPositionVec(), new Vector3d(Minecraft.player.lastTickPosX, Minecraft.player.lastTickPosY, Minecraft.player.lastTickPosZ), event.getPartialTicks());
            position = position.add(0.0, -1.4, 0.0);
            RenderSystem.translated(position.x, position.y + (double)Minecraft.player.getHeight(), position.z);
            double pitch = this.mc.getRenderManager().info.getPitch();
            double yaw = this.mc.getRenderManager().info.getYaw();
            GL11.glRotatef((float)(-yaw), 0.0f, 1.0f, 0.0f);
            RenderSystem.translated(-position.x, -(position.y + (double)Minecraft.player.getHeight()), -position.z);
            RenderSystem.enableBlend();
            RenderSystem.depthMask(false);
            RenderSystem.disableTexture();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(770, 771);
            RenderSystem.shadeModel(7425);
            RenderSystem.lineWidth(3.0f);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            buffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            int x = 0x2200FFFF;
            buffer.pos(position.x, position.y + (double)Minecraft.player.getHeight(), position.z).color(x).endVertex();
            for (z = 0; z <= 360; ++z) {
                float angle = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(z)) * radius));
                xOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(z)) * radius));
                buffer.pos(angle, position.y + (double)Minecraft.player.getHeight(), xOffset).color(x).endVertex();
            }
            tessellator.draw();
            buffer.begin(2, DefaultVertexFormats.POSITION_COLOR);
            z = -2013200385;
            for (int loopIndex = 0; loopIndex <= 360; ++loopIndex) {
                xOffset = (float)(position.x + (double)(MathHelper.sin((float)Math.toRadians(loopIndex)) * radius));
                float zOffset = (float)(position.z + (double)(-MathHelper.cos((float)Math.toRadians(loopIndex)) * radius));
                buffer.pos(xOffset, position.y + (double)Minecraft.player.getHeight(), zOffset).color(z).endVertex();
            }
            tessellator.draw();
            GL11.glHint(3154, 4352);
            GL11.glDisable(2848);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            RenderSystem.shadeModel(7424);
            GlStateManager.popMatrix();
        }
    }
}

