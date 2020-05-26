package org.cn2b2t.common.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;

public class KitManager {

    public static void getSpawnItem(Player p) {
        p.getInventory().setHeldItemSlot(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                p.getInventory().setItem(0, new ItemStack(Material.ENDER_PEARL, 16));
                p.getInventory().setItem(1, new ItemStack(Material.WATER_BUCKET));
                p.getInventory().setItem(7, new ItemStack(Material.BREAD, 32));
                p.getInventory().setItem(8, new ItemStack(Material.BOAT, 1));
                cancel();
            }
        }.runTaskLater(Main.getInstance(), 5L);

    }

}
