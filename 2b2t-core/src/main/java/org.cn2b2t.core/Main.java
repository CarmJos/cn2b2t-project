package org.cn2b2t.core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.cn2b2t.core.commands.Kar;
import org.cn2b2t.core.listener.JoinListener;
import org.cn2b2t.core.listener.TabCompleteListener;
import org.cn2b2t.core.managers.users.UserValueManager;
import org.cn2b2t.core.managers.utils.*;
import org.cn2b2t.core.utils.ColorParser;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Main instance;
    public static PluginManager pluginManager;

    @Override
    public void onEnable() {
        Main.instance = this;
        pluginManager = Bukkit.getPluginManager();

        log("&c&lGhost " + this.getName().substring(5) + " &7开始加载...");

        log("&7├ &f加载配置文件管理器...");
        ConfigManager.loadConfig();

        log("&7├ &f链接数据库...");
        DataManager.init();

        log("&7├ &f启用Messager系统...");
        MessagerManager.enable();
        MessagerManager.registerChannel("chat");
//
        log("&7├ &f启用跨服管理器...");
        ServersManager.init();

        log("&7├ &f启用用户Value管理器....");
        UserValueManager.init();

        log("&7├ &f加载指令...");
        regCMD("kar", new Kar());

        log("&7├ &f加载监听器...");
        regListener(new TabCompleteListener());
        regListener(new JoinListener());

        log("&7├ &f注册变量...");

        log("&7├ &f注册Handlers...");

        log("&7└ &a&l加载完成！");


    }

    @Override
    public void onDisable() {
        log("&c&LGhost &f" + this.getName().substring(5) + " &7开始卸载...");


        log("&7├ &f卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

        log("&7├ &f移除Handlers...");

        log("&7├ &f关闭线程...");
        Main.getInstance().getServer().getScheduler().cancelTasks(Main.getInstance());

        log("&7└ &a&l卸载完成！");
    }

    public static void regListener(Listener listener) {
        pluginManager.registerEvents(listener, Main.getInstance());
    }

    public static void regCMD(String command, CommandExecutor commandExecutor) {
        getInstance().getCommand(command).setExecutor(commandExecutor);
    }

    public static void log(String message) {
        getInstance().getLogger().log(Level.INFO, ColorParser.parse(message));
    }


    public static JavaPlugin getInstance() {
        return instance;
    }
}
