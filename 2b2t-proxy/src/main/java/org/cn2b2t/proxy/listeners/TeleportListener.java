package org.cn2b2t.proxy.listeners;

import org.cn2b2t.proxy.functions.proxyuser.ProxyUser;
import org.cn2b2t.proxy.functions.proxyuser.UserManager;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeleportListener implements Listener {

    @EventHandler
    public void onConnect(ServerConnectEvent e) {

        ProxyUser u = UserManager.getUser(e.getPlayer().getName());
        if (u != null && u.hasTarget()) {
            e.setTarget(u.getServerTarget());
//			if (u.getTargetServerName().equalsIgnoreCase("SYSTEM.MAINLOBBY")) {
//				u.updatePermission();
//			}
        }

    }


}
