package org.cn2b2t.core.managers.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cn2b2t.core.modules.users.AbstractUserHandler;
import org.cn2b2t.core.modules.users.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private static HashMap<String, User> users = new HashMap<>();

    protected static final Map<String, Object> handlers = new HashMap<>();
    protected static final Map<String, Class<? extends AbstractUserHandler>> classes = new HashMap<>();

    public static User registeruser(Player p) {
        User user = new User(p);
        users.put(p.getUniqueId().toString(), user);
        return user;
    }

    public static User getUser(Player p) {
        return getUser(p.getUniqueId());
    }

    public static User getUser(CommandSender sender) {
        return sender instanceof Player ? getUser((Player) sender) : null;
    }

    public static User getUser(UUID uuid) {
        return users.getOrDefault(uuid.toString(), null);
    }

    public static void unregisterUser(Player p) {
        users.get(p.getUniqueId().toString()).saveDatas();
        users.remove(p.getUniqueId().toString());
    }


    public static Collection<User> getRegisteredUsers() {
        return users.values();
    }


    public static boolean containsHandler(Class<? extends AbstractUserHandler> handler) {
        int position = handler.getName().lastIndexOf('.');
        return classes.containsKey(handler.getName().substring(position + 1));
    }

    public static boolean containsHandler(String name) {
        return handlers.containsKey(name);
    }

    public static void regHandler(Class<? extends AbstractUserHandler> c) {
        if (containsHandler(c)) {
            throw new RuntimeException("Handler " + c.getName() + " is existed.");
        } else {
            int position = c.getName().lastIndexOf('.');
            classes.put(c.getName().substring(position + 1), c);
            for (User u : getRegisteredUsers()) {
                u.registerHandler(c);
            }
        }
    }

    public static void regHandler(String name, Object o) {
        if (containsHandler(name)) {
            throw new RuntimeException("Handler " + name + " is existed.");
        } else {
            handlers.put(name, o);
            for (User u : getRegisteredUsers()) {
                u.registerHandler(name, o);
            }
        }
    }

    public static void removeHandler(String name) {
        if (!containsHandler(name)) {
            throw new RuntimeException("Handler " + name + " wasn't found.");
        } else {
            handlers.remove(name);
            for (User u : getRegisteredUsers()) {
                u.removeHandler(name);
            }
        }
    }

    public static void removeHandler(Class<? extends AbstractUserHandler> c) {
        if (!containsHandler(c)) {
            throw new RuntimeException("Handler " + c.getName() + " wasn't found.");
        } else {
            classes.remove(c.getName());
            for (User u : getRegisteredUsers()) {
                u.removeHandler(c);
            }
        }
    }

    public static Map<String, Object> getHandlers() {
        return handlers;
    }

    public static Map<String, Class<? extends AbstractUserHandler>> getHandlerClasses() {
        return classes;
    }
}
