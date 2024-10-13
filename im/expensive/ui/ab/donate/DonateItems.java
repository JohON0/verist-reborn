/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.ab.donate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DonateItems {
    public static ArrayList<ItemStack> donitem = new ArrayList();

    public static void add() {
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiNWNlMGQ0NGMzZTgxMzhkYzJlN2U1MmMyODk3YmI4NzhlMWRiYzIyMGQ3MDY4OWM3YjZiMThkMzE3NWUwZiJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u041c\u0430\u0433\u043c\u044b"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIwMWFlMWE4YTA0ZGY1MjY1NmY1ZTQ4MTNlMWZiY2Y5Nzg3N2RiYmZiYzQyNjhkMDQzMTZkNmY5Zjc1MyJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0422\u0435\u0443\u0440\u0433\u0435\u044f"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RmZDViZjFmZjA1NDMxNDdjOWQ2NGU2ODc2MWRiNmU0YjcxMzJhYzY1OGYwYjhmNzk4MzFmYWQ5YzI4OWVjYSJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u041f\u0430\u043d\u0430\u043a\u0435\u044f"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY2MzZiYTY5ODhjZTliNDBkZGM3NDlhMDljZTBmYjkzOWFmNTI2MDA1OTk1YzE4ZDMyM2FjOTY2MjVmMGQ2ZCJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0424\u0438\u043b\u043e\u043d\u0430"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNmOWVlZGEzYmEyM2ZlMTQyM2M0MDM2ZTdkZDBhNzQ0NjFkZmY5NmJhZGM1YjJmMmI5ZmFhN2NjMTZmMzgyZiJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0410\u0444\u0438\u043d\u0430"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3OTliZmFhM2EyYzYzYWQ4NWRkMzc4ZTY2ZDU3ZDlhOTdhM2Y4NmQwZDlmNjgzYzQ5ODYzMmY0ZjVjIn19fQ=", "\u0421\u0444\u0435\u0440\u0430 \u0421\u043e\u0440\u0430\u043d\u0430"));
        donitem.add(DonateItems.add("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjgyMjAyODJmMmVlNTk5NTExYjRmYzc0NjExMWM5NzM2ZDdiNDkxZThiY2ZiNjQ4YThhMTU2MjkyODFlZTUifX19=", "\u0421\u0444\u0435\u0440\u0430 \u042d\u043f\u0438\u043e\u043d\u0430"));
        donitem.add(DonateItems.add("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFkYzRhMDI0NzE4ZDQwMWVlYWU5ZTk1YjNjOTI3NjdmOTE2ZjMyM2M5ZTgzNjQ5YWQxNWM5MjY1ZWU1MDkyZiJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0418\u0430\u0441\u043e"));
        donitem.add(DonateItems.add("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQxNDQ5MDk3YjRiNzlhOWY2Y2FmNjM0NDQxOGYyMDM0ZGU0YmI5NzFmZWI3YThlNGFhY2JmYjkwNWFjZGNlZiJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0410\u0431\u0430\u043d\u0442\u044b"));
        donitem.add(DonateItems.add("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNkMTQ1NjFiYmQwNjNmNzA0MjRhOGFmY2MzN2JmZTljNzQ1NjJlYTM2ZjdiZmEzZjIzMjA2ODMwYzY0ZmFmMSJ9fX0=", "\u0421\u0444\u0435\u0440\u0430 \u0421\u043a\u0438\u0444\u0430"));
        ItemStack desorientationItem = new ItemStack(Items.ENDER_EYE);
        desorientationItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u0414\u0435\u0437\u043e\u0440\u0438\u0435\u043d\u0442\u0430\u0446\u0438\u044f"));
        ItemStack plastItem = new ItemStack(Items.DRIED_KELP);
        plastItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u041f\u043b\u0430\u0441\u0442"));
        ItemStack obviousDustItem = new ItemStack(Items.SUGAR);
        obviousDustItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u042f\u0432\u043d\u0430\u044f \u043f\u044b\u043b\u044c"));
        ItemStack tridentCrusherItem = new ItemStack(Items.TRIDENT);
        tridentCrusherItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u0422\u0440\u0435\u0437\u0443\u0431\u0435\u0446 \u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f"));
        ItemStack trapItem = new ItemStack(Items.NETHERITE_SCRAP);
        trapItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u0422\u0440\u0430\u043f\u043a\u0430"));
        ItemStack potionOfInvisibilityItem = new ItemStack(Items.POTION);
        potionOfInvisibilityItem.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u0417\u0435\u043b\u044c\u0435 \u043d\u0435\u0432\u0438\u0434\u0438\u043c\u043e\u0441\u0442\u0438"));
        ItemStack potionOfStrength = new ItemStack(Items.POTION);
        potionOfStrength.setDisplayName(ITextComponent.getTextComponentOrEmpty("\u0417\u0435\u043b\u044c\u0435 \u0441\u0438\u043b\u044b"));
        donitem.addAll(List.of((Object)desorientationItem, (Object)plastItem, (Object)obviousDustItem, (Object)tridentCrusherItem, (Object)trapItem, (Object)potionOfInvisibilityItem, (Object)potionOfStrength));
        HashMap<Enchantment, Integer> fake = new HashMap<Enchantment, Integer>();
        fake.put(Enchantments.UNBREAKING, 0);
        for (ItemStack s : donitem) {
            EnchantmentHelper.setEnchantments(fake, s);
        }
    }

    public static ItemStack add(String texture, String name) {
        try {
            ItemStack magma = new ItemStack(Items.PLAYER_HEAD);
            magma.setTag(JsonToNBT.getTagFromJson(String.format("{SkullOwner:{Id:[I;-1949909288,1299464445,-1707774066,-249984712],Properties:{textures:[{Value:\"%s\"}]},Name:\"%s\"}}", texture, name)));
            magma.setDisplayName(new StringTextComponent(name));
            return magma;
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

