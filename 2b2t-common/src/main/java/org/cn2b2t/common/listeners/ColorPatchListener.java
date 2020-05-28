package org.cn2b2t.common.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import org.cn2b2t.core.utils.ColorParser;

public class ColorPatchListener implements Listener {

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

    @EventHandler
    public void colorSign(final SignChangeEvent e) {
        e.setLine(0, ColorParser.parse(e.getLine(0)));
        e.setLine(1, ColorParser.parse(e.getLine(1)));
        e.setLine(2, ColorParser.parse(e.getLine(2)));
        e.setLine(3, ColorParser.parse(e.getLine(3)));
    }


}
