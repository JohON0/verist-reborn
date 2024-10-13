/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.ab.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import im.expensive.ui.ab.logic.ActivationLogic;
import im.expensive.ui.ab.model.IItem;
import im.expensive.utils.client.IMinecraft;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class AuctionLogic
implements IMinecraft {
    private final ActivationLogic parent;
    private final Minecraft mc;
    private long lastActionTime = 0L;
    private final Random random = new Random();

    public AuctionLogic(ActivationLogic parent) {
        this.parent = parent;
        this.mc = Minecraft.getInstance();
    }

    public void processActive() {
        Screen screen = this.mc.currentScreen;
        if (screen instanceof ChestScreen) {
            ChestScreen chestScreen = (ChestScreen)screen;
            this.processBuy(chestScreen);
        }
    }

    public void processBuy(ChestScreen chestScreen) {
        Object container = chestScreen.getContainer();
        String title = chestScreen.getTitle().getString().toLowerCase();
        if (title.contains("\u043f\u043e\u0434\u043e\u0437\u0440\u0438\u0442\u0435\u043b\u044c\u043d\u0430\u044f \u0446\u0435\u043d\u0430!")) {
            this.performAction(() -> this.mc.playerController.windowClick(container.windowId, 10, 0, ClickType.PICKUP, Minecraft.player));
        } else if (title.contains("\u0430\u0443\u043a\u0446\u0438\u043e\u043d") || title.contains("\u043f\u043e\u0438\u0441\u043a:")) {
            this.auctionBotLogic((Container)container, chestScreen);
        }
    }

    public void auctionBotLogic(Container container, ChestScreen chestScreen) {
        for (Slot slot : container.inventorySlots) {
            this.processAuctionSlot(chestScreen, slot);
        }
        this.performAction(() -> this.safeClick(container.windowId, 49, ClickType.QUICK_MOVE));
    }

    public void processAuctionSlot(ChestScreen chestScreen, Slot slot) {
        Object container = chestScreen.getContainer();
        if (this.parent.itemStorage == null) {
            return;
        }
        CopyOnWriteArrayList<IItem> items = this.parent.itemStorage.getItems();
        for (IItem item : items) {
            String sellerName;
            boolean itemIsFound;
            int targetPrice = item.getPrice();
            int currentPrice = this.extractPriceFromStack(slot.getStack());
            boolean bl = itemIsFound = currentPrice != -1 && currentPrice <= targetPrice && this.isItemWasFound(item, slot);
            if (this.parent.itemList.contains(slot.getStack()) || slot.slotNumber > 48 || !itemIsFound || !this.checkItem(item, slot.getStack()) || (sellerName = this.extractPidorFromStack(slot.getStack())).isEmpty()) continue;
            this.performAction(() -> this.buyItem((Container)container, slot));
        }
    }

    protected void performAction(Runnable action) {
        long delay;
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastActionTime > (delay = (long)(750 + this.random.nextInt(500)))) {
            action.run();
            this.lastActionTime = currentTime;
        }
    }

    protected void buyItem(Container container, Slot slot) {
        int slotId = slot.slotNumber;
        this.performAction(() -> {
            try {
                Thread.sleep(200 + this.random.nextInt(300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mc.playerController.windowClick(container.windowId, slotId, 0, ClickType.QUICK_MOVE, Minecraft.player);
            this.parent.itemList.add(slot.getStack());
        });
    }

    protected boolean checkItem(IItem item, ItemStack stack) {
        boolean don;
        boolean bl = don = stack.getTag() != null && stack.getTag().contains("don-item");
        if (stack.getCount() < item.getQuantity()) {
            return false;
        }
        if (!item.getEnchantments().isEmpty()) {
            Map<Enchantment, Integer> stackEnchants = EnchantmentHelper.getEnchantments(stack);
            for (Map.Entry<Enchantment, Integer> enchantmentEntry : item.getEnchantments().entrySet()) {
                if (stackEnchants.containsKey(enchantmentEntry.getKey()) && stackEnchants.get(enchantmentEntry.getKey()) >= enchantmentEntry.getValue()) continue;
                return false;
            }
        }
        return true;
    }

    private void safeClick(int windowId, int slotId, ClickType clickType) {
        this.performAction(() -> this.mc.playerController.windowClick(windowId, slotId, 0, clickType, Minecraft.player));
    }

    private boolean isItemWasFound(IItem item, Slot slot) {
        return item.getItem() == slot.getStack().getItem();
    }

    protected int extractPriceFromStack(ItemStack stack) {
        CompoundNBT display;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("display", 10) && (display = tag.getCompound("display")).contains("Lore", 9)) {
            ListNBT lore = display.getList("Lore", 8);
            for (int j = 0; j < lore.size(); ++j) {
                JsonObject title;
                JsonArray array;
                JsonObject object = JsonParser.parseString(lore.getString(j)).getAsJsonObject();
                if (!object.has("extra") || (array = object.getAsJsonArray("extra")).size() <= 2 || !(title = array.get(1).getAsJsonObject()).get("text").getAsString().trim().toLowerCase().contains("\u0446\u0435\u043da")) continue;
                String line = array.get(2).getAsJsonObject().get("text").getAsString().trim().substring(1).replaceAll(" ", "");
                return Integer.parseInt(line);
            }
        }
        return -1;
    }

    protected String extractPidorFromStack(ItemStack stack) {
        CompoundNBT display;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("display", 10) && (display = tag.getCompound("display")).contains("Lore", 9)) {
            ListNBT lore = display.getList("Lore", 8);
            for (int j = 0; j < lore.size(); ++j) {
                JsonObject title;
                JsonArray array;
                JsonObject object = JsonParser.parseString(lore.getString(j)).getAsJsonObject();
                if (!object.has("extra") || (array = object.getAsJsonArray("extra")).size() <= 2 || !(title = array.get(1).getAsJsonObject()).get("text").getAsString().trim().toLowerCase().startsWith("\u043f\u0440o\u0434a\u0432e\u0446")) continue;
                return array.get(2).getAsJsonObject().get("text").getAsString().trim().replaceAll(" ", "");
            }
        }
        return "";
    }
}

