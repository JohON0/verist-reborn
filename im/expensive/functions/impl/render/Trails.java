/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.render.ColorUtils;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="Trails", type=Category.Render)
public class Trails
extends Function {
    private final BooleanSetting showInFirstPerson = new BooleanSetting("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u0432 \u043f\u0435\u0440\u0432\u043e\u043c \u043b\u0438\u0446\u0435", false);

    public Trails() {
        this.addSettings(this.showInFirstPerson);
    }

    @Subscribe
    public void onRender(WorldEvent event) {
        for (PlayerEntity playerEntity : Minecraft.world.getPlayers()) {
            playerEntity.points.removeIf(p -> p.time.isReached(500L));
            if (!(playerEntity instanceof ClientPlayerEntity)) continue;
            if (playerEntity == Minecraft.player && Trails.mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON && !((Boolean)this.showInFirstPerson.get()).booleanValue()) continue;
            Vector3d player = new Vector3d(MathUtil.interpolate(playerEntity.getPosX(), playerEntity.lastTickPosX, (double)event.getPartialTicks()), MathUtil.interpolate(playerEntity.getPosY(), playerEntity.lastTickPosY, (double)event.getPartialTicks()), MathUtil.interpolate(playerEntity.getPosZ(), playerEntity.lastTickPosZ, (double)event.getPartialTicks()));
            if (playerEntity == Minecraft.player && Trails.mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                player = new Vector3d(playerEntity.prevPosX + (playerEntity.getPosX() - playerEntity.prevPosX) * (double)event.getPartialTicks(), playerEntity.prevPosY + (playerEntity.getPosY() - playerEntity.prevPosY) * (double)event.getPartialTicks(), playerEntity.prevPosZ + (playerEntity.getPosZ() - playerEntity.prevPosZ) * (double)event.getPartialTicks());
            }
            playerEntity.points.add(new Point(player));
        }
        RenderSystem.pushMatrix();
        Vector3d projection = Trails.mc.getRenderManager().info.getProjectedView();
        RenderSystem.translated(-projection.x, -projection.y, -projection.z);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableTexture();
        RenderSystem.blendFunc(770, 771);
        RenderSystem.shadeModel(7425);
        RenderSystem.disableAlphaTest();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(3.0f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        for (Entity entity3 : Minecraft.world.getAllEntities()) {
            float alpha;
            float index;
            GL11.glBegin(8);
            ArrayList<Point> points = entity3.points;
            for (Point point : points) {
                index = points.indexOf(point);
                alpha = index / (float)points.size();
                ColorUtils.setAlphaColor(HUD.getColor(0, 1.0f), alpha * 0.5f);
                GL11.glVertex3d(point.getPosition().x, point.getPosition().y, point.getPosition().z);
                GL11.glVertex3d(point.getPosition().x, point.getPosition().y + (double)entity3.getHeight(), point.getPosition().z);
            }
            GL11.glEnd();
            GL11.glBegin(3);
            for (Point point : points) {
                index = points.indexOf(point);
                alpha = index / (float)points.size();
                ColorUtils.setAlphaColor(HUD.getColor(0, 1.0f), alpha);
                GL11.glVertex3d(point.getPosition().x, point.getPosition().y, point.getPosition().z);
            }
            GL11.glEnd();
            GL11.glBegin(3);
            for (Point point : points) {
                index = points.indexOf(point);
                alpha = index / (float)points.size();
                ColorUtils.setAlphaColor(HUD.getColor(0, 1.0f), alpha);
                GL11.glVertex3d(point.getPosition().x, point.getPosition().y + (double)entity3.getHeight(), point.getPosition().z);
            }
            GL11.glEnd();
        }
        GL11.glHint(3154, 4352);
        GL11.glDisable(2848);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableCull();
        RenderSystem.shadeModel(7424);
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
    }

    public static class Point {
        private final Vector3d position;
        private final StopWatch time = new StopWatch();

        public Point(Vector3d position) {
            this.position = position;
        }

        public Vector3d getPosition() {
            return this.position;
        }

        public StopWatch getTime() {
            return this.time;
        }
    }
}

