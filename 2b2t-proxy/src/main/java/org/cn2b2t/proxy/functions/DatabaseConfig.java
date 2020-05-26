package org.cn2b2t.proxy.functions;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.utils.FileConfig;
import net.md_5.bungee.config.Configuration;

public class DatabaseConfig {

	public static Configuration databaseConfig;

	public static String host;
	public static int port;
	public static String user;
	public static String passwd;


	public static String data_kettle;
	public static String data_temp;
	public static String data_website;

	public static void load() {
		databaseConfig = null;
		DatabaseConfig.databaseConfig = new FileConfig(Main.getPlugin(), "database.yml").getConfig();

		host = databaseConfig.getString("Database.host");
		port = databaseConfig.getInt("Database.port");
		user = databaseConfig.getString("Database.user");
		passwd = databaseConfig.getString("Database.password");


		data_kettle = databaseConfig.getString("Database.databases.kettle");
		data_temp = databaseConfig.getString("Database.databases.temp");
		data_website = databaseConfig.getString("Database.databases.website");

	}


}
