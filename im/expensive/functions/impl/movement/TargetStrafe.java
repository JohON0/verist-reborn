/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.ActionEvent;
import im.expensive.events.EventDamageReceive;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.events.MovingEvent;
import im.expensive.events.PostMoveEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.DamagePlayerUtil;
import im.expensive.utils.player.MoveUtils;
import im.expensive.utils.player.StrafeMovement;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

@FunctionRegister(name="TargetStrafe", type=Category.Movement)
public class TargetStrafe
extends Function {
    private final SliderSetting distanceSetting = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f", 1.0f, 0.1f, 6.0f, 0.05f);
    private final BooleanSetting damageBoostSetting = new BooleanSetting("\u0411\u0443\u0441\u0442 \u0441 \u0434\u0430\u043c\u0430\u0433\u043e\u043c", true);
    private final SliderSetting boostValueSetting = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0431\u0443\u0441\u0442\u0430", 1.5f, 0.1f, 5.0f, 0.05f);
    private final SliderSetting timeSetting = new SliderSetting("\u0412\u0440\u0435\u043c\u044f \u0431\u0443\u0441\u0442\u0430", 10.0f, 1.0f, 20.0f, 1.0f);
    private final BooleanSetting saveTarget = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0442\u044c \u0446\u0435\u043b\u044c", true);
    private float side = 1.0f;
    private LivingEntity target = null;
    private final DamagePlayerUtil damageUtil = new DamagePlayerUtil();
    private String targetName = "";
    public StrafeMovement strafeMovement = new StrafeMovement();
    private final KillAura killAura;

    public TargetStrafe(KillAura killAura) {
        this.killAura = killAura;
        this.addSettings(this.distanceSetting, this.damageBoostSetting, this.timeSetting, this.saveTarget);
    }

    @Subscribe
    private void onAction(ActionEvent e) {
        block3: {
            block2: {
                if (Minecraft.player == null) break block2;
                if (Minecraft.world != null) break block3;
            }
            return;
        }
        this.handleEventAction(e);
    }

    @Subscribe
    public void onMotion(MovingEvent event) {
        block9: {
            block8: {
                if (Minecraft.player == null) break block8;
                if (Minecraft.world == null) break block8;
                if (Minecraft.player.ticksExisted >= 10) break block9;
            }
            return;
        }
        boolean isLeftKeyPressed = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 65);
        boolean isRightKeyPressed = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 68);
        LivingEntity auraTarget = this.getTarget();
        if (auraTarget != null) {
            this.targetName = auraTarget.getName().getString();
        }
        this.target = this.shouldSaveTarget(auraTarget) ? this.updateTarget(this.target) : auraTarget;
        if (this.target != null && this.target.isAlive() && this.target.getHealth() > 0.0f) {
            if (Minecraft.player.collidedHorizontally) {
                this.side *= -1.0f;
            }
            if (isLeftKeyPressed) {
                this.side = 1.0f;
            }
            if (isRightKeyPressed) {
                this.side = -1.0f;
            }
            double angle = Math.atan2(Minecraft.player.getPosZ() - this.target.getPosZ(), Minecraft.player.getPosX() - this.target.getPosX());
            double x = this.target.getPosX() + (double)((Float)this.distanceSetting.get()).floatValue() * Math.cos(angle += MoveUtils.getMotion() / (double)Math.max(Minecraft.player.getDistance(this.target), this.distanceSetting.min) * (double)this.side);
            double z = this.target.getPosZ() + (double)((Float)this.distanceSetting.get()).floatValue() * Math.sin(angle);
            double yaw = this.getYaw(Minecraft.player, x, z);
            this.damageUtil.time(((Float)this.timeSetting.get()).longValue() * 100L);
            float damageSpeed = ((Float)this.boostValueSetting.get()).floatValue() / 10.0f;
            double speed = this.strafeMovement.calculateSpeed(event, (Boolean)this.damageBoostSetting.get(), this.damageUtil.isNormalDamage(), true, damageSpeed);
            event.getMotion().x = speed * -Math.sin(Math.toRadians(yaw));
            event.getMotion().z = speed * Math.cos(Math.toRadians(yaw));
        }
    }

    @Subscribe
    private void onPostMove(PostMoveEvent e) {
        block3: {
            block2: {
                if (Minecraft.player == null) break block2;
                if (Minecraft.world != null) break block3;
            }
            return;
        }
        this.strafeMovement.postMove(e.getHorizontalMove());
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        block6: {
            block5: {
                if (Minecraft.player == null) break block5;
                if (Minecraft.world != null) break block6;
            }
            return;
        }
        if (e.getType() == EventPacket.Type.RECEIVE) {
            this.damageUtil.onPacketEvent(e);
            if (e.getPacket() instanceof SPlayerPositionLookPacket) {
                this.strafeMovement.setOldSpeed(0.0);
            }
        }
    }

    @Subscribe
    private void onDamage(EventDamageReceive e) {
        block3: {
            block2: {
                if (Minecraft.player == null) break block2;
                if (Minecraft.world != null) break block3;
            }
            return;
        }
        this.damageUtil.processDamage(e);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (Minecraft.player.isOnGround() && !TargetStrafe.mc.gameSettings.keyBindJump.pressed && this.target != null && this.target.isAlive()) {
            Minecraft.player.jump();
        }
    }

    @Override
    public boolean onEnable() {
        this.strafeMovement.setOldSpeed(0.0);
        this.target = null;
        super.onEnable();
        return false;
    }

    private void handleEventAction(ActionEvent action) {
        if (this.strafes() && CEntityActionPacket.lastUpdatedSprint != this.strafeMovement.isNeedSprintState()) {
            action.setSprintState(!CEntityActionPacket.lastUpdatedSprint);
        }
        if (this.strafeMovement.isNeedSwap()) {
            action.setSprintState(!Minecraft.player.serverSprintState);
            this.strafeMovement.setNeedSprintState(false);
        }
    }

    private LivingEntity getTarget() {
        return this.killAura.isState() ? this.killAura.getTarget() : null;
    }

    private LivingEntity updateTarget(LivingEntity currentTarget) {
        for (Entity entity2 : Minecraft.world.getAllEntities()) {
            if (!(entity2 instanceof PlayerEntity) || !entity2.getName().getString().equalsIgnoreCase(this.targetName)) continue;
            return (LivingEntity)entity2;
        }
        return currentTarget;
    }

    private boolean shouldSaveTarget(LivingEntity target) {
        boolean settingIsEnabled = (Boolean)this.saveTarget.get();
        boolean targetAndTargetNameExist = target != null && this.targetName != null;
        return settingIsEnabled && targetAndTargetNameExist && this.killAura.isState();
    }

    private double getYaw(LivingEntity entity2, double x, double z) {
        return Math.toDegrees(Math.atan2(z - entity2.getPosZ(), x - entity2.getPosX())) - 90.0;
    }

    public boolean strafes() {
        BlockPos belowPosition;
        if (this.isInvalidPlayerState()) {
            return false;
        }
        BlockPos playerPosition = new BlockPos(Minecraft.player.getPositionVec());
        BlockPos abovePosition = playerPosition.up();
        if (this.isSurfaceLiquid(abovePosition, belowPosition = playerPosition.down())) {
            return false;
        }
        if (this.isPlayerInWebOrSoulSand(playerPosition)) {
            return false;
        }
        return this.isPlayerAbleToStrafe();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isInvalidPlayerState() {
        if (Minecraft.player == null) return true;
        if (Minecraft.world == null) return true;
        if (Minecraft.player.isSneaking()) return true;
        if (Minecraft.player.isElytraFlying()) return true;
        if (Minecraft.player.isInWater()) return true;
        if (!Minecraft.player.isInLava()) return false;
        return true;
    }

    private boolean isSurfaceLiquid(BlockPos abovePosition, BlockPos belowPosition) {
        Block aboveBlock = Minecraft.world.getBlockState(abovePosition).getBlock();
        Block belowBlock = Minecraft.world.getBlockState(belowPosition).getBlock();
        return aboveBlock instanceof AirBlock && belowBlock == Blocks.WATER;
    }

    private boolean isPlayerInWebOrSoulSand(BlockPos playerPosition) {
        Material playerMaterial = Minecraft.world.getBlockState(playerPosition).getMaterial();
        Block oneBelowBlock = Minecraft.world.getBlockState(playerPosition.down()).getBlock();
        return playerMaterial == Material.WEB || oneBelowBlock instanceof SoulSandBlock;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isPlayerAbleToStrafe() {
        if (Minecraft.player.abilities.isFlying) return false;
        if (Minecraft.player.isPotionActive(Effects.LEVITATION)) return false;
        return true;
    }
}

