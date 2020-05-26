package org.cn2b2t.proxy.listeners;

import org.cn2b2t.proxy.Config;
import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.functions.Account;
import org.cn2b2t.proxy.functions.proxyuser.PreUser;
import org.cn2b2t.proxy.functions.proxyuser.ProxyUser;
import org.cn2b2t.proxy.functions.proxyuser.UserManager;
import org.cn2b2t.proxy.functions.serverinfo.ServerInfoConfig;
import org.cn2b2t.proxy.managers.DataManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/* proxy端用户
 * 在用户进入子服（登陆服或大厅服）之前，先经过logging in的过程，在此过程决定玩家是否需要正版验证、是否可以进入等
 * 这些函数都不是在主线程中运行的，所以无需担心主线程堵塞
 * 但是，尽量减少验证的时间可以提减少玩家进入服务器的时间，太长的登录时间可能会造成time out，降低玩家体验
 * 不过，大部分时间并不在这些事件中耗费，而是握手包和mojang验证的时间中耗费
 *
 * 当用户进入服务器时，
 *
 * 如果该用户已注册，且该玩家是正版，则设置该玩家为正版登入
 *    接着获取该用户的UUID(1)，若与数据库中的mojangUUID不匹配(2)，则另开一个线程去更新玩家(2)的名字，然后再去寻找(1)的UUID是否在数据库中的mojangUUID存在
 *       若存在，则证明该用户注册过，更新用户(1)的名字(要检测是否占用)，并传送至大厅
 *       若不存在，则证明该用户未注册过，告知到网站上注册
 *
 * 若该用户已注册，且该玩家是盗版，则设置该玩家为盗版登入，并传送至登录服(输入密码登陆)
 *
 * 如果该用户未注册，在mojang中获取该name的uuid，若存在，在数据库中查找mojangUUID是否存在，若存在，更新其名字(要检测是否占用)，并设为正版
 * 	  否则，从mojang中检测该用户是否为正版ID，若不是，告知请在官网上登陆
 *
 *
 * 检测名字占用(开另一个线程去检测)
 * 当更新一个正版玩家的名字时，从数据库中查找是否占用了另一个玩家的名字，若占用
 *    占用的是一个正版玩家的名字，在mojang获取该正版玩家的新名字并更新，再次检测是否占用(递归到无占用或占用盗版玩家名字为止)
 *    占用的是一个盗版玩家的名字，将该盗版玩家的名字设为NULL
 *
 *
 * 传送至登录服后，接下来的事情就交给登录服处理了。
 */

public class LoginListener implements Listener {

	public static List<String> regOffline = new ArrayList<>();

	public static boolean recover = ServerInfoConfig.fixing;

	private boolean debug_time = false;

	private static String hideName(String name){
		if(name.length() >= 3){
			int length = name.length()-3;
			StringBuilder nameBuilder = new StringBuilder(name.substring(0, 3));
			for(int i = 0; i<length; i++){
				nameBuilder.append("*");
			}
			name = nameBuilder.toString();
			return name;
		} else {
			return name;
		}
	}

	/*
	 * 玩家登陆之前，此时玩家还没有UUID，是否验证正版在此处决定
	 */
	@EventHandler
	public void onPreLogin(PreLoginEvent e) {
		long start = 0;
		if(debug_time) start = System.currentTimeMillis();

		if (e.getConnection() == null || e.isCancelled()) return;

		PendingConnection connection = e.getConnection();

		String playerName = connection.getName();
		//名字检测
		if(useIllegalCharacter(playerName)){
			e.setCancelReason("§b--§3-§8----------------------------------------§3-§b--\n" +
					"§7尊敬的玩家您好，\n" +
					"§7您当前使用的ID包含非法字符\n" +
					"§7请更改您的名称后再加入服务器\n" +
					"§7尽量使用字母和数字，可以使用中文，除此以外的所有字符都为非法字符!\n" +
					"§b--§3-§8----------------------------------------§3-§b--");
			e.setCancelled(true);
			return;
		}

		PreUser preUser = UserManager.loadPreUser(playerName);

		if(preUser.exist()){ //若该玩家的名字在数据库中存在
			if(preUser.isPremium()) {
				connection.setOnlineMode(true);
				preUser.readyPremium();
			} else {
				connection.setOnlineMode(preUser.isOnlineMode());
//				System.out.println(playerName + "在数据库中存在");
			}
		} else { //若该玩家的名字在数据库中不存在
			connection.setOnlineMode(true);// 未在网站上注册或者正版改名
		}

//		System.out.println(playerName+"正版登入："+connection.isOnlineMode());

		if(!connection.isOnlineMode()){//在玩家是盗版的情况下才需要检测名字是否带有违规字符，避免正版玩家躺枪
			checkName(e);
		}

		if(debug_time){
			ProxyServer.getInstance().getLogger().info("Debug>>> PreLogin用时 "+(System.currentTimeMillis() - start)+"ms");
		}
	}

	/*
	 * 玩家登陆，此时进行玩家的初始化，可以更改玩家的UUID
	 */
	@EventHandler
	public void onJoinUUID(LoginEvent loginEvent) {
		long start = 0;
		if(debug_time) start = System.currentTimeMillis();

		PendingConnection connection = loginEvent.getConnection();
		PreUser preUser = UserManager.getPreUser(connection.getName());
		if(preUser == null){ //PreUser不存在，通常是在前面报错时出现
//			loginEvent.setCancelReason("§cError: Cannot find PreUser "+connection.getName()+".\n"
//			+"§cPlease turn to admin.");
			loginEvent.setCancelReason("§fDisconnected");
			loginEvent.setCancelled(true);
		} else {
			if (connection.isOnlineMode()) {//当玩家是正版登入时
				UUID uuid = connection.getUniqueId();
				UUID mojangUUIDinDatabase = preUser.getMojangUUID();
				if (uuid.equals(mojangUUIDinDatabase)) { //当玩家的UUID和数据库中的mojangUUID相同时，确认是本人
					changeUUID(loginEvent, preUser);
					checkChangeName(preUser.getMojangUUID());
				} else { //不是本人, 检查该用户的mojangUUID是否存在，以认定是否为正版改名
					checkChangeName(preUser.getMojangUUID());//检查ID原主人是否改名
					PreUser realPreUser = new PreUser(uuid);
					if (realPreUser.exist()) { //该正版玩家已注册并已改名
						UserManager.unloadPreUser(preUser);//非本人可以退休了
						checkChangeName(realPreUser.getMojangUUID());//检测改名并放行
						changeUUID(loginEvent, preUser);
					} else { //正版玩家未注册
						//直接放行至登录服
					}
				}
			} else {//当玩家是盗版登入时
				if (preUser.getName() == null || preUser.getName().isEmpty()) { //盗版名称被占用
					loginEvent.setCancelReason("§c您的ID被一名正版玩家占用\n"
							+ "§c请到我们的官网上更改您的ID\n"
							+ "§b§nwww.kar.red");
					loginEvent.setCancelled(true);
					UserManager.unloadPreUser(preUser);
				} else { //盗版名称未被占用，放行至登录服
					changeUUID(loginEvent, preUser);//放行至登录服
				}
			}
		}

		if(debug_time){
			ProxyServer.getInstance().getLogger().info("Debug>>> Login用时 "+(System.currentTimeMillis() - start)+"ms");
		}
	}

	/*
	 * 玩家成功登陆之后做的事情，此时可以决定玩家传送至哪个服务器
	 */
	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		long start = 0;
		if(debug_time) start = System.currentTimeMillis();

		PreUser preUser = UserManager.getPreUser(e.getPlayer().getName());
		if(preUser == null){
			e.getPlayer().disconnect("§c错误：无法加载玩家信息\n"+
					"§cError: Cannot load player profile.");
			return;
		}
		ProxyUser u = UserManager.loadUser(preUser, e.getPlayer().getPendingConnection().getVirtualHost().getHostName(),
				e.getPlayer().getPendingConnection().getVersion());
		UserManager.unloadPreUser(preUser);

		if (u.isRegistered()) {
			if (u.isOnlineMode()) {
				u.setServerTarget(u.getValues().containsKey("rejoin") ? u.getValues().get("rejoin") : u.getTargetServerName());//断线重连
			} else {
//				u.setServerTarget("SYSTEM.LOGIN");
				if (e.getPlayer().getPendingConnection().getVirtualHost().getAddress().toString().equalsIgnoreCase(Account.getLastIP(u.getInkID()))) {
					u.setServerTarget(u.getValues().containsKey("rejoin") ? u.getValues().get("rejoin") : u.getTargetServerName());
				} else {
					u.setServerTarget("SYSTEM.LOGIN");
				}
			}
		} else {
			u.setServerTarget("SYSTEM.LOGIN");
		}


		if(debug_time){
			ProxyServer.getInstance().getLogger().info("Debug>>> PostLogin用时 "+(System.currentTimeMillis() - start)+"ms");
		}
	}

//	@EventHandler(priority = 5)
//	public void onMultiIps(ServerConnectEvent e){
//		//重复IP检测
//		if(!e.getPlayer().hasPermission("kar.proxy.multiip")){
//			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
//				if(p != e.getPlayer())
//					if(p.getPendingConnection().getAddress().getHostName().equals(e.getPlayer().getAddress().getHostName())){
//						e.getPlayer().disconnect("§b--§3-§8----------------------------------------§3-§b--\n" +
//								"§7尊敬的玩家您好，\n" +
//								"§7您的IP下已有玩家"+hideName(p.getName())+"在线。\n" +
//								"§7请不要同时使用多个账户进入服务器\n" +
//								"§b--§3-§8----------------------------------------§3-§b--");
//						return;
//					}
//			}
//		}
//	}

	//更换UUID
	private void changeUUID(LoginEvent loginEvent, PreUser preUser){
		PendingConnection connection = loginEvent.getConnection();
		try { //设置该用户的UUID为数据库中储存的UUID，以保证生存服数据不丢失
			Field declaredField = InitialHandler.class.getDeclaredField("uniqueId");
			declaredField.setAccessible(true);
			declaredField.set(connection, preUser.getUUID());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
			loginEvent.setCancelReason("§cError: Wrong Bungeecord Version"); //由于bc版本的变化，可能导致变量uniqueId改名，从而找不到该变量
			loginEvent.setCancelled(true);
			UserManager.unloadPreUser(preUser);
		}
	}

	/*
	 * 检测正版改名
	 */
	private void checkChangeName(UUID mojangUUID){
		if(mojangUUID == null) return;
		ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				PreUser realPreUser = new PreUser(mojangUUID);
				if(realPreUser.exist()){
					String newName = Account.getNameFromMojang(realPreUser.getMojangUUID());
					if(newName != null) {
						if(Account.isRegistered(newName)) {//新名字是否占用了一名玩家的名字
							UUID targetUUID = Account.getMojangUniqueID(newName);
							if (targetUUID == null) { //占用了盗版玩家的名字
								DataManager.getWebsiteConnection().update("nl2_users", "username", "NULL"
										, "username", newName);
							} else { //改名的名字又占用了另一个改名但未进入服务器的正版玩家的名字
								if(!mojangUUID.equals(targetUUID))
									checkChangeName(targetUUID);
							}
						}
						DataManager.getWebsiteConnection().update("nl2_users", "username", newName
								, "mojanguuid", realPreUser.getMojangUUID().toString().replace("-", ""));
					}
				}
			}
		});
	}

//	@EventHandler(priority = 4)
//	public void onPreLogin(PreLoginEvent e) {
//		if (e.getConnection() == null || e.isCancelled() || checkName(e)) return;
//
//		PendingConnection c = e.getConnection();
//		String playerName = c.getName();
//
//		int version = c.getVersion();
//
//		ProxyUser u = UserManager.loadUser(e.getConnection().getName(), e.getConnection().getVirtualHost().getHostName());
//
//		boolean loginAsOffline = regOffline.contains(playerName);
//
//		switch (userData) {
//			case 0: {
//				// 已经注册了的盗版玩家。
//				if (Config.onlineIps.contains(c.getVirtualHost().getHostName())) {
//					// 继承模式 启动！
//					c.setOnlineMode(true);
//
//					new BungeeScheduler().runAsync(Main.getInstance(), () -> u.createTempUser(true, version));
//				} else {
//					//普通登入
//					c.setOnlineMode(false);
//					if (Config.getServerInfo(c.getVirtualHost().getHostName()).isDirectTeleport()) {
//						if (Config.getServerInfo(c.getVirtualHost().getHostName()).isOnlyOnlineMode()) {
//							e.setCancelReason("§b--§3-§8----------------------------------------§3-§b--\n" +
//									"§7尊敬的 §f" + playerName + "§7您好，\n" +
//									"§7您正尝试链接 §b" + c.getVirtualHost().getHostName() + " §7,但此服务器仅允许正版玩家登入。\n" +
//									"\n" +
//									"§7若您是一位正版玩家，请尝试使用正版启动器重新启动，谢谢配合。\n" +
//									"§b--§3-§8----------------------------------------§3-§b--");
//							e.setCancelled(true);
//							return;
//						} else {
//							u.setServerTarget(u.getTargetServerName());
//						}
//					} else {
//						u.setServerTarget("SYSTEM.LOGIN");
//					}
//					new BungeeScheduler().runAsync(Main.getInstance(), () -> u.createTempUser(false, version));
//				}
//				break;
//			}
//			case 1: {
//				// 已经注册了的正版玩家。
//				if (Config.offlineIps.contains(c.getVirtualHost().getHostName())) {
//					e.setCancelReason("§b--§3-§8----------------------------------------§3-§b--\n" +
//							"§7尊敬的 §f" + playerName + "§7您好，\n" +
//							"§7您当前使用的ID已经被该ID的正版所有者所使用。\n" +
//							"§7若您是号主，请使用IP §b§lmocimc.cn §7登入，\n" +
//							"§7若您是一位盗版玩家，请更换您的ID，谢谢配合。\n" +
//							"§b--§3-§8----------------------------------------§3-§b--");
//					e.setCancelled(true);
//					return;
//				} else {
//					c.setOnlineMode(true);
//					new BungeeScheduler().runAsync(Main.getInstance(), () -> u.createTempUser(true, version));
//					u.setServerTarget(u.getTargetServerName());
//
//				}
//				break;
//			}
//			default:
//				if (Config.getServerInfo(c.getVirtualHost().getHostName()).isDirectTeleport()) {
//					c.setOnlineMode(Config.getServerInfo(c.getVirtualHost().getHostName()).isOnlyOnlineMode());
//					u.setServerTarget(u.getTargetServerName());
//				} else {
//					if (Config.offlineIps.contains(c.getVirtualHost().getHostName()) || loginAsOffline) {
//
//						c.setOnlineMode(false);
//						u.createTempUser(false, version);
//						regOffline.remove(playerName);
//					} else if (Config.onlineIps.contains(c.getVirtualHost().getHostName())) {
//						c.setOnlineMode(true);
//						u.createTempUser(true, version);
//					} else {
//						regOffline.add(playerName);
//						u.createTempUser(true, version);
//						new BungeeScheduler().schedule(Main.instance, () -> regOffline.remove(playerName), 5, 5, TimeUnit.MINUTES);
//					}
//					u.setServerTarget("SYSTEM.LOGIN");
//				}
//				break;
//		}
//
//	}


	private boolean checkName(PreLoginEvent e) {
		String name = e.getConnection().getName();
		//名字检测
		for (String s : Config.illegalWords) {
			if (name.contains(s)) {
				e.setCancelReason("§b--§3-§8----------------------------------------§3-§b--\n" +
						"§7尊敬的玩家您好，\n" +
						"§7您当前使用的ID包含非法词汇！\n" +
						"§7请更改您的名称后再加入服务器。\n" +
						"§7尽量使用字母和数字，减少中文的使用。\n" +
						"§b--§3-§8----------------------------------------§3-§b--");
				e.setCancelled(true);
				UserManager.unloadPreUser(UserManager.getPreUser(name));
				return true;
			}
		}
		return false;
	}

	private boolean useIllegalCharacter(String name){
		int n;
		for (int i = 0; i < name.length(); i++) {
			n = name.charAt(i);
			if (!((n >= 19968 && n <= 40869) || (n >= 48 && n <= 57) || (n >= 97 && n <= 122) || (n >= 65 && n <= 90) || n == '_')) {
				return true;
			}

		}
		return false;
	}

	@EventHandler
	public void onConnect(ServerConnectedEvent e) {
		regOffline.remove(e.getPlayer().getName());
	}

}

