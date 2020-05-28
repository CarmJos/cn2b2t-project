package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cn2b2t.common.enums.Locations;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.common.managers.KitManager;
import org.cn2b2t.common.managers.ScoreboardManager;
import org.cn2b2t.core.events.UserHandlerLoadedEvent;
import org.cn2b2t.core.managers.render.NamePrefix;
import org.cn2b2t.core.utils.ColorParser;

public class LoginListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Player p = e.getPlayer();


        if (!p.hasPlayedBefore()) {
            KitManager.getSpawnItem(p);
            p.teleport(Locations.SPAWN.getLocation());
            Bukkit.getOnlinePlayers().stream()
                    .filter(pl -> ProfileData.get(pl) != null)
                    .filter(pl -> ProfileData.get(pl).showJoinAndLeaveAlerts)
                    .forEachOrdered(pl -> pl.sendMessage(ColorParser.parse(" \n&7欢迎新玩家 &6" + p.getName() + " &7加入了游戏。\n ")));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7尊敬的 &6&l" + p.getName() + "&7，"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7您好，欢迎来到&7&o&lcn2b2t.org&7！"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服&c无规则、无管理，无任何官方组织&7。"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服基于 &6" + Bukkit.getVersion() + " &7搭建。"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7本服额外添加几个了几个命令，详细请输入 &6/help &7。"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6--&e-&f-----------------------------&e-&6--"));
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1L, 1L);
        }


    }

    @EventHandler
    public void onJoin(UserHandlerLoadedEvent e) {
        if (e.getHandler() instanceof ProfileData) {
            Player p = e.getUser().getPlayer();
            if (p.hasPlayedBefore()) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (ProfileData.get(pl) != null) {
                        if (ProfileData.get(pl).showJoinAndLeaveAlerts) {
                            ColorParser.parse("&7玩家 " + ProfileData.get(p).getPrefix() + p.getName() + " &7加入了游戏。");
                        }
                        NamePrefix.set(p, pl, ProfileData.get(pl).getPrefix());
                    }
                    NamePrefix.set(pl, p, ProfileData.get(p).getPrefix());
                }
            }
            ScoreboardManager.scoreboards.keySet().forEach(player -> ScoreboardListener.updateLine(player, 9));
        }


    }
}
