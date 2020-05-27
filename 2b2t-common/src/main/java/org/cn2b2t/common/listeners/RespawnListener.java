package org.cn2b2t.common.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.cn2b2t.common.functions.ProfileData;

import java.util.Random;

public class RespawnListener implements Listener {


    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.setMaxHealth(20);
        p.setHealth(20);
        p.setFoodLevel(30);

        if (ProfileData.getProfileData(p).isBedThere()) {
            e.setRespawnLocation(ProfileData.getProfileData(p).bedLocation);
        } else {
            p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
            e.setRespawnLocation(randomLocation(Bukkit.getWorld("world")));
        }

    }

    @EventHandler
    public void onDeath(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getName().equalsIgnoreCase("world_the_end")) {
            if (ProfileData.getProfileData(p).isBedThere()) {
                p.teleport(ProfileData.getProfileData(p).bedLocation);
            } else {
                p.sendMessage("§7由于您之前的床已被破坏或遮挡，无法将您传送到您的床边。");
                p.teleport(randomLocation(Bukkit.getWorld("world")));
            }

        }
    }

    public Location randomLocation(World w) {
        Random r = new Random();

        while (true) {
            int x = -30000 + r.nextInt(60001);
            int z = -30000 + r.nextInt(60001);
            int y = w.getHighestBlockYAt(x, z);
            Location loc = new Location(w, x, y, z);
            Biome locBiome = w.getBiome(loc.getChunk().getX(), loc.getChunk().getZ());
            if (locBiome != Biome.OCEAN
                    && locBiome != Biome.DEEP_OCEAN
                    && locBiome != Biome.RIVER
                    && locBiome != Biome.DESERT
                    && locBiome != Biome.DESERT_HILLS) {
                return loc;
            }
        }
    }

}
