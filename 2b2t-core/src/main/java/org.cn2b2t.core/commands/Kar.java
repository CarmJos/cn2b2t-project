package org.cn2b2t.core.commands;

import org.cn2b2t.core.managers.utils.ServersManager;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.utils.ColorParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kar implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ColorParser.parse("&7本服由 &f&lKa&7&lr &8Network &7提供技术支持。"));
                sender.sendMessage(ColorParser.parse("&7您可以输入 &c/kar go &7直接传送到Kar服务器。"));
                sender.sendMessage(ColorParser.parse("&7官方网站 &chttps://www.kar.red/"));
            } else {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("debugsb")) {
                        UserManager.getUser(sender).getSbRender().setLine(1, "init");
                        return true;
                    }
                }
                ServersManager.switchServer((Player) sender, "cn.kar.red");
            }
        } else {
            sender.sendMessage(ColorParser.parse("&7本服由 &f&lKa&7&lr &8Network &7提供技术支持。"));
            sender.sendMessage(ColorParser.parse("&7官方网站 &chttps://www.kar.red/"));
        }

        return true;
    }

}
