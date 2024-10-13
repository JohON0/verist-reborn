/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.ab.factory;

import im.expensive.ui.ab.factory.ItemFactory;
import im.expensive.ui.ab.model.IItem;
import im.expensive.ui.ab.model.ItemImpl;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;

public class ItemFactoryImpl
implements ItemFactory {
    @Override
    public IItem createNewItem(Item item, int price, int quantity, int damage, Map<Enchantment, Integer> enchantments) {
        return new ItemImpl(item, price, quantity, damage, enchantments);
    }
}

