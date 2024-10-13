//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.events.EventUpdate;
import im.expensive.events.PlaceObsidianEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(
        name = "CrystalAura",
        type = Category.Combat
)
public class AutoExplosion extends Function {
    private final BooleanSetting safeYourSelf = new BooleanSetting("Не взрывать себя", true);
    private final String targetPlayerName = "TargetPlayer";
    private Entity crystalEntity = null;
    private BlockPos obsidianPos = null;
    private int oldCurrentSlot = -1;
    private Vector2f rotationVector = new Vector2f(0.0F, 0.0F);
    StopWatch attackStopWatch = new StopWatch();
    int bestSlot = -1;
    int oldSlot = -1;

    public AutoExplosion() {
        this.addSettings(new Setting[]{this.safeYourSelf});
    }

    @Subscribe
    public void onObsidianPlace(PlaceObsidianEvent e) {
        BlockPos obsidianPos = e.getPos();
        if (obsidianPos != null) {
            Minecraft var10000;
            boolean isOffHand;
            int slotInInventory;
            int slotInHotBar;
            boolean var7;
            label42: {
                var10000 = mc;
                isOffHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
                slotInInventory = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.END_CRYSTAL, false);
                slotInHotBar = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.END_CRYSTAL, true);
                this.bestSlot = InventoryUtil.getInstance().findBestSlotInHotBar();
                if (this.bestSlot != -1) {
                    var10000 = mc;
                    if (Minecraft.player.inventory.getStackInSlot(this.bestSlot).getItem() != Items.AIR) {
                        var7 = true;
                        break label42;
                    }
                }

                var7 = false;
            }

            boolean slotNotNull = var7;
            if (!isOffHand && slotInHotBar == -1) {
                if (slotInInventory != -1 && this.bestSlot != -1) {
                    InventoryUtil.moveItem(slotInInventory, this.bestSlot + 36, slotNotNull);
                    if (slotNotNull && this.oldSlot == -1) {
                        this.oldSlot = slotInInventory;
                    }

                    Minecraft var10001 = mc;
                    this.oldCurrentSlot = Minecraft.player.inventory.currentItem;
                    this.setAndUseCrystal(this.bestSlot, obsidianPos);
                    var10000 = mc;
                    Minecraft.player.inventory.currentItem = this.oldCurrentSlot;
                    Minecraft var10005 = mc;
                    mc.playerController.windowClick(0, this.oldSlot, 0, ClickType.PICKUP, Minecraft.player);
                    int var10002 = this.bestSlot + 36;
                    var10005 = mc;
                    mc.playerController.windowClick(0, var10002, 0, ClickType.PICKUP, Minecraft.player);
                }
            } else {
                this.setAndUseCrystal(isOffHand ? -1 : slotInHotBar, obsidianPos);
            }

            this.obsidianPos = obsidianPos;
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (this.obsidianPos != null) {
            this.findEnderCrystals(this.obsidianPos).forEach(this::attackCrystal);
        }

        if (this.crystalEntity != null && !this.crystalEntity.isAlive()) {
            this.reset();
        }

    }

    @Subscribe
    private void onMotion(EventMotion e) {
        if (this.isValid(this.crystalEntity)) {
            this.rotationVector = MathUtil.rotationToEntity(this.crystalEntity);
            e.setYaw(this.rotationVector.x);
            e.setPitch(this.rotationVector.y);
            Minecraft var10000 = mc;
            Minecraft.player.renderYawOffset = this.rotationVector.x;
            var10000 = mc;
            Minecraft.player.rotationYawHead = this.rotationVector.x;
            var10000 = mc;
            Minecraft.player.rotationPitchHead = this.rotationVector.y;
        }

    }

    public void onDisable() {
        this.reset();
        super.onDisable();
    }

    private void attackCrystal(Entity entity) {
        if (this.isValid(entity) && this.isTargetNearby()) {
            Minecraft var10000 = mc;
            if (Minecraft.player.getCooledAttackStrength(1.0F) >= 1.0F && this.attackStopWatch.hasTimeElapsed()) {
                this.attackStopWatch.setLastMS(500L);
                Minecraft var10001 = mc;
                mc.playerController.attackEntity(Minecraft.player, entity);
                var10000 = mc;
                Minecraft.player.swingArm(Hand.MAIN_HAND);
                this.crystalEntity = entity;
            }
        }

        if (!entity.isAlive()) {
            this.reset();
        }

    }

    private void setAndUseCrystal(int slot, BlockPos pos) {
        Minecraft var10000 = mc;
        boolean isOffHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        if (!isOffHand && slot != -1) {
            var10000 = mc;
            Minecraft.player.inventory.currentItem = slot;
        }

        Hand hand = isOffHand ? Hand.OFF_HAND : Hand.MAIN_HAND;
        Vector3d center = new Vector3d((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F));
        Minecraft var10001 = mc;
        Minecraft var10002 = mc;
        if (mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, hand, new BlockRayTraceResult(center, Direction.UP, pos, false)) == ActionResultType.SUCCESS) {
            var10000 = mc;
            Minecraft.player.swingArm(hand);
            List<Entity> enderCrystals = this.findEnderCrystals(pos);
            if (!enderCrystals.isEmpty()) {
                this.attackCrystalImmediately((Entity)enderCrystals.get(0));
            }
        }

    }

    private void attackCrystalImmediately(Entity entity) {
        if (this.isValid(entity)) {
            Minecraft var10001 = mc;
            mc.playerController.attackEntity(Minecraft.player, entity);
            Minecraft var10000 = mc;
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.crystalEntity = entity;
        }

    }

    private boolean isValid(Entity entity) {
        if (entity != null && entity.isAlive()) {
            if (this.obsidianPos == null) {
                return false;
            } else {
                if ((Boolean)this.safeYourSelf.get()) {
                    Minecraft var10000 = mc;
                    if (Minecraft.player.getPosY() > (double)this.obsidianPos.getY()) {
                        return false;
                    }
                }

                return this.isCorrectDistance();
            }
        } else {
            return false;
        }
    }

    private boolean isCorrectDistance() {
        Minecraft var10000 = mc;
        return Minecraft.player.getPositionVec().distanceTo(new Vector3d((double)this.obsidianPos.getX(), (double)this.obsidianPos.getY(), (double)this.obsidianPos.getZ())) <= (double)mc.playerController.getBlockReachDistance();
    }

    public List<Entity> findEnderCrystals(BlockPos position) {
        Minecraft var10000 = mc;
        return (List)Minecraft.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB((double)position.getX(), (double)position.getY(), (double)position.getZ(), (double)position.getX() + 1.0, (double)position.getY() + 2.0, (double)position.getZ() + 1.0)).stream().filter((entity) -> {
            return entity instanceof EnderCrystalEntity;
        }).collect(Collectors.toList());
    }

    private boolean isTargetNearby() {
        Minecraft var10000 = mc;
        List<PlayerEntity> players = Minecraft.world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double)(this.obsidianPos.getX() - 5), (double)(this.obsidianPos.getY() - 5), (double)(this.obsidianPos.getZ() - 5), (double)(this.obsidianPos.getX() + 5), (double)(this.obsidianPos.getY() + 5), (double)(this.obsidianPos.getZ() + 5)));
        Iterator var2 = players.iterator();

        PlayerEntity player;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            player = (PlayerEntity)var2.next();
        } while(!player.getName().getString().equals("TargetPlayer"));

        return true;
    }

    private void reset() {
        this.crystalEntity = null;
        this.obsidianPos = null;
        Minecraft var10003 = mc;
        Minecraft var10004 = mc;
        this.rotationVector = new Vector2f(Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
        this.oldCurrentSlot = -1;
        this.bestSlot = -1;
    }
}
