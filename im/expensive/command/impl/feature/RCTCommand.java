/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl.feature;

import im.expensive.command.Command;
import im.expensive.command.Logger;
import im.expensive.command.MultiNamedCommand;
import im.expensive.command.Parameters;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;

public class RCTCommand
implements Command,
MultiNamedCommand {
    private final Logger logger;
    private final Minecraft mc;
    public final File CONFIG_DIR;

    @Override
    public void execute(Parameters parameters) {
        this.savePlayerNamesAndDonations();
    }

    private void savePlayerNamesAndDonations() {
        if (!this.CONFIG_DIR.exists()) {
            this.CONFIG_DIR.mkdirs();
        }
        File file = new File(this.CONFIG_DIR, "player_names_and_donations.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));){
            List playerNames = Minecraft.player.connection.getPlayerInfoMap().stream().map(info -> info.getGameProfile().getName()).collect(Collectors.toList());
            Map<String, List<String>> donations = this.getDonations();
            writer.write("Player Names:");
            writer.newLine();
            for (String string : playerNames) {
                writer.write("  - " + string);
                writer.newLine();
            }
            writer.newLine();
            writer.write("Donations:");
            writer.newLine();
            for (Map.Entry entry : donations.entrySet()) {
                writer.write((String)entry.getKey() + ":");
                writer.newLine();
                for (String name : (List)entry.getValue()) {
                    writer.write("  - " + name);
                    writer.newLine();
                }
                writer.newLine();
            }
            this.logger.log("\u041d\u0438\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0438 \u0434\u043e\u043d\u0430\u0442\u044b \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u044b \u0432 " + file.getAbsolutePath());
        } catch (IOException e) {
            this.logger.log("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u0438 \u043d\u0438\u043a\u043e\u0432 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0438 \u0434\u043e\u043d\u0430\u0442\u043e\u0432: " + e.getMessage());
        }
    }

    private Map<String, List<String>> getDonations() {
        HashMap<String, List<String>> donations = new HashMap<String, List<String>>();
        donations.put("VIP Rank", Arrays.asList("Player1", "Player2"));
        donations.put("MVP Rank", Arrays.asList("Player3"));
        return donations;
    }

    @Override
    public String name() {
        return "parse";
    }

    @Override
    public String description() {
        return "\u0421\u043e\u0445\u0440\u0430\u043d\u044f\u0435\u0442 \u043d\u0438\u043a\u0438 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u0438 \u0434\u043e\u043d\u0430\u0442\u044b \u0432 \u0444\u0430\u0439\u043b";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("reconnect");
    }

    public RCTCommand(Logger logger, Minecraft mc) {
        this.CONFIG_DIR = new File(Minecraft.getInstance().gameDir, "verist/configs");
        this.logger = logger;
        this.mc = mc;
    }
}

