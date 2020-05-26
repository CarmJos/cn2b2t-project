package org.cn2b2t.core.managers.utils;

import org.cn2b2t.core.Main;
import org.bukkit.configuration.Configuration;

public class ConfigManager {

    public static Configuration config;


    public static void loadConfig() {
        Main.getInstance().saveDefaultConfig();
        Main.getInstance().saveConfig();
        Main.getInstance().reloadConfig();


        config = Main.getInstance().getConfig();

    }

}
