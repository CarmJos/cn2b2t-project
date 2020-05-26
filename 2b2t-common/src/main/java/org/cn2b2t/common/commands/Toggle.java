package org.cn2b2t.common.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.cn2b2t.common.Main;
import org.cn2b2t.common.functions.ProfileData;

import java.util.ArrayList;
import java.util.List;

public class Toggle implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("toggle") && sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length >= 1) {
				String aim = args[0];
				if (aim.equalsIgnoreCase("scoreboard") || aim.equalsIgnoreCase("sb")) {
					ProfileData.getProfileData(p).setShowScoreboard(!ProfileData.getProfileData(p).showScoreboard);
				} else if (aim.equalsIgnoreCase("deathmessages") || aim.equalsIgnoreCase("dm")) {
					ProfileData.getProfileData(p).setShowDeathMessages(!ProfileData.getProfileData(p).showDeathMessages);
				} else if (aim.equalsIgnoreCase("joinandquitalerts") || aim.equalsIgnoreCase("jaqa")) {
					ProfileData.getProfileData(p).setShowJoinAndLeaveAlerts(!ProfileData.getProfileData(p).showJoinAndLeaveAlerts);
				} else {
					help(p);
				}
			} else {
				help(p);
			}
		}
		return true;
	}
	// /toggle scoreboard / sb
	// /toggle joinandquitmessages / jaqm

	private static void help(Player p) {
		p.sendMessage(Main.color("&6-&e--&8----------------------------&e--&6-\n" +
				"&7开关积分榜  &6/toggle scoreboard\n" +
				"&7开关死亡消息 &6/toggle deathmessages\n" +
				"&7开关玩家加入、退出提示信息\n" +
				"     &6/toggle joinandquitalerts\n" +
				"&6-&e--&8----------------------------&e--&6-"));
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
			case 1: {
				List<String> completions = new ArrayList<>();
				String[] strings = new String[]{"scoreboard", "deathmessages", "joinandquitalerts"};
				for (String s : strings) {
					if (StringUtil.startsWithIgnoreCase(s, args[0].toLowerCase())) {
						completions.add(s);
					}
				}
				return completions;
			}
			default:
				return ImmutableList.of();
		}
	}


}

