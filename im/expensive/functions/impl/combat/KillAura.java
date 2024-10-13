//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package im.expensive.functions.impl.combat;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.Expensive;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventInput;
import im.expensive.events.EventMotion;
import im.expensive.events.EventUpdate;
import im.expensive.events.WorldEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.Direction;
import im.expensive.utils.animations.impl.DecelerateAnimation;
import im.expensive.utils.math.SensUtils;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.player.InventoryUtil;
import im.expensive.utils.player.MouseUtil;
import im.expensive.utils.player.MoveUtils;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

@FunctionRegister(
        name = "Killaura",
        type = Category.Combat
)
public class KillAura extends Function {
    private final ModeSetting type = new ModeSetting("Тип", "Плавная", new String[]{"Плавная", "Резкая"});
    private final SliderSetting attackRange = new SliderSetting("Дистанция аттаки", 3.0F, 3.0F, 6.0F, 0.1F);
    private final ModeSetting targetMode = new ModeSetting("Режим", "Здоровье", new String[]{"Здоровье", "Дистанция"});
    final ModeSetting mode = new ModeSetting("Мод", "Призраки", new String[]{"Квадрат", "Призраки"});
    private final BooleanSetting checkWallObstruction = new BooleanSetting("Не бить через стену", true);
    private LivingEntity currentTarget;
    final ModeListSetting targets = new ModeListSetting("Таргеты", new BooleanSetting[]{new BooleanSetting("Игроки", true), new BooleanSetting("Голые", true), new BooleanSetting("Мобы", false), new BooleanSetting("Животные", false), new BooleanSetting("Друзья", false), new BooleanSetting("Голые невидимки", true), new BooleanSetting("Невидимки", true)});
    final BooleanSetting correctionType = new BooleanSetting("Незаметная", true);
    private final BooleanSetting el = new BooleanSetting("ЭлитраТаргет", false);
    private final BooleanSetting elspeed = new BooleanSetting("Ускорение на элитре", false);
    private final SliderSetting rotateDistanceElytra = (new SliderSetting("Дистанция наводки на Элитре", 1.0F, 0.0F, 30.0F, 0.1F)).setVisible(() -> {
        return (Boolean)this.el.get();
    });
    final BooleanSetting correctionType7 = new BooleanSetting("Только криты", true);
    final BooleanSetting correctionType6 = new BooleanSetting("Ломать щит", true);
    final BooleanSetting correctionType5 = new BooleanSetting("Отжимать щит", true);
    final BooleanSetting correctionType4 = new BooleanSetting("Ускорять атаку", false);
    final BooleanSetting correctionType3 = new BooleanSetting("Атака с ТПС", false);
    final BooleanSetting correctionType2 = new BooleanSetting("Одна цель", true);
    final BooleanSetting correctionType1 = new BooleanSetting("Коррекция движения", true);
    private final BooleanSetting inventoryBlock = new BooleanSetting("Inventory Block", true);
    private final StopWatch stopWatch = new StopWatch();
    private Vector2f rotateVector = new Vector2f(0.0F, 0.0F);
    private LivingEntity target;
    private Entity selected;
    int ticks = 0;
    boolean isRotated;
    private PlayerEntity entity;
    public static long startTime = System.currentTimeMillis();
    private final Animation alpha = new DecelerateAnimation(600, 255.0);
    float lastYaw;
    float lastPitch;

    public KillAura(AutoPotion autopotion) {
        this.addSettings(new Setting[]{this.type, this.attackRange, this.targets, this.targetMode, this.mode, this.correctionType, this.checkWallObstruction, this.correctionType1, this.correctionType2, this.correctionType3, this.correctionType4, this.correctionType5, this.correctionType6, this.correctionType7, this.inventoryBlock});
    }

    @Subscribe
    public void Input(EventInput eventInput) {
        if ((Boolean)this.correctionType1.get() && (Boolean)this.correctionType.get() && this.target != null) {
            Minecraft var10000 = mc;
            if (Minecraft.player != null) {
                MoveUtils.fixMovement(eventInput, this.rotateVector.x);
            }
        }

    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (!(Boolean)this.inventoryBlock.get() || mc.currentScreen == null) {
            if ((Boolean)this.correctionType2.get() && (this.target == null || !this.isValid(this.target)) || !(Boolean)this.correctionType2.get()) {
                this.updateTarget();
            }

            if (this.target != null) {
                this.isRotated = false;
                if (this.shouldPlayerFalling() && this.stopWatch.hasTimeElapsed()) {
                    this.updateAttack();
                    this.ticks = 2;
                }

                if (this.type.is("Резкая")) {
                    if (this.ticks > 0) {
                        this.updateRotation(true, 180.0F, 90.0F);
                        --this.ticks;
                    } else {
                        this.reset();
                    }
                } else if (!this.isRotated) {
                    this.updateRotation(false, 80.0F, 35.0F);
                }
            } else {
                this.stopWatch.setLastMS(0L);
                this.reset();
            }

        }
    }

    @Subscribe
    private void onWalking(EventMotion e) {
        if (this.target != null) {
            float yaw = this.rotateVector.x;
            float pitch = this.rotateVector.y;
            e.setYaw(yaw);
            e.setPitch(pitch);
            Minecraft var10000 = mc;
            Minecraft.player.rotationYawHead = yaw;
            var10000 = mc;
            Minecraft.player.renderYawOffset = yaw;
            var10000 = mc;
            Minecraft.player.rotationPitchHead = pitch;
        }
    }

    private void updateTarget() {
        List<LivingEntity> potentialTargets = new ArrayList();
        Minecraft var10000 = mc;
        Iterator var2 = Minecraft.world.getAllEntities().iterator();

        while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity instanceof LivingEntity living) {
                if (this.isValid(living)) {
                    potentialTargets.add(living);
                }
            }
        }

        if (potentialTargets.isEmpty()) {
            this.target = null;
        } else {
            switch ((String)this.targetMode.get()) {
                case "Здоровье":
                    potentialTargets.sort(Comparator.comparingDouble(this::getEntityHealth));
                    break;
                case "Дистанция":
                    Minecraft var10001 = mc;
                    ClientPlayerEntity var7 = Minecraft.player;
                    Objects.requireNonNull(var7);
                    potentialTargets.sort(Comparator.comparingDouble(var7::getDistance));
            }

            this.target = (LivingEntity)potentialTargets.get(0);
        }
    }

    private void updateRotation(boolean attack, float rotationYawSpeed, float rotationPitchSpeed) {
        Vector3d var10000 = this.target.getPositionVec();
        Minecraft var10002 = mc;
        double var17 = Minecraft.player.getPosYEye() - this.target.getPosY();
        double var10004 = (double)this.target.getHeight();
        Minecraft var10005 = mc;
        var10000 = var10000.add(0.0, MathHelper.clamp(var17, 0.0, var10004 * (Minecraft.player.getDistanceEyePos(this.target) / (double)(Float)this.attackRange.get())), 0.0);
        Minecraft var10001 = mc;
        Vector3d vec = var10000.subtract(Minecraft.player.getEyePosition(1.0F));
        this.isRotated = true;
        float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(vec.y, Math.hypot(vec.x, vec.z))));
        float yawDelta = MathHelper.wrapDegrees(yawToTarget - this.rotateVector.x);
        float pitchDelta = MathHelper.wrapDegrees(pitchToTarget - this.rotateVector.y);
        switch ((String)this.type.get()) {
            case "Плавная":
                float clampedYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0F), rotationYawSpeed);
                float clampedPitch = Math.min(Math.max(Math.abs(pitchDelta), 1.0F), rotationPitchSpeed);
                if (attack && this.selected != this.target && (Boolean)this.correctionType4.get()) {
                    clampedPitch = Math.max(Math.abs(pitchDelta), 1.0F);
                } else {
                    clampedPitch /= 3.0F;
                }

                if (Math.abs(clampedYaw - this.lastYaw) <= 3.0F) {
                    clampedYaw = 0.0F;
                }

                float yaw = this.rotateVector.x + (yawDelta > 0.0F ? clampedYaw : -clampedYaw);
                float pitch = MathHelper.clamp(this.rotateVector.y + (pitchDelta > 0.0F ? clampedPitch : -clampedPitch), -89.0F, 89.0F);
                float gcd = SensUtils.getGCDValue();
                yaw -= (yaw - this.rotateVector.x) % gcd;
                pitch -= (pitch - this.rotateVector.y) % gcd;
                this.rotateVector = new Vector2f(yaw, pitch);
                this.lastYaw = clampedYaw;
                this.lastPitch = clampedPitch;
                if ((Boolean)this.correctionType1.get()) {
                    Minecraft var16 = mc;
                    Minecraft.player.rotationYawOffset = yaw;
                }
            default:
        }
    }

    private void updateAttack() {
        Minecraft var10000;
        Minecraft var10001;
        if (this.shouldPerformCriticalHit()) {
            var10000 = mc;
            Minecraft.player.fallDistance = 0.0F;
            var10000 = mc;
            Minecraft.player.isOnGround();
            this.stopWatch.setLastMS(475L);
            var10001 = mc;
            mc.playerController.attackEntity(Minecraft.player, this.target);
            var10000 = mc;
            Minecraft.player.swingArm(Hand.MAIN_HAND);
        } else if (!(Boolean)this.inventoryBlock.get() || mc.currentScreen == null) {
            if (!(Boolean)this.checkWallObstruction.get() || this.canSeeThroughWall(this.target)) {
                this.selected = MouseUtil.getMouseOver(this.target, this.rotateVector.x, this.rotateVector.y, (double)(Float)this.attackRange.get());
                if ((Boolean)this.correctionType4.get()) {
                    this.updateRotation(true, 60.0F, 35.0F);
                }

                if (this.selected == null || this.selected != this.target) {
                    var10000 = mc;
                    if (!Minecraft.player.isElytraFlying()) {
                        return;
                    }
                }

                var10000 = mc;
                if (Minecraft.player.isBlocking() && (Boolean)this.correctionType5.get()) {
                    var10001 = mc;
                    mc.playerController.onStoppedUsingItem(Minecraft.player);
                }

                var10000 = mc;
                LivingEntity var2;
                PlayerEntity player;
                if (!Minecraft.player.isPotionActive(Effects.WEAKNESS)) {
                    var10000 = mc;
                    if (!Minecraft.player.isPotionActive(Effects.MINING_FATIGUE)) {
                        var10000 = mc;
                        if (Minecraft.player.getLastDamageSource() == null) {
                            this.stopWatch.setLastMS(475L);
                            var10001 = mc;
                            mc.playerController.attackEntity(Minecraft.player, this.target);
                            var10000 = mc;
                            Minecraft.player.swingArm(Hand.MAIN_HAND);
                            var2 = this.target;
                            if (var2 instanceof PlayerEntity) {
                                player = (PlayerEntity)var2;
                                if ((Boolean)this.correctionType6.get()) {
                                    this.breakShieldPlayer(player);
                                    return;
                                }
                            }

                            return;
                        }
                    }
                }

                this.stopWatch.setLastMS(475L);
                var10001 = mc;
                mc.playerController.attackEntity(Minecraft.player, this.target);
                var10000 = mc;
                Minecraft.player.swingArm(Hand.MAIN_HAND);
                var2 = this.target;
                if (var2 instanceof PlayerEntity) {
                    player = (PlayerEntity)var2;
                    if ((Boolean)this.correctionType6.get()) {
                        this.breakShieldPlayer(player);
                    }
                }
            }

        }
    }

    private boolean shouldPerformCriticalHit() {
        Minecraft var10000 = mc;
        boolean var1;
        if (!Minecraft.player.isOnGround()) {
            var10000 = mc;
            if (!Minecraft.player.isInLava()) {
                var10000 = mc;
                if (!Minecraft.player.isInWater()) {
                    var10000 = mc;
                    if (!Minecraft.player.isInBubbleColumn()) {
                        var10000 = mc;
                        if (!Minecraft.player.isOnLadder()) {
                            var10000 = mc;
                            if (!Minecraft.player.isPotionActive(Effects.BLINDNESS)) {
                                var10000 = mc;
                                if (Minecraft.player.fallDistance > 0.0F) {
                                    var10000 = mc;
                                    if (!Minecraft.player.isSprinting()) {
                                        var1 = true;
                                        return var1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var1 = false;
        return var1;
    }

    private boolean canSeeThroughWall(Entity entity) {
        Minecraft var10000 = mc;
        ClientWorld clientWorld = Minecraft.world;
        var10000 = mc;
        Vector3d var4 = Minecraft.player.getEyePosition(1.0F);
        Vector3d var10004 = entity.getEyePosition(1.0F);
        Minecraft var10007 = mc;
        RayTraceResult result = clientWorld.rayTraceBlocks(new RayTraceContext(var4, var10004, BlockMode.COLLIDER, FluidMode.NONE, Minecraft.player));
        return result == null || result.getType() == Type.MISS;
    }

    private boolean shouldPlayerFalling() {
        Minecraft var10000;
        boolean var4;
        label46: {
            label45: {
                var10000 = mc;
                if (Minecraft.player.isInWater()) {
                    var10000 = mc;
                    if (Minecraft.player.areEyesInFluid(FluidTags.WATER)) {
                        break label45;
                    }
                }

                var10000 = mc;
                if (!Minecraft.player.isInLava()) {
                    var10000 = mc;
                    if (!Minecraft.player.isOnLadder()) {
                        var10000 = mc;
                        if (!Minecraft.player.isPassenger()) {
                            var10000 = mc;
                            if (!Minecraft.player.abilities.isFlying) {
                                var4 = false;
                                break label46;
                            }
                        }
                    }
                }
            }

            var4 = true;
        }

        boolean cancelReason = var4;
        float tpsAdjustment = (Boolean)this.correctionType3.get() ? Expensive.getInstance().getTpsCalc().getAdjustTicks() : 1.5F;
        var10000 = mc;
        float attackStrength = Minecraft.player.getCooledAttackStrength(tpsAdjustment);
        if (attackStrength < 0.92F) {
            return false;
        } else if (!cancelReason && (Boolean)this.correctionType7.get()) {
            var10000 = mc;
            if (!Minecraft.player.isOnGround()) {
                var10000 = mc;
                if (Minecraft.player.fallDistance > 0.0F) {
                    var4 = true;
                    return var4;
                }
            }

            var4 = false;
            return var4;
        } else {
            return true;
        }
    }

    private boolean isValid(LivingEntity entity) {
        if (entity instanceof ClientPlayerEntity) {
            return false;
        } else if (entity.ticksExisted < 3) {
            return false;
        } else {
            Minecraft var10000 = mc;
            if (Minecraft.player.getDistanceEyePos(entity) > (double)(Float)this.attackRange.get()) {
                return false;
            } else {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity p = (PlayerEntity)entity;
                    if (AntiBot.isBot(entity)) {
                        return false;
                    }

                    if (!(Boolean)this.targets.getValueByName("Друзья").get() && FriendStorage.isFriend(p.getName().getString())) {
                        return false;
                    }

                    String var3 = p.getName().getString();
                    Minecraft var10001 = mc;
                    if (var3.equalsIgnoreCase(Minecraft.player.getName().getString())) {
                        return false;
                    }
                }

                if (entity instanceof PlayerEntity && !(Boolean)this.targets.getValueByName("Игроки").get()) {
                    return false;
                } else if (entity instanceof PlayerEntity && entity.getTotalArmorValue() == 0 && !(Boolean)this.targets.getValueByName("Голые").get()) {
                    return false;
                } else if (entity instanceof PlayerEntity && entity.isInvisible() && entity.getTotalArmorValue() == 0 && !(Boolean)this.targets.getValueByName("Голые невидимки").get()) {
                    return false;
                } else if (entity instanceof PlayerEntity && entity.isInvisible() && !(Boolean)this.targets.getValueByName("Невидимки").get()) {
                    return false;
                } else if (entity instanceof MonsterEntity && !(Boolean)this.targets.getValueByName("Мобы").get()) {
                    return false;
                } else if (entity instanceof AnimalEntity && !(Boolean)this.targets.getValueByName("Животные").get()) {
                    return false;
                } else {
                    double var4;
                    double var5;
                    float var6;
                    label84: {
                        var10000 = mc;
                        var4 = Minecraft.player.getDistanceEyePos(entity);
                        var5 = (double)(Float)this.attackRange.get() + (double)(Float)this.rotateDistanceElytra.get();
                        if ((Boolean)this.el.get()) {
                            Minecraft var10002 = mc;
                            if (Minecraft.player.isElytraFlying()) {
                                var6 = (Float)this.rotateDistanceElytra.get();
                                break label84;
                            }
                        }

                        var6 = 0.0F;
                    }

                    if (var4 > var5 + (double)var6) {
                        return false;
                    } else {
                        return !entity.isInvulnerable() && entity.isAlive() && !(entity instanceof ArmorStandEntity);
                    }
                }
            }
        }
    }

    private void breakShieldPlayer() {
        this.breakShieldPlayer((PlayerEntity)null);
    }

    private void breakShieldPlayer(PlayerEntity entity) {
        this.entity = entity;
        if (entity.isBlocking()) {
            int invSlot = InventoryUtil.getInstance().getAxeInInventory(false);
            int hotBarSlot = InventoryUtil.getInstance().getAxeInInventory(true);
            Minecraft var10000;
            Minecraft var10001;
            int i;
            if (hotBarSlot == -1 && invSlot != -1) {
                i = InventoryUtil.getInstance().findBestSlotInHotBar();
                Minecraft var10005 = mc;
                mc.playerController.windowClick(0, invSlot, 0, ClickType.PICKUP, Minecraft.player);
                int var10002 = i + 36;
                var10005 = mc;
                mc.playerController.windowClick(0, var10002, 0, ClickType.PICKUP, Minecraft.player);
                var10005 = mc;
                mc.playerController.windowClick(0, invSlot, 0, ClickType.PICKUP, Minecraft.player);

                for(int i1 = 0; i < 5; ++i) {
                    var10001 = mc;
                    mc.playerController.attackEntity(Minecraft.player, entity);
                    var10000 = mc;
                    Minecraft.player.swingArm(Hand.MAIN_HAND);
                }

                var10002 = i + 36;
                var10005 = mc;
                mc.playerController.windowClick(0, var10002, 0, ClickType.PICKUP, Minecraft.player);
                var10005 = mc;
                mc.playerController.windowClick(0, invSlot, 0, ClickType.PICKUP, Minecraft.player);
            } else if (hotBarSlot != -1) {
                var10000 = mc;
                Minecraft.player.inventory.currentItem = hotBarSlot;

                for(i = 0; i < 5; ++i) {
                    var10001 = mc;
                    mc.playerController.attackEntity(Minecraft.player, entity);
                    var10000 = mc;
                    Minecraft.player.swingArm(Hand.MAIN_HAND);
                }
            }
        }

    }

    private void reset() {
        if ((Boolean)this.correctionType1.get()) {
            Minecraft var10000 = mc;
            Minecraft.player.rotationYawOffset = -2.14748365E9F;
        }

        Minecraft var10003 = mc;
        Minecraft var10004 = mc;
        this.rotateVector = new Vector2f(Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
    }

    public boolean onEnable() {
        super.onEnable();
        this.reset();
        this.target = null;
        return false;
    }

    public void onDisable() {
        super.onDisable();
        this.reset();
        this.stopWatch.setLastMS(0L);
        this.target = null;
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        if (this.mode.is("Квадрат")) {
            if (e.getType() != im.expensive.events.EventDisplay.Type.PRE) {
                return;
            }

            if (this.currentTarget != null) {
                Minecraft var10001 = mc;
                if (this.currentTarget != Minecraft.player) {
                    double sin = Math.sin((double)System.currentTimeMillis() / 1000.0);
                    float size = 140.0F;
                    Vector3d interpolated = this.currentTarget.getPositon(e.getPartialTicks());
                    Vector2f pos = ProjectionUtil.project(interpolated.x, interpolated.y + (double)(this.currentTarget.getHeight() / 2.0F), interpolated.z);
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef(pos.x, pos.y, 0.0F);
                    GlStateManager.rotatef((float)sin * 360.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translatef(-pos.x, -pos.y, 0.0F);
                    if (pos != null) {
                        DisplayUtils.drawImageAlpha(new ResourceLocation("expensive/images/target.png"), pos.x - size / 2.0F, pos.y - size / 2.0F, size, size, new Vector4i(ColorUtils.setAlpha(HUD.getColor(0, 1.0F), (int)this.alpha.getOutput()), ColorUtils.setAlpha(HUD.getColor(90, 1.0F), (int)this.alpha.getOutput()), ColorUtils.setAlpha(HUD.getColor(180, 1.0F), (int)this.alpha.getOutput()), ColorUtils.setAlpha(HUD.getColor(270, 1.0F), (int)this.alpha.getOutput())));
                        GlStateManager.popMatrix();
                    }
                }
            }
        }

    }

    @Subscribe
    private void onEvent(EventUpdate eventUpdate) {
        boolean bl = Expensive.getInstance().getFunctionRegistry().getKillAura().isState();
        if (this.target != null) {
            this.currentTarget = this.target;
        }

        this.alpha.setDirection(bl && this.target != null ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    private double getEntityArmor(PlayerEntity entityPlayer2) {
        double d2 = 0.0;

        for(int i2 = 0; i2 < 4; ++i2) {
            ItemStack is = (ItemStack)entityPlayer2.inventory.armorInventory.get(i2);
            if (is.getItem() instanceof ArmorItem) {
                d2 += this.getProtectionLvl(is);
            }
        }

        return d2;
    }

    private double getProtectionLvl(ItemStack stack) {
        Item var3 = stack.getItem();
        if (var3 instanceof ArmorItem i) {
            double damageReduceAmount = (double)i.getDamageReduceAmount();
            if (stack.isEnchanted()) {
                damageReduceAmount += (double)EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack) * 0.25;
            }

            return damageReduceAmount;
        } else {
            return 0.0;
        }
    }

    private double getEntityHealth(LivingEntity ent) {
        if (ent instanceof PlayerEntity player) {
            return (double)(player.getHealth() + player.getAbsorptionAmount()) * (this.getEntityArmor(player) / 20.0);
        } else {
            return (double)(ent.getHealth() + ent.getAbsorptionAmount());
        }
    }

    public ModeSetting getType() {
        return this.type;
    }

    public BooleanSetting getCorrectionType() {
        return this.correctionType;
    }

    public BooleanSetting getEl() {
        return this.el;
    }

    public BooleanSetting getElspeed() {
        return this.elspeed;
    }

    public StopWatch getStopWatch() {
        return this.stopWatch;
    }

    public Vector2f getRotateVector() {
        return this.rotateVector;
    }

    public LivingEntity getTarget() {
        return this.target;
    }
}
