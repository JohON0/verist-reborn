/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Fly FT", type=Category.Movement)
public class FlyFt
extends Function {
    private final ModeSetting mode = new ModeSetting("\u041c\u043e\u0434", "Funtime", "Funtime");
    final ModeListSetting options = new ModeListSetting("\u041e\u043f\u0446\u0438\u0438", new BooleanSetting("\u0410\u0432\u0442\u043e \u043f\u0440\u044b\u0436\u043e\u043a", false), new BooleanSetting("\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u0438\u0433\u0440\u043e\u043a\u0430", true), new BooleanSetting("\u0411\u043b\u043e\u043a\u0438 \u0438\u0437 \u0445\u043e\u0442\u0431\u0430\u0440\u0430", true), new BooleanSetting("\u0420\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0432 \u0432\u043e\u0437\u0434\u0443\u0445\u0435", false), new BooleanSetting("\u0421\u0442\u0430\u0432\u0438\u0442\u044c \u0411\u043b\u043e\u043a\u0438", true), new BooleanSetting("\u0420\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u043d\u0430 \u043a\u0440\u0430\u044e \u0431\u043b\u043e\u043a\u043e\u0432", false), new BooleanSetting("\u0420\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u043d\u0430 \u043f\u043e\u043b\u0443\u0431\u043b\u043e\u043a\u0430\u0445 \u0438 \u0442.\u0434", false)).setVisible(() -> this.mode.is("Funtime"));
    private final ModeSetting speed_mode = new ModeSetting("\u0422\u0438\u043f \u0421\u043a\u043e\u0440\u043e\u0441\u0442\u0438", "\u041c\u043e\u0443\u0448\u0435\u043d", "\u041c\u043e\u0443\u0448\u0435\u043d", "\u041c\u0443\u0432 \u0443\u0442\u0438\u043b").setVisible(() -> this.mode.is("Funtime"));
    public SliderSetting speed_Motion = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u041c\u043e\u0443\u0448\u0435\u043d", 1.2f, 1.01f, 1.9f, 0.01f).setVisible(() -> this.mode.is("Funtime") && this.speed_mode.is("\u041c\u043e\u0443\u0448\u0435\u043d"));
    public SliderSetting speed_MoveUlti = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u041c\u0443\u0432 \u0443\u0442\u0438\u043b", 0.3f, 0.1f, 1.0f, 0.01f).setVisible(() -> this.mode.is("Funtime") && this.speed_mode.is("\u041c\u0443\u0432 \u0443\u0442\u0438\u043b"));
    boolean workInAir = (Boolean)this.options.getValueByName("\u0420\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0432 \u0432\u043e\u0437\u0434\u0443\u0445\u0435").get();

    public FlyFt() {
        this.addSettings(this.mode, this.options, this.speed_mode, this.speed_Motion, this.speed_MoveUlti);
    }

    @Subscribe
    public void autojump(EventUpdate e) {
        if (((Boolean)this.options.getValueByName("\u0410\u0432\u0442\u043e \u043f\u0440\u044b\u0436\u043e\u043a").get()).booleanValue()) {
            if (Minecraft.player.isOnGround()) {
                Minecraft.player.jump();
            }
        }
    }

    @Subscribe
    public void rotation(EventMotion e) {
        if (((Boolean)this.options.getValueByName("\u0420\u043e\u0442\u0430\u0446\u0438\u044f \u0438\u0433\u0440\u043e\u043a\u0430").get()).booleanValue()) {
            Minecraft.player.rotationPitchHead = 90.0f;
            e.setPitch(90.0f);
        }
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        block11: {
            block10: {
                if (Minecraft.player == null) break block10;
                if (Minecraft.world != null) break block11;
            }
            return;
        }
        switch (this.mode.getIndex()) {
            case 0: {
                if (this.speed_mode.is("\u041c\u043e\u0443\u0448\u0435\u043d")) {
                    if (Minecraft.player.isOnGround()) {
                        Minecraft.player.motion.x *= (double)((Float)this.speed_Motion.get()).floatValue();
                        Minecraft.player.motion.z *= (double)((Float)this.speed_Motion.get()).floatValue();
                    }
                }
                if (this.speed_mode.is("\u041c\u0443\u0432 \u0443\u0442\u0438\u043b")) {
                    if (Minecraft.player.isOnGround()) {
                        MoveUtils.setMotion(((Float)this.speed_MoveUlti.get()).floatValue());
                    }
                }
                if (!((Boolean)this.options.getValueByName("\u0421\u0442\u0430\u0432\u0438\u0442\u044c \u0411\u043b\u043e\u043a\u0438").get()).booleanValue()) break;
                BlockPos posBelow = Minecraft.player.getPosition().down();
                if (!Minecraft.world.getBlockState(posBelow).getMaterial().isReplaceable()) break;
                this.placeBlockUnderPlayer(posBelow);
            }
        }
    }

    private void placeBlockUnderPlayer(BlockPos pos) {
        int lastSlot = Minecraft.player.inventory.currentItem;
        int blockSlot = this.findBlockInHotbar();
        if (blockSlot == -1) {
            return;
        }
        Minecraft.player.inventory.currentItem = blockSlot;
        Vector3d vector3d = new Vector3d((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5);
        BlockRayTraceResult result = new BlockRayTraceResult(vector3d, Direction.UP, pos, false);
        Minecraft.player.swingArm(Hand.MAIN_HAND);
        Minecraft.player.connection.sendPacket(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, result));
        Minecraft.player.inventory.currentItem = lastSlot;
        Minecraft.player.setMotion(Minecraft.player.getMotion().x, 0.01, Minecraft.player.getMotion().z);
    }

    private int findBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) continue;
            return i;
        }
        return -1;
    }

    public ModeListSetting getOptions() {
        return this.options;
    }
}

