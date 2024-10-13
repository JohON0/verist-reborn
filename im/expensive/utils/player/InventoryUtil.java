/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import im.expensive.events.EventPacket;
import im.expensive.utils.client.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;

public class InventoryUtil
implements IMinecraft {
    private static InventoryUtil instance = new InventoryUtil();
    private static Item item;

    public static int findEmptySlot(boolean inHotBar) {
        int start = inHotBar ? 0 : 9;
        int end = inHotBar ? 9 : 45;
        for (int i = start; i < end; ++i) {
            if (!Minecraft.player.inventory.getStackInSlot(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    public static void moveItem(int from, int to, boolean air) {
        if (from == to) {
            return;
        }
        InventoryUtil.pickupItem(from, 0);
        InventoryUtil.pickupItem(to, 0);
        if (air) {
            InventoryUtil.pickupItem(from, 0);
        }
    }

    public static void moveItemTime(int from, int to, boolean air, int time) {
        if (from == to) {
            return;
        }
        InventoryUtil.pickupItem(from, 0, time);
        InventoryUtil.pickupItem(to, 0, time);
        if (air) {
            InventoryUtil.pickupItem(from, 0, time);
        }
    }

    public static void moveItem(int from, int to) {
        if (from == to) {
            return;
        }
        InventoryUtil.pickupItem(from, 0);
        InventoryUtil.pickupItem(to, 0);
        InventoryUtil.pickupItem(from, 0);
    }

    public static void pickupItem(int slot, int button) {
        InventoryUtil.mc.playerController.windowClick(0, slot, button, ClickType.PICKUP, Minecraft.player);
    }

    public static void pickupItem(int slot, int button, int time) {
        InventoryUtil.mc.playerController.windowClickFixed(0, slot, button, ClickType.PICKUP, Minecraft.player, time);
    }

    public static boolean doesHotbarHaveItem(Item elytra) {
        for (int i = 0; i < 9; ++i) {
            Minecraft.player.inventory.getStackInSlot(i);
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
            return true;
        }
        return false;
    }

    public static void inventorySwapClick(Item item) {
        if (InventoryUtil.doesHotbarHaveItem(item)) {
            for (int i = 0; i < 9; ++i) {
                if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
                boolean levin1337 = false;
                if (i != Minecraft.player.inventory.currentItem) {
                    Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(i));
                    levin1337 = true;
                }
                Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(net.minecraft.util.Hand.MAIN_HAND));
                if (!levin1337) break;
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
                break;
            }
        }
    }

    public static int getItemSlot(ItemStack input) {
        for (ItemStack stack : Minecraft.player.getArmorInventoryList()) {
            if (stack != input) continue;
            return -2;
        }
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s != input) continue;
            slot = i;
            break;
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }

    public int getAxeInInventory(boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof AxeItem)) continue;
            return i;
        }
        return -1;
    }

    public int findBestSlotInHotBar() {
        int emptySlot = this.findEmptySlot();
        if (emptySlot != -1) {
            return emptySlot;
        }
        return this.findNonSwordSlot();
    }

    private int findEmptySlot() {
        for (int i = 0; i < 9; ++i) {
            if (!Minecraft.player.inventory.getStackInSlot(i).isEmpty()) continue;
            if (Minecraft.player.inventory.currentItem == i) continue;
            return i;
        }
        return -1;
    }

    private int findNonSwordSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof SwordItem) continue;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ElytraItem) continue;
            if (Minecraft.player.inventory.currentItem == i) continue;
            return i;
        }
        return -1;
    }

    public int getSlotInInventory(Item item) {
        int finalSlot = -1;
        for (int i = 0; i < 36; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
            finalSlot = i;
        }
        return finalSlot;
    }

    public int getSlotInInventoryOrHotbar(Item item, boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
            finalSlot = i;
        }
        return finalSlot;
    }

    public static int getSlotInInventoryOrHotbar() {
        int firstSlot = 0;
        int lastSlot = 9;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (!(Block.getBlockFromItem(Minecraft.player.inventory.getStackInSlot(i).getItem()) instanceof SlabBlock)) continue;
            finalSlot = i;
        }
        return finalSlot;
    }

    public static InventoryUtil getInstance() {
        return instance;
    }

    public static class Hand {
        public static boolean isEnabled;
        private boolean isChangingItem;
        private int originalSlot = -1;

        public void onEventPacket(EventPacket eventPacket) {
            if (!eventPacket.isReceive()) {
                return;
            }
            if (eventPacket.getPacket() instanceof SHeldItemChangePacket) {
                this.isChangingItem = true;
            }
        }

        public static boolean doesHotbarHaveItem(Item item) {
            for (int i = 0; i < 9; ++i) {
                Minecraft.player.inventory.getStackInSlot(i);
                if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
                return true;
            }
            return false;
        }

        public static void inventorySwapClick(Item item) {
            if (InventoryUtil.doesHotbarHaveItem(item)) {
                for (int i = 0; i < 9; ++i) {
                    if (Minecraft.player.inventory.getStackInSlot(i).getItem() != item) continue;
                    boolean levin1337 = false;
                    if (i != Minecraft.player.inventory.currentItem) {
                        Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(i));
                        levin1337 = true;
                    }
                    Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(net.minecraft.util.Hand.MAIN_HAND));
                    if (!levin1337) break;
                    Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
                    break;
                }
            }
        }

        public void handleItemChange(boolean resetItem) {
            if (this.isChangingItem && this.originalSlot != -1) {
                isEnabled = true;
                Minecraft.player.inventory.currentItem = this.originalSlot;
                if (resetItem) {
                    this.isChangingItem = false;
                    this.originalSlot = -1;
                    isEnabled = false;
                }
            }
        }

        public void setOriginalSlot(int slot) {
            this.originalSlot = slot;
        }
    }
}

