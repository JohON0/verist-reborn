//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.AirItem;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;

@FunctionRegister(
        name = "AutoGapple",
        type = Category.Combat
)
public class AutoGapple extends Function {
    private final SliderSetting healthSetting = new SliderSetting("Здоровье", 16.0F, 1.0F, 20.0F, 0.05F);
    private final BooleanSetting eatAtTheStart = new BooleanSetting("Съесть в начале", true);
    private boolean isEating;
    private final StopWatch stopWatch = new StopWatch();

    public AutoGapple() {
        this.addSettings(new Setting[]{this.healthSetting, this.eatAtTheStart});
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (this.shouldToTakeGApple() && (Boolean)this.eatAtTheStart.get()) {
            this.takeGappleInOffHand();
        }

        this.eatGapple();
    }

    private void eatGapple() {
        if (this.conditionToEat()) {
            this.startEating();
        } else if (this.isEating) {
            this.stopEating();
        }

    }

    private boolean shouldToTakeGApple() {
        Minecraft var10000;
        boolean isTicksExisted;
        boolean var6;
        label34: {
            var10000 = mc;
            isTicksExisted = Minecraft.player.ticksExisted == 15;
            var10000 = mc;
            if (Minecraft.player.getAbsorptionAmount() != 0.0F) {
                var10000 = mc;
                if (Minecraft.player.isPotionActive(Effects.REGENERATION)) {
                    var6 = false;
                    break label34;
                }
            }

            var6 = true;
        }

        boolean appleNotEaten = var6;
        var10000 = mc;
        boolean appleIsNotOffHand = Minecraft.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE;
        boolean timeHasPassed = this.stopWatch.isReached(200L);
        boolean settingIsEnalbed = (Boolean)this.eatAtTheStart.get();
        return isTicksExisted && appleNotEaten && appleIsNotOffHand & timeHasPassed && settingIsEnalbed;
    }

    private void takeGappleInOffHand() {
        int gappleSlot = InventoryUtil.getInstance().getSlotInInventory(Items.GOLDEN_APPLE);
        if (gappleSlot >= 0) {
            this.moveGappleToOffhand(gappleSlot);
        }

    }

    private void moveGappleToOffhand(int gappleSlot) {
        if (gappleSlot < 9 && gappleSlot != -1) {
            gappleSlot += 36;
        }

        Minecraft var10005 = mc;
        mc.playerController.windowClick(0, gappleSlot, 0, ClickType.PICKUP, Minecraft.player);
        var10005 = mc;
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, Minecraft.player);
        Minecraft var10000 = mc;
        if (!(Minecraft.player.getHeldItemOffhand().getItem() instanceof AirItem)) {
            var10005 = mc;
            mc.playerController.windowClick(0, gappleSlot, 0, ClickType.PICKUP, Minecraft.player);
        }

        this.stopWatch.reset();
    }

    private void startEating() {
        if (mc.currentScreen != null) {
            mc.currentScreen.passEvents = true;
        }

        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
            mc.gameSettings.keyBindUseItem.setPressed(true);
            this.isEating = true;
        }

    }

    private void stopEating() {
        mc.gameSettings.keyBindUseItem.setPressed(false);
        this.isEating = false;
    }

    private boolean conditionToEat() {
        Minecraft var10000;
        float myHealth;
        boolean var4;
        label30: {
            var10000 = mc;
            float var3 = Minecraft.player.getHealth();
            Minecraft var10001 = mc;
            myHealth = var3 + Minecraft.player.getAbsorptionAmount();
            var10000 = mc;
            if (Minecraft.player.getAbsorptionAmount() != 0.0F) {
                var10000 = mc;
                if (Minecraft.player.isPotionActive(Effects.REGENERATION)) {
                    var4 = false;
                    break label30;
                }
            }

            var4 = true;
        }

        label24: {
            boolean appleNotEaten = var4;
            if (!this.isHealthLow(myHealth)) {
                var10000 = mc;
                if (Minecraft.player.ticksExisted >= 100 || !appleNotEaten) {
                    break label24;
                }
            }

            if (this.hasGappleInHand() && !this.isGappleOnCooldown()) {
                var4 = true;
                return var4;
            }
        }

        var4 = false;
        return var4;
    }

    private boolean isGappleOnCooldown() {
        Minecraft var10000 = mc;
        return Minecraft.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE);
    }

    private boolean isHealthLow(float health) {
        return health <= (Float)this.healthSetting.get();
    }

    private boolean hasGappleInHand() {
        Minecraft var10000 = mc;
        boolean var1;
        if (Minecraft.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE) {
            var10000 = mc;
            if (Minecraft.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
                var1 = false;
                return var1;
            }
        }

        var1 = true;
        return var1;
    }

    private void reset() {
        this.stopWatch.reset();
    }

    public void onDisable() {
        this.reset();
        super.onDisable();
    }
}
