package org.cn2b2t.proxy.commands;


import org.cn2b2t.proxy.functions.permission.PermissionManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

/**
 * @author cam
 */
public class ProxyPermdog extends Command {

	public ProxyPermdog(final String name) {
		super(name);
	}

	@Override
	public void execute(final CommandSender sender, final String[] args) {

		if (!sender.hasPermission("moci.admin")) {
			return;
		}

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("user")) {
				if (args.length == 2) {
					ProxiedPlayer p = getPlayer(args[1]);
					if (p != null) {
						sender.sendMessage("§8----------------------------------------");
						sender.sendMessage("§7UUID §e" + p.getUUID());

						sender.sendMessage("§8----------------------------------------");
					} else {
						sender.sendMessage("玩家" + args[1] + "不存在");
					}
				}
			} else if (args[0].equalsIgnoreCase("uuid")) {
				if (args.length == 2) {
					ProxiedPlayer p = getPlayer(args[1]);
					if (p != null) {
						sender.sendMessage("UUID:" + p.getUUID());
						sender.sendMessage("RealPerm:"
								+ Arrays.toString(p.getPermissions().toArray()));
					} else {
						sender.sendMessage("玩家" + args[1] + "不存在");
					}
				}
			} else if (args[0].equalsIgnoreCase("update")) {

                PermissionManager.updateAll();

				sender.sendMessage("权限狗数据已更新");
			}
		}

	}

	private ProxiedPlayer getPlayer(String name) {
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

}
