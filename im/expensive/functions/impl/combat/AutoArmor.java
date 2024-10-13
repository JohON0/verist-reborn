/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.MoveUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FunctionRegister(name="AutoArmor", type=Category.Combat)
public class AutoArmor
extends Function {
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430", 100.0f, 0.0f, 1000.0f, 1.0f);
    private final StopWatch stopWatch = new StopWatch();

    public AutoArmor() {
        this.addSettings(this.delay);
    }

    @Subscribe
    private void onUpdate(EventUpdate event) {
        int i;
        if (MoveUtils.isMoving()) {
            return;
        }
        PlayerInventory inventoryPlayer = Minecraft.player.inventory;
        int[] bestIndexes = new int[4];
        int[] bestValues = new int[4];
        for (i = 0; i < 4; ++i) {
            Item item;
            bestIndexes[i] = -1;
            ItemStack stack = inventoryPlayer.armorItemInSlot(i);
            if (!this.isItemValid(stack) || !((item = stack.getItem()) instanceof ArmorItem)) continue;
            ArmorItem armorItem = (ArmorItem)item;
            bestValues[i] = this.calculateArmorValue(armorItem, stack);
        }
        for (i = 0; i < 36; ++i) {
            Item item;
            ItemStack stack = inventoryPlayer.getStackInSlot(i);
            if (!this.isItemValid(stack) || !((item = stack.getItem()) instanceof ArmorItem)) continue;
            ArmorItem armorItem = (ArmorItem)item;
            int armorTypeIndex = armorItem.getSlot().getIndex();
            int value = this.calculateArmorValue(armorItem, stack);
            if (value <= bestValues[armorTypeIndex]) continue;
            bestIndexes[armorTypeIndex] = i;
            bestValues[armorTypeIndex] = value;
        }
        ArrayList<Integer> randomIndexes = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(randomIndexes);
        for (int index : randomIndexes) {
            int bestIndex = bestIndexes[index];
            if (bestIndex == -1 || this.isItemValid(inventoryPlayer.armorItemInSlot(index)) && inventoryPlayer.getFirstEmptyStack() == -1) continue;
            if (bestIndex < 9) {
                bestIndex += 36;
            }
            if (!this.stopWatch.isReached(((Float)this.delay.get()).longValue())) break;
            ItemStack armorItemStack = inventoryPlayer.armorItemInSlot(index);
            if (this.isItemValid(armorItemStack)) {
                AutoArmor.mc.playerController.windowClick(0, 8 - index, 0, ClickType.QUICK_MOVE, Minecraft.player);
            }
            AutoArmor.mc.playerController.windowClick(0, bestIndex, 0, ClickType.QUICK_MOVE, Minecraft.player);
            this.stopWatch.reset();
            break;
        }
    }

    private boolean isItemValid(ItemStack stack) {
        return stack != null && !stack.isEmpty();
    }

    private int calculateArmorValue(ArmorItem armor, ItemStack stack) {
        int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
        IArmorMaterial armorMaterial = armor.getArmorMaterial();
        int damageReductionAmount = armorMaterial.getDamageReductionAmount(armor.getEquipmentSlot());
        return armor.getDamageReduceAmount() * 20 + protectionLevel * 12 + (int)(armor.getToughness() * 2.0f) + damageReductionAmount * 5 >> 3;
    }
}

