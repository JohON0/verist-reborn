/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.render.ColorUtils;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

@FunctionRegister(name="MinPrice", type=Category.Misc)
public class BuyHelperS
extends Function {
    private int minPrice = Integer.MAX_VALUE;
    private Slot cheapestSlot = null;

    public void ProcessHelping(ChestScreen chestScreen, Slot slot) {
        int currentPrice = this.extractPriceFromStack(slot.getStack());
        if (currentPrice != -1 && currentPrice < this.minPrice) {
            this.minPrice = currentPrice;
            if (this.cheapestSlot != null) {
                this.resetSlotColor(this.cheapestSlot);
            }
            this.cheapestSlot = slot;
            this.highlightSlot(slot, ColorUtils.getColor(90));
        }
    }

    protected int extractPriceFromStack(ItemStack stack) {
        CompoundNBT display;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("display", 10) && (display = tag.getCompound("display")).contains("Lore", 9)) {
            ListNBT lore = display.getList("Lore", 8);
            for (int j = 0; j < lore.size(); ++j) {
                JsonObject title;
                JsonArray array;
                JsonObject object = JsonParser.parseString(lore.getString(j)).getAsJsonObject();
                if (!object.has("extra") || (array = object.getAsJsonArray("extra")).size() <= 2 || !(title = array.get(1).getAsJsonObject()).get("text").getAsString().trim().toLowerCase().contains("\u0446\u0435\u043da")) continue;
                String line = array.get(2).getAsJsonObject().get("text").getAsString().trim().substring(1).replaceAll(" ", "");
                return Integer.parseInt(line);
            }
        }
        return -1;
    }

    protected String extractPidorFromStack(ItemStack stack) {
        CompoundNBT display;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("display", 10) && (display = tag.getCompound("display")).contains("Lore", 9)) {
            ListNBT lore = display.getList("Lore", 8);
            for (int j = 0; j < lore.size(); ++j) {
                JsonObject title;
                JsonArray array;
                JsonObject object = JsonParser.parseString(lore.getString(j)).getAsJsonObject();
                if (!object.has("extra") || (array = object.getAsJsonArray("extra")).size() <= 2 || !(title = array.get(1).getAsJsonObject()).get("text").getAsString().trim().toLowerCase().startsWith("\u043f\u0440o\u0434a\u0432e\u0446")) continue;
                return array.get(2).getAsJsonObject().get("text").getAsString().trim().replaceAll(" ", "");
            }
        }
        return "";
    }

    private void highlightSlot(Slot slot, int color) {
        slot.getBackground();
    }

    private void resetSlotColor(Slot slot) {
        slot.getBackground();
    }

    public static int kPVdqShmIepd8tH7zYl3() {
        return 2035918877;
    }

    public static int Vzuk9Knwrnk8l4neTuJ8() {
        return 1909009989;
    }
}

