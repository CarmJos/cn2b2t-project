package org.cn2b2t.common.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.utils.ColorParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {

    private static Set<String> coolingPlayers = new HashSet<>();
    private static HashMap<String, String> messages = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String name = e.getPlayer().getName();
        String message = e.getMessage();

        if (coolingPlayers.contains(name)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f您的说话速度太快，每次发言请至少等待3秒。"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
        } else if (messages.containsKey(name) && messages.get(name)
                .equalsIgnoreCase(removeUseless(message))) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f请不要重复说相同的内容。"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
            e.setCancelled(true);
        } else {
            coolingPlayers.add(name);
            new BukkitRunnable() {
                @Override
                public void run() {
                    coolingPlayers.remove(name);
                }
            }.runTaskLater(Main.getInstance(), 60L);
            messages.put(name, removeUseless(message));

            e.setMessage(ColorParser.parse(message));
            e.setFormat(ProfileData.get(e.getPlayer()).getPrefix() + "%1$s§8 : §f%2$s");

            e.getRecipients().stream().filter(recipient -> ProfileData.get(recipient).isIgnored(e.getPlayer().getUniqueId()))
                    .forEachOrdered(recipient -> e.getRecipients().remove(recipient));
        }
    }

    private static String removeUseless(String str) {
        return str
                .replaceAll("。", "")
                .replaceAll(" ", "")
                .replaceAll("!", "")
                .replaceAll("！", "")
                .replaceAll("？", "");
    }


}
