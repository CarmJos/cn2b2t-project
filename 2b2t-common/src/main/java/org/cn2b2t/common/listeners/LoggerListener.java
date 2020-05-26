package org.cn2b2t.common.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.cn2b2t.common.enums.LogType;
import org.cn2b2t.common.managers.LoggerManager;

public class LoggerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent e) {
        LoggerManager.log(LogType.CHAT, e.getPlayer(), e.getMessage().replace("\\", "\\\\"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        LoggerManager.log(LogType.COMMAND, e.getPlayer(), e.getMessage().replace("\\", "\\\\"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent e) {
        LoggerManager.log(LogType.JOIN, e.getPlayer(), "Joined");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        LoggerManager.log(LogType.QUIT, e.getPlayer(), "quit");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCMD(final ServerCommandEvent e) {
        LoggerManager.logConsole(LogType.COMMAND, e.getCommand().replace("\\", "\\\\"));
    }


}
