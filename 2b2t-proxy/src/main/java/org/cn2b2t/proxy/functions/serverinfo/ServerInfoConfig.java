package org.cn2b2t.proxy.functions.serverinfo;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.utils.FileConfig;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerInfoConfig {

	public static HashMap<String, String> ipToName = new HashMap<>();
	public static HashMap<String, ServerInfos> severinfos = new HashMap<>();

	public static List<String> onlineIps;
	public static List<String> offlineIps;

	public static ServerInfos defaultInfo;
	public static boolean fixing;

	public static String fixingInfo;
	public static String fixingMotd;
	public static List<String> allowedPlayers;

	public static Configuration serverinfo;

	public static ServerInfos getServerInfo(String ip) {
		return ServerInfoConfig.ipToName.containsKey(ip.toLowerCase()) ? (
				(severinfos.containsKey(ipToName.get(ip.toLowerCase())) ?
						severinfos.get(ipToName.get(ip.toLowerCase()))
						: defaultInfo))
				: defaultInfo;
	}

	public static void load() {
		serverinfo = null;
		ServerInfoConfig.serverinfo = new FileConfig(Main.getPlugin(), "serverinfo.yml").getConfig();



		ipToName.clear();
		severinfos.clear();
		fixing = serverinfo.getBoolean("FixingMode.enable");


		if (fixing) {
			fixingInfo = serverinfo.getString("FixingMode.Info");
			fixingMotd = serverinfo.getString("FixingMode.Motd");
			allowedPlayers = new ArrayList<>();
			allowedPlayers = serverinfo.getStringList("FixingMode.players");
		}

		ServerInfoConfig.defaultInfo = new ServerInfos(ServerInfoConfig.serverinfo.getString("Default.Motd.line1"),
				fixing ? fixingMotd : serverinfo.getString("Default.Motd.line2"),
				serverinfo.getStringList("Default.PlayerInfo"),
				fixing ? fixingInfo : serverinfo.getString("Default.version"),
				serverinfo.getBoolean("Default.useFakePlayer"),
				serverinfo.getInt("Default.maxPlayer"),
				serverinfo.getString("Default.icon"),
				"SYSTEM.MAINLOBBY",
				false,
				false);

		onlineIps = serverinfo.getStringList("IP.online");
		offlineIps = serverinfo.getStringList("IP.offline");

		serverinfo.getSection("Settings").getKeys().forEach(name -> {
			serverinfo.getStringList("Settings." + name + ".IP").forEach(ip -> ServerInfoConfig.ipToName.put(ip.toLowerCase(), name.toLowerCase()));
			ServerInfoConfig.severinfos.put(name.toLowerCase(),
					new ServerInfos(serverinfo.getString("Settings." + name + ".Motd.line1"),
							fixing ? fixingMotd : serverinfo.getString("Settings." + name + ".Motd.line2"),
							serverinfo.getStringList("Settings." + name + ".PlayerInfo"),
							fixing ? fixingInfo : serverinfo.getString("Settings." + name + ".version"),
							serverinfo.getBoolean("Settings." + name + ".useFakePlayer"),
							serverinfo.getStringList("Settings." + name + ".playersFrom"),
							serverinfo.getInt("Settings." + name + ".maxPlayer"),
							serverinfo.getString("Settings." + name + ".icon"),
							serverinfo.getString("Settings." + name + ".target"),
							serverinfo.getBoolean("Settings." + name + ".directTeleport", false),
							serverinfo.getBoolean("Settings." + name + ".onlyOnlineMode", false),
							serverinfo.getString("Settings." + name + ".modType", "BUKKIT")));
		});

	}


}
