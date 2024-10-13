/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Speed", type=Category.Movement)
public class Speed
extends Function {
    private final ModeSetting spdMode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c", "Matrix", "Matrix", "Timer", "Sunrise DMG", "Really World", "InfinityHVH");
    public boolean boosting;

    public Speed() {
        this.addSettings(this.spdMode);
    }

    private void handleEventMove(EventMotion eventMove) {
        if (this.spdMode.is("Matrix")) {
            if (!Minecraft.player.isOnGround()) {
                if (Minecraft.player.fallDistance >= 0.5f && eventMove.isOnGround()) {
                    this.applyMatrixSpeed();
                }
            }
        }
    }

    @Subscribe
    private void handleEventUpdate(EventUpdate eventUpdate) {
        switch ((String)this.spdMode.get()) {
            case "Matrix": {
                if (!Minecraft.player.isOnGround() || !MoveUtils.isMoving()) break;
                Minecraft.player.jump();
                break;
            }
            case "Really World": {
                this.handleRWMode();
                break;
            }
            case "Timer": {
                this.handleTimerMode();
            }
        }
    }

    private void handlePacketEvent(EventPacket e) {
        if (this.spdMode.is("Really World")) {
            IPacket<?> var3 = e.getPacket();
            if (var3 instanceof CConfirmTransactionPacket) {
                CConfirmTransactionPacket cConfirmTransactionPacket = (CConfirmTransactionPacket)var3;
            }
            if ((var3 = e.getPacket()) instanceof SPlayerPositionLookPacket) {
                SPlayerPositionLookPacket p = (SPlayerPositionLookPacket)var3;
                Minecraft.player.func_242277_a(new Vector3d(p.getX(), p.getY(), p.getZ()));
                Minecraft.player.setRawPosition(p.getX(), p.getY(), p.getZ());
                this.toggle();
            }
        }
    }

    private void handleRWMode() {
        if (this.boosting) {
            if (Minecraft.player.isOnGround() && !Speed.mc.gameSettings.keyBindJump.pressed) {
                Minecraft.player.jump();
            }
            Speed.mc.timer.timerSpeed = Minecraft.player.ticksExisted % 2 == 0 ? 1.5f : 1.2f;
        } else {
            Speed.mc.timer.timerSpeed = 0.05f;
        }
    }

    private void applyMatrixSpeed() {
        double speed = 2.0;
        Vector3d var10000 = Minecraft.player.motion;
        var10000.x *= speed;
        var10000 = Minecraft.player.motion;
        var10000.z *= speed;
    }

    private void handleTimerMode() {
        if (!Minecraft.player.isInWater()) {
            if (!Minecraft.player.isInLava()) {
                if (!Minecraft.player.isOnLadder()) {
                    float timerValue = 1.0f;
                    if (Minecraft.player.fallDistance <= 0.1f) {
                        timerValue = 1.34f;
                    }
                    if (Minecraft.player.fallDistance > 1.0f) {
                        timerValue = 0.6f;
                    }
                    if (MoveUtils.isMoving()) {
                        Speed.mc.timer.timerSpeed = 1.0f;
                        if (Minecraft.player.isOnGround()) {
                            if (!Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                Minecraft.player.jump();
                            }
                        } else {
                            Speed.mc.timer.timerSpeed = timerValue;
                        }
                    } else {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                }
            }
        }
    }

    private void handleSunriseDamageMode(double radians) {
        radians = 4.0;
        if (MoveUtils.isMoving()) {
            if (Minecraft.player.isOnGround()) {
                this.applySunriseGroundMotion(radians);
            } else if (Minecraft.player.isInWater()) {
                this.applySunriseWaterMotion(radians);
            } else if (!Minecraft.player.isOnGround()) {
                this.applySunriseAirMotion(radians);
            } else {
                this.applySunriseDefaultMotion(radians);
            }
        }
    }

    private void applySunriseGroundMotion(double radians) {
        Minecraft.player.addVelocity((double)(-MathHelper.sin((float)radians)) * 9.5 / 24.5, 0.0, (double)MathHelper.cos((float)radians) * 9.5 / 24.5);
        MoveUtils.setMotion(MoveUtils.getMotion());
    }

    private void applySunriseWaterMotion(double radians) {
        Minecraft.player.addVelocity((double)(-MathHelper.sin((float)radians)) * 9.5 / 24.5, 0.0, (double)MathHelper.cos((float)radians) * 9.5 / 24.5);
        MoveUtils.setMotion(MoveUtils.getMotion());
    }

    private void applySunriseAirMotion(double radians) {
        Minecraft.player.addVelocity((double)(-MathHelper.sin((float)radians)) * 0.11 / 24.5, 0.0, (double)MathHelper.cos((float)radians) * 0.11 / 24.5);
        MoveUtils.setMotion(MoveUtils.getMotion());
    }

    private void applySunriseDefaultMotion(double radians) {
        Minecraft.player.addVelocity((double)(-MathHelper.sin((float)radians)) * 0.005 * MoveUtils.getMotion(), 0.0, (double)MathHelper.cos((float)radians) * 0.005 * MoveUtils.getMotion());
        MoveUtils.setMotion(MoveUtils.getMotion());
    }

    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

