/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventKey;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.functions.settings.impl.StringSetting;
import im.expensive.utils.math.StopWatch;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

@FunctionRegister(name="ChatHelper", type=Category.Render)
public class ChatHelper
extends Function {
    public final BooleanSetting autoAuth = new BooleanSetting("\u0410\u0432\u0442\u043e \u043b\u043e\u0433\u0438\u043d", true);
    public final ModeSetting loginMode = new ModeSetting("\u041f\u0430\u0440\u043e\u043b\u044c", "tryhackmecom1337", "tryhackmecom1337", "1t_M4kes_N0n_S3ns3", "Custom").setVisible(() -> (Boolean)this.autoAuth.get());
    public final StringSetting customPassword = new StringSetting("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439 \u043f\u0430\u0440\u043e\u043b\u044c", "123123123qwe", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u0432\u0430\u0448\u0435\u0433\u043e \u043f\u0430\u0440\u043e\u043b\u044f").setVisible(() -> (Boolean)this.autoAuth.get() != false && this.loginMode.is("Custom"));
    public final BooleanSetting autoText = new BooleanSetting("\u0410\u0432\u0442\u043e \u0442\u0435\u043a\u0441\u0442", false);
    public final BindSetting textBind = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430", -1).setVisible(() -> (Boolean)this.autoText.get());
    public final ModeSetting textMode = new ModeSetting("\u0422\u0435\u043a\u0441\u0442", "\u041a\u043e\u0440\u0434\u044b", "\u041a\u043e\u0440\u0434\u044b", "ezz", "Custom").setVisible(() -> (Boolean)this.autoText.get());
    public final StringSetting customText = new StringSetting("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439 \u0442\u0435\u043a\u0441\u0442", "\u0436\u0434\u0438 \u0441\u0432\u0430\u0442", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442").setVisible(() -> (Boolean)this.autoText.get() != false && this.textMode.is("Custom"));
    public final BooleanSetting globalChat = new BooleanSetting("\u041f\u0438\u0441\u0430\u0442\u044c \u0432 \u0433\u043b\u043e\u0431\u0430\u043b", true).setVisible(() -> (Boolean)this.autoText.get());
    public final BooleanSetting spammer = new BooleanSetting("C\u043f\u0430\u043c\u043c\u0435\u0440", false);
    public final SliderSetting spamDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0432 \u0441\u0435\u043a\u0443\u043d\u0434\u0430\u0445", 5.0f, 1.0f, 30.0f, 0.5f).setVisible(() -> (Boolean)this.spammer.get());
    public final StringSetting spammerText = new StringSetting("\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0439 \u0442\u0435\u043a\u0441\u0442", "\u0436\u0434\u0438 \u0441\u0432\u0430\u0442", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442").setVisible(() -> (Boolean)this.spammer.get());
    public final BooleanSetting emoji = new BooleanSetting("\u042d\u043c\u043e\u0434\u0437\u0438", true);
    public List<String> lastMessages = new ArrayList<String>();
    public String password;
    public StopWatch stopWatch = new StopWatch();

    public ChatHelper() {
        this.addSettings(this.autoAuth, this.loginMode, this.customPassword, this.autoText, this.textBind, this.textMode, this.customText, this.globalChat, this.spammer, this.spamDelay, this.spammerText, this.emoji);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (!(((Boolean)this.autoAuth.get()).booleanValue() || ((Boolean)this.autoAuth.get()).booleanValue() || ((Boolean)this.spammer.get()).booleanValue() || ((Boolean)this.emoji.get()).booleanValue())) {
            this.toggle();
        }
        if (this.loginMode.is("tryhackmecom1337")) {
            this.password = "tryhackmecom1337";
        } else if (this.loginMode.is("1t_M4kes_N0n_S3ns3")) {
            this.password = "1t_M4kes_N0n_S3ns3";
        } else if (this.loginMode.is("Custom")) {
            this.password = (String)this.customPassword.get();
        }
        if (((Boolean)this.spammer.get()).booleanValue() && this.stopWatch.isReached(((Float)this.spamDelay.get()).longValue() * 1000L)) {
            Minecraft.player.sendChatMessage((String)this.spammerText.get());
        }
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        if (e.isReceive()) {
            if (Minecraft.player == null) {
                return;
            }
            IPacket<?> iPacket = e.getPacket();
            if (iPacket instanceof SChatPacket) {
                SChatPacket wrapper2 = (SChatPacket)iPacket;
                if (this.lastMessages.size() > 10) {
                    this.lastMessages.remove(0);
                }
                this.lastMessages.add(wrapper2.getChatComponent().getString());
                List acceptWords = List.of((Object)"reg", (Object)"register", (Object)"\u0417\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c", (Object)"/reg \u041f\u0430\u0440\u043e\u043b\u044c \u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435\u041f\u0430\u0440\u043e\u043b\u044c");
                List loginWords = List.of((Object)"login", (Object)"/l", (Object)"\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0443\u0439\u0442\u0435\u0441\u044c", (Object)"/login \u041f\u0430\u0440\u043e\u043b\u044c", (Object)"/login <password>");
                boolean containsWords = false;
                for (String string : this.lastMessages) {
                    for (Object acceptWord : acceptWords) {
                        if (containsWords) continue;
                        containsWords = string.contains((CharSequence)acceptWord);
                    }
                }
                boolean containsLoginWords = false;
                for (String lastMessage : this.lastMessages) {
                    for (String acceptWord : loginWords) {
                        if (containsWords) continue;
                        containsLoginWords = lastMessage.contains(acceptWord);
                    }
                }
                boolean bl = containsWords;
                boolean containsLogin = containsLoginWords;
                String emptyField = "\u0412\u044b \u043d\u0435 \u0443\u043a\u0430\u0437\u0430\u043b\u0438 \u043f\u0430\u0440\u043e\u043b\u044c, \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f \u043f\u043e\u0434 \u043f\u0430\u0440\u043e\u043b\u0435\u043c " + TextFormatting.GREEN + "qweasdzxc";
                String success = "\u0412\u0430\u0448 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u0431\u044b\u043b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d";
                String successLogin = "\u0410\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u044f \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u043e\u0439\u0434\u0435\u043d\u0430";
                if (containsLogin || bl) {
                    if (this.password == null || this.password.equals("")) {
                        assert (Minecraft.player != null);
                        if (bl && !this.lastMessages.contains(emptyField) && !this.lastMessages.contains(success)) {
                            this.print(emptyField);
                            Minecraft.player.sendChatMessage("/register qweasdzxc123 qweasdzxc123");
                        }
                        if (containsLogin && !this.lastMessages.contains(emptyField) && !this.lastMessages.contains(success)) {
                            this.print(this.getName() + ": \u042f \u043d\u0435 \u0437\u043d\u0430\u044e \u0432\u0430\u0448 \u043f\u0430\u0440\u043e\u043b\u044c!");
                        }
                        this.lastMessages.clear();
                    } else {
                        assert (Minecraft.player != null);
                        if (bl && !this.lastMessages.contains(emptyField) && !this.lastMessages.contains(success)) {
                            Minecraft.player.sendChatMessage("/register " + this.password + " " + this.password);
                        }
                        if (containsLogin && !this.lastMessages.contains(emptyField) && !this.lastMessages.contains(success)) {
                            Minecraft.player.sendChatMessage("/login " + this.password);
                        }
                        this.lastMessages.clear();
                        GLFW.glfwSetClipboardString(Minecraft.getInstance().getMainWindow().getHandle(), this.password);
                        this.print("\u041f\u0430\u0440\u043e\u043b\u044c \u0441\u043a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u043c \u0432 \u0431\u0443\u0444\u0435\u0440 \u043e\u0431\u043c\u0435\u043d\u0430!");
                    }
                }
            }
        }
    }

    @Subscribe
    public void onKeyPress(EventKey e) {
        if (e.getKey() == ((Integer)this.textBind.get()).intValue() && ((Boolean)this.autoText.get()).booleanValue()) {
            String text = (Boolean)this.globalChat.get() != false ? "!" : "";
            String pos = (int)Minecraft.player.getPosX() + ", " + (int)Minecraft.player.getPosY() + ", " + (int)Minecraft.player.getPosZ();
            if (this.textMode.is("\u041a\u043e\u0440\u0434\u044b")) {
                Minecraft.player.sendChatMessage(text + pos);
            }
            if (this.textMode.is("ezz")) {
                Minecraft.player.sendChatMessage(text + "ezz");
            }
            if (this.textMode.is("Custom")) {
                Minecraft.player.sendChatMessage(text + (String)this.customText.get());
            }
        }
    }
}

