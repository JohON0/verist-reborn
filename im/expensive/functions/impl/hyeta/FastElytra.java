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
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="FastElytra", type=Category.Misc)
public class FastElytra
extends Function {
    private final Minecraft mc = Minecraft.getInstance();

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (Minecraft.player != null) {
            ItemStack chestItem = Minecraft.player.inventory.armorInventory.get(2);
            if (chestItem.getItem() == Items.ELYTRA) {
                KeyBinding useItemKey;
                if (Minecraft.player.isElytraFlying() && (useItemKey = this.mc.gameSettings.keyBindUseItem).isKeyDown()) {
                    Vector3d lookVec = Minecraft.player.getLook(1.0f);
                    double targetSpeed = 3.0;
                    Minecraft.player.setMotion(lookVec.x * targetSpeed, lookVec.y * targetSpeed, lookVec.z * targetSpeed);
                }
            }
        }
    }
}

