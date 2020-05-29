package org.cn2b2t.logger.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.events.UserHandlerLoadedEvent;
import org.cn2b2t.logger.managers.LoggerManager;

public class LoggerListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final PlayerChatEvent e) {
        LoggerManager.log(LoggerManager.LogType.CHAT, e.getPlayer(), e.getMessage().replace("\\", "\\\\"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        LoggerManager.log(LoggerManager.LogType.COMMAND, e.getPlayer(), e.getMessage().replace("\\", "\\\\"));
    }

    @EventHandler
    public void onJoin(final UserHandlerLoadedEvent e) {
        if (e.getHandler() instanceof ProfileData) {
            String ip = e.getUser().getPlayer().getAddress().getAddress().toString();
            LoggerManager.log(LoggerManager.LogType.JOIN, e.getUser().getPlayer(), ip);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        LoggerManager.log(LoggerManager.LogType.QUIT, e.getPlayer(), "quit");
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCMD(final ServerCommandEvent e) {
        LoggerManager.logConsole(LoggerManager.LogType.COMMAND, e.getCommand().replace("\\", "\\\\"));
    }


}
