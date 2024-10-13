/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl.feature;

import im.expensive.command.Command;
import im.expensive.command.CommandWithAdvice;
import im.expensive.command.Logger;
import im.expensive.command.MultiNamedCommand;
import im.expensive.command.Parameters;
import im.expensive.command.Prefix;
import im.expensive.command.impl.CommandException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;

public class ParseCommand
implements Command,
CommandWithAdvice,
MultiNamedCommand {
    private final Prefix prefix;
    private final Logger logger;

    @Override
    public void execute(Parameters parameters) {
        String action = parameters.asString(0).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435 (start \u0438\u043b\u0438 dir)!"));
        File PARSE_DIR = new File(Minecraft.getInstance().gameDir, "\\verist\\configs");
        if (!PARSE_DIR.exists()) {
            PARSE_DIR.mkdirs();
        }
        switch (action.toLowerCase()) {
            case "start": {
                Minecraft.getInstance();
                Collection<NetworkPlayerInfo> playerInfos = Minecraft.player.connection.getPlayerInfoMap();
                ArrayList<String> donate = new ArrayList<String>();
                File file = new File(PARSE_DIR, Minecraft.getInstance().getCurrentServerData().serverIP + ".txt");
                try (FileWriter fileWriter = new FileWriter(file);){
                    for (NetworkPlayerInfo playerInfo : playerInfos) {
                        String text;
                        String prefix = playerInfo.getPlayerTeam().getPrefix().getString();
                        if (prefix.length() < 3 || donate.contains(text = TextFormatting.getTextWithoutFormattingCodes(prefix.substring(0, prefix.length() - 1)))) continue;
                        donate.add(text);
                    }
                    if (donate.isEmpty()) {
                        this.logger.log(TextFormatting.RED + "\u0414\u043e\u043d\u0430\u0442\u044b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0435 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b!");
                        return;
                    }
                    for (String don : donate) {
                        fileWriter.append("// ").append(don).append("\n\n");
                        for (NetworkPlayerInfo playerInfo : playerInfos) {
                            if (!playerInfo.getPlayerTeam().getPrefix().getString().contains(don)) continue;
                            fileWriter.append(playerInfo.getGameProfile().getName()).append("\n");
                        }
                        fileWriter.append("\n");
                    }
                    this.logger.log(TextFormatting.GREEN + "\u041d\u0438\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u044b \u0432 \u0444\u0430\u0439\u043b " + TextFormatting.RED + file.getName());
                    Runtime.getRuntime().exec("explorer " + PARSE_DIR.getAbsolutePath());
                } catch (Exception e) {
                    this.logger.log(TextFormatting.RED + "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0438 \u043d\u0438\u043a\u043e\u0432 \u0432 \u0444\u0430\u0439\u043b: " + e.getMessage());
                }
                break;
            }
            case "dir": {
                try {
                    Runtime.getRuntime().exec("explorer " + PARSE_DIR.getAbsolutePath());
                } catch (IOException e) {
                    this.logger.log(TextFormatting.RED + "\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u043e\u0442\u043a\u0440\u044b\u0442\u0438\u0438 \u043f\u0430\u043f\u043a\u0438: " + e.getMessage());
                }
                break;
            }
            default: {
                this.logger.log(TextFormatting.RED + "\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u043e\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435: " + action);
            }
        }
    }

    @Override
    public String name() {
        return "parse";
    }

    @Override
    public String description() {
        return "\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0435\u0442 \u043d\u0438\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u0432 \u0442\u0435\u043a\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b \u0438\u043b\u0438 \u043e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u043f\u0430\u043f\u043a\u0443 \u0441 \u0444\u0430\u0439\u043b\u0430\u043c\u0438";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = this.prefix.get();
        return List.of((Object)(commandPrefix + this.name() + " start - \u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043d\u0438\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0432 \u0442\u0435\u043a\u0441\u0442\u043e\u0432\u044b\u0439 \u0444\u0430\u0439\u043b"), (Object)(commandPrefix + this.name() + " dir - \u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u0441 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u043d\u044b\u043c\u0438 \u0444\u0430\u0439\u043b\u0430\u043c\u0438"), (Object)("\u041f\u0440\u0438\u043c\u0435\u0440: " + TextFormatting.RED + commandPrefix + "parse start"));
    }

    @Override
    public List<String> aliases() {
        return List.of((Object)"parse");
    }

    public ParseCommand(Prefix prefix, Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
    }
}

