package org.cn2b2t.core.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.managers.utils.MessagerManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;

public class MessageListener implements PluginMessageListener {


	@Override
	public void onPluginMessageReceived(String tag, Player player, byte[] data) {
		if (!tag.equals("BungeeCord")) return;

		ByteArrayDataInput in = ByteStreams.newDataInput(data);
		String channel = in.readUTF();
		if (!MessagerManager.hasChannel(channel)) {
			return;
		}
		short len = in.readShort();
		byte[] msgbytes = new byte[len];
		in.readFully(msgbytes);

		DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
		try {
			long time = msgin.readLong();
			if (System.currentTimeMillis() - time > MessagerManager.timeOutTime) {
				Main.getInstance().getLogger().log(Level.WARNING, "从BC接收到一条消息，但已经超时。");
				return;
			}

			String server = msgin.readUTF();
			String pluginName = msgin.readUTF();
			boolean encoded = msgin.readInt() == 1;
			String key = msgin.readUTF();
			String value = msgin.readUTF();
			if (encoded) {
				new BukkitRunnable() {
					@Override
					public void run() {
						String[] messages = value.split(":");
						if (messages.length < 1) {
							Main.getInstance().getLogger().log(Level.WARNING, "错误！ 受到的value = null");
							return;
						}

						String[] values = (String[]) Arrays.stream(messages).map(message -> new String(Base64.getDecoder().decode(message))).toArray();
						MessagerManager.callEvent(channel, time, server, pluginName, key, values);
					}
				}.runTaskAsynchronously(Main.getInstance());
			} else {
				MessagerManager.callEvent(channel, time, server, pluginName, key, new String[]{value});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}