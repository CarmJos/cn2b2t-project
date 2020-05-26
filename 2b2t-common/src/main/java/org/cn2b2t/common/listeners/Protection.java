package org.cn2b2t.common.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Protection implements Listener {

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        Location b = e.getBlockPlaced().getLocation();
        if ((b.getBlockX() < 16 && b.getBlockX() > -16)
                && (b.getBlockY() > 100)
                && (b.getBlockZ() < 16 && b.getBlockZ() > -16) && !e.getPlayer().getName().equalsIgnoreCase("CUMR")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBuild(BlockBreakEvent e) {
        Location b = e.getBlock().getLocation();
        if ((b.getBlockX() < 16 && b.getBlockX() > -16)
                && (b.getBlockY() > 100)
                && (b.getBlockZ() < 16 && b.getBlockZ() > -16) && !e.getPlayer().getName().equalsIgnoreCase("CUMR")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player
                && (e.getEntity().getLocation().getBlockX() < 16 && e.getEntity().getLocation().getBlockX() > -16)
                && (e.getEntity().getLocation().getBlockY() > 100)
                && (e.getEntity().getLocation().getBlockZ() < 16 && e.getEntity().getLocation().getBlockZ() > -16)) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().isOp()) {
            e.getPlayer().setOp(false);
            e.getPlayer().setHealth(0);
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e) {
        if (e.getBlockClicked().getType() == Material.ENDER_PORTAL
                || e.getBlockClicked().getType() == Material.ENDER_PORTAL_FRAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketFillEvent e) {
        if (e.getBlockClicked().getType() == Material.ENDER_PORTAL
                || e.getBlockClicked().getType() == Material.ENDER_PORTAL_FRAME) {
            e.setCancelled(true);
        }
    }
}
