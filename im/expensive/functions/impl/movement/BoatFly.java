/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CEntityActionPacket;

@FunctionRegister(name="Pig Fly", type=Category.Movement)
public class BoatFly
extends Function {
    final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 10.0f, 1.0f, 20.0f, 0.05f);
    final BooleanSetting noDismount = new BooleanSetting("\u041d\u0435 \u0432\u044b\u043b\u0435\u0437\u0430\u0442\u044c", true);
    final BooleanSetting savePig = new BooleanSetting("\u0421\u043f\u0430\u0441\u0430\u0442\u044c \u0441\u0432\u0438\u043d\u044c\u044e", true);

    public BoatFly() {
        this.addSettings(this.speed, this.noDismount, this.savePig);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        block9: {
            block10: {
                if (Minecraft.player.getRidingEntity() == null) break block9;
                if (!(Minecraft.player.getRidingEntity() instanceof PigEntity)) break block9;
                Minecraft.player.getRidingEntity().motion.y = 0.0;
                if (!Minecraft.player.isPassenger()) break block9;
                if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.player.getRidingEntity().motion.y = 1.0;
                } else if (BoatFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.player.getRidingEntity().motion.y = -1.0;
                }
                if (MoveUtils.isMoving()) {
                    double yaw = MoveUtils.getDirection(true);
                    Minecraft.player.getRidingEntity().motion.x = -Math.sin(yaw) * (double)((Float)this.speed.get()).floatValue();
                    Minecraft.player.getRidingEntity().motion.z = Math.cos(yaw) * (double)((Float)this.speed.get()).floatValue();
                } else {
                    Minecraft.player.getRidingEntity().motion.x = 0.0;
                    Minecraft.player.getRidingEntity().motion.z = 0.0;
                }
                if (!MoveUtils.isBlockUnder(4.0f)) break block10;
                if (Minecraft.player.collidedHorizontally) break block10;
                if (!Minecraft.player.collidedVertically) break block9;
            }
            if (((Boolean)this.savePig.get()).booleanValue()) {
                Minecraft.player.getRidingEntity().motion.y += 1.0;
            }
        }
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        block4: {
            CEntityActionPacket actionPacket;
            block6: {
                block5: {
                    IPacket<?> iPacket = e.getPacket();
                    if (!(iPacket instanceof CEntityActionPacket)) break block4;
                    actionPacket = (CEntityActionPacket)iPacket;
                    if (!((Boolean)this.noDismount.get()).booleanValue()) break block5;
                    if (Minecraft.player.getRidingEntity() instanceof BoatEntity) break block6;
                }
                return;
            }
            CEntityActionPacket.Action action = actionPacket.getAction();
            if (action == CEntityActionPacket.Action.PRESS_SHIFT_KEY || action == CEntityActionPacket.Action.RELEASE_SHIFT_KEY) {
                e.cancel();
            }
        }
    }

    public boolean notStopRidding() {
        return this.isState() && (Boolean)this.noDismount.get() != false;
    }
}

