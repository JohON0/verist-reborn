/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.mainmenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import im.expensive.Expensive;
import im.expensive.ui.mainmenu.Account;
import im.expensive.utils.client.IMinecraft;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.util.Session;

public class AltConfig
implements IMinecraft {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File file = new File(AltConfig.mc.gameDir, "expensive/files/alts.cfg");
    private static final File lastAccountFile = new File(AltConfig.mc.gameDir, "expensive/files/last_account.txt");

    public void init() throws Exception {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            this.readAlts();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                AltConfig.updateFile();
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }));
        this.readLastAccount();
    }

    public static void updateFile() {
        try {
            StringBuilder builder = new StringBuilder();
            for (Account alt : Expensive.getInstance().getAltManager().accounts) {
                builder.append(alt.accountName).append(":").append(alt.dateAdded).append("\n");
            }
            Files.write(file.toPath(), builder.toString().getBytes(), new OpenOption[0]);
            AltConfig.saveLastAccount();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void readAlts() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file.getAbsolutePath()))));){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String username = parts[0];
                Expensive.getInstance().getAltManager().accounts.add(new Account(username, Long.parseLong(parts[1])));
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    private static void saveLastAccount() {
        try {
            String lastAccount = mc.getSession().getUsername();
            Files.write(lastAccountFile.toPath(), lastAccount.getBytes(), new OpenOption[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLastAccount() {
        try {
            if (lastAccountFile.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(lastAccountFile.getAbsolutePath()))));
                String lastAccount = reader.readLine();
                reader.close();
                AltConfig.mc.session = new Session(lastAccount, "", "", "mojang");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

