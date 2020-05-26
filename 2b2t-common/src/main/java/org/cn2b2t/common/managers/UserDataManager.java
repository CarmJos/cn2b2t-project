package org.cn2b2t.common.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cn2b2t.common.Main;

import java.io.File;
import java.io.IOException;

public class UserDataManager {


	private static File configfile;
	private static FileConfiguration datas;

	public static void init() {
		configfile = new File(Main.getInstance().getDataFolder(), "userdatas.yml");
		if (!configfile.exists()) {
			try {
				configfile.createNewFile();
			} catch (IOException ex) {
				Bukkit.getLogger().info("Could not load file userdatas.yml" + ex);
			}
		}

		datas = YamlConfiguration.loadConfiguration(configfile);
	}

	public static FileConfiguration getDatas() {
		return datas;
	}

	public static File getConfigfile() {
		return configfile;
	}


}
