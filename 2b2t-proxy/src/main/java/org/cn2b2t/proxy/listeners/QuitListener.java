package org.cn2b2t.proxy.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.cn2b2t.proxy.functions.proxyuser.UserManager;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
//		IPManager.ipTables.remove(e.getPlayer().getAddress().getAddress().getHostAddress());
        UserManager.unloadUser(e.getPlayer().getName());
    }

}
