package org.cn2b2t.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageReceiveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	String channel;
	long sendTime;

	String fromServer;
	String fromPlugin;

	String key;
	String[] values;


	public MessageReceiveEvent(String channel, long sendTime, String fromServer, String fromPlugin, String key, String[] values) {
		this.channel = channel;
		this.sendTime = sendTime;
		this.fromPlugin = fromPlugin;
		this.fromServer = fromServer;
		this.key = key;
		this.values = values;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isTimeOut() {
		return System.currentTimeMillis() - sendTime > 5000;
	}

	public String getChannel() {
		return channel;
	}

	public String getKey() {
		return key;
	}

	public String[] getValues() {
		return values;
	}

	@Override
	public String getEventName() {
		return super.getEventName();
	}

	@Override
	public String toString() {
		return "MessageReceiveEvent{" +
				"channel='" + channel + '\'' +
				", sendTime=" + sendTime +
				", fromServer=" + fromServer +
				", fromPlugin='" + fromPlugin + '\'' +
				", key='" + key + '\'' +
				", value='" + values + '\'' +
				'}';
	}

	public String getSendPlugin() {
		return fromPlugin;
	}

	public long getSendTime() {
		return sendTime;
	}

	public String getSendServer() {
		return fromServer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}