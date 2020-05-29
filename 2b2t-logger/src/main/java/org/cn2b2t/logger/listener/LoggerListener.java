package org.cn2b2t.logger.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent e) {
        LoggerManager.log(LoggerManager.LogType.JOIN, e.getPlayer(), "Joined");
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
