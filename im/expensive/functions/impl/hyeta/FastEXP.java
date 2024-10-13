/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@FunctionRegister(name="FastEXP", type=Category.Misc)
public class FastEXP
extends Function {
    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.fastEXP();
    }

    public void fastEXP() {
        if (Minecraft.player != null) {
            ItemStack mainhandItem = Minecraft.player.getHeldItemMainhand();
            if (!mainhandItem.isEmpty() && mainhandItem.getItem() == Items.EXPERIENCE_BOTTLE) {
                FastEXP.mc.rightClickDelayTimer = 0;
                return;
            }
            ItemStack offhandItem = Minecraft.player.getHeldItemOffhand();
            if (!offhandItem.isEmpty() && offhandItem.getItem() == Items.EXPERIENCE_BOTTLE) {
                FastEXP.mc.rightClickDelayTimer = 0;
            }
        }
    }
}

