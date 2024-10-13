/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

@FunctionRegister(name="BetterChat", type=Category.Misc)
public class BetterChat
extends Function {
    private String lastMessage = "";
    private int amount = 1;
    private int line = 0;

    @Subscribe
    private void onPacket(EventPacket e) {
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SChatPacket) {
            ITextComponent newMessage;
            SChatPacket chatPacket = (SChatPacket)iPacket;
            ITextComponent originalMessage = chatPacket.getChatComponent();
            String rawMessage = originalMessage.getUnformattedComponentText();
            NewChatGui chatGui = BetterChat.mc.ingameGUI.getChatGUI();
            if (this.lastMessage.equals(rawMessage)) {
                chatGui.deleteChatLine(this.line);
                ++this.amount;
                newMessage = ITextComponent.Serializer.getComponentFromJsonLenient("{\"text\":\"" + originalMessage.getString() + "\",\"extra\":[{\"text\":\" " + TextFormatting.GRAY + "[x" + this.amount + "]\"}]}");
            } else {
                this.amount = 1;
                newMessage = originalMessage;
            }
            ++this.line;
            this.lastMessage = rawMessage;
            chatGui.printChatMessageWithOptionalDeletion(newMessage, this.line);
            if (this.line > 256) {
                this.line = 0;
            }
            e.cancel();
        }
    }
}

