/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.StringSetting;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.math.StopWatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;

@FunctionRegister(name="AutoTransfer", type=Category.Misc)
public class AutoTransfer
extends Function {
    private final StringSetting anarchyNumberSetting = new StringSetting("\u0410\u043d\u0430\u0440\u0445\u0438\u044f", "", "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u043e\u043c\u0435\u0440 \u0430\u043d\u0430\u0440\u0445\u0438\u0438 \n\u0434\u043b\u044f \u043f\u0435\u0440\u0435\u043d\u043e\u0441\u0430 \u0432\u0435\u0449\u0435\u0439", true);
    private final StringSetting itemsCountSetting = new StringSetting("\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", "", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u0434\u043b\u044f \u043f\u0440\u043e\u0434\u0430\u0436\u0438", true);
    private final StringSetting sellPriceSetting = new StringSetting("\u0426\u0435\u043d\u0430", "", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0446\u0435\u043d\u0443 (\u043e\u0442 10$)", true);
    private final StopWatch stopWatch = new StopWatch();
    private final StopWatch changeServerStopWatch = new StopWatch();
    private boolean allItemsToSell = false;
    private boolean connectedToServer = false;
    private final List<Item> playerItems = new ArrayList<Item>();
    private int sellCount = 0;
    private boolean isReadyToSell;
    List<ItemStack> stacks = new LinkedList<ItemStack>();

    public AutoTransfer() {
        this.addSettings(this.anarchyNumberSetting, this.itemsCountSetting, this.sellPriceSetting);
    }

    @Subscribe
    private void onPacket(EventPacket packetEvent) {
        if (!ClientUtil.isConnectedToServer("funtime")) {
            return;
        }
        IPacket<?> iPacket = packetEvent.getPacket();
        if (iPacket instanceof SChatPacket) {
            SChatPacket chatPacket = (SChatPacket)iPacket;
            String chatMessage = chatPacket.getChatComponent().getString().toLowerCase(Locale.ROOT);
            if (chatMessage.contains("\u043e\u0441\u0432\u043e\u0431\u043e\u0434\u0438\u0442\u0435 \u0445\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435") && !this.playerItems.isEmpty()) {
                this.allItemsToSell = true;
            }
            if (chatMessage.contains("\u0432\u044b \u0443\u0436\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u044b")) {
                this.connectedToServer = true;
            }
            if (chatMessage.contains("\u0432\u044b\u0441\u0442\u0430\u0432\u043b\u0435\u043d \u043d\u0430 \u043f\u0440\u043e\u0434\u0430\u0436\u0443")) {
                ++this.sellCount;
            }
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate updateEvent) {
        int itemCountToSell;
        String serverHeader;
        if (Minecraft.player.ticksExisted < 500 && !this.isReadyToSell) {
            int ticksRemaining = 500 - Minecraft.player.ticksExisted;
            int secondsRemaining = ticksRemaining / 20;
            this.print("\u041f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435 \u0435\u0449\u0451 " + TextFormatting.RED + secondsRemaining + TextFormatting.RESET + " \u0441\u0435\u043a\u0443\u043d\u0434(\u044b), \u043f\u0440\u0435\u0436\u0434\u0435 \u0447\u0435\u043c \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c");
            this.toggle();
            return;
        }
        if (AutoTransfer.mc.ingameGUI.getTabList().header != null && (serverHeader = TextFormatting.getTextWithoutFormattingCodes(AutoTransfer.mc.ingameGUI.getTabList().header.getString())) != null && serverHeader.contains((CharSequence)this.anarchyNumberSetting.get())) {
            this.connectedToServer = true;
        }
        if ((itemCountToSell = Integer.parseInt((String)this.itemsCountSetting.get())) > 9) {
            this.print("\u041a\u043e\u043b-\u0432\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u043d\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u043f\u0440\u0435\u0432\u044b\u0448\u0430\u0442\u044c 9!");
            this.toggle();
            return;
        }
        int sellPrice = Integer.parseInt((String)this.sellPriceSetting.get());
        if (!this.isReadyToSell) {
            for (int i = 0; i < 9; ++i) {
                if (Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.AIR || !this.stopWatch.isReached(100L)) continue;
                Minecraft.player.inventory.currentItem = i;
                Minecraft.player.sendChatMessage("/ah dsell " + sellPrice);
                this.playerItems.add(Minecraft.player.inventory.getStackInSlot(i).getItem());
                this.stopWatch.reset();
            }
        }
        if (this.sellCount >= itemCountToSell || this.allItemsToSell) {
            this.isReadyToSell = true;
            int anarchyNumber = Integer.parseInt((String)this.anarchyNumberSetting.get());
            if (!this.connectedToServer) {
                if (this.changeServerStopWatch.isReached(100L)) {
                    Minecraft.player.sendChatMessage("/an" + anarchyNumber);
                    this.changeServerStopWatch.reset();
                }
                return;
            }
            Container container = Minecraft.player.openContainer;
            if (container instanceof ChestContainer) {
                ChestContainer container2 = (ChestContainer)container;
                IInventory lowerChestInventory = container2.getLowerChestInventory();
                for (int index = 0; index < lowerChestInventory.getSizeInventory(); ++index) {
                    if (!this.stopWatch.isReached(200L) || lowerChestInventory.getStackInSlot(index).getItem() == Items.AIR) continue;
                    if (this.playerItems.contains(lowerChestInventory.getStackInSlot(index).getItem())) {
                        AutoTransfer.mc.playerController.windowClick(container2.windowId, index, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        this.stopWatch.reset();
                        continue;
                    }
                    this.resetAndToggle();
                    this.toggle();
                }
            } else if (this.stopWatch.isReached(500L)) {
                Minecraft.player.sendChatMessage("/ah " + Minecraft.player.getNameClear());
                this.stopWatch.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        this.resetAndToggle();
        super.onDisable();
    }

    private void resetAndToggle() {
        this.allItemsToSell = false;
        this.connectedToServer = false;
        this.playerItems.clear();
        this.isReadyToSell = false;
        this.sellCount = 0;
    }
}

