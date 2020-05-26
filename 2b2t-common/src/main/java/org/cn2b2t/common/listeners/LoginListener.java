package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cn2b2t.common.enums.Locations;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.common.managers.KitManager;
import org.cn2b2t.core.managers.render.NamePrefix;

public class LoginListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");

        ProfileData.loadProfileData(e.getPlayer());


        if (!e.getPlayer().hasPlayedBefore()) {
            KitManager.getSpawnItem(e.getPlayer());
            e.getPlayer().teleport(Locations.SPAWN.getLocation());
            Bukkit.getOnlinePlayers().stream().filter(p -> ProfileData.getProfileData(p).showJoinAndLeaveAlerts).forEachOrdered(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', " \n&7欢迎新玩家 &6" + e.getPlayer().getName() + " &7加入了游戏。\n ")));
        } else {
            Bukkit.getOnlinePlayers().stream().filter(p -> ProfileData.getProfileData(p).showJoinAndLeaveAlerts).forEachOrdered(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7玩家 " + ProfileData.getProfileData(e.getPlayer()).getPrefix() + e.getPlayer().getName() + " &7加入了游戏。")));
        }

        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7尊敬的 &6&l" + e.getPlayer().getName() + "&7，"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7您好，欢迎来到&7&o&lcn2b2t.org&7！"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服&c无规则、无管理，无任何官方组织&7。"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服基于 &6" + Bukkit.getVersion() + " &7搭建。"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服额外添加几个了几个命令，详细请输入 &6/help &7。"));
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (ProfileData.getProfileData(onlinePlayer) != null) {
                NamePrefix.set(e.getPlayer(), onlinePlayer, ProfileData.getProfileData(onlinePlayer).getPrefix());
            }
            NamePrefix.set(onlinePlayer, e.getPlayer(), ProfileData.getProfileData(e.getPlayer()).getPrefix());
        }

    }
}
