/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.InventoryUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector2f;

@FunctionRegister(name="ElytraTarget", type=Category.Combat)
public class AutoPilo2
extends Function {
    private Set<PlayerEntity> targetedPlayers = new HashSet<PlayerEntity>();
    private boolean isTargeting = false;
    private long lastFireworkTime = 0L;
    private long fireworkCooldown = 750L;
    private long lastChatMessageTime = 0L;
    private long chatMessageInterval = 5000L;
    public Vector2f rotate = new Vector2f(0.0f, 0.0f);
    private BooleanSetting save = new BooleanSetting("\u0411\u0435\u0437\u043e\u043f\u0430\u0441\u043d\u043e\u0441\u0442\u044c", true);
    private BooleanSetting autofirework = new BooleanSetting("\u0410\u0432\u0442\u043e-\u0424\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a", true);
    private BooleanSetting deadtoggle = new BooleanSetting("\u041e\u043a\u043b\u044e\u0447\u0430\u0442\u044c \u043f\u0440\u0438 \u0441\u043c\u0435\u0440\u0442\u0438 \u0442\u0430\u0440\u0433\u0435\u0442\u0430", true);
    private SliderSetting distanse = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f", 50.0f, 5.0f, 50.0f, 1.0f);
    private SliderSetting hptoggle = new SliderSetting("\u0425\u043f \u0434\u043b\u044f \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435", 6.0f, 0.0f, 20.0f, 1.0f).setVisible(() -> (Boolean)this.save.get());

    public AutoPilo2() {
        this.addSettings(this.save, this.autofirework, this.deadtoggle, this.distanse, this.hptoggle);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        Minecraft.getInstance();
        if (Minecraft.player.isElytraFlying()) {
            if (!this.isTargeting) {
                this.targetPlayer();
            } else {
                this.updateRotationToPlayer();
                this.useFirework();
                this.checkChatMessage();
            }
        } else if (this.isTargeting) {
            this.stopTargeting();
        }
    }

    private void targetPlayer() {
        Minecraft.getInstance();
        ClientWorld world = Minecraft.world;
        if (world != null) {
            Minecraft.getInstance();
            Minecraft.getInstance();
            double d = Minecraft.player.getPosX() - 10.0;
            Minecraft.getInstance();
            double d2 = Minecraft.player.getPosY() - 5.0;
            Minecraft.getInstance();
            double d3 = Minecraft.player.getPosZ() - 10.0;
            Minecraft.getInstance();
            double d4 = Minecraft.player.getPosX() + 10.0;
            Minecraft.getInstance();
            double d5 = Minecraft.player.getPosY() + 5.0;
            Minecraft.getInstance();
            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(Minecraft.player, new AxisAlignedBB(d, d2, d3, d4, d5, Minecraft.player.getPosZ() + 10.0));
            for (Entity entity2 : entities) {
                PlayerEntity target;
                if (!(entity2 instanceof PlayerEntity) || !entity2.isAlive() || this.targetedPlayers.contains(target = (PlayerEntity)entity2)) continue;
                this.targetedPlayers.clear();
                this.targetedPlayers.add(target);
                this.isTargeting = true;
                this.setRotationToPlayer(target);
                return;
            }
        }
    }

    private void setRotationToPlayer(PlayerEntity player) {
        if (player != null) {
            double d = player.getPosX();
            Minecraft.getInstance();
            double deltaX = d - Minecraft.player.getPosX();
            double d2 = player.getPosZ();
            Minecraft.getInstance();
            double deltaZ = d2 - Minecraft.player.getPosZ();
            double d3 = player.getPosY();
            Minecraft.getInstance();
            double deltaY = d3 - Minecraft.player.getPosY();
            double yaw = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0;
            double pitch = -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)));
            Minecraft.getInstance();
            Minecraft.player.rotationYaw = (float)yaw;
            Minecraft.getInstance();
            Minecraft.player.rotationPitch = (float)pitch;
        }
    }

    private void updateRotationToPlayer() {
        if (!this.targetedPlayers.isEmpty()) {
            PlayerEntity target = this.targetedPlayers.iterator().next();
            this.setRotationToPlayer(target);
        }
    }

    private void useFirework() {
        long currentTime = System.currentTimeMillis();
        if (((Boolean)this.autofirework.get()).booleanValue() && currentTime - this.lastFireworkTime >= this.fireworkCooldown) {
            int hbSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, true);
            int invSlot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.FIREWORK_ROCKET, false);
            if (invSlot == -1 && hbSlot == -1) {
                return;
            }
            Minecraft.getInstance();
            int currentSlot = Minecraft.player.inventory.currentItem;
            Minecraft.getInstance();
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            Minecraft.getInstance();
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            Minecraft.getInstance();
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(currentSlot));
            this.lastFireworkTime = currentTime;
            Minecraft.getInstance();
            double distanceToTarget = Minecraft.player.getDistance(this.targetedPlayers.iterator().next());
            this.fireworkCooldown = distanceToTarget > (double)((Float)this.distanse.get()).floatValue() ? 300L : 200L;
        }
    }

    private void stopTargeting() {
        this.targetedPlayers.clear();
        this.isTargeting = false;
    }

    private void checkChatMessage() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastChatMessageTime >= this.chatMessageInterval) {
            float mchealth;
            PlayerEntity target;
            if (!this.targetedPlayers.isEmpty() && (target = this.targetedPlayers.iterator().next()) != null && (mchealth = target.getHealth()) <= 0.01f) {
                this.targetedPlayers.clear();
                this.onDisable();
            }
            this.lastChatMessageTime = currentTime;
        }
        float mchealth = Minecraft.player.getHealth();
        if (((Boolean)this.save.get()).booleanValue() && mchealth < ((Float)this.hptoggle.get()).floatValue()) {
            double d = Minecraft.player.getPosX();
            Minecraft.getInstance();
            double deltaX = d - Minecraft.player.getPosX();
            double d2 = Minecraft.player.getPosZ();
            Minecraft.getInstance();
            double deltaZ = d2 - Minecraft.player.getPosZ();
            double d3 = Minecraft.player.getPosY();
            Minecraft.getInstance();
            double deltaY = d3 - Minecraft.player.getPosY();
            double yaw = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 185.0;
            double pitch = -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)));
            Minecraft.player.rotationYaw = (float)yaw;
            Minecraft.player.rotationPitch = (float)pitch;
            this.targetedPlayers.clear();
            this.stopTargeting();
            this.onDisable();
        }
    }

    public PlayerEntity[] getTargetedPlayers() {
        return this.targetedPlayers.toArray(new PlayerEntity[0]);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

