package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.managers.render.NamePrefix;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            NamePrefix.reset(onlinePlayer, e.getPlayer());
        }

        Bukkit.getOnlinePlayers().stream().filter(p -> ProfileData.getProfileData(p).showJoinAndLeaveAlerts).forEachOrdered(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7玩家 " + ProfileData.getProfileData(e.getPlayer()).getPrefix() + e.getPlayer().getName() + " &7离开了游戏。")));
        ProfileData.unloadProfileData(e.getPlayer());
    }
}
