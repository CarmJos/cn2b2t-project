package org.cn2b2t.common.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.utils.ColorParser;

import java.util.List;
import java.util.stream.Collectors;

public class Ignore implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ignore") && sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length >= 1) {
				String aim = args[0];
				if (Bukkit.getPlayer(aim) != null && Bukkit.getPlayer(aim).isOnline()) {
					ignore(p, Bukkit.getPlayer(aim));
				} else {
					p.sendMessage("&7您所想屏蔽的玩家并不在线！");
				}
			} else {
				help(p);
			}
		}
		return true;
	}

	private static void help(Player p) {
		p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
				"&7屏蔽某人消息 &6/ignore &8<&6ID&8>\n" +
				"&7注意，每次屏蔽退出服务器后即失效，若需要请再次屏蔽。" +
				"&6-&e--&8----------------------------&e--&6-"));
	}

	private static void ignore(Player p, Player target) {
		if (p != target) {
			p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
					"&7已成功屏蔽 &2" + target.getName() + "&7 !\n" +
					"&7注意，本次屏蔽退出服务器后即失效，若需要请再次屏蔽。" +
					"&6-&e--&8----------------------------&e--&6-"));
			ProfileData.get(p).ignorePlayer(target.getUniqueId());
		} else {
			p.sendMessage(ColorParser.parse("&7您无法屏蔽您自己！"));
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
			case 1: {
				return Bukkit.getOnlinePlayers().stream().filter(p -> sender instanceof Player
						&& p != sender
						&& StringUtil.startsWithIgnoreCase(p.getName(), args[0].toLowerCase())).map(HumanEntity::getName)
						.collect(Collectors.toList());
			}
			default:
				return ImmutableList.of();
		}
	}


}
