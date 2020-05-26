package org.cn2b2t.core.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import org.cn2b2t.core.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class TabCompleteListener implements Listener {

	private final ProtocolManager pm;

	public TabCompleteListener() {
		this.pm = ProtocolLibrary.getProtocolManager();
		setupPackets();
	}

	private void setupPackets() {
		this.pm.addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				if (!event.getPlayer().isOp()) {
					if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
						try {

							PacketContainer packet = event.getPacket();

							String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
							if (message.equalsIgnoreCase("/")
									|| shouldCancel(message, "//")
									|| shouldCancel(message, "/cmi")
									|| shouldCancel(message, "/mcpay")
									|| shouldCancel(message, "/help")
									|| shouldCancel(message, "/bukkit:")
									|| shouldCancel(message, "/minecraft:")) {
								event.setCancelled(true);
							}
						} catch (FieldAccessException ignored) {
						}
					}
				}
			}
		});
	}


	@EventHandler
	public void onAtTab(PlayerCommandPreprocessEvent e) {
		if (!e.getPlayer().isOp()) {
			String message = e.getMessage();
			if (message.equalsIgnoreCase("/")
					|| shouldCancel(message, "//")
					|| message.startsWith("/minecraft:")
					|| message.startsWith("/bukkit:")
					|| message.startsWith("/help")) {
				e.setCancelled(true);
			}
		}
	}

	public static boolean shouldCancel(String message, String command) {
		return message.startsWith(command) || command.startsWith(message);
	}

//	@EventHandler
//	public void onAtTab(PlayerChatTabCompleteEvent e) {
//		if (Bukkit.getOnlinePlayers().size() < 1 || ServersManager.allPlayers.size() < 1) return;
//		String s = e.getChatMessage().substring(e.getChatMessage().lastIndexOf('@') + 1);
//		Bukkit.getOnlinePlayers().stream().filter(all -> all.getName().toLowerCase().startsWith(s.toLowerCase())).forEachOrdered(all -> e.getTabCompletions().add("@" + all.getName()));
//	}
//
//	@EventHandler
//	public void onNameTab(TabCompleteEvent e) {
//		if (Bukkit.getOnlinePlayers().size() < 1 || ServersManager.allPlayers.size() < 1) return;
//		String toComplete = e.getBuffer();
//		e.setCompletions(ServersManager.allPlayers.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, toComplete)).collect(Collectors.toList()));
//	}


}
