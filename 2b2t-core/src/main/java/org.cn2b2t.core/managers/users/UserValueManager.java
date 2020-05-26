package org.cn2b2t.core.managers.users;

import org.bukkit.entity.Player;
import org.cn2b2t.core.managers.utils.DataManager;
import org.cn2b2t.core.managers.utils.database.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class UserValueManager {

    public static void init() {
        DataManager.getConnection().SQLqueryWithNoResult(
                "CREATE TABLE IF NOT EXISTS `uservalues` (`id` INT(11) NOT NULL AUTO_INCREMENT , `uuid` varchar(64), `key` varchar(256), `value` varchar(256), PRIMARY KEY (`id`), UNIQUE KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
    }


    public static Map<String, String> getDataValues(UUID uuid) {
        Map<String, String> values = new HashMap<>();
        ResultSet query = getConnection().SQLquery("select * from `uservalues` where `uuid` = '" + uuid.toString() + "'");
        try {
            if (query != null) {
                while (query.next()) {
                    String key = query.getString("key");
                    String value = query.getString("value");
                    values.put(key, value);

                }
                query.close();
                return values;
            }
        } catch (SQLException e) {
            getConnection().info(e.getLocalizedMessage());
        }
        return values;
    }

    public static String getDataValuesByKey(UUID uuid, String key) {
        //`uservalues` WHERE `uuid` = 1 AND `key` = 'afk'
        ResultSet query = getConnection().SQLquery("select * from `uservalues` where `uuid` = '" + uuid.toString() + "' AND `key`='" + key + "'");
        try {
            if (query != null) {
                String value = null;
                if (query.next()) {
                    value = query.getString("value");
//					Bukkit.getConsoleSender().sendMessage(Main.color("§f#'" + uuid.toString() + "' &7key &c" + key + " &7value&c" + value));
                }
                query.close();
                return value;
            }
        } catch (SQLException e) {
            getConnection().info(e.getLocalizedMessage());
        }
        return null;
    }


    public static void addDataValue(UUID uuid, String key, String values) {
        getConnection().SQLqueryWithNoResult("INSERT INTO `uservalues` " +
                "(`uuid`,`key`,`value`) " +
                "VALUES " +
                "('" + uuid.toString() + "','" + key + "','" + values + "');");
    }

    public static void removeDataValueByKey(UUID uuid, String key) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where `uuid`='" + uuid.toString() + "' and `key`='" + key + "';");
    }

    public static void removeDataValue(UUID uuid, String value) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where `uuid`='" + uuid.toString() + "' and `value`=`" + value + "`;");
    }

    public static void setDataValues(UUID uuid, HashMap<String, String> values) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where `uuid`='" + uuid.toString() + "';");
        values.keySet().forEach(key -> addDataValue(uuid, key, values.get(key)));
    }

    public static Map<String, String> getDataValues(Player player) {
        return getDataValues(player.getUniqueId());
    }

    public static void addDataValue(Player player, String key, String values) {
        addDataValue(player.getUniqueId(), key, values);
    }

    public static void removeDataValueByKey(Player player, String key) {
        removeDataValueByKey(player.getUniqueId(), key);
    }

    public static void removeDataValue(Player player, String value) {
        removeDataValue(player.getUniqueId(), value);
    }

    public static void setDataValues(Player player, HashMap<String, String> values) {
        setDataValues(player.getUniqueId(), values);
    }

    public static Connection getConnection() {
        return DataManager.getConnection();
    }
}
