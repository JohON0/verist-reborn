/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;

@FunctionRegister(name="AutoAccept", type=Category.Misc)
public class AutoAccept
extends Function {
    private final BooleanSetting onlyFriend = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0434\u0440\u0443\u0437\u044c\u044f", true);

    public AutoAccept() {
        this.addSettings(this.onlyFriend);
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        SChatPacket p;
        String raw;
        block8: {
            block7: {
                if (Minecraft.player == null) break block7;
                if (Minecraft.world != null) break block8;
            }
            return;
        }
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SChatPacket && ((raw = (p = (SChatPacket)iPacket).getChatComponent().getString().toLowerCase(Locale.ROOT)).contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") || raw.contains("has requested teleport") || raw.contains("\u043f\u0440\u043e\u0441\u0438\u0442 \u043a \u0432\u0430\u043c \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") || raw.contains("\u043f\u0440\u043e\u0441\u0438\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043a \u0412\u0430\u043c"))) {
            if (((Boolean)this.onlyFriend.get()).booleanValue()) {
                boolean yes = false;
                for (String friend : FriendStorage.getFriends()) {
                    if (!raw.contains(friend.toLowerCase(Locale.ROOT))) continue;
                    yes = true;
                    break;
                }
                if (!yes) {
                    return;
                }
            }
            Minecraft.player.sendChatMessage("/tpyes");
        }
    }
}

