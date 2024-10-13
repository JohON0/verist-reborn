/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.config;

import im.expensive.utils.client.IMinecraft;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;

public class Parse
implements IMinecraft {
    private final File file;
    private final String name;

    public Parse(String name) {
        this.name = name;
        File configDir = new File(Minecraft.getInstance().gameDir, "verist" + File.separator + "configs");
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw new RuntimeException("Failed to create configuration directory: " + configDir.getAbsolutePath());
        }
        this.file = new File(configDir, name + ".txt");
    }

    public void saveUsernames(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            throw new IllegalArgumentException("Usernames list cannot be null or empty");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));){
            for (String username : usernames) {
                writer.write(username);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save usernames to file: " + this.file.getAbsolutePath(), e);
        }
    }

    public List<String> loadUsernames() {
        return null;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }
}

