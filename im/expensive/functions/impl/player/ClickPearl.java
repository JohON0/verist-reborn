/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.Expensive;
import im.expensive.events.EventKey;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.events.TickEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.player.ItemCooldown;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;

@FunctionRegister(name="ClickPearl", type=Category.Player)
public class ClickPearl
extends Function {
    private final ModeSetting mode = new ModeSetting("\u0422\u0438\u043f", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439");
    private final BindSetting pearlKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430", -98);
    private final InventoryUtil.Hand handUtil = new InventoryUtil.Hand();
    private final ItemCooldown itemCooldown;
    private long delay;
    private final StopWatch waitMe = new StopWatch();
    private final StopWatch stopWatch = new StopWatch();
    private final StopWatch stopWatch2 = new StopWatch();
    public ActionType actionType = ActionType.START;
    private Runnable runnableAction;
    private int oldSlot = -1;

    public ClickPearl(ItemCooldown itemCooldown) {
        this.itemCooldown = itemCooldown;
        this.addSettings(this.mode, this.pearlKey);
    }

    @Subscribe
    public void onKey(EventKey e) {
        if (e.getKey() == ((Integer)this.pearlKey.get()).intValue()) {
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.ENDER_PEARL)) {
                KeyBinding[] pressedKeys = new KeyBinding[]{ClickPearl.mc.gameSettings.keyBindForward, ClickPearl.mc.gameSettings.keyBindBack, ClickPearl.mc.gameSettings.keyBindLeft, ClickPearl.mc.gameSettings.keyBindRight, ClickPearl.mc.gameSettings.keyBindJump, ClickPearl.mc.gameSettings.keyBindSprint};
                if (ClientUtil.isConnectedToServer("funtime") && !this.waitMe.isReached(400L)) {
                    for (KeyBinding keyBinding : pressedKeys) {
                        keyBinding.setPressed(false);
                    }
                    return;
                }
                this.sendRotatePacket();
                this.oldSlot = Minecraft.player.inventory.currentItem;
                if (this.mode.is("\u041e\u0431\u044b\u0447\u043d\u044b\u0439")) {
                    InventoryUtil.inventorySwapClick(Items.ENDER_PEARL);
                } else if (this.runnableAction == null) {
                    this.actionType = ActionType.START;
                    this.runnableAction = () -> this.vebatSoli();
                    this.stopWatch.reset();
                    this.stopWatch2.reset();
                }
            } else {
                ItemCooldown.ItemEnum itemEnum = ItemCooldown.ItemEnum.getItemEnum(Items.ENDER_PEARL);
                if (this.itemCooldown.isState() && itemEnum != null && this.itemCooldown.isCurrentItem(itemEnum)) {
                    this.itemCooldown.lastUseItemTime.put(itemEnum.getItem(), System.currentTimeMillis());
                }
            }
        }
    }

    @Subscribe
    public void onTick(TickEvent e) {
        if (this.runnableAction != null) {
            this.runnableAction.run();
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        this.handUtil.onEventPacket(e);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void vebatSoli() {
        Hand hand;
        int slot = InventoryUtil.getInstance().getSlotInInventoryOrHotbar(Items.ENDER_PEARL, true);
        Hand hand2 = hand = Minecraft.player.getHeldItemOffhand().getItem() instanceof EnderPearlItem ? Hand.OFF_HAND : Hand.MAIN_HAND;
        if (slot != -1) {
            this.interact(slot, hand);
        } else {
            this.runnableAction = null;
        }
    }

    private void swingAndSendPacket(Hand hand) {
        Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        Minecraft.player.swingArm(hand);
    }

    private void interact(Integer slot, Hand hand) {
        if (this.actionType == ActionType.START) {
            this.switchSlot(slot, hand);
            this.actionType = ActionType.WAIT;
        } else if (this.actionType == ActionType.WAIT && this.stopWatch.isReached(50L)) {
            this.actionType = ActionType.USE_ITEM;
        } else if (this.actionType == ActionType.USE_ITEM) {
            this.sendRotatePacket();
            this.swingAndSendPacket(hand);
            this.switchSlot(Minecraft.player.inventory.currentItem, hand);
            this.actionType = ActionType.SWAP_BACK;
        } else if (this.actionType == ActionType.SWAP_BACK && this.stopWatch2.isReached(300L)) {
            Minecraft.player.inventory.currentItem = this.oldSlot;
            this.runnableAction = null;
        }
    }

    private void switchSlot(int slot, Hand hand) {
        if (slot != Minecraft.player.inventory.currentItem && hand != Hand.OFF_HAND) {
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(slot));
            Minecraft.player.inventory.currentItem = slot;
        }
    }

    private void sendRotatePacket() {
        if (Expensive.getInstance().getFunctionRegistry().getKillAura().getTarget() != null) {
            Minecraft.player.connection.sendPacket(new CPlayerPacket.RotationPacket(Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, Minecraft.player.isOnGround()));
        }
    }

    public static enum ActionType {
        START,
        WAIT,
        USE_ITEM,
        SWAP_BACK;

    }
}

