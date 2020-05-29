package org.cn2b2t.logger.managers;


import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cn2b2t.core.managers.utils.DataManager;
import org.cn2b2t.core.managers.utils.database.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class LoggerManager {
    public static Connection kettleConnection;

    private static String tableName = "game_cn2b2t_logger";

    public static void init() {
        kettleConnection = DataManager.getConnection();
        createTables();
    }

    public enum LogType {
        CHAT,
        JOIN,
        QUIT,
        AC,
        COMMAND
    }


    private static void createTables() {
        kettleConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + tableName + "`(`id` INT(11) NOT NULL AUTO_INCREMENT,`uuid` varchar(32) ,`server` int, `time` varchar(30) ,`name` varchar(30), `type` varchar(30), `value` varchar(1024) ,PRIMARY KEY (`id`), UNIQUE KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
    }

    public static void writeIn(UUID uuid, final String name, final int port, final LogType t, final String value) {
        DataManager.getConnection().insertAsyn(tableName,
                new String[]{"uuid", "server", "time", "name", "type", "value"},
                new Object[]{uuid.toString(), port, getDateString(), name, t.name(), StringEscapeUtils.escapeSql(value)});
    }

    public static void log(final LogType lt, final Player p, final String s) {
        writeIn(p.getUniqueId(), p.getName(), Bukkit.getServer().getPort(), lt, s);
    }

    public static void logConsole(final LogType lt, final String s) {
        final String name = "CONSOLE";
        writeIn(UUID.fromString("00000000000000000000000000000000"), name, Bukkit.getServer().getPort(), lt, s);
    }

    public static String getDateString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
