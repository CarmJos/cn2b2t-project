package org.cn2b2t.core.managers.team;

import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.modules.users.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamListener implements Listener {

    @EventHandler
    public void onHurt(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            User en = UserManager.getUser(((Player) e.getEntity()));
            Team t = TeamManager.getTeam(en);
            if (t != null && !t.friendlyHurt()) {
                User d = UserManager.getUser(((Player) e.getDamager()));
                if (t.containsTeammate(d)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
