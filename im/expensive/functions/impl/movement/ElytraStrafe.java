/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="ElytraStrafe", type=Category.Movement)
public class ElytraStrafe
extends Function {
    private final SliderSetting speedBoost = new SliderSetting("\u0411\u0443\u0441\u0442", 2.95f, 1.0f, 2.0f, 0.05f);
    boolean groundStart = false;

    public ElytraStrafe() {
        this.addSettings(this.speedBoost);
    }

    @Subscribe
    public void onUpdate(EventUpdate event) {
        if (event instanceof EventUpdate) {
            if (InventoryUtil.getItemSlot(Items.FIREWORK_ROCKET.getDefaultInstance()) == -1) {
                return;
            }
            if (!InventoryUtil.doesHotbarHaveItem(Items.ELYTRA)) {
                return;
            }
            if (Minecraft.player.isOnGround()) {
                this.groundStart = true;
            }
            if (Minecraft.player.isElytraFlying()) {
                float speed2 = this.speedBoost.getValue().floatValue();
                Vector3d var10000 = Minecraft.player.getMotion();
                var10000.x *= (double)speed2;
                var10000 = Minecraft.player.getMotion();
                var10000.z *= (double)speed2;
            }
            if (Minecraft.player.fallDistance != 0.0f) {
                if (!Minecraft.player.isElytraFlying() && this.groundStart) {
                    this.groundStart = false;
                    for (int i = 0; i < 36; ++i) {
                        if (Minecraft.player.inventory.getStackInSlot(i).getItem() != Items.ELYTRA) continue;
                        ElytraStrafe.mc.playerController.windowClick(0, 6, i, ClickType.SWAP, Minecraft.player);
                        Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                        ElytraStrafe.mc.playerController.windowClick(0, 6, i, ClickType.SWAP, Minecraft.player);
                        InventoryUtil.inventorySwapClick(Items.FIREWORK_ROCKET);
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

