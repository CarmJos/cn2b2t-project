package org.cn2b2t.proxy.listeners;

import org.cn2b2t.proxy.functions.serverinfo.ServerInfoConfig;
import org.cn2b2t.proxy.functions.serverinfo.ServerInfos;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

	@EventHandler(priority = 10)
	public void onPinging(final ProxyPingEvent e) {
		if (e.getConnection() == null || e.getResponse() == null
				|| e.getConnection().getVirtualHost() == null
				|| e.getConnection().getVirtualHost().getHostName() == null
				|| e.getConnection().getVirtualHost().getHostName().equalsIgnoreCase("127.0.0.1"))
			return;

//		try {
//			String ip = e.getConnection().getAddress().getAddress().getHostAddress();
//			if (IPManager.isAttacking(ip)) {
//				e.getConnection().disconnect(new TextComponent());
//				IPManager.updateTime(ip);
//				return;
//			}
//			IPManager.updateTime(ip);
//		} catch (Exception exception) {
//			e.getConnection().disconnect(new TextComponent());
//			return;
//		}

		final ServerPing ping = (e.getResponse() == null) ? new ServerPing() : e.getResponse();

		ServerInfos info = ServerInfoConfig.getServerInfo(e.getConnection().getVirtualHost().getHostName());

		ping.setDescription(info.getMotd());
		ping.setPlayers(new ServerPing.Players(info.getMaxPlayers()
				, info.getPlayerNumber()
				, info.getPlayerinfo()));

		ServerPing.Protocol version = ping.getVersion();

		version.setName(ServerInfoConfig.fixing ? ServerInfoConfig.fixingInfo : info.getVersion());
		if (ServerInfoConfig.fixing) version.setProtocol(999);
		ping.setVersion(version);
		ping.getModinfo().setType(info.getModType());
		ping.setFavicon(info.getFavicon());
		e.setResponse(ping);
	}


}
