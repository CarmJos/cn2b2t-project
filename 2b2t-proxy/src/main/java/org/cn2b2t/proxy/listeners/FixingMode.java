package org.cn2b2t.proxy.listeners;

import org.cn2b2t.proxy.functions.serverinfo.ServerInfoConfig;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class FixingMode implements Listener {

	@EventHandler(priority = 10)
	public void onPinging(PreLoginEvent e) {
		if (!ServerInfoConfig.allowedPlayers.contains(e.getConnection().getName())) {
			e.setCancelReason("§7抱歉，本服务器仍在维护。\n \n" + ServerInfoConfig.fixingMotd);
			e.setCancelled(true);
		}
	}

}
