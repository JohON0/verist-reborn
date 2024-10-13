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
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTeleportPacket;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Fly", type=Category.Movement)
public class Fly
extends Function {
    private final ModeSetting mode = new ModeSetting("\u041c\u043e\u0434", "Vanilla", "Vanilla", "Matrix Jump", "Matrix Glide", "GrimAC");
    private final SliderSetting horizontal = new SliderSetting("\u041f\u043e \u0433\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u0438", 0.5f, 0.0f, 5.0f, 0.1f);
    private final SliderSetting vertical = new SliderSetting("\u041f\u043e \u0432\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u0438", 0.5f, 0.0f, 5.0f, 0.1f);
    public Entity vehicle;

    public Fly() {
        this.addSettings(this.mode, this.horizontal, this.vertical);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        block16: {
            block15: {
                if (Minecraft.player == null) break block15;
                if (Minecraft.world != null) break block16;
            }
            return;
        }
        block0 : switch (this.mode.getIndex()) {
            case 0: {
                this.updatePlayerMotion();
                break;
            }
            case 1: {
                if (Minecraft.player.isOnGround()) {
                    Minecraft.player.jump();
                    break;
                }
                MoveUtils.setMotion(Math.min(((Float)this.horizontal.get()).floatValue(), 1.97f));
                Minecraft.player.motion.y = ((Float)this.vertical.get()).floatValue();
                break;
            }
            case 2: {
                Minecraft.player.motion = Vector3d.ZERO;
                MoveUtils.setMotion(((Float)this.horizontal.get()).floatValue());
                Minecraft.player.setMotion(Minecraft.player.getMotion().x, -0.003, Minecraft.player.getMotion().z);
                break;
            }
            case 3: {
                for (Entity en : Minecraft.world.getAllEntities()) {
                    if (!(en instanceof BoatEntity)) continue;
                    if (!(Minecraft.player.getDistance(en) <= 2.0f)) continue;
                    MoveUtils.setMotion(1.2f);
                    Minecraft.player.motion.y = 1.0;
                    break block0;
                }
                break;
            }
            case 4: {
                if (Minecraft.player.ticksExisted % 2 != 0) {
                    return;
                }
                int slot = -1;
                for (ItemStack stack : Minecraft.player.inventory.mainInventory) {
                    if (!(stack.getItem() instanceof ElytraItem)) continue;
                    slot = Minecraft.player.inventory.mainInventory.indexOf(stack);
                }
                Minecraft.player.abilities.isFlying = false;
                if (slot == -1) {
                    return;
                }
                int chestSlot = 6;
                Fly.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Minecraft.player);
                Fly.mc.playerController.windowClick(0, chestSlot, 0, ClickType.PICKUP, Minecraft.player);
                Fly.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Minecraft.player);
                Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                Minecraft.player.abilities.isFlying = true;
                Fly.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Minecraft.player);
                Fly.mc.playerController.windowClick(0, chestSlot, 0, ClickType.PICKUP, Minecraft.player);
                Fly.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, Minecraft.player);
            }
        }
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        block9: {
            block8: {
                if (Minecraft.player == null) break block8;
                if (Minecraft.world != null) break block9;
            }
            return;
        }
        switch (this.mode.getIndex()) {
            case 1: {
                IPacket<?> iPacket = e.getPacket();
                if (!(iPacket instanceof SPlayerPositionLookPacket)) break;
                SPlayerPositionLookPacket p = (SPlayerPositionLookPacket)iPacket;
                if (Minecraft.player == null) {
                    this.toggle();
                }
                Minecraft.player.setPosition(p.getX(), p.getY(), p.getZ());
                Minecraft.player.connection.sendPacket(new CConfirmTeleportPacket(p.getTeleportId()));
                e.cancel();
                this.toggle();
                break;
            }
            case 3: {
                IPacket<?> iPacket = e.getPacket();
                if (!(iPacket instanceof SPlayerPositionLookPacket)) break;
                SPlayerPositionLookPacket p = (SPlayerPositionLookPacket)iPacket;
                this.toggle();
            }
        }
    }

    private void updatePlayerMotion() {
        double motionX = Minecraft.player.getMotion().x;
        double motionY = this.getMotionY();
        double motionZ = Minecraft.player.getMotion().z;
        MoveUtils.setMotion(((Float)this.horizontal.get()).floatValue());
        Minecraft.player.motion.y = motionY;
    }

    private double getMotionY() {
        return Fly.mc.gameSettings.keyBindSneak.pressed ? (double)(-((Float)this.vertical.get()).floatValue()) : (Fly.mc.gameSettings.keyBindJump.pressed ? (double)((Float)this.vertical.get()).floatValue() : 0.0);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.player.abilities.isFlying = false;
    }
}

