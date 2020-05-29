package org.cn2b2t.functions.death.listeners;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.cn2b2t.logger.managers.LoggerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null && new Random().nextDouble() < 0.02) {
            List<String> lore = new ArrayList<>();
            lore.add(e.getDeathMessage());
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(e.getEntity().getName());
            meta.setLore(lore);
            skull.setItemMeta(meta);
            e.getDrops().add(skull);
            LoggerManager.log(LoggerManager.LogType.KILLED, e.getEntity(), e.getDeathMessage());
        } else {
            LoggerManager.log(LoggerManager.LogType.DEATH, e.getEntity(), e.getDeathMessage());
        }


    }


}
