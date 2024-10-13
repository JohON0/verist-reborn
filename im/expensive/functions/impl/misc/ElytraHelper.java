/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventKey;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;

@FunctionRegister(name="ElytraHelper", type=Category.Misc)
public class ElytraHelper
extends Function {
    private final BindSetting swapChestKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0441\u0432\u0430\u043f\u0430", -1);
    private final BindSetting fireWorkKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0444\u0435\u0435\u0440\u0432\u0435\u0440\u043a\u043e\u0432", -1);
    private final BooleanSetting autoFly = new BooleanSetting("\u0410\u0432\u0442\u043e \u0432\u0437\u043b\u0451\u0442", true);
    private final InventoryUtil.Hand handUtil = new InventoryUtil.Hand();
    private ItemStack currentStack = ItemStack.EMPTY;
    public static StopWatch stopWatch = new StopWatch();
    private long delay;
    private boolean fireworkUsed;

    public ElytraHelper() {
        this.addSettings(this.swapChestKey, this.fireWorkKey, this.autoFly);
    }

    @Subscribe
    private void onEventKey(EventKey e) {
        if (e.getKey() == ((Integer)this.swapChestKey.get()).intValue() && stopWatch.isReached(100L)) {
            this.changeChestPlate(this.currentStack);
            stopWatch.reset();
        }
        if (e.getKey() == ((Integer)this.fireWorkKey.get()).intValue() && stopWatch.isReached(200L)) {
            this.fireworkUsed = true;
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.currentStack = Minecraft.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if (((Boolean)this.autoFly.get()).booleanValue() && this.currentStack.getItem() == Items.ELYTRA) {
            if (Minecraft.player.isOnGround()) {
                Minecraft.player.jump();
            } else if (ElytraItem.isUsable(this.currentStack)) {
                if (!Minecraft.player.isElytraFlying()) {
                    Minecraft.player.startFallFlying();
                    Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                }
            }
        }
        if (this.fireworkUsed) {
            int slot;
            int hbSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, true);
            int invSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0424\u0435\u0435\u0440\u0432\u0435\u0440\u043a\u0438 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b!");
                this.fireworkUsed = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.FIREWORK_ROCKET) && (slot = this.findAndTrowItem(hbSlot, invSlot)) > 8) {
                ElytraHelper.mc.playerController.pickItem(slot);
            }
            this.fireworkUsed = false;
        }
        this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        this.handUtil.onEventPacket(e);
    }

    private int findAndTrowItem(int hbSlot, int invSlot) {
        if (hbSlot != -1) {
            this.handUtil.setOriginalSlot(Minecraft.player.inventory.currentItem);
            if (hbSlot != Minecraft.player.inventory.currentItem) {
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            }
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            if (hbSlot != Minecraft.player.inventory.currentItem) {
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
            }
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return hbSlot;
        }
        if (invSlot != -1) {
            this.handUtil.setOriginalSlot(Minecraft.player.inventory.currentItem);
            ElytraHelper.mc.playerController.pickItem(invSlot);
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return invSlot;
        }
        return -1;
    }

    private void changeChestPlate(ItemStack stack) {
        int armorSlot;
        if (ElytraHelper.mc.currentScreen != null) {
            return;
        }
        if (stack.getItem() != Items.ELYTRA) {
            int elytraSlot = this.getItemSlot(Items.ELYTRA);
            if (elytraSlot >= 0) {
                InventoryUtil.moveItem(elytraSlot, 6);
                this.print(TextFormatting.RED + "\u0421\u0432\u0430\u043f\u043d\u0443\u043b \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0443!");
                return;
            }
            this.print("\u042d\u043b\u0438\u0442\u0440\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
        }
        if ((armorSlot = this.getChestPlateSlot()) >= 0) {
            InventoryUtil.moveItem(armorSlot, 6);
            this.print(TextFormatting.RED + "\u0421\u0432\u0430\u043f\u043d\u0443\u043b \u043d\u0430 \u043d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a!");
        } else {
            this.print("\u041d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d!");
        }
    }

    private int getChestPlateSlot() {
        Item[] items;
        for (Item item : items = new Item[]{Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE}) {
            for (int i = 0; i < 36; ++i) {
                Item stack = Minecraft.player.inventory.getStackInSlot(i).getItem();
                if (stack != item) continue;
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDisable() {
        stopWatch.reset();
        super.onDisable();
    }

    private int getItemSlot(Item input) {
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() != input) continue;
            slot = i;
            break;
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }
}

