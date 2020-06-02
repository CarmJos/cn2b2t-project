package org.cn2b2t.core.managers.utils;

import org.cn2b2t.core.Main;
import org.cn2b2t.core.managers.utils.database.Connection;

public class DataManager {

    private static Connection connection;
    private static Connection tempConnection;

    public static void init() {
        connection = new Connection(
                ConfigManager.config.getString("Database.host", "127.0.0.1"),
                ConfigManager.config.getInt("Database.port", 3306),
                ConfigManager.config.getString("Database.data", "moci"),
                ConfigManager.config.getString("Database.user", "moci"),
                ConfigManager.config.getString("Database.password", "moci"));
        tempConnection = new Connection(
                ConfigManager.config.getString("Database.host", "127.0.0.1"),
                ConfigManager.config.getInt("Database.port", 3306),
                ConfigManager.config.getString("Database.tempdata", "temp"),
                ConfigManager.config.getString("Database.user", "moci"),
                ConfigManager.config.getString("Database.password", "moci"));
        if (connection.connect()) {
            Main.log("&7├ └ &a数据库连接成功！");
        } else {
            Main.log("&7├ └ &c数据库连接失败！");
        }
        if (tempConnection.connect()) {
            Main.log("&7├ └ &a临时数据库连接成功！");
        } else {
            Main.log("&7├ └ &c临时数据库连接失败！");
        }

    }

    public static Connection getTempConnection() {
        return tempConnection;
    }

    public static Connection getConnection() {
        return connection;
    }

}
