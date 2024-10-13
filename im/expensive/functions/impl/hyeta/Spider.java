/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.hyeta;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventMotion;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.player.MouseUtil;
import im.expensive.utils.player.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.server.SEntityMetadataPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="Spider", type=Category.Movement)
public class Spider
extends Function {
    public ModeSetting mode = new ModeSetting("Mode", "Grim", "Grim", "Grim 2", "Matrix", "Elytra");
    private final SliderSetting spiderSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 2.0f, 1.0f, 10.0f, 0.05f).setVisible(() -> this.mode.is("Matrix"));
    private final BooleanSetting bypass = new BooleanSetting("\u0412\u0442\u043e\u0440\u043e\u0439 \u043e\u0431\u0445\u043e\u0434", true).setVisible(() -> this.mode.is("Elytra"));
    StopWatch stopWatch = new StopWatch();
    int oldItem = -1;
    int oldItem1 = -1;
    int i;
    long speed;

    public Spider() {
        this.addSettings(this.mode, this.bypass, this.spiderSpeed);
    }

    @Subscribe
    private void onMotion(EventMotion motion) {
        switch ((String)this.mode.get()) {
            case "Matrix": {
                if (!Minecraft.player.collidedHorizontally) {
                    return;
                }
                long speed = MathHelper.clamp(500L - ((Float)this.spiderSpeed.get()).longValue() / 2L * 100L, 0L, 500L);
                if (!this.stopWatch.isReached(speed)) break;
                motion.setOnGround(true);
                Minecraft.player.setOnGround(true);
                Minecraft.player.collidedVertically = true;
                Minecraft.player.collidedHorizontally = true;
                Minecraft.player.isAirBorne = true;
                Minecraft.player.jump();
                this.stopWatch.reset();
                break;
            }
            case "Grim": {
                int slotInHotBar = this.getSlotInInventoryOrHotbar(true);
                if (slotInHotBar == -1) {
                    this.print("\u0411\u043b\u043e\u043a\u0438 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b!");
                    this.toggle();
                    return;
                }
                if (!Minecraft.player.collidedHorizontally) {
                    return;
                }
                if (Minecraft.player.isOnGround()) {
                    motion.setOnGround(true);
                    Minecraft.player.setOnGround(true);
                    Minecraft.player.jump();
                }
                if (!(Minecraft.player.fallDistance > 0.0f)) break;
                if (!(Minecraft.player.fallDistance < 2.0f)) break;
                this.placeBlocks(motion, slotInHotBar);
                break;
            }
            case "Grim 2": {
                if (Minecraft.player.isOnGround()) break;
                this.speed = (long)MathHelper.clamp(500.0f - ((Float)this.spiderSpeed.get()).floatValue() / 2.0f * 100.0f, 0.0f, 500.0f);
                if (!this.stopWatch.isReached(this.speed)) break;
                Spider.mc.gameSettings.keyBindSneak.setPressed(true);
                motion.setOnGround(true);
                Minecraft.player.setOnGround(true);
                Minecraft.player.collidedVertically = true;
                Minecraft.player.collidedHorizontally = true;
                Minecraft.player.isAirBorne = true;
                if (Minecraft.player.fallDistance != 0.0f) {
                    Spider.mc.gameSettings.keyBindForward.setPressed(true);
                    Spider.mc.gameSettings.keyBindForward.setPressed(false);
                }
                Minecraft.player.jump();
                Spider.mc.gameSettings.keyBindSneak.setPressed(false);
                this.stopWatch.reset();
                break;
            }
            case "Elytra": {
                motion.setPitch(0.0f);
                Minecraft.player.rotationPitchHead = 0.0f;
            }
        }
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        switch ((String)this.mode.get()) {
            case "Elytra": {
                if (!((Boolean)this.bypass.get()).booleanValue()) {
                    this.i = 0;
                    while (this.i < 9) {
                        if (Minecraft.player.inventory.getStackInSlot(this.i).getItem() == Items.ELYTRA) {
                            if (!Minecraft.player.isOnGround()) {
                                if (Minecraft.player.collidedHorizontally) {
                                    if (Minecraft.player.fallDistance == 0.0f) {
                                        Spider.mc.playerController.windowClick(0, 6, this.i, ClickType.SWAP, Minecraft.player);
                                        Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                                        MoveUtils.setMotion(0.06);
                                        Minecraft.player.motion.y = 0.366;
                                        Spider.mc.playerController.windowClick(0, 6, this.i, ClickType.SWAP, Minecraft.player);
                                        this.oldItem = this.i;
                                    }
                                }
                            }
                        }
                        ++this.i;
                    }
                    break;
                }
                if (Minecraft.player.inventory.armorInventory.get(2).getItem() != Items.ELYTRA) {
                    if (Minecraft.player.collidedHorizontally) {
                        this.i = 0;
                        while (this.i < 9) {
                            if (Minecraft.player.inventory.getStackInSlot(this.i).getItem() == Items.ELYTRA) {
                                Spider.mc.playerController.windowClick(0, 6, this.i, ClickType.SWAP, Minecraft.player);
                                this.oldItem1 = this.i;
                                this.stopWatch.reset();
                            }
                            ++this.i;
                        }
                    }
                }
                if (Minecraft.player.collidedHorizontally) {
                    Spider.mc.gameSettings.keyBindJump.setPressed(false);
                    if (this.stopWatch.isReached(180L)) {
                        Spider.mc.gameSettings.keyBindJump.setPressed(true);
                    }
                }
                if (Minecraft.player.inventory.armorInventory.get(2).getItem() == Items.ELYTRA) {
                    if (!Minecraft.player.collidedHorizontally && this.oldItem1 != -1) {
                        Spider.mc.playerController.windowClick(0, 6, this.oldItem1, ClickType.SWAP, Minecraft.player);
                        this.oldItem1 = -1;
                    }
                }
                if (Minecraft.player.inventory.armorInventory.get(2).getItem() != Items.ELYTRA) break;
                if (Minecraft.player.isOnGround()) break;
                if (!Minecraft.player.collidedHorizontally) break;
                if (Minecraft.player.fallDistance != 0.0f) {
                    return;
                }
                Minecraft.player.connection.sendPacket(new CEntityActionPacket(Minecraft.player, CEntityActionPacket.Action.START_FALL_FLYING));
                MoveUtils.setMotion(0.02);
                Minecraft.player.motion.y = 0.36;
            }
        }
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        switch ((String)this.mode.get()) {
            case "Elytra": {
                if (!((Boolean)this.bypass.get()).booleanValue()) {
                    IPacket<?> var4 = e.getPacket();
                    if (var4 instanceof SPlayerPositionLookPacket) {
                        SPlayerPositionLookPacket p = (SPlayerPositionLookPacket)var4;
                        Minecraft.player.func_242277_a(new Vector3d(p.getX(), p.getY(), p.getZ()));
                        Minecraft.player.setRawPosition(p.getX(), p.getY(), p.getZ());
                        return;
                    }
                    if (!(e.getPacket() instanceof SEntityMetadataPacket)) break;
                    if (((SEntityMetadataPacket)e.getPacket()).getEntityId() != Minecraft.player.getEntityId()) break;
                    e.cancel();
                    break;
                }
                if (!(e.getPacket() instanceof SEntityMetadataPacket)) break;
                if (((SEntityMetadataPacket)e.getPacket()).getEntityId() != Minecraft.player.getEntityId()) break;
                e.cancel();
            }
        }
    }

    private void placeBlocks(EventMotion motion, int block2) {
        int last = Minecraft.player.inventory.currentItem;
        Minecraft.player.inventory.currentItem = block2;
        motion.setPitch(80.0f);
        motion.setYaw(Minecraft.player.getHorizontalFacing().getHorizontalAngle());
        BlockRayTraceResult r = (BlockRayTraceResult)MouseUtil.rayTrace(4.0, motion.getYaw(), motion.getPitch(), Minecraft.player);
        Minecraft.player.swingArm(Hand.MAIN_HAND);
        Spider.mc.playerController.processRightClickBlock(Minecraft.player, Minecraft.world, Hand.MAIN_HAND, r);
        Minecraft.player.inventory.currentItem = last;
        Minecraft.player.fallDistance = 0.0f;
    }

    public int getSlotInInventoryOrHotbar(boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() == Items.TORCH) continue;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof BlockItem)) {
                if (Minecraft.player.inventory.getStackInSlot(i).getItem() != Items.WATER_BUCKET) continue;
            }
            finalSlot = i;
        }
        return finalSlot;
    }
}

