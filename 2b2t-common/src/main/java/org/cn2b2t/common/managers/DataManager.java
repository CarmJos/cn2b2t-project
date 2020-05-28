package org.cn2b2t.common.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.managers.utils.database.Connection;
import org.cn2b2t.core.utils.ColorParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author cam
 */
public class DataManager {

    private static Connection dataConnection;

    public static void init() {
        dataConnection = org.cn2b2t.core.managers.utils.DataManager.getConnection();

        dataConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + "game_cn2b2t_donatelist" + "`(`id` INT(11) NOT NULL AUTO_INCREMENT,`uuid` varchar(50) , `order_on` varchar(50), `price` int, `time` bigint, PRIMARY KEY (`id`), UNIQUE KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
    }

    public static void saveData(Player p, String order_on, int price) {
        dataConnection.insertAsyn("game_cn2b2t_donatelist", new String[]{"uuid", "order_on", "price", "time"}, new Object[]{p.getUniqueId().toString(), order_on, price, System.currentTimeMillis()});
        new BukkitRunnable() {
            @Override
            public void run() {
                DonateManager.monthly = DataManager.getDonateInAMonth();
                DonateManager.total = DataManager.getTotalDonate();
                Bukkit.getConsoleSender().sendMessage(ColorParser.parse(" \n本月赞助 &6" + DonateManager.monthly + "\n" +
                        "总共收到 &6" + DonateManager.total));
                if (p.isOnline() && ProfileData.get(p) != null) {
                    ProfileData.get(p).updateDonates();
                }
            }
        }.runTaskLater(Main.getInstance(), 100L);
    }

    public static int getDonateInAMonth(UUID uuid) {
        ResultSet query = dataConnection.SQLquery("game_cn2b2t_donatelist", "`uuid` = '" + uuid.toString() + "' AND time > " + (System.currentTimeMillis() - 2_592_000_000L));
        int result = 0;
        try {
            while (query.next()) {
                result += query.getInt("price");
            }
            query.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().warning(ex.getLocalizedMessage());
        }
        return result;
    }

    public static int getTotalDonate(UUID uuid) {
        ResultSet query = dataConnection.SQLquery("game_cn2b2t_donatelist", "`uuid` = '" + uuid.toString() + "'");
        int result = 0;
        try {
            while (query.next()) {
                result += query.getInt("price");
            }
            query.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().warning(ex.getLocalizedMessage());
        }
        return result;
    }

    public static int getDonateInAMonth() {
        ResultSet query = dataConnection.SQLquery("game_cn2b2t_donatelist", "time > " + (System.currentTimeMillis() - 2_592_000_000L));
        int result = 0;
        try {
            while (query.next()) {
                result += query.getInt("price");
            }
            query.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().warning(ex.getLocalizedMessage());
        }
        return result;
    }

    public static int getTotalDonate() {
        ResultSet query = dataConnection.SQLquery("SELECT * FROM `2b2t_donatelist`");
        int result = 0;
        try {
            while (query.next()) {
                result += query.getInt("price");
            }
            query.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().warning(ex.getLocalizedMessage());
        }
        return result;
    }

}
