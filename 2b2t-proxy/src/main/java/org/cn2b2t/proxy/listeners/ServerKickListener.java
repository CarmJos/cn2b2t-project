package org.cn2b2t.proxy.listeners;

import org.cn2b2t.proxy.Main;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerKickListener implements Listener {


	@EventHandler
	public void onServerKickEvent(ServerKickEvent e) {
		ServerInfo kickedFrom = null;

		if (e.getPlayer().getServer() != null) {
			kickedFrom = e.getPlayer().getServer().getInfo();
		} else if (Main.getInstance().getProxy().getReconnectHandler() != null) {
			kickedFrom = Main.getInstance().getProxy().getReconnectHandler().getServer(e.getPlayer());
		} else {
			kickedFrom = AbstractReconnectHandler.getForcedHost(e.getPlayer().getPendingConnection());
			if (kickedFrom == null) {
				kickedFrom = ProxyServer.getInstance().getServerInfo(e.getPlayer().getPendingConnection().getListener().getDefaultServer());
			}
		}

		if (kickedFrom != null && kickedFrom.equals(Main.getInstance().getProxy().getServerInfo("SYSTEM.LOGIN"))) {
			return;
		}


		ServerInfo kickTo = Main.getInstance().getProxy().getServerInfo("SYSTEM.LOGIN");

		if (kickedFrom != null && kickedFrom.equals(kickTo)) {
			return;
		}

		e.setCancelled(true);
		e.setCancelServer(kickTo);


		e.getPlayer().sendMessage(
				new TextComponent(
						ChatColor.translateAlternateColorCodes('&', "&7由于您先前所在的服务器出现故障，现已将您移至大厅。")));
		e.getPlayer().sendMessage(
				new TextComponent(
						ChatColor.translateAlternateColorCodes('&', "&7故障信息如下 &r")
								+ ChatColor.stripColor(BaseComponent.toLegacyText(e.getKickReasonComponent()))));


	}
}