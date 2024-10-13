/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.Direction;
import im.expensive.utils.animations.impl.DecelerateAnimation;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="TargetESP", type=Category.Render)
public class TargetESP
extends Function {
    private final KillAura killAura;
    final ModeSetting mod = new ModeSetting("\u041c\u043e\u0434", "Client", "Client", "\u041f\u0440\u0438\u0437\u0440\u0430\u043a\u0438");
    private final BooleanSetting targetesp = new BooleanSetting("\u0422\u0430\u0440\u0433\u0435\u0442 \u0435\u0441\u043f", true);
    public SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 3.0f, 0.7f, 9.0f, 1.0f);
    public SliderSetting size = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440", 30.0f, 5.0f, 140.0f, 1.0f);
    public SliderSetting bright = new SliderSetting("\u042f\u0440\u043a\u043e\u0441\u0442\u044c", 255.0f, 1.0f, 255.0f, 1.0f);
    private LivingEntity currentTarget;
    private LivingEntity target;
    public static long startTime = System.currentTimeMillis();
    private final Animation alpha = new DecelerateAnimation(600, 255.0);

    public TargetESP(KillAura killAura) {
        this.killAura = killAura;
        this.addSettings(this.mod, this.speed, this.size, this.bright, this.targetesp);
    }

    @Subscribe
    private void onEvent(EventUpdate eventUpdate) {
        boolean bl = Expensive.getInstance().getFunctionRegistry().getKillAura().isState();
        if (this.target != null) {
            this.currentTarget = this.target;
        }
        this.alpha.setDirection(bl && this.target != null ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    @Subscribe
    private void onWorldEvent(WorldEvent e) {
        if (this.mod.is("\u041f\u0440\u0438\u0437\u0440\u0430\u043a\u0438")) {
            MatrixStack stack = new MatrixStack();
            EntityRendererManager rm = mc.getRenderManager();
            float c = (float)((double)((float)(System.currentTimeMillis() - startTime) / 1500.0f) + Math.sin((float)(System.currentTimeMillis() - startTime) / 1500.0f) / 10.0);
            double x = this.target.lastTickPosX + (this.target.getPosX() - this.target.lastTickPosX) * (double)e.getPartialTicks() - rm.info.getProjectedView().getX();
            double y = this.target.lastTickPosY + (this.target.getPosY() - this.target.lastTickPosY) * (double)e.getPartialTicks() - rm.info.getProjectedView().getY();
            double z = this.target.lastTickPosZ + (this.target.getPosZ() - this.target.lastTickPosZ) * (double)e.getPartialTicks() - rm.info.getProjectedView().getZ();
            float alpha = Shaders.shaderPackLoaded ? 1.0f : 0.5f;
            alpha *= 0.2f;
            float pl = 0.0f;
            boolean fa = false;
            for (int b = 0; b < 3; ++b) {
                for (float i = c * 360.0f; i < c * 360.0f + 70.0f; i += 2.0f) {
                    float cur = i;
                    float min2 = c * 360.0f;
                    float max2 = c * 360.0f + 70.0f;
                    float dc = MathHelper.normalize(cur, c * 360.0f - 45.0f, max2);
                    float degrees = i;
                    int color = HUD.getColor(1);
                    int color2 = HUD.getColor(1);
                    float rf = 0.7f;
                    double radians = Math.toRadians(degrees);
                    double plY = (double)pl + Math.sin(radians * (double)1.2f) * (double)0.1f;
                    stack.push();
                    stack.translate(x, y, z);
                    stack.rotate(Vector3f.YP.rotationDegrees(-rm.info.getYaw()));
                    RenderSystem.depthMask(false);
                    float q = (!fa ? 0.15f : 0.15f) * (Math.max(fa ? 0.15f : 0.15f, fa ? dc : (1.0f - -(0.4f - dc)) / 2.0f) + 0.45f);
                    float w = q * (2.0f + (0.5f - alpha) * 2.0f);
                    DisplayUtils.drawtargetespimage(stack, new ResourceLocation("expensive/images/glow1.png"), Math.cos(radians) * (double)rf - (double)(w / 2.0f), plY + 1.0 - 0.7, Math.sin(radians) * (double)rf - (double)(w / 2.0f), w, w, color, color2, color2, color);
                    GL11.glEnable(2929);
                    RenderSystem.depthMask(true);
                    stack.pop();
                }
                c *= -1.25f;
                pl += 0.45f;
            }
        }
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        if (this.mod.is("Client")) {
            if (e.getType() != EventDisplay.Type.PRE) {
                return;
            }
            if (this.killAura.isState() && this.killAura.getTarget() != null) {
                double sin2 = Math.sin((double)System.currentTimeMillis() / 1000.0);
                float size = 70.0f;
                Vector3d interpolated = this.killAura.getTarget().getPositon(e.getPartialTicks());
                Vector2f pos = ProjectionUtil.project(interpolated.x, interpolated.y + (double)(this.killAura.getTarget().getHeight() / 2.0f), interpolated.z);
                GlStateManager.pushMatrix();
                GlStateManager.translatef(pos.x, pos.y, 0.0f);
                GlStateManager.rotatef((float)sin2 * 360.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translatef(-pos.x, -pos.y, 0.0f);
                DisplayUtils.drawImage(new ResourceLocation("expensive/images/target.png"), pos.x - size / 2.0f, pos.y - size / 2.0f, size, size, new Vector4i(ColorUtils.setAlpha(HUD.getColor(0, 1.0f), 220), ColorUtils.setAlpha(HUD.getColor(90, 1.0f), 220), ColorUtils.setAlpha(HUD.getColor(180, 1.0f), 220), ColorUtils.setAlpha(HUD.getColor(270, 1.0f), 220)));
                GlStateManager.popMatrix();
            }
        }
        if (this.mod.is("\u041a\u0440\u0443\u0433")) {
            int i;
            if (e.getType() != EventDisplay.Type.PRE) {
                return;
            }
            EntityRendererManager rm = mc.getRenderManager();
            double x = this.target.lastTickPosX + (this.target.getPosX() - this.target.lastTickPosX) * (double)e.getPartialTicks() - rm.info.getProjectedView().getX();
            double y = this.target.lastTickPosY + (this.target.getPosY() - this.target.lastTickPosY) * (double)e.getPartialTicks() - rm.info.getProjectedView().getY();
            double z = this.target.lastTickPosZ + (this.target.getPosZ() - this.target.lastTickPosZ) * (double)e.getPartialTicks() - rm.info.getProjectedView().getZ();
            float height = this.target.getHeight();
            double duration = 2000.0;
            double elapsed = (double)System.currentTimeMillis() % duration;
            boolean side = elapsed > duration / 2.0;
            double progress = elapsed / (duration / 2.0);
            progress = side ? (progress -= 1.0) : 1.0 - progress;
            progress = progress < 0.5 ? 2.0 * progress * progress : 1.0 - Math.pow(-2.0 * progress + 2.0, 2.0) / 2.0;
            double eased = (double)(height / 2.0f) * (progress > 0.5 ? 1.0 - progress : progress) * (double)(side ? -1 : 1);
            RenderSystem.pushMatrix();
            GL11.glDepthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableCull();
            RenderSystem.lineWidth(1.5f);
            RenderSystem.color4f(-1.0f, -1.0f, -1.0f, -1.0f);
            buffer.begin(8, DefaultVertexFormats.POSITION_COLOR);
            for (i = 0; i <= 360; ++i) {
                buffer.pos(x + Math.cos(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8, y + (double)height * progress, z + Math.sin(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8).color(ColorUtils.rgb(255, 255, 255)).endVertex();
                buffer.pos(x + Math.cos(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8, y + (double)height * progress + eased, z + Math.sin(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8).color(ColorUtils.rgb(255, 255, 255)).endVertex();
            }
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
            RenderSystem.color4f(-1.0f, -1.0f, -1.0f, -1.0f);
            buffer.begin(2, DefaultVertexFormats.POSITION_COLOR);
            for (i = 0; i <= 360; ++i) {
                buffer.pos(x + Math.cos(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8, y + (double)height * progress, z + Math.sin(Math.toRadians(i)) * (double)this.target.getWidth() * 0.8).color(ColorUtils.rgb(255, 255, 255)).endVertex();
            }
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            RenderSystem.enableAlphaTest();
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4354);
            RenderSystem.shadeModel(7424);
            RenderSystem.popMatrix();
        }
    }

    public KillAura getKillAura() {
        return this.killAura;
    }

    public LivingEntity getTarget() {
        return this.target;
    }
}

