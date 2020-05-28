package org.cn2b2t.functions.death.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cn2b2t.common.Main;

public class Suicide implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("suicide")) {
            if (sender instanceof Player) ((Player) sender).setHealth(0);
            sender.sendMessage(Main.color(
                    "&f这个世界虽然不完美，但我们仍然可以治愈自己。\n" +
                            "&7全国免费心理咨询热线 &a&l800-810-1117"));
            return true;
        }
        return true;
    }
}