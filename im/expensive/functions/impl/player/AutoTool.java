/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

@FunctionRegister(name="AutoTool", type=Category.Player)
public class AutoTool
extends Function {
    public final BooleanSetting silent = new BooleanSetting("\u041d\u0435\u0437\u0430\u043c\u0435\u0442\u043d\u044b\u0439", true);
    public final BooleanSetting haste = new BooleanSetting("\u0421\u043f\u0435\u0448\u043a\u0430", false);
    public int itemIndex = -1;
    public int oldSlot = -1;
    boolean status;
    boolean clicked;

    public AutoTool() {
        this.addSettings(this.silent, this.haste);
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        block15: {
            block14: {
                if (Minecraft.player == null) break block14;
                if (!Minecraft.player.isCreative()) break block15;
            }
            this.itemIndex = -1;
            return;
        }
        if (this.isMousePressed()) {
            this.itemIndex = this.findBestToolSlotInHotBar();
            if (this.itemIndex != -1) {
                this.status = true;
                if (this.oldSlot == -1) {
                    this.oldSlot = Minecraft.player.inventory.currentItem;
                }
                if (((Boolean)this.silent.get()).booleanValue()) {
                    Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(this.itemIndex));
                } else {
                    Minecraft.player.inventory.currentItem = this.itemIndex;
                }
            }
        } else if (this.status && this.oldSlot != -1) {
            if (((Boolean)this.silent.get()).booleanValue()) {
                Minecraft.player.connection.sendPacket(new CHeldItemChangePacket(this.oldSlot));
            } else {
                Minecraft.player.inventory.currentItem = this.oldSlot;
            }
            this.itemIndex = this.oldSlot;
            this.status = false;
            this.oldSlot = -1;
        }
        if (((Boolean)this.haste.get()).booleanValue()) {
            Minecraft.player.addPotionEffect(new EffectInstance(Effects.HASTE, 100, 1, false, false));
        }
    }

    @Override
    public void onDisable() {
        this.status = false;
        this.itemIndex = -1;
        this.oldSlot = -1;
        Minecraft.player.removePotionEffect(Effects.HASTE);
        super.onDisable();
    }

    private int findBestToolSlotInHotBar() {
        RayTraceResult rayTraceResult = AutoTool.mc.objectMouseOver;
        if (rayTraceResult instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTraceResult;
            Block block2 = Minecraft.world.getBlockState(blockRayTraceResult.getPos()).getBlock();
            int bestSlot = -1;
            float bestSpeed = 1.0f;
            for (int slot = 0; slot < 9; ++slot) {
                float speed = Minecraft.player.inventory.getStackInSlot(slot).getDestroySpeed(block2.getDefaultState());
                if (!(speed > bestSpeed)) continue;
                bestSpeed = speed;
                bestSlot = slot;
            }
            return bestSlot;
        }
        return -1;
    }

    private boolean isMousePressed() {
        return AutoTool.mc.objectMouseOver != null && AutoTool.mc.gameSettings.keyBindAttack.isKeyDown();
    }
}

