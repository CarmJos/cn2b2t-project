package org.cn2b2t.common.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class UnfairListener implements Listener {


	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getItem().getType() != Material.AIR
				&& e.getItem().hasItemMeta()
				&& e.getItem().getItemMeta().hasEnchants()) {
			e.getItem().getItemMeta().getEnchants().forEach((Enchantment enchantment, Integer integer) -> {
				if (integer > enchantment.getMaxLevel()) {
					e.setCancelled(true);
					e.getItem().removeEnchantment(enchantment);
				}
			});
		}
	}

}
