/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.events.EventSpawnEntity;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@FunctionRegister(name="AutoTotem", type=Category.Combat)
public class AutoTotem
extends Function {
    private final ModeSetting totemMode = new ModeSetting("\u041c\u043e\u0434", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "Funtime");
    private final SliderSetting health = new SliderSetting("\u0417\u0434\u043e\u0440\u043e\u0432\u044c\u0435", 3.5f, 1.0f, 20.0f, 0.05f);
    private final BooleanSetting swapBack = new BooleanSetting("\u0412\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442", true);
    private final BooleanSetting noBallSwitch = new BooleanSetting("\u041d\u0435 \u0431\u0440\u0430\u0442\u044c \u0435\u0441\u043b\u0438 \u0448\u0430\u0440 \u0432 \u0440\u0443\u043a\u0435", false);
    private final BooleanSetting saveEnchanted = new BooleanSetting("\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0442\u044c \u0437\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0435", true);
    int nonEnchantedTotems;
    private final ModeListSetting mode = new ModeListSetting("\u0423\u0447\u0438\u0442\u044b\u0432\u0430\u0442\u044c", new BooleanSetting("\u0417\u043e\u043b\u043e\u0442\u044b\u0435 \u0441\u0435\u0440\u0434\u0446\u0430", true), new BooleanSetting("\u041a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b", true), new BooleanSetting("\u042f\u043a\u043e\u0440\u044c", false), new BooleanSetting("\u041f\u0430\u0434\u0435\u043d\u0438\u0435", false));
    int oldItem = -1;
    public boolean isActive;
    private Timer timer;
    private boolean crystalExploded;
    private boolean keepItemInHand = false;
    StopWatch stopWatch = new StopWatch();
    private Item backItem = Items.AIR;
    private ItemStack backItemStack;
    private int itemInMouse = -1;
    private int totemCount = 0;
    private boolean totemIsUsed;

    public AutoTotem() {
        this.addSettings(this.totemMode, this.health, this.swapBack, this.saveEnchanted, this.noBallSwitch, this.mode);
    }

    @Subscribe
    private void onSpawnEntity(EventSpawnEntity spawnEntity) {
        Entity entity2 = spawnEntity.getEntity();
        if (entity2 instanceof EnderCrystalEntity) {
            EnderCrystalEntity entity3 = (EnderCrystalEntity)entity2;
            if (((Boolean)this.mode.getValueByName("\u041a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b").get()).booleanValue()) {
                if (entity3.getDistance(Minecraft.player) <= 6.0f) {
                    this.swapToTotem();
                }
            }
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.totemCount = this.countTotems(true);
        switch ((String)this.totemMode.get()) {
            case "\u041e\u0431\u044b\u0447\u043d\u044b\u0439": {
                boolean handNotNull;
                this.nonEnchantedTotems = (int)IntStream.range(0, 36).mapToObj(i -> Minecraft.player.inventory.getStackInSlot(i)).filter(s -> s.getItem() == Items.TOTEM_OF_UNDYING && !s.isEnchanted()).count();
                int slot = this.getSlotInInventory(Items.TOTEM_OF_UNDYING);
                boolean bl = handNotNull = !(Minecraft.player.getHeldItemOffhand().getItem() instanceof AirItem);
                if (this.shouldToSwapTotem()) {
                    if (slot == -1 || this.isTotemInHands()) break;
                    InventoryUtil.moveItem(slot, 45, handNotNull);
                    if (!handNotNull || this.oldItem != -1) break;
                    this.oldItem = slot;
                    break;
                }
                if (this.oldItem == -1 || !((Boolean)this.swapBack.get()).booleanValue()) break;
                InventoryUtil.moveItem(this.oldItem, 45, handNotNull);
                this.oldItem = -1;
                break;
            }
            case "Funtime": {
                if (this.shouldToSwapTotem()) {
                    if (this.itemIsHand(Items.TOTEM_OF_UNDYING)) {
                        return;
                    }
                    this.swapToTotem();
                }
                this.swapBack();
            }
        }
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        SEntityStatusPacket statusPacket;
        IPacket<?> iPacket;
        if (e.isReceive() && (iPacket = e.getPacket()) instanceof SEntityStatusPacket && (statusPacket = (SEntityStatusPacket)iPacket).getOpCode() == 35) {
            if (statusPacket.getEntity(Minecraft.world) == Minecraft.player) {
                this.totemIsUsed = true;
            }
        }
    }

    private void swapBack() {
        int backItemSlot;
        if (this.backItemStack != null && (backItemSlot = this.getSlotInInventory(this.backItem)) != -1) {
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, backItemSlot, 1, ClickType.PICKUP, Minecraft.player);
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, 45, 1, ClickType.PICKUP, Minecraft.player);
        }
        this.backItemStack = null;
        this.itemInMouse = -1;
        this.backItem = null;
    }

    private boolean itemIsBack() {
        if (Minecraft.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING && this.itemInMouse != -1 && this.backItem != Items.AIR) {
            ItemStack itemStack = Minecraft.player.container.getSlot(this.itemInMouse).getStack();
            boolean offHandAreEqual = itemStack != ItemStack.EMPTY && !ItemStack.areItemStacksEqual(itemStack, this.backItemStack);
            int oldItemSlot = this.findItemSlotIndex(this.backItemStack, this.backItem);
            if (oldItemSlot < 9 && oldItemSlot != -1) {
                oldItemSlot += 36;
            }
            int containerId = Minecraft.player.container.windowId;
            if (Minecraft.player.inventory.getItemStack().getItem() != Items.AIR) {
                AutoTotem.mc.playerController.windowClick(containerId, 45, 0, ClickType.PICKUP, Minecraft.player);
                this.backItemInMouse();
                return false;
            }
            if (oldItemSlot == -1) {
                return false;
            }
            AutoTotem.mc.playerController.windowClick(containerId, oldItemSlot, 0, ClickType.PICKUP, Minecraft.player);
            AutoTotem.mc.playerController.windowClick(containerId, 45, 0, ClickType.PICKUP, Minecraft.player);
            if (this.itemInMouse != -1) {
                if (!offHandAreEqual) {
                    AutoTotem.mc.playerController.windowClick(containerId, this.itemInMouse, 0, ClickType.PICKUP, Minecraft.player);
                } else {
                    int emptySlot = AutoTotem.getEmptySlot(false);
                    if (emptySlot != -1) {
                        AutoTotem.mc.playerController.windowClick(containerId, emptySlot, 0, ClickType.PICKUP, Minecraft.player);
                    }
                }
            }
        }
        return true;
    }

    public static int getEmptySlot(boolean hotBar) {
        for (int i = hotBar ? 0 : 9; i < (hotBar ? 9 : 45); ++i) {
            if (!Minecraft.player.inventory.getStackInSlot(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    public int findItemSlotIndex(ItemStack targetItemStack, Item targetItem) {
        if (targetItemStack == null) {
            return -1;
        }
        for (int i = 0; i < 45; ++i) {
            ItemStack currentStack = Minecraft.player.inventory.getStackInSlot(i);
            if (!ItemStack.areItemStacksEqual(currentStack, targetItemStack) || currentStack.getItem() != targetItem) continue;
            return i;
        }
        return -1;
    }

    public boolean itemIsHand(Item item) {
        for (Hand enumHand : Hand.values()) {
            if (Minecraft.player.getHeldItem(enumHand).getItem() != item) continue;
            return true;
        }
        return false;
    }

    private void swapToTotem() {
        if (this.keepItemInHand) {
            if (this.crystalExploded && !this.isOffhandItemBall() && !this.shouldToSwapTotem()) {
                this.keepItemInHand = false;
                this.swapBack1();
            }
            return;
        }
        int totemSlot = this.getSlotInInventory1(Items.TOTEM_OF_UNDYING);
        this.stopWatch.reset();
        Item offhandItem = Minecraft.player.getHeldItemOffhand().getItem();
        if (offhandItem == Items.TOTEM_OF_UNDYING) {
            return;
        }
        if (totemSlot == -1 && !this.isCurrentItem1(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        if (this.itemInMouse == -1) {
            this.itemInMouse = totemSlot;
            this.backItem = offhandItem;
            this.backItemStack = Minecraft.player.getHeldItemOffhand().copy();
        }
        AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, totemSlot, 1, ClickType.PICKUP, Minecraft.player);
        AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, 45, 1, ClickType.PICKUP, Minecraft.player);
        if (this.totemCount > 1 && this.totemIsUsed) {
            this.backItemInMouse1();
            this.totemIsUsed = false;
        }
        this.backItemInMouse1();
        this.keepItemInHand = true;
        this.startCrystalCheckTimer();
    }

    private void swapBack1() {
        int backItemSlot;
        if (this.backItemStack != null && (backItemSlot = this.getSlotInInventory1(this.backItem)) != -1) {
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, backItemSlot, 1, ClickType.PICKUP, Minecraft.player);
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, 45, 1, ClickType.PICKUP, Minecraft.player);
        }
        this.backItemStack = null;
        this.itemInMouse = -1;
        this.backItem = null;
    }

    private void startCrystalCheckTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        this.crystalExploded = false;
        TimerTask task = new TimerTask(){

            @Override
            public void run() {
                if (AutoTotem.this.isEnderCrystalExploded()) {
                    AutoTotem.this.crystalExploded = true;
                    AutoTotem.this.timer.cancel();
                    AutoTotem.this.keepItemInHand = false;
                    AutoTotem.this.swapBack();
                }
            }
        };
        this.timer.schedule(task, 0L, 500L);
    }

    private boolean isEnderCrystalExploded() {
        double radius = 10.0;
        List<EnderCrystalEntity> enderCrystals = Minecraft.world.getEntitiesWithinAABB(EnderCrystalEntity.class, Minecraft.player.getBoundingBox().grow(radius));
        return enderCrystals.isEmpty();
    }

    private int getSlotInInventory1(Item item) {
        int i = 0;
        while (true) {
            if (i >= Minecraft.player.inventory.mainInventory.size()) break;
            if (Minecraft.player.inventory.mainInventory.get(i).getItem() == item) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private boolean isCurrentItem1(Item item) {
        return Minecraft.player.getHeldItemMainhand().getItem() == item;
    }

    private void backItemInMouse1() {
        if (this.itemInMouse != -1) {
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, 45, 1, ClickType.PICKUP, Minecraft.player);
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, this.itemInMouse, 1, ClickType.PICKUP, Minecraft.player);
            this.itemInMouse = -1;
        }
    }

    public int countTotems(boolean includeEnchanted) {
        long totemCount = 0L;
        int inventorySize = Minecraft.player.inventory.getSizeInventory();
        for (int slotIndex = 0; slotIndex < inventorySize; ++slotIndex) {
            ItemStack slotStack = Minecraft.player.inventory.getStackInSlot(slotIndex);
            if (slotStack.getItem() != Items.TOTEM_OF_UNDYING || !includeEnchanted && slotStack.isEnchanted()) continue;
            ++totemCount;
        }
        return (int)totemCount;
    }

    private void backItemInMouse() {
        if (this.itemInMouse != -1) {
            AutoTotem.mc.playerController.windowClick(Minecraft.player.container.windowId, this.itemInMouse, 0, ClickType.PICKUP, Minecraft.player);
        }
    }

    public static boolean isCurrentItem(Item item) {
        return Minecraft.player.inventory.getItemStack().getItem() == item;
    }

    private boolean isTotemInHands() {
        Hand[] hands;
        for (Hand hand : hands = Hand.values()) {
            ItemStack heldItem = Minecraft.player.getHeldItem(hand);
            if (heldItem.getItem() != Items.TOTEM_OF_UNDYING || this.isSaveEnchanted(heldItem)) continue;
            return true;
        }
        return false;
    }

    private boolean isSaveEnchanted(ItemStack itemStack) {
        return (Boolean)this.saveEnchanted.get() != false && itemStack.isEnchanted() && this.nonEnchantedTotems > 0;
    }

    private boolean shouldToSwapTotem() {
        float absorptionAmount = Minecraft.player.isPotionActive(Effects.ABSORPTION) ? Minecraft.player.getAbsorptionAmount() : 0.0f;
        float currentHealth = Minecraft.player.getHealth();
        if (((Boolean)this.mode.getValueByName("\u0417\u043e\u043b\u043e\u0442\u044b\u0435 \u0441\u0435\u0440\u0434\u0446\u0430").get()).booleanValue()) {
            currentHealth += absorptionAmount;
        }
        if (!this.isOffhandItemBall() && this.isInDangerousSituation()) {
            return true;
        }
        return currentHealth <= ((Float)this.health.get()).floatValue();
    }

    private boolean isInDangerousSituation() {
        return this.checkCrystal() || this.checkAnchor() || this.checkFall();
    }

    private boolean checkFall() {
        if (!((Boolean)this.mode.getValueByName("\u041f\u0430\u0434\u0435\u043d\u0438\u0435").get()).booleanValue()) {
            return false;
        }
        if (Minecraft.player.isInWater()) {
            return false;
        }
        if (Minecraft.player.isElytraFlying()) {
            return false;
        }
        return Minecraft.player.fallDistance > 10.0f;
    }

    private boolean checkAnchor() {
        if (!((Boolean)this.mode.getValueByName("\u042f\u043a\u043e\u0440\u044c").get()).booleanValue()) {
            return false;
        }
        return this.getBlock(6.0f, Blocks.RESPAWN_ANCHOR) != null;
    }

    private boolean checkCrystal() {
        if (!((Boolean)this.mode.getValueByName("\u041a\u0440\u0438\u0441\u0442\u0430\u043b\u043b\u044b").get()).booleanValue()) {
            return false;
        }
        for (Entity entity2 : Minecraft.world.getAllEntities()) {
            if (!this.isDangerousEntityNearPlayer(entity2)) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isOffhandItemBall() {
        boolean isFallingConditionMet;
        if (((Boolean)this.mode.getValueByName("\u041f\u0430\u0434\u0435\u043d\u0438\u0435").get()).booleanValue()) {
            if (Minecraft.player.fallDistance > 5.0f) {
                return false;
            }
        }
        boolean bl = isFallingConditionMet = false;
        if (isFallingConditionMet) {
            return false;
        }
        if ((Boolean)this.noBallSwitch.get() == false) return false;
        if (Minecraft.player.getHeldItemOffhand().getItem() != Items.PLAYER_HEAD) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isDangerousEntityNearPlayer(Entity entity2) {
        if (!(entity2 instanceof TNTEntity)) {
            if (!(entity2 instanceof TNTMinecartEntity)) return false;
        }
        if (!(Minecraft.player.getDistance(entity2) <= 6.0f)) return false;
        return true;
    }

    private final BlockPos getBlock(float distance, Block block2) {
        return this.getSphere(this.getPlayerPosLocal(), distance, 6, false, true, 0).stream().filter(position -> Minecraft.world.getBlockState((BlockPos)position).getBlock() == block2).min(Comparator.comparing(blockPos -> this.getDistanceOfEntityToBlock(Minecraft.player, (BlockPos)blockPos))).orElse(null);
    }

    private final List<BlockPos> getSphere(BlockPos center, float radius, int height, boolean hollow, boolean fromBottom, int yOffset) {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        int centerX = center.getX();
        int centerY = center.getY();
        int centerZ = center.getZ();
        int x = centerX - (int)radius;
        while ((float)x <= (float)centerX + radius) {
            int z = centerZ - (int)radius;
            while ((float)z <= (float)centerZ + radius) {
                int yStart = fromBottom ? centerY - (int)radius : centerY;
                int yEnd = fromBottom ? centerY + (int)radius : centerY + height;
                for (int y = yStart; y < yEnd; ++y) {
                    if (!AutoTotem.isPositionWithinSphere(centerX, centerY, centerZ, x, y, z, radius, hollow)) continue;
                    positions.add(new BlockPos(x, y + yOffset, z));
                }
                ++z;
            }
            ++x;
        }
        return positions;
    }

    private final BlockPos getPlayerPosLocal() {
        if (Minecraft.player == null) {
            return BlockPos.ZERO;
        }
        return new BlockPos(Math.floor(Minecraft.player.getPosX()), Math.floor(Minecraft.player.getPosY()), Math.floor(Minecraft.player.getPosZ()));
    }

    private final double getDistanceOfEntityToBlock(Entity entity2, BlockPos blockPos) {
        return this.getDistance(entity2.getPosX(), entity2.getPosY(), entity2.getPosZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    private final double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
        double n7 = n - n4;
        double n8 = n2 - n5;
        double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    private static boolean isPositionWithinSphere(int centerX, int centerY, int centerZ, int x, int y, int z, float radius, boolean hollow) {
        double distanceSq = Math.pow(centerX - x, 2.0) + Math.pow(centerZ - z, 2.0) + Math.pow(centerY - y, 2.0);
        return distanceSq < Math.pow(radius, 2.0) && (!hollow || distanceSq >= Math.pow(radius - 1.0f, 2.0));
    }

    public int getSlotInInventory(Item item) {
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING || this.isSaveEnchanted(itemStack)) continue;
            slot = this.adjustSlotNumber(i);
            break;
        }
        return slot;
    }

    private int adjustSlotNumber(int slot) {
        return slot < 9 ? slot + 36 : slot;
    }

    private void reset() {
        this.oldItem = -1;
        this.itemInMouse = -1;
        this.backItem = Items.AIR;
        this.backItemStack = null;
    }

    @Override
    public void onDisable() {
        this.reset();
        super.onDisable();
    }
}

