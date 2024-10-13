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
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.math.StopWatch;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;

@FunctionRegister(name="SaveInventory", type=Category.Misc)
public class SaveInventory
extends Function {
    private final BooleanSetting clanHome = new BooleanSetting("\u041a\u043b\u0430\u043d \u0445\u043e\u043c", true);
    private final BooleanSetting clanInvest = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0434\u0435\u043d\u044c\u0433\u0438", true);
    private final StopWatch stopWatch = new StopWatch();
    private int serverNumber = -1;
    private boolean inHub;
    private boolean inClanHome;

    public SaveInventory() {
        this.addSettings(this.clanHome, this.clanInvest);
    }

    @Subscribe
    private void onPacket(EventPacket packetEvent) {
        SChatPacket chatPacket;
        String chatMessage;
        if (!ClientUtil.isConnectedToServer("funtime")) {
            return;
        }
        IPacket<?> iPacket = packetEvent.getPacket();
        if (iPacket instanceof SChatPacket && (chatMessage = (chatPacket = (SChatPacket)iPacket).getChatComponent().getString().toLowerCase(Locale.ROOT)).contains("\u0443\u0441\u043f\u0435\u0448\u043d\u0430\u044f \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0432 \u0431\u0430\u0437\u0443 \u043a\u043b\u0430\u043d\u0430!")) {
            this.inClanHome = true;
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        String tabHeader = SaveInventory.mc.ingameGUI.getTabList().header.getString();
        if (!tabHeader.contains("\u0425\u0430\u0431")) {
            this.leaveInHub();
        } else {
            this.inHub = true;
        }
        if (this.serverNumber == -1) {
            return;
        }
        if (this.stopWatch.isReached(150L) && this.inHub) {
            this.returnToAnarchyAndDropItem(tabHeader);
            this.stopWatch.reset();
        }
    }

    private void returnToAnarchyAndDropItem(String header) {
        if (!header.contains(String.valueOf(this.serverNumber))) {
            Minecraft.player.sendChatMessage("/an" + this.serverNumber);
            return;
        }
        if (((Boolean)this.clanHome.get()).booleanValue() && !this.inClanHome) {
            Minecraft.player.sendChatMessage("/clan home");
        } else {
            this.dropItems();
        }
    }

    private void dropItems() {
        int i = 0;
        while (true) {
            if (i >= Minecraft.player.container.getInventory().size()) break;
            SaveInventory.mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, Minecraft.player);
            SaveInventory.mc.playerController.windowClick(0, -999, 0, ClickType.PICKUP, Minecraft.player);
            ++i;
        }
        if (this.isInventoryEmpty()) {
            this.toggle();
            this.reset();
        }
    }

    private boolean isInventoryEmpty() {
        int i = 0;
        while (true) {
            if (i >= Minecraft.player.container.getInventory().size()) break;
            if (!Minecraft.player.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private void leaveInHub() {
        System.gc();
        this.serverNumber = this.getAnarchyServerNumber();
        if (this.stopWatch.isReached(100L) && !this.inHub) {
            Minecraft.player.sendChatMessage("/hub");
            this.stopWatch.reset();
        }
    }

    private int getAnarchyServerNumber() {
        String serverHeader;
        if (SaveInventory.mc.ingameGUI.getTabList().header != null && (serverHeader = TextFormatting.getTextWithoutFormattingCodes(SaveInventory.mc.ingameGUI.getTabList().header.getString())) != null && serverHeader.contains("\u0410\u043d\u0430\u0440\u0445\u0438\u044f-")) {
            return Integer.parseInt(serverHeader.split("\u0410\u043d\u0430\u0440\u0445\u0438\u044f-")[1].trim());
        }
        return -1;
    }

    private void reset() {
        this.inHub = false;
        this.inClanHome = false;
        this.serverNumber = -1;
    }

    @Override
    public void onDisable() {
        this.reset();
        super.onDisable();
    }
}

