/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventEntityLeave;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;

@FunctionRegister(name="LeaveTracker", type=Category.Misc)
public class LeaveTracker
extends Function {
    @Subscribe
    private void onEntityLeave(EventEntityLeave eel) {
        Entity entity2 = eel.getEntity();
        if (!this.isEntityValid(entity2)) {
            return;
        }
        String message = "\u0418\u0433\u0440\u043e\u043a " + entity2.getDisplayName().getString() + " \u043b\u0438\u0432\u043d\u0443\u043b \u043d\u0430 " + entity2.getStringPosition();
        this.print(message);
    }

    private boolean isEntityValid(Entity entity2) {
        if (!(entity2 instanceof AbstractClientPlayerEntity) || entity2 instanceof ClientPlayerEntity) {
            return false;
        }
        return !(Minecraft.player.getDistance(entity2) < 100.0f);
    }
}

