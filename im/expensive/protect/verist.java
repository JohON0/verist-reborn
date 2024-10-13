/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class verist {
    public static int finalUID;

    public static int getRandom(int min2, int max2) {
        return (int)(Math.random() * (double)(max2 - min2 + 1)) + min2;
    }

    public static void saveUUID() {
        try {
            File file = new File("hs_err_pid5569.log");
            if (file.exists()) {
                String line;
                System.out.println("\u0420\u00a4\u0420\u00b0\u0420\u2116\u0420\u00bb \u0421\u0403 uid \u0421\u0453\u0420\u00b6\u0420\u00b5 \u0420\u00b5\u0421\u0403\u0421\u201a\u0421\u040a!");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((line = bufferedReader.readLine()) != null) {
                    finalUID = Integer.parseInt(line);
                }
                return;
            }
            FileWriter fw = new FileWriter(file);
            finalUID = verist.getRandom(1, 999);
            fw.write(String.valueOf(finalUID));
            fw.close();
        } catch (Exception var4) {
            var4.printStackTrace(System.err);
        }
    }
}

