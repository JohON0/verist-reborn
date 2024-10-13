/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

import im.expensive.protect.DisplayUtils;
import im.expensive.protect.NoStackTraceThrowable;
import im.expensive.protect.SystemUtil;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HWIDManager {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void main() throws Exception {
        String dbServer = "phpmyadmin.co";
        String databaseName = "sql11677553";
        String username = "sql11677553";
        String password = "vpzSL3Az9e";
        String url = "jdbc:mysql://" + dbServer + "/" + databaseName + "?useSSL=false&allowPublicKeyRetrieval=true";
        Connection conn = null;
        Statement ps = null;
        try {
            String hwid;
            conn = DriverManager.getConnection(url, username, password);
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet rs = dbm.getTables(null, null, "hwid", null);
            if (!rs.next()) {
                hwid = "CREATE TABLE hwid ( hwid VARCHAR(255) NOT NULL, PRIMARY KEY(hwid))";
                ps = conn.prepareStatement(hwid);
                ps.execute();
            }
            hwid = SystemUtil.getSystemInfo();
            String selectQuery = "SELECT hwid FROM hwid WHERE hwid = ?";
            ps = conn.prepareStatement(selectQuery);
            ps.setString(1, hwid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                DisplayUtils.Display();
                throw new NoStackTraceThrowable("");
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}

