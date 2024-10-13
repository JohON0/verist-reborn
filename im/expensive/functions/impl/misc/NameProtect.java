/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.Expensive;
import im.expensive.command.friends.EventFriendRemove;
import im.expensive.command.friends.EventManager;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.StringSetting;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;

@FunctionRegister(name="NameProtect", type=Category.Misc)
public class NameProtect
extends Function {
    public static String fakeName = "";
    private String friendReplacementName = "";
    private Map<String, String> originalNames = new HashMap<String, String>();
    public StringSetting name = new StringSetting("\u0417\u0430\u043c\u0435\u043d\u044f\u0435\u043c\u043e\u0435 \u0418\u043c\u044f", "VeristClient.fun", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u0437\u0430\u043c\u0435\u043d\u044b \u0432\u0430\u0448\u0435\u0433\u043e \u0438\u0433\u0440\u043e\u0432\u043e\u0433\u043e \u043d\u0438\u043a\u0430");
    public StringSetting friendName = new StringSetting("\u0418\u043c\u044f \u0414\u0440\u0443\u0437\u0435\u0439", "VeristClient.fun", "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u0437\u0430\u043c\u0435\u043d\u044b \u0438\u043c\u0435\u043d \u0434\u0440\u0443\u0437\u0435\u0439");

    public NameProtect() {
        this.addSettings(this.name);
        this.addSettings(this.friendName);
        EventManager.register(this);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        fakeName = (String)this.name.get();
        this.friendReplacementName = (String)this.friendName.get();
    }

    @Subscribe
    private void onFriendRemove(EventFriendRemove e) {
        String friend = e.getFriendName();
        if (this.originalNames.containsKey(friend)) {
            this.originalNames.remove(friend);
        }
    }

    public static String getReplaced(String input) {
        if (Expensive.getInstance() != null && Expensive.getInstance().getFunctionRegistry().getNameProtect().isState()) {
            NameProtect np = Expensive.getInstance().getFunctionRegistry().getNameProtect();
            input = input.replace(Minecraft.getInstance().session.getUsername(), fakeName);
            for (String friend : FriendStorage.getFriends()) {
                if (!np.originalNames.containsKey(friend)) {
                    np.originalNames.put(friend, friend);
                }
                input = input.replace(friend, np.friendReplacementName);
            }
            for (String originalName : np.originalNames.keySet()) {
                if (FriendStorage.getFriends().contains(originalName)) continue;
                input = input.replace(np.friendReplacementName, originalName);
            }
        }
        return input;
    }
}

