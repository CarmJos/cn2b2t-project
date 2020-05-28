package org.cn2b2t.functions.death;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.functions.death.commands.Suicide;
import org.cn2b2t.functions.death.listeners.BedListener;
import org.cn2b2t.functions.death.listeners.DeathListener;
import org.cn2b2t.functions.death.listeners.RespawnListener;
import org.cn2b2t.functions.death.managers.users.UserBedManager;


public class Main extends JavaPlugin {

    public static String pluginName = "2b2t-common";
    public static String pluginVersion = "1.0-SNAPSHOT";

    private static Main instance;

    public static Main getInstance() {
        return Main.instance;
    }

    private void pluginMessage(String s) {
        this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + pluginName + " " + ChatColor.WHITE + pluginVersion + ChatColor.GRAY + s);
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        this.getCommand("suicide").setExecutor(new Suicide());

        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new BedListener(), this);

        UserManager.regHandler(UserBedManager.class);

        pluginMessage("已启用。");
    }

    @Override
    public void onDisable() {
        UserManager.removeHandler(UserBedManager.class);
        this.pluginMessage("已卸载。");
    }

}
