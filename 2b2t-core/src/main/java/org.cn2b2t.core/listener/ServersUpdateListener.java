package org.cn2b2t.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.managers.utils.ServersManager;

public class ServersUpdateListener implements Listener {


    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        if (ServersManager.getLocalRegisteredServer() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ServersManager.getLocalRegisteredServer().writeInfo();
                }
            }.runTaskLaterAsynchronously(Main.getInstance(), 10L);
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent e) {
        ServersManager.requests.remove(e.getPlayer());
        if (ServersManager.getLocalRegisteredServer() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ServersManager.getLocalRegisteredServer().writeInfo();
                }
            }.runTaskLaterAsynchronously(Main.getInstance(), 10L);
        }
    }
}
