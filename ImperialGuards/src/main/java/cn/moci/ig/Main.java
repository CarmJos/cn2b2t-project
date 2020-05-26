package cn.moci.ig;

import cn.moci.ig.commands.IG;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    //<editor-fold defaultstate="collapsed" desc="定义和基础方法">
    public static String pluginName = "ImperialGuards";
    public static String pluginVersion = "dev-SNAPSHOT";

    private static Main instance;
    public ProtocolManager protocolManager;

    public static Main getInstance() {
        return Main.instance;
    }

    public static String color(final String text) {
        return text.replaceAll("&", "§");
    }

    private void pluginMessage(String s) {
        this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + pluginName + " " + ChatColor.WHITE + pluginVersion + ChatColor.GRAY + s);
    }

    //</editor-fold>
    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        Main.instance = this;
        regCmds();
        pluginMessage("已启用。");

    }


    private void regCmds() {
        this.getCommand("ig").setExecutor(new IG());
    }


}
