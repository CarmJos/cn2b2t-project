package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.common.managers.ScoreboardManager;
import org.cn2b2t.common.runnables.RestartRunnable;
import org.cn2b2t.core.events.UserLoadedEvent;

public class ScoreboardListener implements Listener {


    @EventHandler
    public void onJoin(UserLoadedEvent e) {
        if (ProfileData.get(e.getPlayer()).showScoreboard) addScoreboard(e.getPlayer());
    }

    public static void addScoreboard(Player p) {
        ScoreboardManager sm = new ScoreboardManager(p, "§f生存 §8| §6§lCN2B2T");
        sm.create();
        ScoreboardManager.scoreboards.put(p, sm);

        updateLine(p, 0);

        Bukkit.getOnlinePlayers().stream().filter(player -> player != p).forEachOrdered(player -> updateLine(player, 9));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.isOnline() || !ProfileData.get(p).showScoreboard) {
                    cancel();
                }
                if (ScoreboardManager.scoreboards.containsKey(p)) {
                    updateLine(p, 5);
                    updateLine(p, 6);
                    updateLine(p, 10);

                }
            }
        }.runTaskTimer(Main.getInstance(), 100L, 100L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (p == null || !p.isOnline() || ProfileData.get(p) != null || !ProfileData.get(p).showScoreboard) {
                    cancel();
                }
                if (ScoreboardManager.scoreboards.containsKey(p)) {
                    updateLine(p, 11);
                }
            }
        }.runTaskTimer(Main.getInstance(), 100L, 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (ScoreboardManager.scoreboards.containsKey(e.getPlayer())) {
            ScoreboardManager.scoreboards.get(e.getPlayer()).destroy();
            ScoreboardManager.scoreboards.remove(e.getPlayer());
        }

    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            updateLine(e.getEntity().getKiller(), 1);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        updateLine(e.getEntity(), 1);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        updateLine(e.getPlayer(), 5);
    }


    public static void updateLine(Player p, int i) {
        if (!ProfileData.get(p).showScoreboard) return;

        ScoreboardManager sm = ScoreboardManager.scoreboards.get(p);
        switch (i) {
            case 0: {
                sm.setLine(1, "杀敌/死亡 §6" + (p.getStatistic(Statistic.PLAYER_KILLS) + p.getStatistic(Statistic.MOB_KILLS)) + "§8/§6" + p.getStatistic(Statistic.DEATHS));
                sm.setLine(2, "§7 ");
                sm.setLine(3, "§7世界信息");
                sm.setLine(4, " 生物数量 §6" + Bukkit.getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum());
                sm.setLine(5, " 当前边界 §6±" + (int) (p.getWorld().getWorldBorder().getSize() / 2));
                sm.setLine(6, " 创生时长 §6" + ((int) Bukkit.getWorld("world").getFullTime() / 24000) + "§8天");
                sm.setLine(7, "§8 ");
                sm.setLine(8, "§7本服信息");
                sm.setLine(9, " 本服在线 §6" + Bukkit.getOnlinePlayers().size());
                sm.setLine(10, " 当前TPS §6" + (int) (Bukkit.getServer().getTPS()[0] * 100000.0) / 100000.0);
                sm.setLine(11, " 下次重启 §6" + RestartRunnable.getTimeString());
                sm.setLine(12, "§9");
                sm.setLine(13, "§6CN2b2t.org");
            }
            case 1: {
                sm.setLine(1, "杀敌/死亡 §6" + (p.getStatistic(Statistic.PLAYER_KILLS) + p.getStatistic(Statistic.MOB_KILLS)) + "§8/§6" + p.getStatistic(Statistic.DEATHS));
                break;
            }
            case 2: {
                sm.setLine(2, "§7 ");
                break;
            }
            case 3: {
                sm.setLine(3, "§7世界信息");
                break;
            }
            case 4: {
                sm.setLine(4, " 生物数量 §6" + Bukkit.getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum());
                break;
            }
            case 5: {
                sm.setLine(5, " 当前边界 §6±" + (int) (p.getWorld().getWorldBorder().getSize() / 2));
                break;
            }
            case 6: {
                sm.setLine(6, " 创生时长 §6" + ((int) Bukkit.getWorld("world").getFullTime() / 24000) + "§8天");
                break;
            }
            case 7: {
                sm.setLine(7, "§8 ");
                break;
            }
            case 8: {
                sm.setLine(8, "§7本服信息");
                break;
            }
            case 9: {
                sm.setLine(9, " 本服在线 §6" + Bukkit.getOnlinePlayers().size());
                break;
            }
            case 10: {
                sm.setLine(10, " 当前TPS §6" + (int) (Bukkit.getServer().getTPS()[0] * 100000.0) / 100000.0);
                break;
            }
            case 11: {
                sm.setLine(11, " 下次重启 §6" + RestartRunnable.getTimeString());
                break;
            }
            case 12: {
                sm.setLine(12, "§9");
                break;
            }
            case 13: {
                sm.setLine(12, "§6CN2b2t.org");
                break;
            }
        }
    }

}
