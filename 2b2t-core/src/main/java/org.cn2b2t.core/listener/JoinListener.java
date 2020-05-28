package org.cn2b2t.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.modules.users.User;

public class JoinListener implements Listener {


    @EventHandler(priority = EventPriority.LOWEST)
    public void loadUser(PlayerJoinEvent e) {
        User u = UserManager.registeruser(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void unloadUser(PlayerQuitEvent e) {
        UserManager.unregisterUser(e.getPlayer());
    }

}
