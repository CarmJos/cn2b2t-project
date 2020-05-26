package org.cn2b2t.core.managers.utils;

import org.cn2b2t.core.Main;
import org.cn2b2t.core.managers.utils.database.Connection;

public class DataManager {

    private static Connection connection;

    public static void init() {
        connection = new Connection(
                ConfigManager.config.getString("Database.host", "127.0.0.1"),
                ConfigManager.config.getInt("Database.port", 3306),
                ConfigManager.config.getString("Database.data", "guiying"),
                ConfigManager.config.getString("Database.user", "guiying"),
                ConfigManager.config.getString("Database.password", "guiying"));
        if (connection.connect()) {
            Main.log("&7├ └ &a数据库连接成功！");
        } else {
            Main.log("&7├ └ &c数据库连接失败！");
        }

    }

    public static Connection getConnection() {
        return connection;
    }

}
