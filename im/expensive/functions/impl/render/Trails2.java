/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="EntityTrails", type=Category.Render)
public class Trails2
extends Function {
    private final ModeSetting setting = new ModeSetting("\u0412\u0438\u0434", "\u041e\u0440\u0431\u0438\u0437\u044b", "\u041e\u0440\u0431\u0438\u0437\u044b", "\u0421\u0435\u0440\u0434\u0435\u0447\u043a\u0438", "\u041c\u043e\u043b\u043d\u0438\u044f", "\u0421\u043d\u0435\u0436\u0438\u043d\u043a\u0438", "blur");
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList();
    private final int maxParticles = 30;

    public Trails2() {
        this.addSettings(this.setting);
    }

    private boolean isInView(Vector3d pos) {
        WorldRenderer.frustum.setCameraPosition(IMinecraft.mc.getRenderManager().info.getProjectedView().x, IMinecraft.mc.getRenderManager().info.getProjectedView().y, IMinecraft.mc.getRenderManager().info.getProjectedView().z);
        return WorldRenderer.frustum.isBoundingBoxInFrustum(new AxisAlignedBB(pos.add(0.2, 0.1, 0.1), pos.add(0.2, 0.2, 0.2)));
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        block23: {
            block22: {
                if (Minecraft.player == null) break block22;
                if (Minecraft.world != null && e.getType() == EventDisplay.Type.PRE) break block23;
            }
            return;
        }
        if (this.particles.size() < 30) {
            this.particles.add(new Particle());
        }
        for (Particle p : this.particles) {
            block25: {
                block24: {
                    if (System.currentTimeMillis() - p.time > 4200L) break block24;
                    if (Minecraft.player.getPositionVec().distanceTo(p.pos) > 5.0 || !this.isInView(p.pos)) break block24;
                    if (Minecraft.player.canEntityBeSeen(p.pos)) break block25;
                }
                this.particles.remove(p);
                continue;
            }
            p.update();
            Vector2f pos = ProjectionUtil.project(p.pos.x + 0.5, p.pos.y, p.pos.z + 0.5);
            float size = 1.0f - (float)(System.currentTimeMillis() - p.time) / 4200.0f;
            float offsetX = (float)Math.sin((double)System.currentTimeMillis() * 0.001) * 0.3f;
            float offsetY = (float)Math.cos((double)System.currentTimeMillis() * 0.001) * 0.3f;
            pos.x += offsetX;
            pos.y += offsetY;
            ResourceLocation logo = new ResourceLocation("expensive/images/glow1.png");
            switch ((String)this.setting.get()) {
                case "\u0421\u0435\u0440\u0434\u0435\u0447\u043a\u0438": {
                    Fonts.damage.drawText(e.getMatrixStack(), "B", pos.x - 1.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                    break;
                }
                case "\u0421\u043d\u0435\u0436\u0438\u043d\u043a\u0438": {
                    Fonts.damage.drawText(e.getMatrixStack(), "A", pos.x - 1.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                    break;
                }
                case "\u041c\u043e\u043b\u043d\u0438\u044f": {
                    Fonts.damage.drawText(e.getMatrixStack(), "C", pos.x - 1.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                    break;
                }
                case "\u041e\u0440\u0431\u0438\u0437\u044b": {
                    DisplayUtils.drawCircle(pos.x + 10.0f, pos.y, 5.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)));
                    break;
                }
                case "blur": {
                    DisplayUtils.drawImage(logo, pos.x, pos.y, 20.0f * size, 20.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(200.0f * p.alpha * size)));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.particles.clear();
        super.onDisable();
    }

    private class Particle {
        private Vector3d pos = Minecraft.player.getPositionVec().add(-ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat(-1.0f, 1.0f), -ThreadLocalRandom.current().nextFloat());
        private final Vector3d end = this.pos.add(-ThreadLocalRandom.current().nextFloat(-1.0f, 1.0f), -ThreadLocalRandom.current().nextFloat(), -ThreadLocalRandom.current().nextFloat());
        private final long time = System.currentTimeMillis();
        private float alpha;

        public void update() {
            this.alpha = MathUtil.fast(this.alpha, 1.0f, 2.0f);
            this.pos = MathUtil.fast(this.pos, this.end, 0.5f);
        }
    }
}

