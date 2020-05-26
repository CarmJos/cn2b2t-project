package org.cn2b2t.proxy.functions.permission;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 玩家的权限管理
 * 除加入游戏时的加载，读取和写入都是同步运行，如果如要异步运行，请注意线程安全。
 *
 * @Author LSeng
 */
public class PermissionManager {


//	/**
//	 * 卸载玩家的所有权限
//	 */
//	public void unloadPermission(){
//		for(Map.Entry<String, Boolean> perm : new HashMap<>(permissionAttachment.getPermissions()).entrySet()){
//			permissionAttachment.unsetPermission(perm.getKey());
//		}
//	}
//
//	public void loadPermission() {
//		List<PermGroup> groups = getUserGroup(getUser().getInkID());
//		this.maxOrder = getMaxOrder(groups);
//		PermissionLoader.loadGroupPermission(this, groups);//加载玩家的组权限
//		PermissionLoader.loadUserPermission(this, getPermsIgnoreGroup(getUser().getInkID()));//加载玩家单独权限
//	}

	/**
	 * 由ID获取组
	 *
	 * @param group_id 组id
	 * @return 组
	 */
	public static DataPermissionGroup getGroupByID(int group_id) {
		return PermissionLoader.getPermGroup(group_id);
	}

	/**
	 * 获得组下的所有玩家
	 *
	 * @param group_id 组ID
	 * @return 玩家的inkID和临时名字
	 */
	public static Map<Integer, String> getGroupMember(int group_id) {
		ResultSet userResult = PermissionDataManager.websiteConnection.SQLquery("select * from nl2_users");
		Map<Integer, String> map = new HashMap<>();
		try {
			if (userResult != null) {
				while (userResult.next()) {
					int target_id = userResult.getInt("group_id");
					Integer[] secondary_groups = PermissionLoader.getIntArray(userResult.getString("secondary_groups"));
					List<Integer> groups = new ArrayList<>(secondary_groups.length + 1);
					groups.add(target_id);
					groups.addAll(Arrays.asList(secondary_groups));
					if (groups.contains(group_id)) {
						map.put(userResult.getInt("id"), userResult.getString("username"));
					}
				}
				userResult.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获得玩家所在的组列表，不会顺便把继承的组也一起加载进去(但可以获得继承组的id)
	 * 假设玩家在组 MVP 中，组MVP继承VIP，则会获得组列表[MVP]
	 *
	 * @return 玩家所在的组列表
	 */
	public static List<DataPermissionGroup> getUserGroupsIgnoreExtend(int inkid) {
		ResultSet userResult = PermissionDataManager.websiteConnection.SQLquery("nl2_users", "id", inkid);
		try {
			if (userResult != null) {
				if (userResult.next()) {
					int group_id = userResult.getInt("group_id");
					Integer[] secondary_groups = PermissionLoader.getIntArray(userResult.getString("secondary_groups"));
					List<Integer> groups = new ArrayList<>(secondary_groups.length + 1);
					groups.add(group_id);
					groups.addAll(Arrays.asList(secondary_groups));

					return PermissionLoader.getPermGroups(groups);
				}
				userResult.close();
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}
		return new ArrayList<>();
	}

	/**
	 * 获得玩家的主组
	 *
	 * @param inkid 玩家的inkID
	 * @return 玩家的主组
	 */
	public static DataPermissionGroup getMainGroup(int inkid) {
		ResultSet groupResult = PermissionDataManager.websiteConnection.SQLquery("nl2_users", "id", inkid);
		try {
			if (groupResult != null) {
				if (groupResult.next()) {
					int group_id = groupResult.getInt("group_id");

					return PermissionLoader.getPermGroup(group_id);
				}
				groupResult.close();
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 加载玩家的副组
	 *
	 * @param inkid 玩家的inkID
	 * @return 玩家的副组列表
	 */
	public static List<DataPermissionGroup> getSecondaryGroups(int inkid) {
		ResultSet groupResult = PermissionDataManager.websiteConnection.SQLquery("nl2_users", "id", inkid);
		try {
			if (groupResult != null) {
				if (groupResult.next()) {
					Integer[] secondary_groups = PermissionLoader.getIntArray(groupResult.getString("secondary_groups"));
					List<Integer> groups = new ArrayList<>(secondary_groups.length);
					groups.addAll(Arrays.asList(secondary_groups));

					return PermissionLoader.getPermGroups(groups);
				}
				groupResult.close();
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}
		return new ArrayList<>();
	}

	/**
	 * 获得玩家所在的组列表，顺便把继承的组也一起加载进去
	 * 假设玩家在组 MVP 中，组MVP继承VIP，则会获得组列表[MVP, VIP]
	 *
	 * @return 玩家所在的组列表
	 */
	public static List<DataPermissionGroup> getUserGroup(int inkid) {
		List<DataPermissionGroup> permGroups = getUserGroupsIgnoreExtend(inkid);
		//加载继承组
		for (DataPermissionGroup group : new ArrayList<>(permGroups)) {
			if (!group.extend.isEmpty()) {//有继承的组
				//检测是否有重复组
				label:
				for (int i : group.extend) {
					for (DataPermissionGroup temp : permGroups) {
						if (temp.id == i) {
							continue label;
						}
					}
					permGroups.add(PermissionLoader.getPermGroup(i));//若无重复，则添加组
				}
			}
		}
		return permGroups;
	}

	/**
	 * 获得玩家的单独权限，不获得组权限
	 * 比如玩家拥有权限 kar.abc ，玩家在组 VIP 中，组VIP有权限 kar.vip ，则会获得权限列表[kar.abc]
	 *
	 * @return 玩家的权限列表
	 */
	public static List<DataPermission> getPermsIgnoreGroup(int inkid) {
		ResultSet userResult = PermissionDataManager.websiteConnection.SQLquery("game_perms_user", "inkid", inkid);
		try {
			if (userResult != null) {
				List<DataPermission> perms = new ArrayList<>();
				while (userResult.next()) {
					String perm = userResult.getString("perm");
					String base = userResult.getString("base");

					perms.add(new DataPermission(perm, base));
				}

				userResult.close();

				return perms;
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}
		return new ArrayList<>();
	}

	/**
	 * 获得玩家的所有权限，包括组权限
	 * 比如玩家拥有权限 kar.abc ，玩家在组 VIP 中，组VIP有权限 kar.vip ，则会获得权限列表[kar.abc, kar.vip]
	 *
	 * @return 玩家的权限列表
	 */
	public static List<DataPermission> getAllPerms(int inkid) {
		List<DataPermission> list = new ArrayList<>(getPermsIgnoreGroup(inkid));
		for (DataPermissionGroup group : getUserGroup(inkid)) {
			list.addAll(group.perms);
		}
		return list;
	}

	/**
	 * 更新某玩家的权限
	 *
	 * @param p 玩家
	 */
	public static void updatePermission(ProxiedPlayer p) {
		int inkid = PermissionDataManager.getInkID(p.getUniqueId().toString());
		if (inkid != -1) {
			List<DataPermissionGroup> groups = getUserGroup(inkid);
			PermissionLoader.loadGroupPermission(p, groups);//加载玩家的组权限
			PermissionLoader.loadUserPermission(p, getPermsIgnoreGroup(inkid));//加载玩家单独权限
		}
	}

	public static void unloadPermission(ProxiedPlayer p) {
		for (String s : new ArrayList<>(p.getPermissions())) {
			p.setPermission(s, false);
		}
	}

	/**
	 * 更新所有玩家的权限
	 */
	public static void updateAll() {
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			updatePermission(p);
		}
	}

	/**
	 * 获取存在的组
	 *
	 * @return 得到一个Map，Key为group_id，Value为组名
	 */
	public static Map<Integer, String> getExistedGroups() {
		Map<Integer, String> map = new HashMap<>();
		ResultSet result = PermissionDataManager.permdogConnection.SQLquery("select * from nl2_groups");
		try {
			if (result != null) {
				while (result.next()) {
					int group_id = result.getInt("id");
					String name = result.getString("name");
					map.put(group_id, name);
				}
				result.close();
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}

		return map;
	}

	/**
	 * 获得组列表中order最高的order值
	 */
	public static int getMaxOrder(List<DataPermissionGroup> list) {
		int maxOrder = -1;
		ResultSet result = PermissionDataManager.permdogConnection.SQLquery("select * from nl2_groups");
		try {
			if (result != null) {
				while (result.next()) {
					int group_id = result.getInt("id");
					for (DataPermissionGroup group : list) {
						if (group.id == group_id) {
							int order = result.getInt("order");
							if (order > maxOrder) {
								maxOrder = order;
							}
							break;
						}
					}
				}
				result.close();
			}
		} catch (SQLException e) {
			PermissionDataManager.permdogConnection.info(e.getLocalizedMessage());
		}
		return maxOrder;
	}

	/**
	 * 根据groupID获取组名
	 *
	 * @param group_id 组ID
	 * @return 组名
	 */
	public static String getGroupName(int group_id) {
		return getGroupName(new int[]{group_id})[0];
	}

	/**
	 * 根据groupID批量获取组名
	 *
	 * @param group_ids 批量获取的组的组ID
	 * @return 组名
	 */
	public static String[] getGroupName(int[] group_ids) {
		String[] result = new String[group_ids.length];
		Map<Integer, String> map = getExistedGroups();
		for (int index = 0; index < group_ids.length; index++) {
			int group_id = group_ids[index];
			result[index] = map.get(group_id);
			if (result[index] == null) {
				result[index] = "Unknown";
			}
		}
		return result;
	}

	/**
	 * 根据组名获取组ID
	 *
	 * @param name 组名
	 * @return 组ID
	 */
	public static int getGroupID(String name) {
		return getGroupID(new String[]{name})[0];
	}

	/**
	 * 根据组名批量获取组ID
	 *
	 * @param names 批量获取的组的组名
	 * @return 组ID
	 */
	public static int[] getGroupID(String[] names) {
		int[] result = new int[names.length];
		Map<Integer, String> map = getExistedGroups();
		for (int index = 0; index < names.length; index++) {
			String name = names[index];
			int value = -1;
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				if (entry.getValue().equals(name)) {
					value = entry.getKey();
					break;
				}
			}
			result[index] = value;
		}
		return result;
	}

}
