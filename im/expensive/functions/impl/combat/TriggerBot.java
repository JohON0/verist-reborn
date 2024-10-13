/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.AttackUtil;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

@FunctionRegister(name="TriggerBot", type=Category.Combat)
public class TriggerBot
extends Function {
    private final BooleanSetting players = new BooleanSetting("\u0418\u0433\u0440\u043e\u043a\u0438", true);
    private final BooleanSetting mobs = new BooleanSetting("\u041c\u043e\u0431\u044b", true);
    private final BooleanSetting animals = new BooleanSetting("\u0416\u0438\u0432\u043e\u0442\u043d\u044b\u0435", true);
    private final BooleanSetting onlyCrit = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u0440\u0438\u0442\u044b", true);
    private final BooleanSetting shieldBreak = new BooleanSetting("\u041b\u043e\u043c\u0430\u0442\u044c \u0449\u0438\u0442", false);
    private final StopWatch stopWatch = new StopWatch();

    public TriggerBot() {
        this.addSettings(this.players, this.mobs, this.animals, this.onlyCrit, this.shieldBreak);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        Entity entity2;
        block5: {
            block4: {
                entity2 = this.getValidEntity();
                if (entity2 == null) break block4;
                if (Minecraft.player != null) break block5;
            }
            return;
        }
        if (this.shouldAttack()) {
            this.stopWatch.setLastMS(500L);
            this.attackEntity(entity2);
        }
    }

    private boolean shouldAttack() {
        return AttackUtil.isPlayerFalling((Boolean)this.onlyCrit.get(), true, false) && this.stopWatch.hasTimeElapsed();
    }

    private void attackEntity(Entity entity2) {
        boolean shouldStopSprinting = false;
        if (((Boolean)this.onlyCrit.get()).booleanValue() && CEntityActionPacket.lastUpdatedSprint) {
            Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.STOP_SPRINTING));
            shouldStopSprinting = true;
        }
        TriggerBot.mc.playerController.attackEntity(Minecraft.player, entity2);
        Minecraft.player.swingArm(Hand.MAIN_HAND);
        if (((Boolean)this.shieldBreak.get()).booleanValue() && entity2 instanceof PlayerEntity) {
            TriggerBot.breakShieldPlayer(entity2);
        }
        if (shouldStopSprinting) {
            Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_SPRINTING));
        }
    }

    private Entity getValidEntity() {
        Entity entity2;
        if (TriggerBot.mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY && this.checkEntity((LivingEntity)(entity2 = ((EntityRayTraceResult)TriggerBot.mc.objectMouseOver).getEntity()))) {
            return entity2;
        }
        return null;
    }

    public static void breakShieldPlayer(Entity entity2) {
        if (((LivingEntity)entity2).isBlocking()) {
            int invSlot = InventoryUtil.getInstance().getAxeInInventory(false);
            int hotBarSlot = InventoryUtil.getInstance().getAxeInInventory(true);
            if (hotBarSlot == -1 && invSlot != -1) {
                int bestSlot = InventoryUtil.getInstance().findBestSlotInHotBar();
                TriggerBot.mc.playerController.windowClick(0, invSlot, 0, ClickType.PICKUP, Minecraft.player);
                TriggerBot.mc.playerController.windowClick(0, bestSlot + 36, 0, ClickType.PICKUP, Minecraft.player);
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(bestSlot));
                TriggerBot.mc.playerController.attackEntity(Minecraft.player, entity2);
                Minecraft.player.swingArm(Hand.MAIN_HAND);
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
                TriggerBot.mc.playerController.windowClick(0, bestSlot + 36, 0, ClickType.PICKUP, Minecraft.player);
                TriggerBot.mc.playerController.windowClick(0, invSlot, 0, ClickType.PICKUP, Minecraft.player);
            }
            if (hotBarSlot != -1) {
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(hotBarSlot));
                TriggerBot.mc.playerController.attackEntity(Minecraft.player, entity2);
                Minecraft.player.swingArm(Hand.MAIN_HAND);
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(Minecraft.player.inventory.currentItem));
            }
        }
    }

    private boolean checkEntity(LivingEntity entity2) {
        AttackUtil entitySelector = new AttackUtil();
        if (((Boolean)this.players.get()).booleanValue()) {
            entitySelector.apply(AttackUtil.EntityType.PLAYERS);
        }
        if (((Boolean)this.mobs.get()).booleanValue()) {
            entitySelector.apply(AttackUtil.EntityType.MOBS);
        }
        if (((Boolean)this.animals.get()).booleanValue()) {
            entitySelector.apply(AttackUtil.EntityType.ANIMALS);
        }
        return entitySelector.ofType(entity2, entitySelector.build()) != null && entity2.isAlive();
    }

    @Override
    public void onDisable() {
        this.stopWatch.reset();
        super.onDisable();
    }
}

