package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.managers.render.NamePrefix;
import org.cn2b2t.core.utils.ColorParser;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");

        Player p = e.getPlayer();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            NamePrefix.reset(onlinePlayer, p);
        }


        Bukkit.getOnlinePlayers().stream()
                .filter(pl -> ProfileData.get(pl) != null)
                .filter(pl -> ProfileData.get(p).showJoinAndLeaveAlerts)
                .forEachOrdered(pl -> pl.sendMessage(ColorParser.parse("&7玩家 " + ProfileData.get(p).getPrefix() + p.getName() + " &7离开了游戏。")));
    }

}
