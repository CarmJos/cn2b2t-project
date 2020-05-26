package org.cn2b2t.common.enums;


import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum Locations {


    SPAWN(new Location(Bukkit.getWorld("world"), 0.5D, 100D, 0.5D, 0, 0));


    Location location;

    Locations(Location loc) {
        this.location = loc;
    }

    public Location getLocation() {
        return location;
    }
}
