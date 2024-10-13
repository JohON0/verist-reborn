/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class HTTP {
    private static final HashMap<Integer, FloatBuffer> kernelCache = new HashMap();

    public static String getHTTP(String ip) {
        try {
            String line;
            URL url = new URL(ip);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String resp = response.toString();
            connection.disconnect();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

