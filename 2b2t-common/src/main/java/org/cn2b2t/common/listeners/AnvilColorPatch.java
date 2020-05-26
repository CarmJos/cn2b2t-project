package org.cn2b2t.common.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilColorPatch implements Listener {

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent evt) {
		if (evt.isCancelled() || evt.getInventory().getType() != InventoryType.ANVIL
				|| evt.getSlotType() != InventoryType.SlotType.RESULT) {
			return;
		}
		if (evt.getCurrentItem().hasItemMeta()) {
			ItemMeta meta = evt.getCurrentItem().getItemMeta();
			if (meta.getDisplayName().contains("&")) {
				String name = meta.getDisplayName().replaceAll("&", "§").replaceAll("§§", "&");
				meta.setDisplayName(name);
				evt.getCurrentItem().setItemMeta(meta);
			}
		}
	}

}
