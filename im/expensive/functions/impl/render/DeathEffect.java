/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.AttackEvent;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventMotion;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.render.DisplayUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

@FunctionRegister(name="DeathEffect", type=Category.Render)
public class DeathEffect
extends Function {
    private boolean useAnimation;
    LivingEntity target;
    long time;
    public StopWatch stopWatch = new StopWatch();
    private final List<Vector3d> position = new ArrayList<Vector3d>();
    private int current;
    private Vector3d setPosition;

    @Subscribe
    public void onPacket(AttackEvent e) {
        block5: {
            block4: {
                if (Minecraft.player == null) break block4;
                if (Minecraft.world != null) break block5;
            }
            return;
        }
        if (e.entity instanceof PlayerEntity) {
            this.target = (LivingEntity)e.entity;
        }
        this.time = System.currentTimeMillis();
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        block6: {
            block5: {
                if (Minecraft.player == null) break block5;
                if (Minecraft.world != null) break block6;
            }
            return;
        }
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SDestroyEntitiesPacket) {
            SDestroyEntitiesPacket p = (SDestroyEntitiesPacket)iPacket;
            for (Object ids : (IPacket<?>)p.getEntityIDs()) {
                if (this.target == null) continue;
                if (ids == Minecraft.player.getEntityId() || this.time + 400L < System.currentTimeMillis() || this.target.getEntityId() != ids) continue;
                if (!(((LivingEntity)Minecraft.world.getEntityByID((int)ids)).getHealth() < 5.0f)) continue;
                this.onKill(this.target);
                this.target = null;
            }
        }
    }

    @Subscribe
    public void onUpdate(EventMotion e) {
        block7: {
            block6: {
                if (Minecraft.player == null) break block6;
                if (Minecraft.world != null) break block7;
            }
            return;
        }
        if (this.useAnimation) {
            if (Minecraft.player.ticksExisted % 5 == 0) {
                ++this.current;
            }
            Vector3d player = new Vector3d(MathUtil.interpolate(Minecraft.player.getPosX(), Minecraft.player.lastTickPosX, (double)mc.getRenderPartialTicks()), MathUtil.interpolate(Minecraft.player.getPosY(), Minecraft.player.lastTickPosY, (double)mc.getRenderPartialTicks()), MathUtil.interpolate(Minecraft.player.getPosZ(), Minecraft.player.lastTickPosZ, (double)mc.getRenderPartialTicks())).add(0.0, Minecraft.player.getEyeHeight(), 0.0);
            this.position.add(player);
        }
        if (this.target != null && this.time + 1000L >= System.currentTimeMillis() && this.target.getHealth() <= 0.0f) {
            this.onKill(this.target);
            this.target = null;
        }
    }

    @Subscribe
    public void onDisplay(EventDisplay e) {
        block5: {
            block4: {
                if (Minecraft.player == null) break block4;
                if (Minecraft.world != null && e.getType() == EventDisplay.Type.POST) break block5;
            }
            return;
        }
        if (this.useAnimation && this.setPosition != null && this.position.size() > 1) {
            this.setPosition = MathUtil.fast(this.setPosition, this.position.get(this.current), 1.0f);
            DisplayUtils.drawWhite(1.0f);
        }
    }

    public void onKill(LivingEntity entity2) {
        Vector3d player;
        this.position.clear();
        this.current = 0;
        this.useAnimation = true;
        this.stopWatch.reset();
        this.setPosition = player = new Vector3d(MathUtil.interpolate(Minecraft.player.getPosX(), Minecraft.player.lastTickPosX, (double)mc.getRenderPartialTicks()), MathUtil.interpolate(Minecraft.player.getPosY(), Minecraft.player.lastTickPosY, (double)mc.getRenderPartialTicks()), MathUtil.interpolate(Minecraft.player.getPosZ(), Minecraft.player.lastTickPosZ, (double)mc.getRenderPartialTicks())).add(0.0, Minecraft.player.getEyeHeight(), 0.0);
        if (entity2.world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)entity2.world;
            LightningBoltEntity lightningBolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
            lightningBolt.moveForced(Vector3d.copyCenteredHorizontally(entity2.getPosition()));
            lightningBolt.setEffectOnly(false);
            serverWorld.addEntity(lightningBolt);
        }
    }
}

