package org.cn2b2t.proxy.functions.proxyuser;

import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserManager {

    private static List<ProxyUser> users = new ArrayList<>();

    private static Set<PreUser> preUsers = new HashSet<>();

    public static PreUser loadPreUser(String name) {
        PreUser preUser = new PreUser(name);
        preUsers.add(preUser);
        antiMemoryFlow(); //防止内存溢出
        return preUser;
    }

    public static void unloadPreUser(PreUser preUser) {
        preUsers.remove(preUser);
        System.out.println("PreUser " + preUser.getName() + " has been unloaded.");
    }

    public static PreUser getPreUser(String name) {
        for (PreUser preUser : preUsers) {
            if (preUser.getName().equals(name)) {
                return preUser;
            }
        }
        return null;
    }

    private static long lastCheck;

    private static void antiMemoryFlow() {
        long now = System.currentTimeMillis();
        if (now - lastCheck > 60000) {
            lastCheck = now;
            for (PreUser preUser : new HashSet<>(preUsers)) {
                if (now - preUser.registerTime > 30000) {
                    preUsers.remove(preUser);
                }
            }
            for (ProxyUser user : users) {
                if (ProxyServer.getInstance().getPlayer(user.getGameUUID()) == null) {
                    unloadUser(user.getName());
                    break;
                }
            }
        }
    }

    public static ProxyUser loadUser(PreUser preUser, String connectedIP, Integer version) {
        ProxyUser u = new ProxyUser(preUser.getName(), connectedIP, version, preUser.getUUID(), preUser.getMojangUUID());
        users.add(u);
        return u;
    }

    public static void unloadUser(String name) {
        for (ProxyUser user : users) {
            if (user.getName().equals(name)) {
                users.remove(user);
                break;
            }
        }

    }

    public static ProxyUser getUser(String name) {
        return users.stream().filter(user -> user.getName().equals(name)).findFirst().orElse(null);
    }

    public static List<ProxyUser> getOnlineUsers() {
        return users;
    }

}
