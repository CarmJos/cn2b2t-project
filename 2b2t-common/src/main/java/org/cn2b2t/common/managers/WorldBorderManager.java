package org.cn2b2t.common.managers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;

public class WorldBorderManager {


	public static void init() {
		if ((Bukkit.getWorld("world").getWorldBorder().getSize() < 500000)) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (Bukkit.getOnlinePlayers().size() > 1) {
						double newsize = Bukkit.getWorld("world").getWorldBorder().getSize() + 15;
						if (newsize > 500000) {
							System.out.print("世界边界已最大。");
							Bukkit.getWorld("world").getWorldBorder().setSize(500000);
							Bukkit.getWorld("world_nether").getWorldBorder().setSize(62500);
							cancel();
						}
						Bukkit.getWorld("world").getWorldBorder().setSize(newsize, 600L);
						Bukkit.getWorld("world_nether").getWorldBorder().setSize(newsize / 8, 600L);
						System.out.println("世界边界将在30内扩大为 " + newsize);
					}
				}
			}.runTaskTimer(Main.getInstance(), 20L, 600L);
		}

	}


}
