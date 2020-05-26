package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.cn2b2t.common.functions.ProfileData;

public class RespawnListener implements Listener {


	@EventHandler
	public void onDeath(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(30);

		if (ProfileData.getProfileData(p).isBedThere()) {
			p.teleport(ProfileData.getProfileData(p).bedLocation);
		} else {
			p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}

	}

	@EventHandler
	public void onDeath(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if (e.getFrom().getName().equalsIgnoreCase("world_the_end")) {
			if (ProfileData.getProfileData(p).isBedThere()) {
				p.teleport(ProfileData.getProfileData(p).bedLocation);
			} else {
				p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
				p.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}

		}
	}


}
