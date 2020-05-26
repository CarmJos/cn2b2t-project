package org.cn2b2t.common.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * @author cam (MoCi Games)
 */
public class FastPot implements Listener {

    @EventHandler
    void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION) {
            final Projectile projectile = event.getEntity();
            if (projectile.getShooter() instanceof Player && ((Player) projectile.getShooter()).isSprinting()) {
                projectile.setVelocity(projectile.getVelocity().setY(projectile.getVelocity().getY() - 4));
            }
        }

    }

    @EventHandler
    void onPotionSplash(final PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            final Player shooter = (Player) event.getEntity().getShooter();
            if (shooter.isSprinting() && event.getIntensity(shooter) > 0.5D) {
                event.setIntensity(shooter, 1.0D);
            }
        }
    }

}
