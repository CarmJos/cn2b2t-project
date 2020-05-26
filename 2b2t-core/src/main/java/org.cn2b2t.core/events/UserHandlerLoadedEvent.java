package org.cn2b2t.core.events;

import org.cn2b2t.core.modules.users.AbstractUserHandler;
import org.cn2b2t.core.modules.users.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserHandlerLoadedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private User user;
	private AbstractUserHandler handler;

	public UserHandlerLoadedEvent(AbstractUserHandler handler) {
		this.user = handler.getUser();
		this.handler = handler;
	}
	
	public AbstractUserHandler getHandler() {
		return handler;
	}

	public User getUser() {
		return user;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
