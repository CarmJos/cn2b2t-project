package org.cn2b2t.proxy.functions.permission;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PermissionLoader {

	public static Integer[] getIntArray(String s) {
		if (s == null || s.isEmpty()) return new Integer[0];
		if (s.startsWith("[") && s.endsWith("]")) {
			String inner = s.substring(1, s.length() - 1);
			String[] split = inner.split(",");
			Integer[] result = new Integer[split.length];
			for (int i = 0; i < split.length; i++) {
				String sec = split[i];
				int num;
				if (sec.startsWith("\"") && sec.endsWith("\"")) {
					sec = sec.substring(1, sec.length() - 1);
				}
				try {
					num = Integer.parseInt(sec);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					ProxyServer.getInstance().getLogger().info("Illegal Array: " + s + "(index=" + i + ")");
					num = -1;
				}
				result[i] = num;
			}
			return result;
		} else {
			ProxyServer.getInstance().getLogger().info("Illegal Array: " + s);
			return new Integer[0];
		}
	}

	public static String toString(Integer[] arr) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int index = 0; index < arr.length; index++) {
			int group_id = arr[index];
			sb.append("\"").append(group_id).append("\"");
			if (index != arr.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static List<DataPermissionGroup> getPermGroups(List<Integer> group_ids) {
		return group_ids.stream().map(PermissionLoader::getPermGroup).collect(Collectors.toList());
	}

	public static DataPermissionGroup getPermGroup(int group_id) {
		ResultSet result = PermissionDataManager.websiteConnection.SQLquery("game_perms_group", "group_id", group_id);
		try {
			if (result != null) {
				List<DataPermission> perms = new ArrayList<>();
				List<Integer> extend = new ArrayList<>();
				while (result.next()) {
					String type = result.getString("type");
					String value = result.getString("value");
					String base = result.getString("base");
					if (type.equalsIgnoreCase("perm")) {
						perms.add(new DataPermission(value, base));
					} else if (type.equalsIgnoreCase("extend")) {
						try {
							extend.add(Integer.valueOf(value));
						} catch (NumberFormatException e) {
							e.printStackTrace();
							ProxyServer.getInstance().getLogger().log(Level.WARNING, "In table `game_perms_group`: id=" + result.getInt("id") +
									">value must be integer, not \"" + value + "\"");
						}
					}
				}
				result.close();
				return new DataPermissionGroup(group_id, extend, perms);
			}
		} catch (SQLException e) {
			PermissionDataManager.websiteConnection.info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "PermissionLoader.");
		}
		return new DataPermissionGroup(group_id, new ArrayList<>(), new ArrayList<>());
	}

	public static void loadGroupPermission(ProxiedPlayer p, List<DataPermissionGroup> groups) {
		for (DataPermissionGroup group : groups) {
			loadUserPermission(p, group.perms);
		}
	}

	public static void loadUserPermission(ProxiedPlayer p, List<DataPermission> perms) {
		for (DataPermission perm : perms) {
			String base = perm.base;
			if (base.equals("@")) {
				p.setPermission(perm.perm, true);
			}
		}
	}

}
