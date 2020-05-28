package org.cn2b2t.functions.death.managers.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Random;

public class DeathManager {


    public static Location randomLocation(World w) {
        Random r = new Random();

        while (true) {
            int x = -15000 + r.nextInt(30001);
            int z = -15000 + r.nextInt(30001);
            int y = w.getHighestBlockYAt(x, z);
            Location loc = new Location(w, x, y, z);
            Biome locBiome = w.getBiome(loc.getChunk().getX(), loc.getChunk().getZ());
            if (locBiome != Biome.OCEAN
                    && locBiome != Biome.DEEP_OCEAN
                    && locBiome != Biome.RIVER
                    && locBiome != Biome.DESERT
                    && locBiome != Biome.DESERT_HILLS
                    && !canReplace(loc.getBlock().getType())) {
                return loc.add(0, 1, 0);
            }
        }
    }

    public static boolean canReplace(Material material) {
        switch (material) {
            case WATER:
            case LAVA:
            case STATIONARY_WATER:
            case STATIONARY_LAVA:
            case AIR:
                return true;
            default:
                return false;

        }

    }


}
