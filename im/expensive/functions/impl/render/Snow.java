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
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.biome.Biome;

@FunctionRegister(name="Snow", type=Category.Render)
public class Snow
extends Function {
    private static final long FIXED_TIME_STEP = 16L;
    private final ModeSetting setting = new ModeSetting("\u0412\u0438\u0434", "\u041e\u0440\u0431\u0438\u0437\u044b", "\u041e\u0440\u0431\u0438\u0437\u044b", "\u0421\u0435\u0440\u0434\u0435\u0447\u043a\u0438", "\u041c\u043e\u043b\u043d\u0438\u044f", "\u0421\u043d\u0435\u0436\u0438\u043d\u043a\u0438", "Star", "blur");
    private final SliderSetting sizeSetting = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440", 1.0f, 1.0f, 5.0f, 0.1f);
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList();
    private final Minecraft mc = Minecraft.getInstance();

    public Snow() {
        this.addSettings(this.setting, this.sizeSetting);
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        block24: {
            block23: {
                if (Minecraft.player == null) break block23;
                if (Minecraft.world != null && e.getType() == EventDisplay.Type.PRE) break block24;
            }
            return;
        }
        this.generateSnowParticles();
        for (Particle p : this.particles) {
            if (System.currentTimeMillis() - p.time > 4000L) {
                this.particles.remove(p);
            }
            if (Minecraft.player.getPositionVec().distanceTo(p.pos) > 30.0) {
                this.particles.remove(p);
            }
            if (this.isVisibleToPlayer(p.pos)) {
                e.getMatrixStack().push();
                e.getMatrixStack().rotate(Vector3f.ZP.rotationDegrees(p.getRotation()));
                p.update();
                Vector2f pos = ProjectionUtil.project(p.pos.x, p.pos.y, p.pos.z);
                ResourceLocation logo = new ResourceLocation("expensive/images/star.png");
                ResourceLocation logo1 = new ResourceLocation("expensive/images/glow1.png");
                float baseSize = 1.3f - (float)(System.currentTimeMillis() - p.time) / 5000.0f;
                float size = baseSize * (float)((Float)this.sizeSetting.get()).intValue();
                e.getMatrixStack().pop();
                switch ((String)this.setting.get()) {
                    case "\u0421\u0435\u0440\u0434\u0435\u0447\u043a\u0438": {
                        Fonts.damage.drawText(e.getMatrixStack(), "B", pos.x - 3.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                        break;
                    }
                    case "\u0421\u043d\u0435\u0436\u0438\u043d\u043a\u0438": {
                        Fonts.damage.drawText(e.getMatrixStack(), "A", pos.x - 3.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                        break;
                    }
                    case "\u041c\u043e\u043b\u043d\u0438\u044f": {
                        Fonts.damage.drawText(e.getMatrixStack(), "C", pos.x - 3.0f * size, pos.y - 3.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)), 8.0f * size, 0.05f);
                        break;
                    }
                    case "\u041e\u0440\u0431\u0438\u0437\u044b": {
                        DisplayUtils.drawCircle(pos.x, pos.y, 5.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(155.0f * p.alpha * size)));
                        break;
                    }
                    case "Star": {
                        DisplayUtils.drawImage(logo, pos.x, pos.y, 30.0f * size, 30.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(200.0f * p.alpha * size)));
                        break;
                    }
                    case "blur": {
                        DisplayUtils.drawImage(logo1, pos.x, pos.y, 20.0f * size, 20.0f * size, ColorUtils.setAlpha(HUD.getColor(this.particles.indexOf(p), 1.0f), (int)(200.0f * p.alpha * size)));
                    }
                }
                continue;
            }
            this.particles.remove(p);
        }
    }

    private void generateSnowParticles() {
        double playerSpeed = Minecraft.player.getMotion().lengthSquared();
        double baseProbability = 0.05;
        double speedMultiplier = 0.1;
        double adjustedProbability = baseProbability + playerSpeed * speedMultiplier;
        if (Math.random() < adjustedProbability) {
            ClientWorld world = Minecraft.world;
            int playerX = MathHelper.floor(Minecraft.player.getPosX());
            int playerZ = MathHelper.floor(Minecraft.player.getPosZ());
            for (int i = 0; i < 8; ++i) {
                int z;
                int y;
                int x = playerX + ThreadLocalRandom.current().nextInt(-10, 11);
                BlockPos blockPos = new BlockPos(x, y = ThreadLocalRandom.current().nextInt(0, 257), z = playerZ + ThreadLocalRandom.current().nextInt(-10, 11));
                Biome biome = world.getBiome(blockPos);
                if (biome.getPrecipitation() == Biome.RainType.NONE || !(biome.getTemperature(blockPos) >= 0.15f)) continue;
                this.particles.add(new Particle(x, y, z));
            }
        }
    }

    private boolean isVisibleToPlayer(Vector3d pos) {
        Vector3d playerPos = Minecraft.player.getPositionVec().add(0.0, Minecraft.player.getEyeHeight(), 0.0);
        return Minecraft.world.rayTraceBlocks(new RayTraceContext(playerPos, pos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, Minecraft.player)).getType() == RayTraceResult.Type.MISS;
    }

    @Override
    public void onDisable() {
        this.particles.clear();
        super.onDisable();
    }

    private class Particle {
        private Vector3d pos;
        private final Vector3d end;
        private final long time;
        private long duration = 4000L;
        private float alpha;
        private float rotation;
        private float initialRotationSpeed;

        public Particle(double x, double y, double z) {
            this.pos = new Vector3d(x, y, z);
            this.end = this.pos.add(ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f), ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f), ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f));
            this.time = System.currentTimeMillis();
            this.initialRotationSpeed = ThreadLocalRandom.current().nextFloat(1.0f, 5.0f);
        }

        public void update() {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - this.time;
            long steps = elapsedTime / 16L;
            int i = 0;
            while ((long)i < steps) {
                if (elapsedTime < this.duration) {
                    this.alpha = MathUtil.fast(this.alpha, 1.0f, 10.0f);
                    this.rotation += this.initialRotationSpeed;
                    this.rotation %= 360.0f;
                } else {
                    this.alpha = 0.0f;
                }
                this.pos = MathUtil.fast(this.pos, this.end, 0.7f);
                elapsedTime -= 16L;
                ++i;
            }
        }

        public float getRotation() {
            return this.rotation;
        }
    }
}

