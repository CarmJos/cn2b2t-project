package org.cn2b2t.proxy.functions;

import org.cn2b2t.proxy.managers.DataManager;
import org.cn2b2t.proxy.utils.UUIDUtils;
import net.md_5.bungee.api.ProxyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Account {

	public static String getLastIP(int inkID) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				String lastip = null;
				if (query.next()) {
					lastip = query.getString("lastip");
				}
				query.close();
				return lastip;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static String getName(int inkID) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				String name = null;
				if (query.next()) {
					name = query.getString("username");
				}
				query.close();
				return name;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static UUID getUniqueID(int inkID) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				UUID uuid = null;
				if (query.next()) {
					uuid = UUIDUtils.toUUID(query.getString("uuid"));
				}
				query.close();
				return uuid;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static UUID getUniqueID(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "username", name);
		try {
			if (query != null) {
				UUID uuid = null;
				if (query.next()) {
					uuid = UUIDUtils.toUUID(query.getString("uuid"));
				}
				query.close();
				return uuid;
			}
			throw new NullPointerException("不存在Name: " + name);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在Name: " + name);
	}

	public static boolean isOnlineMode(int inkID) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				UUID uuid = null;
				String mojanguuid = null;
				if (query.next()) {
					mojanguuid = query.getString("mojanguuid");
				}
				if(mojanguuid != null && !mojanguuid.isEmpty() && !mojanguuid.equalsIgnoreCase("null")){
					uuid = UUIDUtils.toUUID(mojanguuid);
				}
				query.close();
				return uuid != null;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static boolean isOnlineMode(String name) {
		return getMojangUniqueID(name) != null;
	}

	public static UUID getMojangUniqueID(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "username", name);
		try {
			if (query != null) {
				UUID result = null;
				String mojanguuid = null;
				if (query.next()) {
					mojanguuid = query.getString("mojanguuid");
				}
				if(mojanguuid != null && !mojanguuid.isEmpty() && !mojanguuid.equalsIgnoreCase("null")){
					result = UUIDUtils.toUUID(mojanguuid);
				}
				query.close();
				return result;
			}
			throw new NullPointerException("不存在Name: " + name);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在Name: " + name);
	}

	public static UUID[] getUUIDandMojangUniqueID(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "username", name);
		try {
			if (query != null) {
				UUID[] uuids = null;
				String uuid = null;
				String mojanguuid = null;
				if (query.next()) {
					uuids = new UUID[2];
					uuid = query.getString("uuid");
					mojanguuid = query.getString("mojanguuid");
				}
				if(uuid != null && !uuid.isEmpty() && !uuid.equalsIgnoreCase("null")){
					uuids[0] = UUIDUtils.toUUID(uuid);
				}
				if(mojanguuid != null && !mojanguuid.isEmpty() && !mojanguuid.equalsIgnoreCase("null")){
					uuids[1] = UUIDUtils.toUUID(mojanguuid);
				}
				query.close();
				return uuids;
			}
			return null;
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		return null;
	}

	public static int getInkID(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "username", name);
		try {
			if (query != null) {
				int id = -1;
				if (query.next()) {
					id = query.getInt("id");
				}
				query.close();
				return id;
			}
			throw new NullPointerException("不存在Name: " + name);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在Name: " + name);
	}

	public static int getInkIDByUUID(String uuid) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "uuid", uuid);
		try {
			if (query != null) {
				int id = -1;
				if (query.next()) {
					id = query.getInt("id");
				}
				query.close();
				return id;
			}
			throw new NullPointerException("不存在UUID: " + uuid);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在UUID: " + uuid);
	}

	public static String getEmail(int inkID) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", inkID);
		try {
			if (query != null) {
				String email = null;
				if (query.next()) {
					email = query.getString("email");
				}
				query.close();
				return email;
			}
			throw new NullPointerException("不存在InkID" + inkID);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在InkID" + inkID);
	}

	public static String getEmail(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "id", name);
		try {
			if (query != null) {
				String email = null;
				if (query.next()) {
					email = query.getString("email");
				}
				query.close();
				return email;
			}
			throw new NullPointerException("不存在Name: " + name);
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		throw new NullPointerException("不存在Name: " + name);
	}

	public static boolean isRegistered(String name) {
		ResultSet query = DataManager.getWebsiteConnection().SQLquery("nl2_users", "username", name);
		try {
			return query != null && query.next();
		} catch (SQLException e) {
			DataManager.getWebsiteConnection().info(e.getLocalizedMessage());
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "code: {0}", query);
		}
		return false;
	}

	public static UUID getUUIDFromMojang(String name) {
		String result = sendHTTP("https://api.mojang.com/users/profiles/minecraft/" + name);
		String uuid = "";
		int start = result.indexOf("id\":\"") + 5;
		while (result.charAt(start) != '\"') {
			uuid += result.charAt(start);
			start++;
		}
		return UUIDUtils.toUUID(uuid);
	}

	public static String getNameFromMojang(UUID uuid) {
		String result = sendHTTP("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-","") + "/names");
		String lastName = "";
		if(result == null){
			return null;
		}
		int start = result.lastIndexOf("name\":\"") + 7;
		while (result.charAt(start) != '\"') {
			lastName += result.charAt(start);
			start++;
		}
		return lastName;
	}

	protected static String sendHTTP(String keyword){
		String result = "";//访问返回结果
		BufferedReader read = null;//读取访问结果
		Map<String, List<String>> map = new HashMap();

		try {
			//创建url
			URL realurl = new URL(keyword);
			//打开连接
			URLConnection connection = realurl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
//                        connection.setRequestProperty("user-agent",
//                                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setReadTimeout(2000);
			//建立连接
			connection.connect();
			// 获取所有响应头字段
			map = connection.getHeaderFields();
			// 遍历所有的响应头字段，获取到cookies等
//                        for (String key : map.keySet()) {
//                            System.out.println(key + "--->" + map.get(key));
//                        }
			// 定义 BufferedReader输入流来读取URL的响应
			read = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			String line;//循环读取
			while ((line = read.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (read != null) {//关闭流
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		System.out.println("keyword = " + keyword + "\nresult = " + result);
		if (result.equals("")) {
			return null;
		} else {
			return result;
		}

	}

}