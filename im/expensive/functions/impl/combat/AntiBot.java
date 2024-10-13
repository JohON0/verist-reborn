/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import io.netty.util.internal.ConcurrentSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlayerListItemPacket;

@FunctionRegister(name="AntiBot", type=Category.Combat)
public class AntiBot
extends Function {
    private final Set<UUID> susPlayers = new ConcurrentSet<UUID>();
    private static final Map<UUID, Boolean> botsMap = new HashMap<UUID, Boolean>();

    @Subscribe
    private void onUpdate(EventUpdate e) {
        for (UUID susPlayer : this.susPlayers) {
            PlayerEntity entity2 = Minecraft.world.getPlayerByUuid(susPlayer);
            if (entity2 != null) {
                Iterator<ItemStack> armor = entity2.getArmorInventoryList().iterator();
                int count = 0;
                while (armor.hasNext()) {
                    ItemStack current = armor.next();
                    if (current.isEmpty()) continue;
                    ++count;
                }
                boolean isFullArmor = count == 4;
                count = 0;
                for (NetworkPlayerInfo networkPlayerInfo : Minecraft.player.connection.getPlayerInfoMap()) {
                    GameProfile profile = networkPlayerInfo.getGameProfile();
                    if (!entity2.getGameProfile().getName().equals(profile.getName())) continue;
                    ++count;
                }
                boolean isBot = isFullArmor || !entity2.getUniqueID().equals(PlayerEntity.getOfflineUUID(entity2.getGameProfile().getName()));
                botsMap.put(susPlayer, isBot);
            }
            this.susPlayers.remove(susPlayer);
        }
        if (Minecraft.player.ticksExisted % 100 == 0) {
            botsMap.keySet().removeIf(uuid -> Minecraft.world.getPlayerByUuid((UUID)uuid) == null);
        }
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        SPlayerListItemPacket p;
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SPlayerListItemPacket && (p = (SPlayerListItemPacket)iPacket).getAction() == SPlayerListItemPacket.Action.ADD_PLAYER) {
            for (SPlayerListItemPacket.AddPlayerData entry : p.getEntries()) {
                boolean isInvalid;
                GameProfile profile = entry.getProfile();
                if (botsMap.containsKey(profile.getId()) || this.susPlayers.contains(profile.getId()) || !(isInvalid = profile.getProperties().isEmpty() && entry.getPing() != 0)) continue;
                this.susPlayers.add(profile.getId());
            }
        }
    }

    public static boolean isBot(Entity entity2) {
        return entity2 instanceof PlayerEntity && botsMap.getOrDefault(entity2.getUniqueID(), false) != false;
    }

    public static boolean isBotU(Entity entity2) {
        if (!entity2.getUniqueID().equals(PlayerEntity.getOfflineUUID(entity2.getName().getString()))) {
            return entity2.isInvisible();
        }
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        botsMap.clear();
    }
}

