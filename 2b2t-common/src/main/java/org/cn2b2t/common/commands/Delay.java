package org.cn2b2t.common.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.common.runnables.RestartRunnable;
import org.cn2b2t.core.utils.ColorParser;

public class Delay implements CommandExecutor {

	private boolean hasDelayed = false;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("delay")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (ProfileData.get(p).getMonthlyDonate() > 10) {
					if (!hasDelayed) {
						RestartRunnable.count = RestartRunnable.count + 600;
						hasDelayed = true;
						Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ColorParser.parse(
								"&6-&e--&8----------------------------&e--&6-\n" +
										"&7服务器重启事项已被玩家 &e" + p.getName() + " &7推迟10分钟。\n" +
										"&6-&e--&8----------------------------&e--&6-"
						)));
					}
				} else {
					p.sendMessage(ColorParser.parse("&7本功能只有至今一个月内赞助&e超过10元&7的玩家才可使用。\n" +
							"&7输入 &6/donate &7，查看赞助方法，一起维护这个服务器吧！"));
				}
			} else {
				RestartRunnable.count = RestartRunnable.count + 3600;
				Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ColorParser.parse(
						"&6-&e--&8----------------------------&e--&6-\n" +
								"&7服务器重启事项已被控制台推迟一小时。\n" +
								"&6-&e--&8----------------------------&e--&6-"
				)));
				hasDelayed = false;
			}
		}
		return true;
	}
}
