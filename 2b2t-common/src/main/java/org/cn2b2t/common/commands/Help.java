package org.cn2b2t.common.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cn2b2t.common.Main;
import org.cn2b2t.core.utils.ColorParser;

public class Help implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("help")) {
			sender.sendMessage(ColorParser.parse(
					"&6-&e--&8----------------------------&e--&6-\n" +
					"&7查看服务器版本 &6/version\n" +
					"&7屏蔽玩家 &6/ignore <ID>\n" +
					"&7想要赞助 &6/donate\n" +
					"&7推迟重启 &6/delay\n" +
					"&7不想活了 &6/suicide\n" +
					"&7个人设置 &6/settings &7查看详情。\n" +
					"&7开关设置 &6/toggle &7查看详情。\n" +
					"&7其余更多原版自带指令，不做详解，自行搜索、询问。\n" +
					"&6-&e--&8----------------------------&e--&6-"
			));
		}
		return true;
	}

}