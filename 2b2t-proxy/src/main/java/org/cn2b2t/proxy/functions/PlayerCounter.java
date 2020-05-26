package org.cn2b2t.proxy.functions;

import java.util.HashMap;


public class PlayerCounter {

	private static HashMap<String, Integer> counts = new HashMap<>();


//	public static void count() {
//		if (BungeeCord.getInstance().getPlayers().size() < 1) return;
//		BungeeCord.getInstance().getServers().forEach((s, serverInfo) -> {
//			int player = serverInfo.getPlayers().size();
//			new BungeeScheduler().runAsync(Main.getInstance(), () -> {
//
//				ResultSet query = DataManager.getTempConnection().SQLquery("countplayer", "server", s);
//				try {
//					if (query != null) {
//						if (query.next()) {
//							DataManager.getTempConnection().update("countplayer", "players", player,
//									"server", s);
//						} else {
//							DataManager.getTempConnection().insert("countplayer",
//									new String[]{"server", "players"},
//									new Object[]{s, player});
//						}
//						query.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//
//			});
//		});
//		update();
//	}
//
//	public static Integer get(String serverName) {
//		return get(serverName, false);
//	}
//
//	public static Integer get(String serverName, boolean isMultiServer) {
//		if (isMultiServer) {
//			return (!counts.isEmpty() && serverName.equalsIgnoreCase("#ALL") || serverName.equalsIgnoreCase("#AII")) ?
//					counts.keySet().stream().mapToInt(s -> counts.getOrDefault(s, 0)).sum()
//					: counts.entrySet().stream().filter(entry -> entry.getKey().startsWith(serverName)).mapToInt(Map.Entry::getValue).sum();
//		} else {
//			return (!counts.isEmpty() && serverName.equalsIgnoreCase("#ALL") || serverName.equalsIgnoreCase("#AII")) ?
//					counts.keySet().stream().mapToInt(s -> counts.getOrDefault(s, 0)).sum()
//					: counts.getOrDefault(serverName, 0);
//		}
//	}

//	public static void update() {
//		counts.clear();
//		Connection temp = DataManager.getTempConnection();
//		ResultSet rs = temp.SQLqueryInTable("countplayer");
//		try {
//			if (rs != null) {
//				while (rs.next()) {
//					counts.put(rs.getString("server"), rs.getInt("players"));
//				}
//				rs.close();
//			}
//		} catch (SQLException e) {
//			cn.moci.kettle.managers.utils.DataManager.getKettleConnection().info(e.getLocalizedMessage());
//			Bukkit.getLogger().log(Level.WARNING, "code: {0}", rs);
//		}
//	}

}
