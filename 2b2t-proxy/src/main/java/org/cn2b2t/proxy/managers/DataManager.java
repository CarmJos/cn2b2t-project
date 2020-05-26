package org.cn2b2t.proxy.managers;

import org.cn2b2t.proxy.functions.DatabaseConfig;
import org.cn2b2t.proxy.utils.data.Connection;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class DataManager {

	public static Connection tempConnection;
	public static Connection kettleConnection;
	public static Connection websiteConnection;

	public static void init() {
		DatabaseConfig.load();

		if (tempConnection != null || kettleConnection != null)
			throw new RuntimeException("Kettle Connection has been created.");
		DataManager.tempConnection = new Connection(
				DatabaseConfig.host,
				DatabaseConfig.port,
				DatabaseConfig.data_temp,
				DatabaseConfig.user,
				DatabaseConfig.passwd);
		DataManager.kettleConnection = new Connection(
				DatabaseConfig.host,
				DatabaseConfig.port,
				DatabaseConfig.data_kettle,
				DatabaseConfig.user,
				DatabaseConfig.passwd);
		DataManager.websiteConnection = new Connection(
				DatabaseConfig.host,
				DatabaseConfig.port,
				DatabaseConfig.data_website,
				DatabaseConfig.user,
				DatabaseConfig.passwd);


		tempConnection.info(tempConnection.connect() ? "temp数据库链接成功" : "temp数据库链接失败, 请检查配置");
		kettleConnection.info(kettleConnection.connect() ? "kettleConnection数据库链接成功" : "kettleConnection数据库链接失败, 请检查配置");
		websiteConnection.info(websiteConnection.connect() ? "websiteConnection数据库链接成功" : "websiteConnection数据库链接失败, 请检查配置");
		createTables();
	}

	private static void createTables() {
		tempConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + "tempuser" +
				"`(`id` INT(11) NOT NULL AUTO_INCREMENT ,`name` varchar(30) , `isonlinemode` tinyint, `version` int, PRIMARY KEY (`id`), UNIQUE KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
		tempConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + "targetserver" +
				"`(`id` INT(11) NOT NULL AUTO_INCREMENT ,`name` varchar(30) , `target` varchar(60), PRIMARY KEY (`id`), UNIQUE KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
		tempConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + "countplayer" +
				"`(`id` INT(11) NOT NULL AUTO_INCREMENT ,`server` varchar(30) , `players` int, PRIMARY KEY (`id`), UNIQUE KEY(`server`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
		tempConnection.SQLqueryWithNoResult("CREATE TABLE IF NOT EXISTS `" + "serverlist" +
				"`(`id` INT(11) NOT NULL AUTO_INCREMENT ,`server` varchar(30) , `port` int, PRIMARY KEY (`id`), UNIQUE KEY(`server`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
	}

	public static void putTarget(String s, String serverName) {
		ResultSet query = tempConnection.SQLquery("targetserver", "name", s);
		String sqlQuery = "";
		try {
			if (query != null) {
				tempConnection.SQLqueryWithNoResult(query.next() ?
						"UPDATE IGNORE targetserver SET target = '" + serverName + "' WHERE name = '" + s + "'" :
						"INSERT IGNORE INTO `targetserver`(`name`, `target`) VALUES ('" + s + "', '" + serverName + "')"
				);
				query.close();
			} else {
				tempConnection.SQLcheck();
			}
		} catch (SQLException e) {
			tempConnection.info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", sqlQuery);
		}
	}

	public static void createTempUser(String userName, boolean isOnlineMode, int version) {

	}


	public static Connection getTempConnection() {
		return tempConnection;
	}

	public static Connection getKettleConnection() {
		return kettleConnection;
	}

	public static Connection getWebsiteConnection() {
		return websiteConnection;
	}

}
