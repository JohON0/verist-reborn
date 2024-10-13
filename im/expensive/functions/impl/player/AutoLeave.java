/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

@FunctionRegister(name="AutoLeave", type=Category.Player)
public class AutoLeave
extends Function {
    private final ModeSetting action = new ModeSetting("\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u0435", "Kick", "Kick", "/hub", "/spawn", "/home");
    private final SliderSetting distance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f", 50.0f, 1.0f, 100.0f, 1.0f);

    public AutoLeave() {
        this.addSettings(this.action, this.distance);
    }

    @Subscribe
    private void onUpdate(EventUpdate event) {
        Minecraft.world.getPlayers().stream().filter(this::isValidPlayer).findFirst().ifPresent(this::performAction);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isValidPlayer(PlayerEntity player) {
        if (!player.isAlive()) return false;
        if (!(player.getHealth() > 0.0f)) return false;
        if (!(player.getDistance(Minecraft.player) <= ((Float)this.distance.get()).floatValue())) return false;
        if (player == Minecraft.player) return false;
        if (!PlayerUtils.isNameValid(player.getName().getString())) return false;
        return true;
    }

    private void performAction(PlayerEntity player) {
        if (!((String)this.action.get()).equalsIgnoreCase("Kick")) {
            Minecraft.player.sendChatMessage((String)this.action.get());
            AutoLeave.mc.ingameGUI.func_238452_a_(new StringTextComponent("[AutoLeave] " + player.getGameProfile().getName()), new StringTextComponent("test"), -1, -1, -1);
        } else {
            Minecraft.player.connection.getNetworkManager().closeChannel(new StringTextComponent("\u0412\u044b \u0432\u044b\u0448\u043b\u0438 \u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430! \n" + player.getGameProfile().getName()));
        }
        this.toggle();
    }
}

