/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.JoinerUtil;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.paste.EventPacket;
import im.expensive.utils.paste.EventUpdate;
import im.expensive.utils.paste.SoundUtils;
import im.expensive.utils.paste.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.network.play.server.SJoinGamePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.Event;

@FunctionRegister(name="RW Joiner", type=Category.Misc)
public class REJOINER
extends Function {
    private final SliderSetting griefSelection = new SliderSetting("\u041d\u043e\u043c\u0435\u0440 \u0433\u0440\u0438\u0444\u0430", 1.0f, 1.0f, 42.0f, 1.0f);
    private final TimerUtil timerUtil = new TimerUtil();

    public REJOINER() {
        this.addSettings(this.griefSelection);
    }

    @Override
    public boolean onEnable() {
        JoinerUtil.selectCompass();
        Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
        Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.OFF_HAND));
        super.onEnable();
        return false;
    }

    @Subscribe
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            this.handleEventUpdate();
        }
        if (event instanceof EventPacket) {
            SChatPacket packet;
            String message;
            Object string2;
            EventPacket eventPacket = (EventPacket)event;
            if (eventPacket.getPacket() instanceof SJoinGamePacket) {
                try {
                    if (IMinecraft.mc.ingameGUI.getTabList().header == null) {
                        return;
                    }
                    String string = TextFormatting.getTextWithoutFormattingCodes(IMinecraft.mc.ingameGUI.getTabList().header.getString());
                    if (!string.contains("Lobby")) {
                        return;
                    }
                    string2 = "\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0437\u0430\u0448\u043b\u0438 \u043d\u0430 " + this.griefSelection.getValue().intValue() + " \u0433\u0440\u0438\u0444!";
                    SoundUtils.playSound(1.0f, 1.0f);
                    this.toggle();
                } catch (Exception string) {
                    // empty catch block
                }
            }
            if ((string2 = eventPacket.getPacket()) instanceof SChatPacket && ((message = TextFormatting.getTextWithoutFormattingCodes((packet = (SChatPacket)string2).getChatComponent().getString())).contains("\u041a \u0441\u043e\u0436\u0430\u043b\u0435\u043d\u0438\u044e \u0441\u0435\u0440\u0432\u0435\u0440 \u043f\u0435\u0440\u0435\u043f\u043e\u043b\u043d\u0435\u043d") || message.contains("\u041f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435 20 \u0441\u0435\u043a\u0443\u043d\u0434!") || message.contains("\u0431\u043e\u043b\u044c\u0448\u043e\u0439 \u043f\u043e\u0442\u043e\u043a \u0438\u0433\u0440\u043e\u043a\u043e\u0432"))) {
                JoinerUtil.selectCompass();
                Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            }
        }
    }

    private void handleEventUpdate() {
        if (IMinecraft.mc.currentScreen == null) {
            if (Minecraft.player.ticksExisted < 5) {
                Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            }
        } else if (IMinecraft.mc.currentScreen instanceof ChestScreen) {
            try {
                int numberGrief = this.griefSelection.getValue().intValue();
                ContainerScreen container = (ContainerScreen)IMinecraft.mc.currentScreen;
                for (int i = 0; i < ((Container)container.getContainer()).inventorySlots.size(); ++i) {
                    String s = ((Container)container.getContainer()).inventorySlots.get(i).getStack().getDisplayName().getString();
                    if (ClientUtil.isConnectedToServer("reallyworld") && s.contains("\u0413\u0420\u0418\u0424\u0415\u0420\u0421\u041a\u041e\u0415 \u0412\u042b\u0416\u0418\u0412\u0410\u041d\u0418\u0415 (1.16.5-1.20.4)") && this.timerUtil.hasTimeElapsed(50L)) {
                        IMinecraft.mc.playerController.windowClick(Minecraft.player.openContainer.windowId, i, 0, ClickType.PICKUP, Minecraft.player);
                        this.timerUtil.reset();
                    }
                    if (!s.contains("\u0413\u0420\u0418\u0424 #" + numberGrief + " (1.16.5)") || !this.timerUtil.hasTimeElapsed(50L)) continue;
                    IMinecraft.mc.playerController.windowClick(Minecraft.player.openContainer.windowId, i, 0, ClickType.PICKUP, Minecraft.player);
                    this.timerUtil.reset();
                }
            } catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

