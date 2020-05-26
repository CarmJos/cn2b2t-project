package org.cn2b2t.core.managers.utils;

import org.cn2b2t.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServersManager
//		implements PluginMessageListener
{


	public static List<Player> requests = new ArrayList<>();
	public static List<String> allPlayers = new ArrayList<>();

	public static void init() {
		Main.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
		Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BeeSwitch");
//		Core.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Core.getInstance(), "BungeeCord", new ServersManager());
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				if (Bukkit.getOnlinePlayers().size() < 1) return;
//				sendGetPlayerList();
//
//			}
//		}.runTaskTimer(Core.getInstance(), 60L, 200L);
	}

//	public static void sendGetPlayerList() {
//		ByteArrayOutputStream b = new ByteArrayOutputStream();
//		DataOutputStream out = new DataOutputStream(b);
//		try {
//			out.writeUTF("PlayerList");
//			out.writeUTF("ALL");
//		} catch (IOException ex) {
//			Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
//		}
//		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
//		if (player != null) {
//			player.sendPluginMessage(Core.getInstance(), "BungeeCord", b.toByteArray());
//		}
//	}
//
//	@Override
//	public void onPluginMessageReceived(String tag, Player player, byte[] data) {
//		if (!tag.equals("BungeeCord")) return;
//
//		ByteArrayDataInput in = ByteStreams.newDataInput(data);
//		String server = in.readUTF();
//
//		if (server.equals("ALL")) {
//			String[] players = in.readUTF().split(", ");
//			allPlayers = Arrays.stream(players).collect(Collectors.toList());
//		} else {
//			return;
//		}
//	}

	public static void teleport(Player p, String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("ConnectOther");
			out.writeUTF(p.getName());
			out.writeUTF(server);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
	}

	public static void teleport(Player player, String server, int t) {
		requests.add(player);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (requests.contains(player) && player.isOnline()) {
					teleport(player, server);
					requests.remove(player);
				}
				cancel();
			}
		}.runTaskLater(Main.getInstance(), 20L * t);
	}

	public static void backToLobby(Player p) {
		teleport(p, "lobby");
	}

	public static void switchServer(Player p, String targetServerIp) {
		p.sendPluginMessage(Main.getInstance(), "BeeSwitch", targetServerIp.getBytes(StandardCharsets.UTF_8));
	}


}
