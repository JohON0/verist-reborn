/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.impl.feature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import im.expensive.Expensive;
import im.expensive.command.Command;
import im.expensive.command.CommandWithAdvice;
import im.expensive.command.Logger;
import im.expensive.command.MultiNamedCommand;
import im.expensive.command.Parameters;
import im.expensive.command.Prefix;
import im.expensive.command.impl.CommandException;
import im.expensive.ui.ab.factory.ItemFactory;
import im.expensive.ui.ab.model.IItem;
import im.expensive.ui.autobuy.AutoBuy;
import im.expensive.utils.client.IMinecraft;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;

public class ABCommand
implements Command,
CommandWithAdvice,
MultiNamedCommand,
IMinecraft {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Prefix prefix;
    private final Logger logger;
    private final ItemFactory itemFactory;
    private static final File CONFIG_DIR = new File(ABCommand.mc.gameDir, "verist/files");

    @Override
    public void execute(Parameters parameters) {
        String commandType;
        switch (commandType = parameters.asString(0).orElse("")) {
            case "load": {
                this.loadConfig(parameters);
                break;
            }
            case "save": {
                this.saveConfig(parameters);
                break;
            }
            case "create": {
                this.createConfig(parameters);
                break;
            }
            case "list": {
                this.configList();
                break;
            }
            case "dir": {
                this.getDirectory();
                break;
            }
            default: {
                throw new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0442\u0438\u043f \u043a\u043e\u043c\u0430\u043d\u0434\u044b:" + TextFormatting.GRAY + " load, save, create, list, dir");
            }
        }
    }

    @Override
    public String name() {
        return "abconfig";
    }

    @Override
    public String description() {
        return "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u0437\u0430\u0438\u043c\u043e\u0434\u0435\u0439\u0441\u0442\u0432\u043e\u0432\u0430\u0442\u044c \u0441 .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0430\u043c\u0438 \u0432 \u0447\u0438\u0442\u0435";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = this.prefix.get();
        return List.of((Object)(commandPrefix + this.name() + " load <config> - \u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c .ab \u043a\u043e\u043d\u0444\u0438\u0433"), (Object)(commandPrefix + this.name() + " save <config> - \u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c .ab \u043a\u043e\u043d\u0444\u0438\u0433"), (Object)(commandPrefix + this.name() + " create <config> - \u0421\u043e\u0437\u0434\u0430\u0442\u044c .ab \u043a\u043e\u043d\u0444\u0438\u0433"), (Object)(commandPrefix + this.name() + " list - \u041f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0441\u043f\u0438\u0441\u043e\u043a .ab \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432"), (Object)(commandPrefix + this.name() + " dir - \u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443 \u0441 \u043a\u043e\u043d\u0444\u0438\u0433\u0430\u043c\u0438"), (Object)("\u041f\u0440\u0438\u043c\u0435\u0440: " + TextFormatting.RED + commandPrefix + "abconfig save myConfig"), (Object)("\u041f\u0440\u0438\u043c\u0435\u0440: " + TextFormatting.RED + commandPrefix + "abconfig load myConfig"));
    }

    @Override
    public List<String> aliases() {
        return List.of((Object)"abcfg");
    }

    private void loadConfig(Parameters parameters) {
        String configName = parameters.asString(1).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0430!"));
        File configFile = new File(CONFIG_DIR, configName + ".ab");
        if (configFile.exists()) {
            try {
                this.readBuyConfig(configFile);
                this.logger.log(TextFormatting.GREEN + ".ab \u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f " + TextFormatting.RED + configName + TextFormatting.GREEN + " \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u0430!");
            } catch (FileNotFoundException e) {
                this.logger.log(TextFormatting.RED + "\u041e\u0448\u0438\u0431\u043a\u0430 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0438: " + e.getMessage());
            }
        } else {
            this.logger.log(TextFormatting.RED + ".ab \u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f " + TextFormatting.GRAY + configName + TextFormatting.RED + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!");
        }
    }

    private void saveConfig(Parameters parameters) {
        String configName = parameters.asString(1).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0430!"));
        File configFile = new File(CONFIG_DIR, configName + ".ab");
        try {
            this.updateFile(configFile);
            this.logger.log(TextFormatting.GREEN + ".ab \u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f " + TextFormatting.RED + configName + TextFormatting.GREEN + " \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0430!");
        } catch (Exception e) {
            this.logger.log(TextFormatting.RED + "\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d\u0438\u044f: " + e.getMessage());
        }
    }

    private void createConfig(Parameters parameters) {
        String configName = parameters.asString(1).orElseThrow(() -> new CommandException(TextFormatting.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0430!"));
        File configFile = new File(CONFIG_DIR, configName + ".ab");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                this.logger.log(TextFormatting.GREEN + ".ab \u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f " + TextFormatting.RED + configName + TextFormatting.GREEN + " \u0441\u043e\u0437\u0434\u0430\u043d\u0430!");
            } else {
                this.logger.log(TextFormatting.RED + "\u0424\u0430\u0439\u043b \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u0438 \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442!");
            }
        } catch (IOException e) {
            this.logger.log(TextFormatting.RED + "\u041e\u0448\u0438\u0431\u043a\u0430 \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u044f \u0444\u0430\u0439\u043b\u0430: " + e.getMessage());
        }
    }

    private void configList() {
        if (!CONFIG_DIR.exists() || CONFIG_DIR.listFiles() == null) {
            this.logger.log(TextFormatting.RED + "\u0421\u043f\u0438\u0441\u043e\u043a .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u0439 \u043f\u0443\u0441\u0442\u043e\u0439");
            return;
        }
        File[] files = CONFIG_DIR.listFiles((dir, name) -> name.endsWith(".ab"));
        if (files == null || files.length == 0) {
            this.logger.log(TextFormatting.RED + "\u0421\u043f\u0438\u0441\u043e\u043a .ab \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u0439 \u043f\u0443\u0441\u0442\u043e\u0439");
        } else {
            this.logger.log(TextFormatting.GRAY + "\u0421\u043f\u0438\u0441\u043e\u043a .ab \u043a\u043e\u043d\u0444\u0438\u0433\u043e\u0432:");
            for (File file : files) {
                this.logger.log(TextFormatting.GRAY + file.getName());
            }
        }
    }

    private void getDirectory() {
        try {
            Runtime.getRuntime().exec("explorer " + CONFIG_DIR.getAbsolutePath());
        } catch (IOException e) {
            this.logger.log(TextFormatting.RED + "\u041f\u0430\u043f\u043a\u0430 \u0441 \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f\u043c\u0438 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430!" + e.getMessage());
        }
    }

    private void readBuyConfig(File configFile) throws FileNotFoundException {
        JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(configFile)));
        if (jsonElement.isJsonNull()) {
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("autoBuys")) {
            for (JsonElement element : jsonObject.get("autoBuys").getAsJsonArray()) {
                JsonObject autoBuyObject = element.getAsJsonObject();
                Item item = Item.getItemById(autoBuyObject.get("item").getAsInt());
                int price = autoBuyObject.get("price").getAsInt();
                int count = autoBuyObject.get("count").getAsInt();
                boolean enchantment = autoBuyObject.get("enchantment").getAsBoolean();
                HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
                if (autoBuyObject.has("enchantments")) {
                    JsonObject enchantmentsObject = autoBuyObject.getAsJsonObject("enchantments");
                    enchantmentsObject.entrySet().forEach(entry -> enchantments.put((Enchantment)Registry.ENCHANTMENT.getByValue(Integer.parseInt((String)entry.getKey())), ((JsonElement)entry.getValue()).getAsInt()));
                }
                IItem newItem = this.itemFactory.createNewItem(item, price, count, autoBuyObject.get("damage").getAsInt(), enchantments);
                Expensive.getInstance().getAutoBuyHandler().items.add(new AutoBuy(newItem.getItem(), price, count, enchantments, enchantment, true, false, false));
            }
        }
    }

    private void updateFile(File configFile) {
        JsonObject jsonObject = new JsonObject();
        JsonArray autoBuysArray = new JsonArray();
        for (AutoBuy autoBuy : Expensive.getInstance().getAutoBuyHandler().items) {
            JsonObject autoBuyObject = new JsonObject();
            autoBuyObject.addProperty("item", Item.getIdFromItem(autoBuy.getItem()));
            autoBuyObject.addProperty("price", autoBuy.getPrice());
            autoBuyObject.addProperty("enchantment", autoBuy.isEnchantment());
            autoBuyObject.addProperty("count", autoBuy.getCount());
            JsonObject enchantmentsObject = new JsonObject();
            if (!autoBuy.getEnchanments().isEmpty()) {
                autoBuy.getEnchanments().forEach((enchantment, level) -> enchantmentsObject.addProperty(Registry.ENCHANTMENT.getKey((Enchantment)enchantment).toString(), (Number)level));
                autoBuyObject.add("enchantments", enchantmentsObject);
            }
            autoBuysArray.add(autoBuyObject);
        }
        jsonObject.add("autoBuys", autoBuysArray);
        try (PrintWriter printWriter = new PrintWriter(configFile);){
            printWriter.println(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ABCommand(Prefix prefix, Logger logger, ItemFactory itemFactory) {
        this.prefix = prefix;
        this.logger = logger;
        this.itemFactory = itemFactory;
    }
}

