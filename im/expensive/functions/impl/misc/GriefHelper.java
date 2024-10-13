/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventKey;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;

@FunctionRegister(name="GriefHelper", type=Category.Misc)
public class GriefHelper
extends Function {
    private final ModeListSetting mode = new ModeListSetting("\u0422\u0438\u043f", new BooleanSetting("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443", true), new BooleanSetting("\u0417\u0430\u043a\u0440\u044b\u0432\u0430\u0442\u044c \u043c\u0435\u043d\u044e", true));
    private final BindSetting disorientationKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0434\u0435\u0437\u043e\u0440\u0435\u043d\u0442\u0430", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting shulkerKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0448\u0430\u043b\u043a\u0435\u0440\u0430", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting trapKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0442\u0440\u0430\u043f\u043a\u0438", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting flameKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0441\u043c\u0435\u0440\u0447\u0430", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting blatantKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u044f\u0432\u043a\u0438", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting bowKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0430\u0440\u0431\u0430\u043b\u0435\u0442\u0430", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting otrigaKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u043e\u0442\u0440\u044b\u0433\u0438", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting serkaKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0441\u0435\u0440\u043a\u0438", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    private final BindSetting plastKey = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u043f\u043b\u0430\u0441\u0442\u0430", -1).setVisible(() -> (Boolean)this.mode.getValueByName("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u043e \u0431\u0438\u043d\u0434\u0443").get());
    final StopWatch stopWatch = new StopWatch();
    InventoryUtil.Hand handUtil = new InventoryUtil.Hand();
    long delay;
    boolean disorientationThrow;
    boolean trapThrow;
    boolean flameThrow;
    boolean blatantThrow;
    boolean serkaThrow;
    boolean otrigaThrow;
    boolean bowThrow;
    boolean plastThrow;
    boolean shulkerThrow;

    public GriefHelper() {
        this.addSettings(this.mode, this.disorientationKey, this.trapKey, this.flameKey, this.blatantKey, this.serkaKey, this.bowKey, this.otrigaKey, this.plastKey, this.shulkerKey);
    }

    @Subscribe
    private void onKey(EventKey e) {
        if (e.getKey() == ((Integer)this.disorientationKey.get()).intValue()) {
            this.disorientationThrow = true;
        }
        if (e.getKey() == ((Integer)this.shulkerKey.get()).intValue()) {
            this.shulkerThrow = true;
        }
        if (e.getKey() == ((Integer)this.trapKey.get()).intValue()) {
            this.trapThrow = true;
        }
        if (e.getKey() == ((Integer)this.flameKey.get()).intValue()) {
            this.flameThrow = true;
        }
        if (e.getKey() == ((Integer)this.blatantKey.get()).intValue()) {
            this.blatantThrow = true;
        }
        if (e.getKey() == ((Integer)this.otrigaKey.get()).intValue()) {
            this.otrigaThrow = true;
        }
        if (e.getKey() == ((Integer)this.serkaKey.get()).intValue()) {
            this.serkaThrow = true;
        }
        if (e.getKey() == ((Integer)this.bowKey.get()).intValue()) {
            this.bowThrow = true;
        }
        if (e.getKey() == ((Integer)this.plastKey.get()).intValue()) {
            this.plastThrow = true;
        }
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        int slot;
        int old;
        int invSlot;
        int hbSlot;
        if (this.disorientationThrow) {
            this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
            hbSlot = this.getItemForName("\u0434\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f", true);
            invSlot = this.getItemForName("\u0434\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
                this.disorientationThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.ENDER_EYE)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u0434\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044e!");
                int slot2 = this.findAndTrowItem(hbSlot, invSlot);
                if (slot2 > 8) {
                    GriefHelper.mc.playerController.pickItem(slot2);
                }
            }
            this.disorientationThrow = false;
        }
        if (this.shulkerThrow) {
            hbSlot = this.getItemForName("\u044f\u0449\u0438\u043a", true);
            invSlot = this.getItemForName("\u044f\u0449\u0438\u043a", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0428\u0430\u043b\u043a\u0435\u0440 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
                this.trapThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.SHULKER_BOX)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u0448\u0430\u043b\u043a\u0435\u0440!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.shulkerThrow = false;
        }
        if (this.trapThrow) {
            hbSlot = this.getItemForName("\u0442\u0440\u0430\u043f\u043a\u0430", true);
            invSlot = this.getItemForName("\u0442\u0440\u0430\u043f\u043a\u0430", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0422\u0440\u0430\u043f\u043a\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430");
                this.trapThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.NETHERITE_SCRAP)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u0442\u0440\u0430\u043f\u043a\u0443!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.trapThrow = false;
        }
        if (this.flameThrow) {
            hbSlot = this.getItemForName("\u043e\u0433\u043d\u0435\u043d\u043d\u044b\u0439", true);
            invSlot = this.getItemForName("\u043e\u0433\u043d\u0435\u043d\u043d\u044b\u0439", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u041e\u0433\u043d\u0435\u043d\u043d\u044b\u0439 \u0441\u043c\u0435\u0440\u0447 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
                this.flameThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.FIRE_CHARGE)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u043e\u0433\u043d\u0435\u043d\u043d\u044b\u0439 \u0441\u043c\u0435\u0440\u0447!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.flameThrow = false;
        }
        if (this.bowThrow) {
            hbSlot = this.getItemForName("\u0430\u0440\u0431\u0430\u043b\u0435\u0442", true);
            invSlot = this.getItemForName("\u0430\u0440\u0431\u0430\u043b\u0435\u0442", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0410\u0440\u0431\u0430\u043b\u0435\u0442 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
                this.bowThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.CROSSBOW)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u0430\u0440\u0431\u0430\u043b\u0435\u0442!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.bowThrow = false;
        }
        if (this.serkaThrow) {
            hbSlot = this.getItemForName("\u0441\u0435\u0440\u043d\u0430\u044f", true);
            invSlot = this.getItemForName("\u0441\u0435\u0440\u043d\u0430\u044f", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u0421\u0435\u0440\u043a\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430");
                this.serkaThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.SPLASH_POTION)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u0441\u0435\u0440\u043a\u0443!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.serkaThrow = false;
        }
        if (this.otrigaThrow) {
            hbSlot = this.getItemForName("\u043e\u0442\u0440\u044b\u0436\u043a\u0438", true);
            invSlot = this.getItemForName("\u043e\u0442\u0440\u044b\u0436\u043a\u0438", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u041e\u0442\u0440\u044b\u0433\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430");
                this.otrigaThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.SPLASH_POTION)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u043e\u0442\u0440\u044b\u0433\u0443!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.otrigaThrow = false;
        }
        if (this.plastThrow) {
            hbSlot = this.getItemForName("\u043f\u043b\u0430\u0441\u0442", true);
            invSlot = this.getItemForName("\u043f\u043b\u0430\u0441\u0442", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u041f\u043b\u0430\u0441\u0442 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
                this.plastThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.DRIED_KELP)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u043f\u043b\u0430\u0441\u0442!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.plastThrow = false;
        }
        if (this.blatantThrow) {
            hbSlot = this.getItemForName("\u044f\u0432\u043d\u0430\u044f", true);
            invSlot = this.getItemForName("\u044f\u0432\u043d\u0430\u044f", false);
            if (invSlot == -1 && hbSlot == -1) {
                this.print("\u042f\u0432\u043d\u0430\u044f \u043f\u044b\u043b\u044c \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430");
                this.blatantThrow = false;
                return;
            }
            if (!Minecraft.player.getCooldownTracker().hasCooldown(Items.TNT)) {
                this.print("\u0417\u0430\u044e\u0437\u0430\u043b \u044f\u0432\u043d\u0443\u044e \u043f\u044b\u043b\u044c!");
                old = Minecraft.player.inventory.currentItem;
                slot = this.findAndTrowItem(hbSlot, invSlot);
                if (slot > 8) {
                    GriefHelper.mc.playerController.pickItem(slot);
                }
                if (InventoryUtil.findEmptySlot(true) != -1) {
                    if (Minecraft.player.inventory.currentItem != old) {
                        Minecraft.player.inventory.currentItem = old;
                    }
                }
            }
            this.blatantThrow = false;
        }
        this.handUtil.handleItemChange(System.currentTimeMillis() - this.delay > 200L);
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        this.handUtil.onEventPacket(e);
    }

    private int findAndTrowItem(int hbSlot, int invSlot) {
        if (hbSlot != -1) {
            this.handUtil.setOriginalSlot(Minecraft.player.inventory.currentItem);
            Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return hbSlot;
        }
        if (invSlot != -1) {
            this.handUtil.setOriginalSlot(Minecraft.player.inventory.currentItem);
            GriefHelper.mc.playerController.pickItem(invSlot);
            Minecraft.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
            Minecraft.player.swingArm(Hand.MAIN_HAND);
            this.delay = System.currentTimeMillis();
            return invSlot;
        }
        return -1;
    }

    @Override
    public void onDisable() {
        this.disorientationThrow = false;
        this.trapThrow = false;
        this.flameThrow = false;
        this.blatantThrow = false;
        this.plastThrow = false;
        this.otrigaThrow = false;
        this.serkaThrow = false;
        this.bowThrow = false;
        this.delay = 0L;
        super.onDisable();
    }

    private int getItemForName(String name, boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; ++i) {
            String displayName;
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof AirItem || (displayName = TextFormatting.getTextWithoutFormattingCodes(itemStack.getDisplayName().getString())) == null || !displayName.toLowerCase().contains(name)) continue;
            return i;
        }
        return -1;
    }
}

