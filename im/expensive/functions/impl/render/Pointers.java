/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.player.MoveUtils;
import im.expensive.utils.player.PlayerUtils;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.GaussinBlur;
import im.expensive.utils.render.font.Fonts;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="Arrows", type=Category.Render)
public class Pointers
extends Function {
    public ModeListSetting remove = new ModeListSetting("\u0423\u0431\u0440\u0430\u0442\u044c", new BooleanSetting("\u041c\u0435\u0442\u0440\u044b", false));
    private final ModeSetting type = new ModeSetting("\u0422\u0438\u043f", "\u041a\u0440\u0430\u0441\u043d\u044b\u0435", "\u041a\u0440\u0430\u0441\u043d\u044b\u0435", "RGB", "\u0411\u0435\u043b\u044b\u0435");
    private final BooleanSetting toggleArrows = new BooleanSetting("\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0441\u0442\u0440\u0435\u043b\u043e\u0447\u043a\u0438", true);
    private final BooleanSetting fixedSize = new BooleanSetting("\u0424\u0438\u043a\u0441\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0439 \u0440\u0430\u0437\u043c\u0435\u0440", false);
    public float animationStep;
    private GaussinBlur GaussianBlur;
    private final SliderSetting horizontal = new SliderSetting("\u041f\u043e \u0433\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u0438", 1.0f, 1.0f, 2.0f, 0.1f);
    Vector4i friendColors = new Vector4i(HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 0, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 90, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 180, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 270, 1.0f));
    private float lastYaw;
    private float lastPitch;
    private float animatedYaw;
    private float animatedPitch;

    public Pointers() {
        this.toggle();
        this.addSettings(this.remove, this.type, this.horizontal, this.toggleArrows, this.fixedSize);
    }

    @Subscribe
    public void onDisplay(EventDisplay e) {
        block20: {
            block19: {
                if (Minecraft.player == null) break block19;
                if (Minecraft.world != null && e.getType() == EventDisplay.Type.PRE) break block20;
            }
            return;
        }
        if (!((Boolean)this.toggleArrows.get()).booleanValue()) {
            return;
        }
        float size = 40.0f;
        if (Pointers.mc.currentScreen instanceof InventoryScreen) {
            size += 80.0f;
        }
        if (!((Boolean)this.fixedSize.get()).booleanValue() && MoveUtils.isMoving()) {
            size += 10.0f;
        }
        this.animationStep = MathUtil.fast(this.animationStep, size, 6.0f);
        for (AbstractClientPlayerEntity player : Minecraft.world.getPlayers()) {
            if (!PlayerUtils.isNameValid(player.getNameClear())) continue;
            if (Minecraft.player == player) continue;
            double x = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * (double)mc.getRenderPartialTicks() - Pointers.mc.getRenderManager().info.getProjectedView().getX();
            double z = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * (double)mc.getRenderPartialTicks() - Pointers.mc.getRenderManager().info.getProjectedView().getZ();
            double cos2 = MathHelper.cos((float)((double)Pointers.mc.getRenderManager().info.getYaw() * (Math.PI / 180)));
            double sin2 = MathHelper.sin((float)((double)Pointers.mc.getRenderManager().info.getYaw() * (Math.PI / 180)));
            double rotY = -(z * cos2 - x * sin2);
            double rotX = -(x * cos2 + z * sin2);
            float angle = (float)(Math.atan2(rotY, rotX) * 180.0 / Math.PI);
            double x2 = this.animationStep * (float)((Float)this.horizontal.get()).intValue() * MathHelper.cos((float)Math.toRadians(angle)) + (float)window.getScaledWidth() / 2.0f;
            double y2 = this.animationStep * (float)((Float)this.horizontal.get()).intValue() * MathHelper.sin((float)Math.toRadians(angle)) + (float)window.getScaledHeight() / 2.0f;
            if (!((Boolean)this.fixedSize.get()).booleanValue()) {
                x2 += (double)this.animatedYaw;
                y2 += (double)this.animatedPitch;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableBlend();
            GlStateManager.translated(x2, y2, 0.0);
            GlStateManager.rotatef(angle, 0.0f, 0.0f, 1.0f);
            int color = FriendStorage.isFriend(player.getGameProfile().getName()) ? FriendStorage.getColor() : ColorUtils.rgb(228, 15, 15);
            int colorRGBA = FriendStorage.isFriend(player.getGameProfile().getName()) ? FriendStorage.getColor() : ColorUtils.getColor(90);
            int colorWHITE = FriendStorage.isFriend(player.getGameProfile().getName()) ? FriendStorage.getColor() : ColorUtils.rgba(255, 255, 255, 255);
            Vector3d playerPos = new Vector3d(player.getPosX(), player.getPosY(), player.getPosZ());
            Vector3d myPos = Minecraft.player.getPositionVec();
            double distance = playerPos.distanceTo(myPos);
            String distanceString = String.format("%.0f", distance) + "m";
            GL11.glPushMatrix();
            Pointers.enableSmoothLine(1.0f);
            GL11.glRotatef(350.0f, 3.0f, 0.0f, 1.0f);
            if (!((Boolean)this.remove.getValueByName("\u041c\u0435\u0442\u0440\u044b").get()).booleanValue()) {
                GlStateManager.rotatef(-angle, 0.0f, 0.0f, 1.0f);
                Fonts.sfMedium.drawText(e.getMatrixStack(), distanceString, -3.0f, 5.0f, -1, 5.0f, 0.05f);
                GlStateManager.rotatef(angle, 0.0f, 0.0f, 1.0f);
            }
            Pointers.disableSmoothLine();
            GL11.glEnable(3553);
            GL11.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);
            GL11.glPopMatrix();
            ResourceLocation logo = new ResourceLocation("expensive/images/hud/triangle.png");
            switch ((String)this.type.get()) {
                case "\u041a\u0440\u0430\u0441\u043d\u044b\u0435": {
                    DisplayUtils.drawImage(logo, -3.0f, -5.0f, 18.0f, 17.0f, color);
                    break;
                }
                case "RGB": {
                    DisplayUtils.drawImageAlpha1(logo, -3.0f, -5.0f, 18.0f, 17.0f, new Vector4i(ColorUtils.setAlpha(colorRGBA, 255), ColorUtils.setAlpha(colorRGBA, 255), ColorUtils.setAlpha(colorRGBA, 255), ColorUtils.setAlpha(colorRGBA, 255)));
                    break;
                }
                case "\u0411\u0435\u043b\u044b\u0435": {
                    DisplayUtils.drawImage(logo, -3.0f, -5.0f, 18.0f, 17.0f, colorWHITE);
                }
            }
            Pointers.drawTriangle(-3.0f, 0.0f, 3.0f, 5.0f, new Color(color));
            GlStateManager.enableBlend();
            GlStateManager.popMatrix();
        }
        this.lastYaw = Minecraft.player.rotationYaw;
        this.lastPitch = Minecraft.player.rotationPitch;
    }

    public static void drawTriangle(float x, float y, float width, float height, Color color) {
        GL11.glPushMatrix();
        Pointers.enableSmoothLine(1.0f);
        GL11.glRotatef(330.0f, 0.0f, 0.0f, 1.0f);
        Pointers.disableSmoothLine();
        GL11.glEnable(3553);
        GL11.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);
        GL11.glPopMatrix();
    }

    private static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
    }

    private static void disableSmoothLine() {
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4354);
    }
}

