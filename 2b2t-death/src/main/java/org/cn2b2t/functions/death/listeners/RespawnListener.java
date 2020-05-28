package org.cn2b2t.functions.death.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.cn2b2t.functions.death.managers.users.UserBedManager;
import org.cn2b2t.functions.death.managers.utils.DeathManager;

public class RespawnListener implements Listener {


    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.setMaxHealth(20);
        p.setHealth(20);
        p.setFoodLevel(30);

        if (UserBedManager.get(p).isBedThere()) {
            e.setRespawnLocation(UserBedManager.get(p).getBedLocation());
        } else {
            p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
            e.setRespawnLocation(DeathManager.randomLocation(Bukkit.getWorld("world")));
        }

    }

    @EventHandler
    public void onDeath(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getName().equalsIgnoreCase("world_the_end")) {
            if (UserBedManager.get(p).isBedThere()) {
                p.teleport(UserBedManager.get(p).getBedLocation());
            } else {
                p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
                p.teleport(DeathManager.randomLocation(Bukkit.getWorld("world")));
            }

        }
    }


}
