package org.cn2b2t.common;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.cn2b2t.common.commands.*;
import org.cn2b2t.common.listeners.*;
import org.cn2b2t.common.managers.DataManager;
import org.cn2b2t.common.managers.DonateManager;
import org.cn2b2t.common.managers.UserDataManager;
import org.cn2b2t.common.managers.WorldBorderManager;
import org.cn2b2t.common.runnables.BoardCastRunnable;
import org.cn2b2t.common.runnables.RestartRunnable;

public class Main extends JavaPlugin {

    public static String pluginName = "2b2t-common";
    public static String pluginVersion = "1.0-SNAPSHOT";

    private static Main instance;

    public static Main getInstance() {
        return Main.instance;
    }

    public static String color(final String text) {
        return text.replaceAll("&", "§").replaceAll("§§", "&");
    }

    private void pluginMessage(String s) {
        this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + pluginName + " " + ChatColor.WHITE + pluginVersion + ChatColor.GRAY + s);
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        inits();
    }

    @Override
    public void onDisable() {
        this.shutdown();
    }

    private void inits() {
        regCmds();
        regListeners();
        pluginMessage("已启用。");
        saveConfig();
        saveDefaultConfig();
        reloadConfig();
        DataManager.init();
        UserDataManager.init();
        WorldBorderManager.init();
        DonateManager.init();
        new BoardCastRunnable();
        new RestartRunnable(21600);

//		BungeeTabListPlusBukkitAPI.registerVariable(this, new Serverprefix());
    }

    private void shutdown() {
        this.pluginMessage("已卸载。");
    }

    private void regCmds() {
        this.getCommand("suicide").setExecutor(new Suicide());
        this.getCommand("delay").setExecutor(new Delay());
        this.getCommand("help").setExecutor(new Help());
        this.getCommand("donate").setExecutor(new Donate());
        this.getCommand("donate").setTabCompleter(new Donate());
        this.getCommand("settings").setExecutor(new Settings());
        this.getCommand("settings").setTabCompleter(new Settings());
        this.getCommand("toggle").setExecutor(new Toggle());
        this.getCommand("toggle").setTabCompleter(new Toggle());
        this.getCommand("ignore").setExecutor(new Ignore());
        this.getCommand("ignore").setTabCompleter(new Ignore());
    }

    private void regListeners() {
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new Protection(), this);
        Bukkit.getPluginManager().registerEvents(new SignColor(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilColorPatch(), this);
        Bukkit.getPluginManager().registerEvents(new FastPot(), this);
        Bukkit.getPluginManager().registerEvents(new BedListener(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(), this);
        Bukkit.getPluginManager().registerEvents(new UnfairListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
    }


}
