package org.cn2b2t.proxy.commands;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.functions.serverinfo.ServerInfoConfig;
import org.cn2b2t.proxy.listeners.FixingMode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class ProxyInfos extends Command {

	public ProxyInfos(final String name) {
		super(name);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length == 1) {
			String aim = args[0];
			if (aim.equalsIgnoreCase("reload")) {
				ServerInfoConfig.load();
				if (ServerInfoConfig.fixing) {
					Main.unregListener(new FixingMode());
					Main.regListener(new FixingMode());
				} else ProxyServer.getInstance().getPluginManager().unregisterListener(new FixingMode());
				sender.sendMessage("§7已重载完成。");
			} else {
				this.helpSender(sender);
			}
		} else {
			this.helpSender(sender);
		}
	}

	private void helpSender(final CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§8[§a?§8] §7BungeeCommon §fMoCi-Proxy");
		sender.sendMessage("  §8-  §breload §8#§7重载配置文件。");
		sender.sendMessage("");
	}
}
