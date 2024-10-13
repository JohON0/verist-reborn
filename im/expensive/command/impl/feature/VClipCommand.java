/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl.feature;

import im.expensive.command.Command;
import im.expensive.command.CommandWithAdvice;
import im.expensive.command.Logger;
import im.expensive.command.Parameters;
import im.expensive.command.Prefix;
import im.expensive.command.impl.CommandException;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;

public class VClipCommand
implements Command,
CommandWithAdvice {
    private final Prefix prefix;
    private final Logger logger;
    private final Minecraft mc;

    @Override
    public void execute(Parameters parameters) {
        String direction;
        BlockPos playerPos = Minecraft.player.getPosition();
        float yOffset = switch (direction = parameters.asString(0).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0443\u043a\u0430\u0437\u0430\u0442\u044c \u043d\u0430\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u0438\u043b\u0438 \u0440\u0430\u0441\u0441\u0442\u043e\u044f\u043d\u0438\u0435."))) {
            case "up" -> this.findOffset(playerPos, true);
            case "down" -> this.findOffset(playerPos, false);
            default -> this.parseOffset(direction);
        };
        if (yOffset != 0.0f) {
            this.teleport(yOffset);
        } else {
            this.logger.log(TextFormatting.RED + "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0432\u044b\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044e.");
        }
    }

    @Override
    public String name() {
        return "vclip";
    }

    @Override
    public String description() {
        return "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0432\u0435\u0440\u0445/\u0432\u043d\u0438\u0437 \u043f\u043e \u0432\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u0438";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = this.prefix.get();
        return List.of((Object)(commandPrefix + "vclip up - \u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0432\u0432\u0435\u0440\u0445 \u0434\u043e \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0435\u0433\u043e \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0430"), (Object)(commandPrefix + "vclip down - \u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0432\u043d\u0438\u0437 \u0434\u043e \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0435\u0433\u043e \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0441\u0442\u0440\u0430\u043d\u0441\u0442\u0432\u0430"), (Object)(commandPrefix + "vclip <distance> - \u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u043d\u0430 \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u043e\u0435 \u0440\u0430\u0441\u0441\u0442\u043e\u044f\u043d\u0438\u0435"), (Object)("\u041f\u0440\u0438\u043c\u0435\u0440: " + TextFormatting.RED + commandPrefix + "vclip 10"));
    }

    private float findOffset(BlockPos playerPos, boolean toUp) {
        int startY = toUp ? 3 : -1;
        int endY = toUp ? 255 : -255;
        int step = toUp ? 1 : -1;
        for (int i = startY; i != endY; i += step) {
            BlockPos targetPos = playerPos.add(0, i, 0);
            BlockPos aboveTargetPos = targetPos.up();
            if (Minecraft.world.getBlockState(targetPos).isAir()) {
                if (Minecraft.world.getBlockState(aboveTargetPos).isAir()) {
                    return i + (toUp ? 1 : -1);
                }
            }
            if (Minecraft.world.getBlockState(targetPos).getBlock() != Blocks.BEDROCK || toUp) continue;
            this.logger.log(TextFormatting.RED + "\u0422\u0443\u0442 \u043d\u0435\u043b\u044c\u0437\u044f \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f \u043f\u043e\u0434 \u0437\u0435\u043c\u043b\u044e.");
            return 0.0f;
        }
        return 0.0f;
    }

    private void teleport(float yOffset) {
        Minecraft.player.connection.sendPacket(new CPlayerPacket.PositionPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ(), Minecraft.player.isOnGround()));
        Minecraft.player.connection.sendPacket(new CPlayerPacket.PositionPacket(Minecraft.player.getPosX(), Minecraft.player.getPosY() + (double)yOffset, Minecraft.player.getPosZ(), Minecraft.player.isOnGround()));
        Minecraft.player.setPosition(Minecraft.player.getPosX(), Minecraft.player.getPosY() + (double)yOffset, Minecraft.player.getPosZ());
        String blockUnit = yOffset > 1.0f ? "\u0431\u043b\u043e\u043a\u0430" : "\u0431\u043b\u043e\u043a";
        this.logger.log(String.format("\u0412\u044b \u0431\u044b\u043b\u0438 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u043d\u0430 %.1f %s \u043f\u043e \u0432\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u0438", Float.valueOf(yOffset), blockUnit));
    }

    private float parseOffset(String distance) {
        if (NumberUtils.isNumber(distance)) {
            return Float.parseFloat(distance);
        }
        this.logger.log(TextFormatting.RED + distance + TextFormatting.GRAY + " \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u0447\u0438\u0441\u043b\u043e\u043c!");
        return 0.0f;
    }

    public VClipCommand(Prefix prefix, Logger logger, Minecraft mc) {
        this.prefix = prefix;
        this.logger = logger;
        this.mc = mc;
    }
}

