package org.cn2b2t.core.modules.users;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.core.events.UserHandlerLoadedEvent;

public abstract class AbstractUserHandler {

    protected User user;

    protected AbstractUserHandler() {
    }

    public final User getUser() {
        return this.user;
    }

    protected void init() {
    }

    protected void onDisable() {
    }

    public void callLoadedEvent(JavaPlugin main) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new UserHandlerLoadedEvent(AbstractUserHandler.this));
            }
        }.runTask(main);
    }

}
