/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl.feature;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.Expensive;
import im.expensive.command.Command;
import im.expensive.command.CommandWithAdvice;
import im.expensive.command.Logger;
import im.expensive.command.MultiNamedCommand;
import im.expensive.command.Parameters;
import im.expensive.command.Prefix;
import im.expensive.command.impl.CommandException;
import im.expensive.events.EventDisplay;
import im.expensive.functions.api.FunctionRegistry;
import im.expensive.functions.impl.misc.SelfDestruct;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.projections.ProjectionUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TextFormatting;

public class GPSCommand
implements Command,
CommandWithAdvice,
MultiNamedCommand,
IMinecraft {
    private final Prefix prefix;
    private final Logger logger;
    private final Map<String, Vector3i> waysMap = new LinkedHashMap<String, Vector3i>();

    public GPSCommand(Prefix prefix, Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
        Expensive.getInstance().getEventBus().register(this);
    }

    @Override
    public void execute(Parameters parameters) {
        String commandType;
        switch (commandType = parameters.asString(0).orElse("")) {
            case "add": {
                this.addGPS(parameters);
                break;
            }
            case "remove": {
                this.removeGPS(parameters);
                break;
            }
            case "clear": {
                this.waysMap.clear();
                this.logger.log("\u0412\u0441\u0435 \u043f\u0443\u0442\u0438 \u0431\u044b\u043b\u0438 \u0443\u0434\u0430\u043b\u0435\u043d\u044b!");
                break;
            }
            case "list": {
                this.logger.log("\u0421\u043f\u0438\u0441\u043e\u043a \u043f\u0443\u0442\u0435\u0439:");
                for (String s : this.waysMap.keySet()) {
                    this.logger.log("- " + s + " " + this.waysMap.get(s));
                }
                break;
            }
            default: {
                throw new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0438\u043f \u043a\u043e\u043c\u0430\u043d\u0434\u044b:" + TextFormatting.GRAY + " add, remove, clear");
            }
        }
    }

    private void addGPS(Parameters param) {
        String name = param.asString(1).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0438\u043c\u044f \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b!"));
        int x = param.asInt(2).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043f\u0435\u0440\u0432\u0443\u044e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0443!"));
        int y = param.asInt(3).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0432\u0442\u043e\u0440\u0443\u044e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0443!"));
        int z = param.asInt(4).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0440\u0435\u0442\u044c\u044e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0443!"));
        Vector3i vec = new Vector3i(x, y, z);
        this.waysMap.put(name, vec);
        this.logger.log("\u041f\u0443\u0442\u044c " + name + " \u0431\u044b\u043b \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d!");
    }

    private void removeGPS(Parameters param) {
        String name = param.asString(1).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0438\u043c\u044f \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b!"));
        this.waysMap.remove(name);
        this.logger.log("\u041f\u0443\u0442\u044c " + name + " \u0431\u044b\u043b \u0443\u0434\u0430\u043b\u0451\u043d!");
    }

    @Override
    public String name() {
        return "gps";
    }

    @Override
    public String description() {
        return "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0430\u0431\u043e\u0442\u0430\u0442\u044c \u0441 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430\u043c\u0438 \u043f\u0443\u0442\u0435\u0439";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = this.prefix.get();
        return List.of((Object)(commandPrefix + "gps add <\u0438\u043c\u044f, x, y, z> - \u041f\u0440\u043e\u043b\u043e\u0436\u0438\u0442\u044c \u043f\u0443\u0442\u044c \u043a WayPoint'\u0443"), (Object)(commandPrefix + "gps remove <\u0438\u043c\u044f> - \u0423\u0434\u0430\u043b\u0438\u0442\u044c WayPoint"), (Object)(commandPrefix + "gps list - \u0421\u043f\u0438\u0441\u043e\u043a WayPoint'\u043e\u0432"), (Object)(commandPrefix + "gps clear - \u041e\u0447\u0438\u0441\u0442\u0438\u0442\u044c \u0441\u043f\u0438\u0441\u043e\u043a WayPoint'\u043e\u0432"), (Object)("\u041f\u0440\u0438\u043c\u0435\u0440: " + TextFormatting.RED + commandPrefix + "gps add \u0430\u0438\u0440\u0434\u0440\u043e\u043f 1000 100 1000"));
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        FunctionRegistry functionRegistry = Expensive.getInstance().getFunctionRegistry();
        SelfDestruct selfDestruct = functionRegistry.getSelfDestruct();
        if (selfDestruct.unhooked) {
            return;
        }
        if (this.waysMap.isEmpty()) {
            return;
        }
        for (String name : this.waysMap.keySet()) {
            Vector3i vec3i = this.waysMap.get(name);
            Vector3d vec3d = new Vector3d((double)vec3i.getX() + 0.5, (double)vec3i.getY() + 0.5, (double)vec3i.getZ() + 0.5);
            Vector2f vec2f = ProjectionUtil.project(vec3d.x, vec3d.y, vec3d.z);
            Minecraft.getInstance();
            int distance = (int)Minecraft.player.getPositionVec().distanceTo(vec3d);
            String text = name + " (" + distance + " M\u0435\u0442\u0440\u043e\u0432)";
            if (vec2f.equals(new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE))) {
                Vector3d localVec = vec3d.subtract(GPSCommand.mc.getRenderManager().info.getProjectedView());
                double x = localVec.getX();
                double z = localVec.getZ();
                double cos2 = MathHelper.cos((float)((double)GPSCommand.mc.getRenderManager().info.getYaw() * (Math.PI / 180)));
                double sin2 = MathHelper.sin((float)((double)GPSCommand.mc.getRenderManager().info.getYaw() * (Math.PI / 180)));
                double rotY = -(z * cos2 - x * sin2);
                double rotX = -(x * cos2 + z * sin2);
                float angle = (float)(Math.atan2(rotY, rotX) * 180.0 / Math.PI);
                double x2 = 30.0f * MathHelper.cos((float)Math.toRadians(angle)) + (float)window.getScaledWidth() / 2.0f;
                double y2 = 30.0f * MathHelper.sin((float)Math.toRadians(angle)) + (float)window.getScaledHeight() / 2.0f;
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                GlStateManager.translated(x2, y2, 0.0);
                Fonts.montserrat.drawCenteredText(e.getMatrixStack(), text, 0.0f, -15.0f, -1, 6.0f);
                GlStateManager.rotatef(angle, 0.0f, 0.0f, 1.0f);
                int color = -1;
                ResourceLocation logo = new ResourceLocation("expensive/images/hud/triangle.png");
                DisplayUtils.drawImage(logo, -4.0f, -1.0f, 18.0f, 17.0f, color);
                GlStateManager.enableBlend();
                GlStateManager.popMatrix();
                continue;
            }
            float textWith = Fonts.montserrat.getWidth(text, 8.0f);
            float fontHeight = Fonts.montserrat.getHeight(8.0f);
            float posX = vec2f.x - textWith / 2.0f;
            float posY = vec2f.y - fontHeight / 2.0f;
            float padding = 2.0f;
            DisplayUtils.drawRectW(posX - padding, posY - padding, padding + textWith + padding, padding + fontHeight + padding, ColorUtils.setAlpha(ColorUtils.getColor(90), 10));
            Fonts.montserrat.drawText(e.getMatrixStack(), text, posX, posY, -1, 8.0f);
        }
    }

    @Override
    public List<String> aliases() {
        return List.of((Object)"way");
    }
}

