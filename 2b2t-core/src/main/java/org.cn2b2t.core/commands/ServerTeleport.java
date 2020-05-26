package org.cn2b2t.core.commands;


import org.cn2b2t.core.managers.utils.ServersManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerTeleport implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length == 1) {
				ServersManager.teleport((Player) sender, args[0]);
				return true;
			}
		}
		return true;
	}


}
