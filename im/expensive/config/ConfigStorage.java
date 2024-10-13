/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import im.expensive.config.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

public class ConfigStorage {
    public final Logger logger = Logger.getLogger(ConfigStorage.class.getName());
    public final File CONFIG_DIR;
    public final File AUTOCFG_DIR;
    public final File LAST_USED_CONFIG_FILE;
    public final JsonParser jsonParser;
    private String lastUsedConfig;

    public ConfigStorage() {
        this.CONFIG_DIR = new File(Minecraft.getInstance().gameDir, "\\verist\\configs");
        this.AUTOCFG_DIR = new File(this.CONFIG_DIR, "");
        this.LAST_USED_CONFIG_FILE = new File(this.CONFIG_DIR, "last_used_config.txt");
        this.jsonParser = new JsonParser();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.saveAllConfigurations();
                this.saveLastUsedConfig();
                this.logger.log(Level.INFO, "All configurations saved successfully.");
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to save configurations during shutdown.", e);
            }
        }));
        this.loadLastUsedConfig();
        if (this.lastUsedConfig != null) {
            this.loadConfiguration(this.lastUsedConfig);
        }
    }

    public void init() throws IOException {
        this.setupFolder();
    }

    public void setupFolder() {
        if (!this.CONFIG_DIR.exists()) {
            this.CONFIG_DIR.mkdirs();
        } else if (this.AUTOCFG_DIR.exists()) {
            this.loadConfiguration("autocfg");
            this.logger.log(Level.SEVERE, "Load system configuration...");
        } else {
            this.logger.log(Level.SEVERE, "Creating system configuration...");
            try {
                this.AUTOCFG_DIR.createNewFile();
                this.logger.log(Level.SEVERE, "Created!");
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to create system configuration file", e);
            }
        }
    }

    public boolean isEmpty() {
        return this.getConfigs().isEmpty();
    }

    public List<Config> getConfigs() {
        ArrayList<Config> configs = new ArrayList<Config>();
        File[] configFiles = this.CONFIG_DIR.listFiles();
        if (configFiles != null) {
            for (File configFile : configFiles) {
                String configName;
                Config config;
                if (!configFile.isFile() || !configFile.getName().endsWith(".cfg") || (config = this.findConfig(configName = configFile.getName().replace(".cfg", ""))) == null) continue;
                configs.add(config);
            }
        }
        return configs;
    }

    public void loadConfiguration(String configuration) {
        this.lastUsedConfig = configuration;
        Config config = this.findConfig(configuration);
        try {
            FileReader reader = new FileReader(config.getFile());
            JsonObject object = (JsonObject)this.jsonParser.parse(reader);
            config.loadConfig(object);
        } catch (FileNotFoundException e) {
            this.logger.log(Level.WARNING, "Not Found Exception", e);
        } catch (NullPointerException pointerException) {
            this.logger.log(Level.WARNING, "Fatal error in Config!", pointerException);
        }
    }

    public void saveConfiguration(String configuration) {
        Config config = new Config(configuration);
        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(config.saveConfig());
        try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "File not found!", e);
        } catch (NullPointerException e) {
            this.logger.log(Level.WARNING, "Fatal Error in Config!", e);
        }
    }

    public void saveAllConfigurations() throws IOException {
        for (Config config : this.getConfigs()) {
            this.saveConfiguration(config.getName());
        }
    }

    public Config findConfig(String configName) {
        if (configName == null) {
            return null;
        }
        if (new File(this.CONFIG_DIR, configName + ".cfg").exists()) {
            return new Config(configName);
        }
        return null;
    }

    private void saveLastUsedConfig() {
        try (FileWriter writer = new FileWriter(this.LAST_USED_CONFIG_FILE);){
            writer.write(this.lastUsedConfig);
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to save last used configuration.", e);
        }
    }

    private void loadLastUsedConfig() {
        if (this.LAST_USED_CONFIG_FILE.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(this.LAST_USED_CONFIG_FILE));){
                this.lastUsedConfig = reader.readLine();
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Failed to load last used configuration.", e);
            }
        }
    }
}

