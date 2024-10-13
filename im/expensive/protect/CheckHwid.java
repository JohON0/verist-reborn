/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

import im.expensive.protect.SystemUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CheckHwid {
    private static final String FILE_NAME = "VeristHwid.txt";

    public static void main() {
        ArrayList<String> hwids = new ArrayList<String>();
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                String hwid = SystemUtil.getSystemInfo();
                FileWriter writer = new FileWriter(file);
                writer.write(hwid + System.lineSeparator());
                writer.close();
                hwids.add(hwid);
            } else {
                String line;
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    hwids.add(line);
                }
                reader.close();
            }
            try {
                String pastebinLine;
                URL pastebinURL = new URL("");
                BufferedReader in = new BufferedReader(new InputStreamReader(pastebinURL.openStream()));
                while ((pastebinLine = in.readLine()) != null) {
                    if (!hwids.contains(pastebinLine)) continue;
                    in.close();
                    return;
                }
                in.close();
            } catch (IOException var5) {
                System.out.println("No searching Database");
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }
}

