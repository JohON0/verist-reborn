/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.DropperTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.optifine.render.RenderUtils;

@FunctionRegister(name="StorageESP", type=Category.Render)
public class StorageESP
extends Function {
    private final BooleanSetting showChest = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a\u0438", true);
    private final BooleanSetting showTrappedChest = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a-\u043b\u043e\u0432\u0443\u0448\u043a\u0430", true);
    private final BooleanSetting showSkull = new BooleanSetting("\u0413\u043e\u043b\u043e\u0432\u0430", true);
    private final BooleanSetting showHopper = new BooleanSetting("\u0412\u043e\u0440\u043e\u043d\u043a\u0438", true);
    private final BooleanSetting showDispenser = new BooleanSetting("\u0420\u0430\u0441\u043f\u044b\u043b\u0438\u0442\u0435\u043b\u044c", true);
    private final BooleanSetting showDropper = new BooleanSetting("\u0412\u043e\u0440\u043e\u043d\u043a\u0438", true);
    private final BooleanSetting showFurnace = new BooleanSetting("\u041f\u0435\u0447\u044c", true);
    private final BooleanSetting showEnderChest = new BooleanSetting("\u042d\u043d\u0434\u0435\u0440-\u0441\u0443\u043d\u0434\u0443\u043a", true);
    private final BooleanSetting showShulkerBox = new BooleanSetting("\u0428\u0430\u043b\u043a\u0435\u0440", true);
    private final BooleanSetting showMobSpawner = new BooleanSetting("\u0421\u043f\u0430\u0432\u043d\u0435\u0440", true);
    private final BooleanSetting showChestMinecart = new BooleanSetting("\u0422\u0435\u043b\u0435\u0436\u043a\u0430-\u0441\u0443\u043d\u0434\u0443\u043a", true);
    private final Map<TileEntityType<?>, BooleanSetting> tiles = new HashMap(Map.of(new ChestTileEntity().getType(), (Object)this.showChest, new TrappedChestTileEntity().getType(), (Object)this.showTrappedChest, new SkullTileEntity().getType(), (Object)this.showSkull, new HopperTileEntity().getType(), (Object)this.showHopper, new DispenserTileEntity().getType(), (Object)this.showDispenser, new DropperTileEntity().getType(), (Object)this.showDropper, new FurnaceTileEntity().getType(), (Object)this.showFurnace, new EnderChestTileEntity().getType(), (Object)this.showEnderChest, new ShulkerBoxTileEntity().getType(), (Object)this.showShulkerBox, new MobSpawnerTileEntity().getType(), (Object)this.showMobSpawner));
    private final Map<TileEntityType<?>, Integer> colors = new HashMap(Map.of(new ChestTileEntity().getType(), (Object)new Color(243, 172, 82).getRGB(), new TrappedChestTileEntity().getType(), (Object)new Color(143, 109, 62).getRGB(), new SkullTileEntity().getType(), (Object)new Color(243, 172, 82).getRGB(), new HopperTileEntity().getType(), (Object)new Color(62, 137, 250).getRGB(), new DispenserTileEntity().getType(), (Object)new Color(27, 64, 250).getRGB(), new DropperTileEntity().getType(), (Object)new Color(0, 23, 255).getRGB(), new FurnaceTileEntity().getType(), (Object)new Color(115, 115, 115).getRGB(), new EnderChestTileEntity().getType(), (Object)new Color(82, 49, 238).getRGB(), new ShulkerBoxTileEntity().getType(), (Object)new Color(246, 123, 123).getRGB(), new MobSpawnerTileEntity().getType(), (Object)new Color(112, 236, 166).getRGB()));

    public StorageESP() {
        this.addSettings(this.showChest, this.showTrappedChest, this.showSkull, this.showHopper, this.showDispenser, this.showDropper, this.showFurnace, this.showEnderChest, this.showShulkerBox, this.showMobSpawner, this.showChestMinecart);
    }

    @Subscribe
    private void onRender(WorldEvent e) {
        for (TileEntity tile : Minecraft.world.loadedTileEntityList) {
            BooleanSetting setting;
            if (!this.tiles.containsKey(tile.getType()) || (setting = this.tiles.get(tile.getType())) == null || !((Boolean)setting.get()).booleanValue()) continue;
            BlockPos pos = tile.getPos();
            RenderUtils.drawBlockBox(pos, this.colors.get(tile.getType()));
        }
        if (((Boolean)this.showChestMinecart.get()).booleanValue()) {
            for (Entity entity2 : Minecraft.world.getAllEntities()) {
                if (!(entity2 instanceof ChestMinecartEntity)) continue;
                RenderUtils.drawBlockBox(entity2.getPosition(), -1);
            }
        }
    }
}

