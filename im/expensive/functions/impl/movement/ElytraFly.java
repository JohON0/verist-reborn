/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AirItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;

@FunctionRegister(name="ElytraFly", type=Category.Movement)
public class ElytraFly
extends Function {
    StopWatch stopWatch = new StopWatch();
    int oldSlot = -1;
    int bestSlot = -1;
    long delay;

    @Subscribe
    public void onUpdate(EventUpdate e) {
        this.bestSlot = InventoryUtil.getInstance().findBestSlotInHotBar();
        boolean slotNotNull = Minecraft.player.inventory.getStackInSlot(this.bestSlot).getItem() != Items.AIR;
        int invSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, false);
        int hbSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, true);
        if (InventoryUtil.getInstance().getSlotInInventory(Items.FIREWORK_ROCKET) == -1) {
            return;
        }
        int elytraSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.ELYTRA, true);
        if (elytraSlot == -1) {
            this.print("\u0412\u043e\u0437\u044c\u043c\u0438\u0442\u0435 \u044d\u043b\u0438\u0442\u0440\u0443 \u0432 \u0445\u043e\u0442\u0431\u0430\u0440.");
            this.toggle();
            return;
        }
        if (Minecraft.player.isOnGround() && !ElytraFly.mc.gameSettings.keyBindJump.pressed) {
            ElytraFly.mc.gameSettings.keyBindJump.setPressed(true);
        }
        if (!Minecraft.player.isInWater()) {
            if (!Minecraft.player.isOnGround()) {
                if (!Minecraft.player.isElytraFlying()) {
                    if (!(Minecraft.player.inventory.armorItemInSlot(2).getItem() instanceof ElytraItem)) {
                        ElytraFly.mc.playerController.windowClick(0, 6, elytraSlot, ClickType.SWAP, Minecraft.player);
                        Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                        ElytraFly.mc.playerController.windowClick(0, 6, elytraSlot, ClickType.SWAP, Minecraft.player);
                        if (this.stopWatch.isReached(800L)) {
                            this.swapAndUseFireWorkFromInv(invSlot, hbSlot, slotNotNull);
                            this.stopWatch.reset();
                        }
                    } else if (this.bestSlot != -1) {
                        ElytraFly.mc.playerController.windowClick(0, 6, this.bestSlot, ClickType.SWAP, Minecraft.player);
                    }
                }
            }
        }
    }

    public static int findNullSlot() {
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (!(stack.getItem() instanceof AirItem)) continue;
            if (i < 9) {
                i += 36;
            }
            return i;
        }
        return 999;
    }

    private void swapAndUseFireWorkFromInv(int slot, int hbSlot, boolean slotNotNull) {
        if (hbSlot == -1) {
            if (slot >= 0) {
                InventoryUtil.moveItem(slot, this.bestSlot + 36, slotNotNull);
                if (slotNotNull && this.oldSlot == -1) {
                    this.oldSlot = slot;
                }
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(this.bestSlot));
                ElytraFly.mc.playerController.syncCurrentPlayItem();
                Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                Minecraft.player.swingArm(Hand.MAIN_HAND);
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
                ElytraFly.mc.playerController.syncCurrentPlayItem();
                MoveUtils.setMotion(MoveUtils.getMotion());
                if (this.oldSlot != -1) {
                    ElytraFly.mc.playerController.windowClick(0, this.oldSlot, 0, ClickType.PICKUP, Minecraft.player);
                    ElytraFly.mc.playerController.windowClick(0, this.bestSlot + 36, 0, ClickType.PICKUP, Minecraft.player);
                    ElytraFly.mc.playerController.windowClickFixed(0, this.oldSlot, 0, ClickType.PICKUP, Minecraft.player, 100);
                    this.oldSlot = -1;
                    this.bestSlot = -1;
                    Minecraft.player.resetActiveHand();
                }
            } else {
                this.print("\u0424\u0435\u0435\u0440\u0432\u0435\u0440\u043a\u0438 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b!");
            }
        } else {
            this.useItem(hbSlot, Hand.MAIN_HAND);
        }
    }

    private void useItem(int slot, Hand hand) {
        if (Minecraft.player.getHeldItemMainhand().getItem() != Items.FIREWORK_ROCKET) {
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(slot));
        }
        Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        Minecraft.player.swingArm(Hand.MAIN_HAND);
        if (Minecraft.player.getHeldItemMainhand().getItem() != Items.FIREWORK_ROCKET) {
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
        }
    }
}

