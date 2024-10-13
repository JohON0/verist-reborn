/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CUseEntityPacket;

@FunctionRegister(name="NoFriendDamage", type=Category.Combat)
public class NoFriendDamage
extends Function {
    @Subscribe
    public void onEvent(EventPacket eventPacket) {
        IPacket<?> packet = eventPacket.getPacket();
        if (packet instanceof CUseEntityPacket) {
            String entityName;
            CUseEntityPacket useEntityPacket = (CUseEntityPacket)packet;
            Entity entity2 = useEntityPacket.getEntityFromWorld(Minecraft.world);
            if (entity2 instanceof RemoteClientPlayerEntity && FriendStorage.isFriend(entityName = entity2.getName().getString()) && useEntityPacket.getAction() == CUseEntityPacket.Action.ATTACK) {
                eventPacket.cancel();
            }
        }
    }
}

