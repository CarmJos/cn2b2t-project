package org.cn2b2t.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.cn2b2t.core.modules.users.User;

/**
 * @author LSeng
 */
public class UserLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private User user;

    private UserLoadedEvent() {
    }

    public UserLoadedEvent(User u) {
        user = u;
    }

    public User getUser() {
        return this.user;
    }

    public Player getPlayer() {
        return this.user.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
