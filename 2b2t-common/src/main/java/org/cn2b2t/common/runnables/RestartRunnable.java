package org.cn2b2t.common.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;

public class RestartRunnable {

	public static int count;

	public RestartRunnable(int time) {
		count = time;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().size() >= 1) {
					count--;
					switch (count) {
						case 30:
							Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Main.color("&8[&c&l!&8] &7服务器将在 &6" + count + "秒 &7后重启。")));
							Bukkit.getConsoleSender().sendMessage(Main.color("&8[&c&l!&8] &7服务器将在 &6" + count + "秒 &7后重启。"));
							break;
						case 10:
							alert(10);
							break;
						case 5:
							alert(5);
							break;
						case 4:
							alert(4);
							break;
						case 3:
							alert(3);
							break;
						case 2:
							alert(2);
							break;
						case 1:
							alert(1);
							break;
						case 0:
							break;
						case -3:
							Main.getInstance().getServer().spigot().restart();
							break;
						default:
							break;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 20L, 20L);
	}

	public static String getTimeString() {
		int temp = 0;
		StringBuilder sb = new StringBuilder();

		temp = count / 60 / 60 % 60;
		sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

		temp = count % 3600 / 60;
		sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

		temp = count % 3600 % 60;
		sb.append((temp < 10) ? "0" + temp : "" + temp);

		return sb.toString();
	}

	private static void alert(int time) {
		Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Main.color("&8[&c&l!&8] &7服务器将在 &6" + time + "秒 &7后重启。")));
		Bukkit.getConsoleSender().sendMessage(Main.color("&8[&c&l!&8] &7服务器将在 &6" + time + "秒 &7后重启。"));
	}


}
