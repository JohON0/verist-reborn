/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.ActionEvent;
import im.expensive.events.EventDamageReceive;
import im.expensive.events.EventPacket;
import im.expensive.events.MovingEvent;
import im.expensive.events.PostMoveEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.impl.movement.TargetStrafe;
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
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;

@FunctionRegister(name="Strafe", type=Category.Movement)
public class Strafe
extends Function {
    private final BooleanSetting damageBoost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u0441 \u0434\u0430\u043c\u0430\u0433\u043e\u043c", false);
    private final SliderSetting boostSpeed = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435 \u0431\u0443\u0441\u0442\u0430", 0.7f, 0.1f, 5.0f, 0.1f);
    private final DamagePlayerUtil damageUtil = new DamagePlayerUtil();
    private final StrafeMovement strafeMovement = new StrafeMovement();
    private final TargetStrafe targetStrafe;
    private final KillAura killAura;

    public Strafe(TargetStrafe targetStrafe, KillAura killAura) {
        this.targetStrafe = targetStrafe;
        this.killAura = killAura;
        this.addSettings(this.damageBoost, this.boostSpeed);
    }

    @Subscribe
    private void onAction(ActionEvent e) {
        this.handleEventAction(e);
    }

    @Subscribe
    private void onMoving(MovingEvent e) {
        this.handleEventMove(e);
    }

    @Subscribe
    private void onPostMove(PostMoveEvent e) {
        this.handleEventPostMove(e);
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        this.handleEventPacket(e);
    }

    @Subscribe
    private void onDamage(EventDamageReceive e) {
        this.handleDamageEvent(e);
    }

    private void handleDamageEvent(EventDamageReceive damage) {
        if (((Boolean)this.damageBoost.get()).booleanValue()) {
            this.damageUtil.processDamage(damage);
        }
    }

    private void handleEventAction(ActionEvent action) {
        if (this.strafes()) {
            this.handleStrafesEventAction(action);
        }
        if (this.strafeMovement.isNeedSwap()) {
            this.handleNeedSwapEventAction(action);
        }
    }

    private void handleEventMove(MovingEvent eventMove) {
        if (this.strafes()) {
            this.handleStrafesEventMove(eventMove);
        } else {
            this.strafeMovement.setOldSpeed(0.0);
        }
    }

    private void handleEventPostMove(PostMoveEvent eventPostMove) {
        this.strafeMovement.postMove(eventPostMove.getHorizontalMove());
    }

    private void handleEventPacket(EventPacket packet) {
        if (packet.getType() == EventPacket.Type.RECEIVE) {
            if (((Boolean)this.damageBoost.get()).booleanValue()) {
                this.damageUtil.onPacketEvent(packet);
            }
            this.handleReceivePacketEventPacket(packet);
        }
    }

    private void handleStrafesEventAction(ActionEvent action) {
        if (CEntityActionPacket.lastUpdatedSprint != this.strafeMovement.isNeedSprintState()) {
            action.setSprintState(!CEntityActionPacket.lastUpdatedSprint);
        }
    }

    private void handleStrafesEventMove(MovingEvent eventMove) {
        if (this.targetStrafe.isState() && this.killAura.isState() && this.killAura.getTarget() != null) {
            return;
        }
        if (((Boolean)this.damageBoost.get()).booleanValue()) {
            this.damageUtil.time(700L);
        }
        float damageSpeed = ((Float)this.boostSpeed.get()).floatValue() / 10.0f;
        double speed = this.strafeMovement.calculateSpeed(eventMove, (Boolean)this.damageBoost.get(), this.damageUtil.isNormalDamage(), false, damageSpeed);
        MoveUtils.MoveEvent.setMoveMotion(eventMove, speed);
    }

    private void handleNeedSwapEventAction(ActionEvent action) {
        action.setSprintState(!Minecraft.player.serverSprintState);
        this.strafeMovement.setNeedSwap(false);
    }

    private void handleReceivePacketEventPacket(EventPacket packet) {
        if (packet.getPacket() instanceof SPlayerPositionLookPacket) {
            this.strafeMovement.setOldSpeed(0.0);
        }
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

    @Override
    public boolean onEnable() {
        this.strafeMovement.setOldSpeed(0.0);
        super.onEnable();
        return false;
    }
}

