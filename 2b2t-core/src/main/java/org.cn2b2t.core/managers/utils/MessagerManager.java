package org.cn2b2t.core.managers.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.events.MessageReceiveEvent;
import org.cn2b2t.core.listener.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;

public class MessagerManager {

	public static List<String> registeredChannels = new ArrayList<>();

	public static long timeOutTime = 1000;

	public static void enable() {
		Main.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
		Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", new MessageListener());
	}

	public static void callEvent(String channel, long sendTime, String fromServer, String fromPlugin, String key, String[] values) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent(
						new MessageReceiveEvent(channel, sendTime, fromServer, fromPlugin, key, values));
			}
		}.runTask(Main.getInstance());
	}


	public static void registerChannel(String channel) {
		if (registeredChannels.contains(channel)) {
			System.out.println(" Channel " + channel + " has registered.");
			return;
		}
		registeredChannels.add(channel);
	}

	public static void unRegisterChannel(String channel) {
		if (!registeredChannels.contains(channel)) {
			System.out.println(" Channel " + channel + " not registered.");
			return;
		}
		registeredChannels.remove(channel);
	}

	public static boolean hasChannel(String channel) {
		return registeredChannels.contains(channel);
	}


	public static void send(JavaPlugin plugin, String channel, String key, String[] values) {
		if (values == null || values.length < 1) {
			Main.getInstance().getLogger().log(Level.WARNING, "错误！ 发送的value = null");
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				StringBuilder message = new StringBuilder();

				for (int i = 0; i < values.length - 1; i++) {
					message.append(Base64.getEncoder().encodeToString(values[i].getBytes()));
					message.append(":");
				}
				message.append(Base64.getEncoder().encodeToString((values[values.length - 1]).getBytes()));

				String value = message.toString();
				new BukkitRunnable() {
					@Override
					public void run() {
						send(plugin, channel, true, key, value);
					}
				}.runTask(Main.getInstance());
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	public static void send(String channel, String key, String[] values) {
		send(Main.getInstance(), channel, key, values);
	}


	public static void send(JavaPlugin plugin, String channel, String key, String value) {
		send(plugin, channel, false, key, value);
	}


	public static void send(String channel, String key, String value) {
		send(Main.getInstance(), channel, false, key, value);
	}


	public static void send(String channel, boolean encoded, String key, String value) {
		send(Main.getInstance(), channel, encoded, key, value);
	}

	public static void send(JavaPlugin plugin, String channel, boolean encoded, String key, String value) {
		if (value == null) {
			Main.getInstance().getLogger().log(Level.WARNING, "错误！ 发送的value = null");
			return;
		}
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF(channel);
			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			//
			msgout.writeLong(System.currentTimeMillis());
			msgout.writeUTF(Bukkit.getServerId()); // from port
			msgout.writeUTF(plugin.getName());
			msgout.writeInt(encoded ? 1 : 0);
			//
			msgout.writeUTF(key);
			msgout.writeUTF(value);

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
			Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			if (player != null) {
				player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
