/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.StringSetting;
import im.expensive.utils.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;

@FunctionRegister(name="AutoAuth", type=Category.Player)
public class AutoAuth
extends Function {
    final StringSetting pass = new StringSetting("\u041f\u0430\u0440\u043e\u043b\u044c", "123", "\u0410\u0432\u0442\u043e \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044f - \u043b\u043e\u0433\u0438\u043d");
    StopWatch delay = new StopWatch();
    boolean isLogined = false;

    public AutoAuth() {
        this.addSettings(this.pass);
    }

    @Subscribe
    public void onPacket(EventPacket eventPacket) {
        IPacket<?> packet = eventPacket.getPacket();
        if (packet instanceof SChatPacket) {
            SChatPacket chatPacket = (SChatPacket)packet;
            String message = chatPacket.getChatComponent().getString();
            if (message.contains("\u0423\u0441\u043f\u0435\u0448\u043d\u0430\u044f \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u044f!")) {
                this.isLogined = true;
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException es) {
                    throw new RuntimeException(es);
                }
                this.isLogined = false;
            }
            if (message.contains("\u0417\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c") && !this.isLogined && this.delay.isReached(250L)) {
                Minecraft.player.sendChatMessage("/register " + (String)this.pass.get());
                this.delay.reset();
            }
            if (message.contains("\u0412\u043e\u0439\u0434\u0438\u0442\u0435 \u0432 \u0438\u0433\u0440\u0443") && !this.isLogined && this.delay.isReached(250L)) {
                Minecraft.player.sendChatMessage("/login " + (String)this.pass.get());
                this.delay.reset();
            }
        }
    }
}

