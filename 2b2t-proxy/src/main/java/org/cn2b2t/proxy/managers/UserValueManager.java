package org.cn2b2t.proxy.managers;

import org.cn2b2t.proxy.functions.proxyuser.ProxyUser;
import org.cn2b2t.proxy.utils.data.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class UserValueManager {
    public static void init() {
        DataManager.getTempConnection().SQLqueryWithNoResult(
                "CREATE TABLE IF NOT EXISTS `uservalues` (`id` INT(11) NOT NULL AUTO_INCREMENT , " +
                        "`inkid` INT(11), `key` varchar(256), `value` varchar(256), PRIMARY KEY (`id`), UNIQUE KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
    }


    public static Map<String, String> getDataValues(Integer inkID) {
        Map<String, String> values = new HashMap<>();
        ResultSet query = getConnection().SQLquery("select * from `uservalues` where `inkid` = '" + inkID + "'");
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

    public static String getDataValuesByKey(Integer inkID, String key) {
        //`uservalues` WHERE `inkid` = 1 AND `key` = 'afk'
        ResultSet query = getConnection().SQLquery("select * from `uservalues` where `inkid` = '" + inkID + "' AND `key`='" + key + "'");
        try {
            if (query != null) {
                String value = null;
                if (query.next()) {
                    value = query.getString("value");
                    return value;
                }
                query.close();
                return value;
            }
        } catch (SQLException e) {
            getConnection().info(e.getLocalizedMessage());
        }
        return null;
    }


    public static void addDataValue(Integer inkID, String key, String values) {
        getConnection().SQLqueryWithNoResult("INSERT INTO `uservalues` " +
                "(`inkid`,`key`,`value`) " +
                "VALUES " +
                "('" + inkID + "','" + key + "','" + values + "');");
    }

    public static void removeDataValueByKey(Integer inkID, String key) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where inkid='" + inkID + "' and `key`='" + key + "';");
    }

    public static void removeDataValue(Integer inkID, String value) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where inkid='" + inkID + "' and value=`" + value + "`;");
    }

    public static void setDataValues(Integer inkID, HashMap<String, String> values) {
        getConnection().SQLqueryWithNoResult("delete from `uservalues` where inkid='" + inkID + "';");
        values.keySet().forEach(key -> addDataValue(inkID, key, values.get(key)));
    }

    public static Map<String, String> getDataValues(ProxyUser u) {
        return getDataValues(u.getInkID());
    }

    public static void addDataValue(ProxyUser u, String key, String values) {
        addDataValue(u.getInkID(), key, values);
    }

    public static void removeDataValueByKey(ProxyUser u, String key) {
        removeDataValueByKey(u.getInkID(), key);
    }

    public static void removeDataValue(ProxyUser u, String value) {
        removeDataValue(u.getInkID(), value);
    }

    public static void setDataValues(ProxyUser u, HashMap<String, String> values) {
        setDataValues(u.getInkID(), values);
    }

    public static Connection getConnection() {
        return DataManager.getTempConnection();
    }
}
