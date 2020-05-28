package org.cn2b2t.functions.death.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.functions.death.managers.users.UserBedManager;

public class BedListener implements Listener {

	@EventHandler
	public void onBed(PlayerInteractEvent e) {
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase("world")
				&& e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getClickedBlock() != null
				&& (e.getClickedBlock().getType() == Material.BED || e.getClickedBlock().getType() == Material.BED_BLOCK)) {
			UserBedManager.get(e.getPlayer()).saveBedLocation(e.getClickedBlock().getLocation());
			e.getPlayer().setBedSpawnLocation(e.getClickedBlock().getLocation(), true);
			e.getPlayer().sendMessage("§7已为您设置出生点。注意，若床被破坏，您将失去无法再次与此处重生！");
		}
	}


}
