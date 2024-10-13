/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventKey;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

@FunctionRegister(name="ClickFriends", type=Category.Player)
public class ClickFrien
extends Function {
    final BindSetting throwKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430", -98);

    public ClickFrien() {
        this.addSettings(this.throwKey);
    }

    @Subscribe
    public void onKey(EventKey e) {
        if (e.getKey() == ((Integer)this.throwKey.get()).intValue() && ClickFrien.mc.pointedEntity instanceof PlayerEntity) {
            if (Minecraft.player == null || ClickFrien.mc.pointedEntity == null) {
                return;
            }
            String playerName = ClickFrien.mc.pointedEntity.getName().getString();
            if (FriendStorage.isFriend(playerName)) {
                FriendStorage.remove(playerName);
                this.printStatus(playerName, true);
            } else {
                FriendStorage.add(playerName);
                this.printStatus(playerName, false);
            }
        }
    }

    void printStatus(String name, boolean remove2) {
        if (remove2) {
            this.print(name + " \u0443\u0434\u0430\u043b\u0451\u043d \u0438\u0437 \u0434\u0440\u0443\u0437\u0435\u0439");
        } else {
            this.print(name + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 \u0434\u0440\u0443\u0437\u044c\u044f");
        }
    }
}

