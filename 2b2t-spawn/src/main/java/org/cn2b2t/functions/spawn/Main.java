package org.cn2b2t.functions.spawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.cn2b2t.functions.spawn.listeners.SpawnProtectionListener;

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
        Bukkit.getPluginManager().registerEvents(new SpawnProtectionListener(), this);

        pluginMessage("已启用。");
    }

    @Override
    public void onDisable() {
        this.shutdown();
    }

    private void shutdown() {
        this.pluginMessage("已卸载。");
    }

}
