package org.cn2b2t.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cn2b2t.core.managers.users.UserValueManager;
import org.cn2b2t.core.managers.utils.MessagerManager;
import org.cn2b2t.core.managers.utils.ServersManager;
import org.cn2b2t.core.utils.ColorParser;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Core implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(ColorParser.parse("&7本服由 &f&lKa&7&lr &8Network &7提供技术支持。"));
            sender.sendMessage(ColorParser.parse("&7官方网站 &chttps://www.kar.red/"));
            return true;
        }
        String aim = args[0].toLowerCase();
        switch (aim) {
        }


        return true;
    }
}