/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import im.expensive.Expensive;
import im.expensive.command.friends.FriendStorage;
import im.expensive.utils.client.IMinecraft;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;

public class AttackUtil
implements IMinecraft {
    public final List<EntityType> entityTypes = new ArrayList<EntityType>();

    /*
     * Unable to fully structure code
     */
    public static boolean isPlayerFalling(boolean onlyCrit, boolean onlySpace, boolean sync) {
        block9: {
            block8: {
                if (!Minecraft.player.isInWater()) break block8;
                if (Minecraft.player.areEyesInFluid(FluidTags.WATER)) ** GOTO lbl-1000
            }
            if (Minecraft.player.isInLava()) ** GOTO lbl-1000
            if (Minecraft.player.isOnLadder()) ** GOTO lbl-1000
            if (Minecraft.player.isPassenger()) ** GOTO lbl-1000
            if (Minecraft.player.abilities.isFlying) lbl-1000:
            // 5 sources

            {
                v0 = true;
            } else {
                v0 = cancelReason = false;
            }
            if (AttackUtil.mc.gameSettings.keyBindJump.isKeyDown()) ** GOTO lbl-1000
            if (Minecraft.player.isOnGround() && onlySpace) {
                v1 = true;
            } else lbl-1000:
            // 2 sources

            {
                v1 = false;
            }
            onSpace = v1;
            attackStrength = Minecraft.player.getCooledAttackStrength(sync != false ? Expensive.getInstance().getTpsCalc().getAdjustTicks() : 1.0f);
            if ((double)attackStrength < 0.92) {
                return false;
            }
            if (cancelReason || !onlyCrit) break block9;
            if (onSpace) ** GOTO lbl-1000
            if (!Minecraft.player.isOnGround()) {
                ** if (!(Minecraft.player.fallDistance > 0.0f)) goto lbl-1000
            }
            ** GOTO lbl-1000
lbl-1000:
            // 2 sources

            {
                v2 = true;
                ** GOTO lbl38
            }
lbl-1000:
            // 2 sources

            {
                v2 = false;
            }
lbl38:
            // 2 sources

            return v2;
        }
        return true;
    }

    public EntityType ofType(Entity entity2, EntityType ... types) {
        List<EntityType> typeList = Arrays.asList(types);
        if (entity2 instanceof PlayerEntity) {
            if (AttackUtil.entityIsMe(entity2, typeList)) {
                return EntityType.SELF;
            }
            if (entity2 != Minecraft.player) {
                if (AttackUtil.entityIsPlayer(entity2, typeList)) {
                    return EntityType.PLAYERS;
                }
                if (AttackUtil.entityIsFriend(entity2, typeList)) {
                    return EntityType.FRIENDS;
                }
                if (AttackUtil.entityIsNakedPlayer(entity2, typeList)) {
                    return EntityType.NAKED;
                }
            }
        } else {
            if (AttackUtil.entityIsMob(entity2, typeList)) {
                return EntityType.MOBS;
            }
            if (this.entityIsAnimal(entity2, typeList)) {
                return EntityType.ANIMALS;
            }
        }
        return null;
    }

    private static boolean entityIsMe(Entity entity2, List<EntityType> typeList) {
        return entity2 == Minecraft.player && typeList.contains((Object)EntityType.SELF);
    }

    private static boolean entityIsPlayer(Entity entity2, List<EntityType> typeList) {
        return typeList.contains((Object)EntityType.PLAYERS) && !FriendStorage.isFriend(entity2.getName().getString());
    }

    private static boolean entityIsFriend(Entity entity2, List<EntityType> typeList) {
        return typeList.contains((Object)EntityType.FRIENDS) && FriendStorage.isFriend(entity2.getName().getString());
    }

    private static boolean entityIsMob(Entity entity2, List<EntityType> typeList) {
        return entity2 instanceof MonsterEntity && typeList.contains((Object)EntityType.MOBS);
    }

    private static boolean entityIsNakedPlayer(Entity entity2, List<EntityType> typeList) {
        return entity2 instanceof PlayerEntity && ((LivingEntity)entity2).getTotalArmorValue() == 0;
    }

    private boolean entityIsAnimal(Entity entity2, List<EntityType> typeList) {
        return (entity2 instanceof AnimalEntity || entity2 instanceof GolemEntity || entity2 instanceof VillagerEntity) && typeList.contains((Object)EntityType.ANIMALS);
    }

    public AttackUtil apply(EntityType type) {
        this.entityTypes.add(type);
        return this;
    }

    public EntityType[] build() {
        return this.entityTypes.toArray(new EntityType[0]);
    }

    public static enum EntityType {
        PLAYERS,
        MOBS,
        ANIMALS,
        NAKED,
        FRIENDS,
        NPC,
        SELF;

    }
}

