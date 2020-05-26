package org.cn2b2t.proxy;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.cn2b2t.proxy.functions.DatabaseConfig;
import org.cn2b2t.proxy.functions.permission.PermissionMain;
import org.cn2b2t.proxy.functions.serverinfo.ServerInfoMain;
import org.cn2b2t.proxy.listeners.LoginListener;
import org.cn2b2t.proxy.listeners.QuitListener;
import org.cn2b2t.proxy.listeners.ServerKickListener;
import org.cn2b2t.proxy.listeners.TeleportListener;
import org.cn2b2t.proxy.managers.DataManager;
import org.cn2b2t.proxy.managers.UserValueManager;

import java.util.ArrayList;
import java.util.List;

public class Main extends Plugin {

    public static Main plugin;
    public static Main instance;
    public CommandSender console;
    List<ScheduledTask> threads = new ArrayList<>();

    @Override
    public void onEnable() {

        Main.plugin = this;
        Main.instance = this;
        console = BungeeCord.getInstance().getConsole();


        console.sendMessage("§8----------------------------------------");
        BungeeCord.getInstance().getServers().forEach((s, serverInfo) -> console.sendMessage("§a" + serverInfo.getAddress().getPort() + "§7 - §f" + s));
        console.sendMessage("§8----------------------------------------");

        loadConfigs();

        DataManager.init();
        PermissionMain.init();
        ServerInfoMain.init();
        UserValueManager.init();

        regListener(new ServerKickListener());
        regListener(new TeleportListener());
        regListener(new QuitListener());
        regListener(new LoginListener());

//		threads.add(new BungeeScheduler().schedule(Main.getInstance(), PlayerCounter::count, 5, 5, TimeUnit.SECONDS));
    }

    @Override
    public void onDisable() {
        for (ScheduledTask task : threads) {
            task.cancel();
        }
//        DataManager.getTempConnection().SQLqueryWithNoResult("TRUNCATE TABLE `countplayer`;");
    }

    public static void loadConfigs() {

        DatabaseConfig.load();
        Config.load();
    }

    public static void regCommmand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(Main.getInstance(), command);
    }

    public static void regListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(Main.getInstance(), listener);
    }

    public static void unregListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
    }

    public static Main getInstance() {
        return Main.instance;
    }

    public static Main getPlugin() {
        return plugin;
    }
}