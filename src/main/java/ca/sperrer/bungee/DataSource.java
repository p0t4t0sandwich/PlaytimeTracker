package ca.sperrer.bungee;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://ip/db");
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    public static void update_playtime() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            System.out.println("Saving data for: " + player.getName() + " (" + player.getUniqueId() + ")");
            try {
                String SQL_QUERY = "UPDATE playtime SET " + player.getServer().getInfo().getName() + " = " + player.getServer().getInfo().getName() + " + 1 WHERE player_id=(SELECT player_id FROM player_data WHERE player_uuid='" + player.getUniqueId() + "');";
                Connection con = DataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                pst.executeUpdate();
                con.close();
            } catch (SQLException e) {
                try {
                    String SQL_QUERY = "ALTER TABLE playtime ADD " + player.getServer().getInfo().getName() + "  INT DEFAULT 0;";
                    Connection con = DataSource.getConnection();
                    PreparedStatement pst = con.prepareStatement(SQL_QUERY);
                    pst.executeUpdate();
                    con.close();
                } catch (SQLException f) {
                    throw new RuntimeException(f);
                }
                throw new RuntimeException(e);
            }
        }
    }
}
