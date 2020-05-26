package org.cn2b2t.proxy.functions.permission;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.managers.DataManager;
import org.cn2b2t.proxy.utils.data.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author cam
 */
public class PermissionDataManager {

	public static Connection permdogConnection;
	public static Connection websiteConnection;

	public static void init() {
		permdogConnection = DataManager.getKettleConnection();
		websiteConnection = DataManager.getWebsiteConnection();
	}

	public static String getUniqueID(int inkID) {
		ResultSet query = permdogConnection.SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				String uuid = null;
				if (query.next()) {
					uuid = query.getString("uuid");
				}
				query.close();
				return uuid;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			permdogConnection.info(e.getLocalizedMessage());
			Main.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static int getInkID(String uuid) {
		ResultSet query = permdogConnection.SQLquery("nl2_users", "uuid", uuid.replace("-", ""));
		try {
			if (query != null) {
				int id = -1;
				if (query.next()) {
					id = query.getInt("id");
				}
				query.close();
				return id;
			}
			return -1;
		} catch (SQLException e) {
			permdogConnection.info(e.getLocalizedMessage());
			Main.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		return -1;
	}


}
