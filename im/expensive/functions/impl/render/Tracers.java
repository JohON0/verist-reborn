/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.AntiBot;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.EntityUtils;
import im.expensive.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="Tracers", type=Category.Render)
public class Tracers
extends Function {
    private final BooleanSetting ignoreNaked = new BooleanSetting("\u0418\u0433\u043d\u043e\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0433\u043e\u043b\u044b\u0445", true);

    public Tracers() {
        this.addSettings(this.ignoreNaked);
    }

    @Subscribe
    public void onRender(WorldEvent e) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0f);
        Vector3d cam = new Vector3d(0.0, 0.0, 150.0).rotatePitch((float)(-Math.toRadians(Tracers.mc.getRenderManager().info.getPitch()))).rotateYaw((float)(-Math.toRadians(Tracers.mc.getRenderManager().info.getYaw())));
        for (AbstractClientPlayerEntity player : Minecraft.world.getPlayers()) {
            if (player == Minecraft.player || !player.isAlive() || AntiBot.isBot(player) || (float)player.getTotalArmorValue() == 0.0f && ((Boolean)this.ignoreNaked.get()).booleanValue()) continue;
            Vector3d pos = EntityUtils.getInterpolatedPositionVec(player).subtract(Tracers.mc.getRenderManager().info.getProjectedView());
            ColorUtils.setColor(FriendStorage.isFriend(player.getGameProfile().getName()) ? FriendStorage.getColor() : -1);
            buffer.begin(1, DefaultVertexFormats.POSITION);
            buffer.pos(cam.x, cam.y, cam.z).endVertex();
            buffer.pos(pos.x, pos.y, pos.z).endVertex();
            tessellator.draw();
        }
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }
}

