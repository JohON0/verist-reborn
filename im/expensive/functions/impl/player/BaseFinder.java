/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventKey;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.optifine.render.RenderUtils;

@FunctionRegister(name="BaseFinder", type=Category.Misc)
public class BaseFinder
extends Function {
    private BindSetting active = new BindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u043f\u043e\u0438\u0441\u043a\u0430", 0);
    public BooleanSetting notif = new BooleanSetting("\u0421\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043e \u0441\u0443\u043d\u0434\u0443\u043a\u0435", true);
    public BooleanSetting gps = new BooleanSetting("GPS \u043d\u0430 \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0438\u0439 \u0441\u0443\u043d\u0434\u0443\u043a", true);
    public BooleanSetting barrel = new BooleanSetting("GPS \u043d\u0430 \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0443\u044e \u0431\u043e\u0447\u043a\u0443", true);
    private final Map<TileEntityType<?>, Integer> tiles = new HashMap(Map.of(new ChestTileEntity().getType(), (Object)new Color(0, 187, 8).getRGB(), new BarrelTileEntity().getType(), (Object)new Color(159, 39, 192).getRGB()));

    public BaseFinder() {
        this.addSettings(this.active, this.notif, this.gps, this.barrel);
    }

    @Subscribe
    public void onKey(EventKey e) {
        if (e.getKey() == ((Integer)this.active.get()).intValue()) {
            boolean foundChest = false;
            boolean foundBarrel = false;
            for (TileEntity t : Minecraft.world.loadedTileEntityList) {
                BlockRayTraceResult rayTraceResult;
                double distanceSq;
                int z;
                int y;
                int x;
                if (t instanceof ChestTileEntity) {
                    x = t.getPos().getX();
                    y = t.getPos().getY();
                    z = t.getPos().getZ();
                    distanceSq = Minecraft.player.getDistanceSq(x, y, z);
                    if (distanceSq < 20000.0) {
                        foundChest = true;
                        if (((Boolean)this.notif.get()).booleanValue()) {
                            this.print(TextFormatting.GREEN + "\u0412\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0431\u044b\u043b\u0430 \u043d\u0430\u0439\u0434\u0435\u043d \u0441\u0443\u043d\u0434\u0443\u043a");
                        }
                        rayTraceResult = new BlockRayTraceResult(new Vector3d(Minecraft.player.getPosX(), Minecraft.player.getPosY() - 1.0, Minecraft.player.getPosZ()), Direction.UP, new BlockPos(x, y, z), false);
                        BaseFinder.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, rayTraceResult);
                        if (((Boolean)this.gps.get()).booleanValue()) {
                            Minecraft.player.sendChatMessage(".gps add \u0421\u0443\u043d\u0434\u0443\u043a " + x + " " + y + " " + z);
                        }
                    }
                }
                if (!(t instanceof BarrelTileEntity)) continue;
                x = t.getPos().getX();
                y = t.getPos().getY();
                z = t.getPos().getZ();
                distanceSq = Minecraft.player.getDistanceSq(x, y, z);
                if (!(distanceSq < 20000.0)) continue;
                foundBarrel = true;
                if (((Boolean)this.notif.get()).booleanValue()) {
                    this.print(TextFormatting.GREEN + "\u0412\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0431\u044b\u043b\u0430 \u043d\u0430\u0439\u0434\u0435\u043d \u0441\u0443\u043d\u0434\u0443\u043a");
                }
                rayTraceResult = new BlockRayTraceResult(new Vector3d(Minecraft.player.getPosX(), Minecraft.player.getPosY() - 1.0, Minecraft.player.getPosZ()), Direction.UP, new BlockPos(x, y, z), false);
                BaseFinder.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, rayTraceResult);
                if (((Boolean)this.barrel.get()).booleanValue()) {
                    Minecraft.player.sendChatMessage(".gps add \u0411\u043e\u0447\u043a\u0430 " + x + " " + y + " " + z);
                }
                if (foundChest || foundBarrel) continue;
                this.print(TextFormatting.RED + "\u0412 \u044d\u0442\u043e\u043c \u0440\u0435\u0433\u0438\u043e\u043d\u0435 \u043d\u0435 \u043d\u0430\u0448\u043b\u043e\u0441\u044c \u0431\u0430\u0437");
            }
        }
    }

    @Subscribe
    private void onRender(WorldEvent e) {
        for (TileEntity tile : Minecraft.world.loadedTileEntityList) {
            if (!this.tiles.containsKey(tile.getType())) continue;
            BlockPos pos = tile.getPos();
            RenderUtils.drawBlockBox(pos, this.tiles.get(tile.getType()));
        }
        for (Entity entity2 : Minecraft.world.getAllEntities()) {
            if (!(entity2 instanceof ChestMinecartEntity)) continue;
            RenderUtils.drawBlockBox(entity2.getPosition(), -1);
        }
    }

    @Override
    public void onDisable() {
    }
}

