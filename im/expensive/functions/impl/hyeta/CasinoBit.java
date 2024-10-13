/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;

@FunctionRegister(name="CasinoBOT", type=Category.Player)
public class CasinoBit
extends Function {
    private final StopWatch timer = new StopWatch();
    private PlayerEntity currentPlayer;
    private int wallet;
    private final Random randomizer = new Random();

    @Subscribe
    public void onGameTick(EventUpdate event) {
        if (this.timer.isReached(20000L)) {
            ClientPlayerEntity player = Minecraft.player;
            String playerName = player.getScoreboardName();
            player.sendChatMessage("!\u041f\u0440\u0438\u0432\u0435\u0442! \u042f - \u0432\u0430\u0448 \u043b\u0438\u0447\u043d\u044b\u0439  \u041a\u0430\u0437\u0438\u043d\u043e-\u0411\u043e\u0442. \u041f\u0440\u043e\u0432\u0435\u0440\u044c\u0442\u0435 \u0441\u0432\u043e\u044e \u0443\u0434\u0430\u0447\u0443! /pay " + playerName + " \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e. \u0412 \u0441\u043b\u0443\u0447\u0430\u0435 \u0443\u0441\u043f\u0435\u0445\u0430, \u044f \u0443\u0434\u0432\u043e\u044e \u0432\u0430\u0448\u0443 \u0441\u0443\u043c\u043c\u0443. \u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u0443\u043c\u043c\u0430 \u0434\u043b\u044f \u0438\u0433\u0440\u044b - 5000.");
            this.timer.reset();
        }
    }

    @Subscribe
    public void onIncomingPacket(EventPacket event) {
        SChatPacket chatPacket;
        String messageContent;
        IPacket<?> packet = event.getPacket();
        if (packet instanceof SChatPacket && (messageContent = (chatPacket = (SChatPacket)packet).getChatComponent().getString().toLowerCase(Locale.ROOT)).contains("\u043f\u043e\u043b\u0443\u0447\u0435\u043d\u043e \u043e\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 ")) {
            boolean isWinner;
            String[] messageWords = messageContent.split(" ");
            String sender = messageWords[messageWords.length - 1];
            Pattern pattern = Pattern.compile("\\$(\\d{1,3}(,\\d{3})*)");
            Matcher matcher = pattern.matcher(messageContent);
            int sum = 0;
            if (matcher.find()) {
                String extractedAmount = matcher.group(1).replace(",", "");
                sum = Integer.parseInt(extractedAmount);
            }
            if (sum < 5000) {
                Minecraft.player.sendChatMessage("/m " + sender + " \u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u0443\u043c\u043c\u0430 \u0434\u043b\u044f \u0443\u0447\u0430\u0441\u0442\u0438\u044f - 5000 \u043c\u043e\u043d\u0435\u0442.");
                return;
            }
            boolean bl = isWinner = this.randomizer.nextDouble() < 0.35;
            if (isWinner) {
                Minecraft.player.sendChatMessage("/m " + sender + " \u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u044f\u044e, \u0432\u044b \u0432\u044b\u0438\u0433\u0440\u0430\u043b\u0438!");
                int reward = sum * 2;
                Minecraft.player.sendChatMessage("/pay " + sender + " " + reward);
                Minecraft.player.sendChatMessage("/pay " + sender + " " + reward);
            } else {
                Minecraft.player.sendChatMessage("/m " + sender + " \u041a \u0441\u043e\u0436\u0430\u043b\u0435\u043d\u0438\u044e, \u0432\u044b \u043f\u0440\u043e\u0438\u0433\u0440\u0430\u043b\u0438.");
            }
        }
    }
}

