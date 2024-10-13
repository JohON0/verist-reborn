/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.events.InventoryCloseEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.MoveUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CClickWindowPacket;

@FunctionRegister(name="InvMove", type=Category.Player)
public class InventoryMove
extends Function {
    private final List<IPacket<?>> packet = new ArrayList();
    public StopWatch wait = new StopWatch();

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (Minecraft.player != null) {
            KeyBinding[] pressedKeys = new KeyBinding[]{InventoryMove.mc.gameSettings.keyBindForward, InventoryMove.mc.gameSettings.keyBindBack, InventoryMove.mc.gameSettings.keyBindLeft, InventoryMove.mc.gameSettings.keyBindRight, InventoryMove.mc.gameSettings.keyBindJump, InventoryMove.mc.gameSettings.keyBindSprint};
            if (ClientUtil.isConnectedToServer("funtime") && !this.wait.isReached(400L)) {
                for (KeyBinding keyBinding : pressedKeys) {
                    keyBinding.setPressed(false);
                }
                return;
            }
            if (InventoryMove.mc.currentScreen instanceof ChatScreen || InventoryMove.mc.currentScreen instanceof EditSignScreen) {
                return;
            }
            this.updateKeyBindingState(pressedKeys);
        }
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        IPacket<?> iPacket;
        if (ClientUtil.isConnectedToServer("funtime") && (iPacket = e.getPacket()) instanceof CClickWindowPacket) {
            CClickWindowPacket p = (CClickWindowPacket)iPacket;
            if (MoveUtils.isMoving() && InventoryMove.mc.currentScreen instanceof InventoryScreen) {
                this.packet.add(p);
                e.cancel();
            }
        }
    }

    @Subscribe
    public void onClose(InventoryCloseEvent e) {
        if (ClientUtil.isConnectedToServer("funtime") && !this.packet.isEmpty() && MoveUtils.isMoving()) {
            new Thread(() -> {
                this.wait.reset();
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (IPacket<?> p : this.packet) {
                    Minecraft.player.connection.sendPacketWithoutEvent(p);
                }
                this.packet.clear();
            }).start();
            e.cancel();
        }
    }

    private void updateKeyBindingState(KeyBinding[] keyBindings) {
        for (KeyBinding keyBinding : keyBindings) {
            boolean isKeyPressed = InputMappings.isKeyDown(mc.getMainWindow().getHandle(), keyBinding.getDefault().getKeyCode());
            keyBinding.setPressed(isKeyPressed);
        }
    }
}

