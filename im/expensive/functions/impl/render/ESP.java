/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import im.expensive.command.friends.FriendStorage;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.AntiBot;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ColorSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

@FunctionRegister(name="ESP", type=Category.Render)
public class ESP
extends Function {
    public ModeListSetting remove = new ModeListSetting("\u0423\u0431\u0440\u0430\u0442\u044c", new BooleanSetting("\u0411\u043e\u043a\u0441\u044b", false), new BooleanSetting("\u041f\u043e\u043b\u043e\u0441\u043a\u0443 \u0445\u043f", false), new BooleanSetting("\u0422\u0435\u043a\u0441\u0442 \u0445\u043f", false), new BooleanSetting("\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u044f", false), new BooleanSetting("\u0421\u043f\u0438\u0441\u043e\u043a \u044d\u0444\u0444\u0435\u043a\u0442\u043e\u0432", true), new BooleanSetting("\u0421\u0444\u0435\u0440\u044b", true), new BooleanSetting("\u041f\u0440\u0435\u0434\u043c\u0435\u0442\u044b", false));
    public ModeSetting boxMode = new ModeSetting("\u0412\u0438\u0434 \u0431\u043e\u043a\u0441\u043e\u0432", "\u0411\u043e\u043a\u0441", "\u0423\u0433\u043b\u044b", "\u0411\u043e\u043a\u0441").setVisible(() -> (Boolean)this.remove.getValueByName("\u0411\u043e\u043a\u0441\u044b").get());
    private final HashMap<Entity, Vector4f> positions = new HashMap();
    public ColorSetting color = new ColorSetting("Color", -1);

    public ESP() {
        this.toggle();
        this.addSettings(this.remove, this.boxMode);
    }

    @Subscribe
    public void onDisplay(EventDisplay e) {
        if (Minecraft.world == null || e.getType() != EventDisplay.Type.PRE) {
            return;
        }
        this.positions.clear();
        Vector4i colors = new Vector4i(HUD.getColor(0, 1.0f), HUD.getColor(90, 1.0f), HUD.getColor(180, 1.0f), HUD.getColor(270, 1.0f));
        Vector4i friendColors = new Vector4i(HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 0, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 90, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 180, 1.0f), HUD.getColor(ColorUtils.rgb(144, 238, 144), ColorUtils.rgb(0, 139, 0), 270, 1.0f));
        for (Entity entity2 : Minecraft.world.getAllEntities()) {
            if (!this.isValid(entity2) || !(entity2 instanceof PlayerEntity) && !(entity2 instanceof ItemEntity)) continue;
            if (entity2 == Minecraft.player && ESP.mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) continue;
            double x = MathUtil.interpolate(entity2.getPosX(), entity2.lastTickPosX, (double)e.getPartialTicks());
            double y = MathUtil.interpolate(entity2.getPosY(), entity2.lastTickPosY, (double)e.getPartialTicks());
            double z = MathUtil.interpolate(entity2.getPosZ(), entity2.lastTickPosZ, (double)e.getPartialTicks());
            Vector3d size = new Vector3d(entity2.getBoundingBox().maxX - entity2.getBoundingBox().minX, entity2.getBoundingBox().maxY - entity2.getBoundingBox().minY, entity2.getBoundingBox().maxZ - entity2.getBoundingBox().minZ);
            AxisAlignedBB aabb = new AxisAlignedBB(x - size.x / 2.0, y, z - size.z / 2.0, x + size.x / 2.0, y + size.y, z + size.z / 2.0);
            Vector4f position = null;
            for (int i = 0; i < 8; ++i) {
                Vector2f vector = ProjectionUtil.project(i % 2 == 0 ? aabb.minX : aabb.maxX, i / 2 % 2 == 0 ? aabb.minY : aabb.maxY, i / 4 % 2 == 0 ? aabb.minZ : aabb.maxZ);
                if (position == null) {
                    position = new Vector4f(vector.x, vector.y, 1.0f, 1.0f);
                    continue;
                }
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }
            this.positions.put(entity2, position);
        }
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        for (Map.Entry entry : this.positions.entrySet()) {
            Entity entity1 = (Entity)entry.getKey();
            Vector4f position1 = (Vector4f)entry.getValue();
            Vector4f position = (Vector4f)entry.getValue();
            Object z = entry.getKey();
            if (!(z instanceof LivingEntity)) continue;
            LivingEntity entity3 = (LivingEntity)z;
            if (((Boolean)this.remove.getValueByName("\u0411\u043e\u043a\u0441\u044b").get()).booleanValue() && this.boxMode.is("\u0411\u043e\u043a\u0441")) {
                DisplayUtils.drawBox(position.x - 0.5f, position.y - 0.5f, position.z + 0.5f, position.w + 0.5f, 2.0, ColorUtils.rgba(0, 0, 0, 128));
                DisplayUtils.drawBoxTest(position.x, position.y, position.z, position.w, 1.0, FriendStorage.isFriend(entity3.getName().getString()) ? friendColors : colors);
            }
            float sect = 10.0f;
            float lineWidth = 1.0f;
            double playerX = Minecraft.player.getPosX();
            double playerY = Minecraft.player.getPosY();
            double playerZ = Minecraft.player.getPosZ();
            double entityX = position.x;
            double entityY = position.y;
            double entityZ = position.z;
            double distance = Math.sqrt(Math.pow(playerX - entityX, 2.0) + Math.pow(playerY - entityY, 2.0) + Math.pow(playerZ - entityZ, 2.0));
            float sectChange = (float)(distance / 10.0);
            double calcSect = sect - sectChange;
            calcSect = Math.max(3.0, calcSect);
            int friendColorInt = FriendStorage.getColor();
            int colorInt = HUD.getColor(270);
            if (((Boolean)this.remove.getValueByName("\u0411\u043e\u043a\u0441\u044b").get()).booleanValue() && this.boxMode.is("\u0423\u0433\u043b\u044b")) {
                double x = position.x;
                double y = position.y;
                double endX = position.z;
                double endY = position.w;
                int back = ColorUtils.rgba(0, 0, 0, 128);
                int getColor = FriendStorage.isFriend(entity3.getName().getString()) ? friendColorInt : colorInt;
                double distanceX = endX - x;
                double distanceY = endY - y;
                double percentage = 0.1;
                double calcSectX = distanceX * percentage;
                double calcSectY = distanceY * percentage;
                ESP.drawMcRect(x - 0.5, y - 0.5, x + calcSectX, y + 1.0, getColor);
                ESP.drawMcRect(endX - calcSectX, y - 0.5, endX + 0.5, y + 1.0, getColor);
                ESP.drawMcRect(x - 0.5, endY - 0.5, x + calcSectX, endY + 1.0, getColor);
                ESP.drawMcRect(endX - calcSectX, endY - 0.5, endX + 0.5, endY + 1.0, getColor);
                ESP.drawMcRect(x - 0.5, y + 1.0, x + 1.0, y + calcSectY, getColor);
                ESP.drawMcRect(x - 0.5, endY - calcSectY, x + 1.0, endY, getColor);
                ESP.drawMcRect(endX - 0.5, y + 1.0, endX + 1.0, y + calcSectY, getColor);
                ESP.drawMcRect(endX - 0.5, endY - calcSectY, endX + 1.0, endY, getColor);
            }
            float hpOffset = 2.0f;
            float out = 0.5f;
            if (((Boolean)this.remove.getValueByName("\u041f\u043e\u043b\u043e\u0441\u043a\u0443 \u0445\u043f").get()).booleanValue()) continue;
            String header = ESP.mc.ingameGUI.getTabList().header == null ? " " : ESP.mc.ingameGUI.getTabList().header.getString().toLowerCase();
            Score score = Minecraft.world.getScoreboard().getOrCreateScore(entity3.getScoreboardName(), Minecraft.world.getScoreboard().getObjectiveInDisplaySlot(2));
            float hp = entity3.getHealth();
            float maxHp = entity3.getMaxHealth();
            if (mc.getCurrentServerData() != null && ESP.mc.getCurrentServerData().serverIP.contains("funtime") && (header.contains("\u0430\u043d\u0430\u0440\u0445\u0438\u044f") || header.contains("\u0433\u0440\u0438\u0444\u0435\u0440\u0441\u043a\u0438\u0439"))) {
                hp = score.getScorePoints();
                maxHp = 20.0f;
            }
            int color = 20;
            if (hp >= 20.0f) {
                color = new Color(2, 255, 2, 255).getRGB();
            } else if (hp >= 18.0f) {
                color = new Color(117, 148, 74, 255).getRGB();
            } else if (hp >= 16.0f) {
                color = new Color(148, 142, 74, 255).getRGB();
            } else if (hp >= 14.0f) {
                color = new Color(148, 118, 74, 255).getRGB();
            } else if (hp >= 12.0f) {
                color = new Color(148, 105, 74, 255).getRGB();
            } else if (hp >= 10.0f) {
                color = new Color(164, 92, 61, 255).getRGB();
            } else if (hp >= 8.0f) {
                color = new Color(159, 79, 50, 255).getRGB();
            } else if (hp >= 6.0f) {
                color = new Color(159, 66, 50, 255).getRGB();
            } else if (hp >= 4.0f) {
                color = new Color(185, 55, 40, 255).getRGB();
            } else if (hp >= 2.0f) {
                color = new Color(255, 26, 0, 255).getRGB();
            }
            DisplayUtils.drawMCVerticalBuilding(position.x - hpOffset - 3.0f, position.y + (position.w - position.y) * (1.0f - MathHelper.clamp(maxHp, 0.0f, 1.0f)), position.x - hpOffset - 2.0f, position.w, FriendStorage.isFriend(entity3.getName().getString()) ? friendColors.w : colors.w, FriendStorage.isFriend(entity3.getName().getString()) ? friendColors.x : ColorUtils.rgba(0, 0, 0, 255));
            DisplayUtils.drawMCVerticalBuilding(position.x - hpOffset - 3.0f, position.y + (position.w - position.y) * (1.0f - MathHelper.clamp(hp / maxHp, 0.0f, 1.0f)), position.x - hpOffset - 2.0f, position.w, FriendStorage.isFriend(entity3.getName().getString()) ? friendColors.w : color, FriendStorage.isFriend(entity3.getName().getString()) ? friendColors.x : color);
        }
        Tessellator.getInstance().draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        for (Map.Entry entry : this.positions.entrySet()) {
            Entity entity4 = (Entity)entry.getKey();
            if (entity4 instanceof LivingEntity) {
                String header;
                LivingEntity living = (LivingEntity)entity4;
                Score score = Minecraft.world.getScoreboard().getOrCreateScore(living.getScoreboardName(), Minecraft.world.getScoreboard().getObjectiveInDisplaySlot(2));
                float hp = living.getHealth();
                float maxHp = living.getMaxHealth();
                String string = header = ESP.mc.ingameGUI.getTabList().header == null ? " " : ESP.mc.ingameGUI.getTabList().header.getString().toLowerCase();
                if (mc.getCurrentServerData() != null && ESP.mc.getCurrentServerData().serverIP.contains("funtime") && (header.contains("\u0430\u043d\u0430\u0440\u0445\u0438\u044f") || header.contains("\u0433\u0440\u0438\u0444\u0435\u0440\u0441\u043a\u0438\u0439"))) {
                    hp = score.getScorePoints();
                    maxHp = 20.0f;
                }
                Vector4f position = (Vector4f)entry.getValue();
                float width = position.z - position.x;
                String hpText = (int)hp + "HP";
                float hpWidth = Fonts.consolas.getWidth(hpText, 6.0f);
                float hpPercent = MathHelper.clamp(hp / maxHp, 0.0f, 1.0f);
                float hpPosY = position.y + (position.w - position.y) * (1.0f - hpPercent);
                if (!((Boolean)this.remove.getValueByName("\u0422\u0435\u043a\u0441\u0442 \u0445\u043f").get()).booleanValue()) {
                    Fonts.consolas.drawText(e.getMatrixStack(), hpText, position.x - hpWidth - 8.0f, hpPosY, -1, 6.0f, 0.05f);
                }
                float length = ESP.mc.fontRenderer.getStringPropertyWidth(entity4.getDisplayName());
                float scaledLength = length * 0.6f;
                float padding = 4.0f;
                float backgroundWidth = scaledLength + padding * 2.0f;
                int backgroundColor = FriendStorage.isFriend(entity4.getName().getString()) ? ColorUtils.rgba(0, 100, 0, 120) : ColorUtils.rgba(10, 10, 10, 120);
                GL11.glPushMatrix();
                DisplayUtils.drawRoundedRect(position.x + width / 2.0f - backgroundWidth / 2.0f - 8.0f, position.y - 15.0f, backgroundWidth + 18.0f, 10.0f, 1.0f, backgroundColor);
                this.glCenteredScale(position.x + width / 2.0f - scaledLength / 2.0f, position.y - 30.0f, scaledLength, 10.0f, 0.6f);
                Object friendPrefix = FriendStorage.isFriend(entity4.getName().getString()) ? TextFormatting.GREEN + "[F] " : "";
                ITextComponent text = entity4.getDisplayName();
                TextComponent name = (TextComponent)text;
                name.append(new StringTextComponent(TextFormatting.GRAY + " [" + TextFormatting.GREEN + (int)hp + TextFormatting.GRAY + "]"));
                ESP.mc.fontRenderer.func_243246_a(e.getMatrixStack(), name, position.x + width / 2.0f - length / 2.0f - 10.0f, position.y - 4.0f, -1);
                GL11.glPopMatrix();
                if (entity4 instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity)entity4;
                    if (((Boolean)this.remove.getValueByName("\u0421\u0444\u0435\u0440\u044b").get()).booleanValue()) {
                        ItemStack stack = player.getHeldItemOffhand();
                        String nameS = "";
                        String itemName = stack.getDisplayName().getString();
                        if (stack.getItem() == Items.PLAYER_HEAD) {
                            String firstLore;
                            int levelIndex;
                            ListNBT lore;
                            CompoundNBT display;
                            CompoundNBT tag = stack.getTag();
                            if (tag != null && tag.contains("display", 10) && (display = tag.getCompound("display")).contains("Lore", 9) && !(lore = display.getList("Lore", 8)).isEmpty() && (levelIndex = (firstLore = lore.getString(0)).indexOf("\u0423\u0440\u043e\u0432\u0435\u043d\u044c")) != -1) {
                                String levelString = firstLore.substring(levelIndex + "\u0423\u0440\u043e\u0432\u0435\u043d\u044c".length()).trim();
                                String gat = levelString;
                                nameS = gat.contains("1/3") ? "- 1/3]" : (gat.contains("2/3") ? "- 2/3]" : (gat.contains("MAX") ? "- MAX]" : ""));
                            }
                            if (itemName.contains("\u041f\u0430\u043d\u0434\u043e")) {
                                itemName = "[PANDORA ";
                            } else if (itemName.contains("\u0410\u043f\u043e\u043b\u043b")) {
                                itemName = "[APOLLON ";
                            } else if (itemName.contains("\u0422\u0438\u0442")) {
                                itemName = "[TITANA ";
                            } else if (itemName.contains("\u041e\u0441\u0438\u0440")) {
                                itemName = "[OSIRIS ";
                            } else if (itemName.contains("\u0410\u043d\u0434\u0440\u043e")) {
                                itemName = "[ANDROMEDA";
                            } else if (itemName.contains("\u0425\u0438\u043c")) {
                                itemName = "[XIMERA ";
                            } else if (itemName.contains("\u0410\u0441\u0442\u0440")) {
                                itemName = "[ASTREYA ";
                            }
                            float textScale = 0.7f;
                            float height = 20.0f;
                            float scaledLength1 = (float)ESP.mc.fontRenderer.getStringPropertyWidth(ITextProperties.func_240652_a_(itemName + nameS)) * textScale;
                            float padding1 = 4.0f;
                            float backgroundWidth1 = scaledLength1 + padding1 * 2.0f;
                            float spherePadding = -30.0f;
                            float sphereDisplayX = position.x + (width - backgroundWidth1) / 2.0f;
                            float f = position.y + height;
                            Objects.requireNonNull(ESP.mc.fontRenderer);
                            float sphereDisplayY = f - 9.0f * textScale - padding - 15.0f;
                            GL11.glPushMatrix();
                            GL11.glScalef(textScale, textScale, textScale);
                            ESP.mc.fontRenderer.func_243246_a(e.getMatrixStack(), new StringTextComponent(itemName + nameS), (sphereDisplayX + backgroundWidth1 / 2.0f - scaledLength1 / 2.0f) / textScale, sphereDisplayY / textScale, ColorUtils.rgb(255, 14, 14));
                            GL11.glPopMatrix();
                        }
                    }
                }
                if (!((Boolean)this.remove.getValueByName("\u0421\u043f\u0438\u0441\u043e\u043a \u044d\u0444\u0444\u0435\u043a\u0442\u043e\u0432").get()).booleanValue()) {
                    this.drawPotions(e.getMatrixStack(), living, position.z + 4.0f, position.y);
                }
                this.drawItems(e.getMatrixStack(), living, (int)(position.x + width / 2.0f + 3.0f), (int)(position.y - 25.0f));
                continue;
            }
            if (!(entity4 instanceof ItemEntity)) continue;
            ItemEntity item = (ItemEntity)entity4;
            if (((Boolean)this.remove.getValueByName("\u041f\u0440\u0435\u0434\u043c\u0435\u0442\u044b").get()).booleanValue()) continue;
            Vector4f position = (Vector4f)entry.getValue();
            String displayName = entity4.getDisplayName().getString();
            int itemCount = item.getItem().getCount();
            String text = displayName + " x" + itemCount;
            float scale = 1.0f;
            float textWidth = (float)ESP.mc.fontRenderer.getStringWidth(text) * scale;
            float padding = 3.0f;
            GL11.glPushMatrix();
            float x = position.x - textWidth / 2.0f;
            float y = position.y - 7.0f;
            float backgroundWidth = textWidth + 2.0f * padding;
            float backgroundHeight = 9.0f * scale;
            DisplayUtils.drawRoundedRect(x - padding, y, backgroundWidth, backgroundHeight, 2.0f, ColorUtils.rgba(0, 0, 0, 120));
            Fonts.consolas.drawText(e.getMatrixStack(), text, x, y, ColorUtils.rgb(255, 255, 255), 9.0f * scale, 0.1f);
            GL11.glPopMatrix();
        }
    }

    public boolean isInView(Entity ent) {
        if (mc.getRenderViewEntity() == null) {
            return false;
        }
        WorldRenderer.frustum.setCameraPosition(ESP.mc.getRenderManager().info.getProjectedView().x, ESP.mc.getRenderManager().info.getProjectedView().y, ESP.mc.getRenderManager().info.getProjectedView().z);
        return WorldRenderer.frustum.isBoundingBoxInFrustum(ent.getBoundingBox()) || ent.ignoreFrustumCheck;
    }

    private void drawPotions(MatrixStack matrixStack, LivingEntity entity2, float posX, float posY) {
        for (EffectInstance pot : entity2.getActivePotionEffects()) {
            int amp = pot.getAmplifier();
            Object ampStr = "";
            if (amp >= 1 && amp <= 9) {
                ampStr = " " + I18n.format("enchantment.level." + (amp + 1), new Object[0]);
            }
            String text = I18n.format(pot.getEffectName(), new Object[0]) + (String)ampStr + " - " + EffectUtils.getPotionDurationString(pot, 1.0f);
            Fonts.consolas.drawText(matrixStack, text, posX, posY, -1, 6.0f, 0.05f);
            posY += Fonts.consolas.getHeight(6.0f);
        }
    }

    private void drawItems(MatrixStack matrixStack, LivingEntity entity2, int posX, int posY) {
        int size = 8;
        int padding = -3;
        int backgroundWidth = size;
        int backgroundHeight = size;
        int backgroundColor = ColorUtils.rgba(10, 10, 10, 120);
        int redBackgroundColor = ColorUtils.rgba(255, 0, 0, 120);
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack mainStack = entity2.getHeldItemMainhand();
        if (!mainStack.isEmpty()) {
            items.add(mainStack);
        }
        for (ItemStack itemStack : entity2.getArmorInventoryList()) {
            if (itemStack.isEmpty()) continue;
            items.add(itemStack);
        }
        ItemStack offStack = entity2.getHeldItemOffhand();
        if (!offStack.isEmpty()) {
            items.add(offStack);
        }
        posX = (int)((float)posX - ((float)(items.size() * (size + padding)) / 2.0f + 15.0f));
        for (ItemStack itemStack : items) {
            if (itemStack.isEmpty()) continue;
            float itemPosX = posX + (size + padding) * items.indexOf(itemStack);
            float itemPosY = posY;
            GL11.glPushMatrix();
            this.glCenteredScale(itemPosX, itemPosY, (float)size / 2.0f, (float)size / 2.0f, 0.5f);
            int currentBackgroundColor = itemStack == offStack ? redBackgroundColor : backgroundColor;
            DisplayUtils.drawRoundedRect(itemPosX + (float)size / 2.0f - (float)backgroundWidth / 2.0f, itemPosY, (float)(backgroundWidth + 10), (float)(backgroundHeight + 8), 1.0f, currentBackgroundColor);
            mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, (int)itemPosX, (int)itemPosY);
            mc.getItemRenderer().renderItemOverlayIntoGUI(ESP.mc.fontRenderer, itemStack, (int)itemPosX, (int)itemPosY, null);
            GL11.glPopMatrix();
            float fontHeight = Fonts.consolas.getHeight(6.0f);
            if (itemStack.isEnchanted() && !((Boolean)this.remove.getValueByName("\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u044f").get()).booleanValue()) {
                int ePosY = (int)((float)posY - fontHeight);
                Map<Enchantment, Integer> enchantmentsMap = EnchantmentHelper.getEnchantments(itemStack);
                for (Enchantment enchantment : enchantmentsMap.keySet()) {
                    int level = enchantmentsMap.get(enchantment);
                    if (level < 1 || !enchantment.canApply(itemStack)) continue;
                    TranslationTextComponent iformattabletextcomponent = new TranslationTextComponent(enchantment.getName());
                    String enchText = iformattabletextcomponent.getString().substring(0, 2) + level;
                    Fonts.consolas.drawText(matrixStack, enchText, posX, ePosY, -1, 6.0f, 0.05f);
                    ePosY -= (int)fontHeight;
                }
            }
            posX += size + padding;
        }
    }

    public static void drawMcRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.pos(left, bottom, 1.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, bottom, 1.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(right, top, 1.0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(left, top, 1.0).color(f, f1, f2, f3).endVertex();
    }

    public boolean isValid(Entity e) {
        if (AntiBot.isBot(e)) {
            return false;
        }
        return this.isInView(e);
    }

    public void glCenteredScale(float x, float y, float w, float h, float f) {
        GL11.glTranslatef(x + w / 2.0f, y + h / 2.0f, 2.0f);
        GL11.glScalef(f, f, 1.0f);
        GL11.glTranslatef(-x - w / 2.0f, -y - h / 2.0f, 0.0f);
    }
}

