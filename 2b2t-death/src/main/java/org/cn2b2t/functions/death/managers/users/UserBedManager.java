package org.cn2b2t.functions.death.managers.users;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.modules.users.AbstractUserHandler;
import org.cn2b2t.core.modules.users.User;

public class UserBedManager extends AbstractUserHandler {

    Player p;
    User u;

    public Location bedLocation;
    public Location spawnLocation;

    public static UserBedManager get(User u) {
        return u.containsHandler(UserBedManager.class) ? u.getHandler(UserBedManager.class) : null;
    }

    public static UserBedManager get(Player p) {
        return get(UserManager.getUser(p));
    }


    @Override
    protected void init() {
        p = getUser().getPlayer();
        u = getUser();

        bedLocation = new Location(Bukkit.getWorld("world"),
                getUser().getDatas().getDouble("bedLocation.x", 0D),
                getUser().getDatas().getDouble("bedLocation.y", 101D),
                getUser().getDatas().getDouble("bedLocation.z", 0D),
                getUser().getDatas().getLong("bedLocation.yaw", 0L),
                getUser().getDatas().getLong("bedLocation.pitch", 0L));

        spawnLocation = new Location(Bukkit.getWorld("world"),
                getUser().getDatas().getDouble("spawnLocation.x", 0D),
                getUser().getDatas().getDouble("spawnLocation.y", 101D),
                getUser().getDatas().getDouble("spawnLocation.z", 0D),
                getUser().getDatas().getLong("spawnLocation.yaw", 0L),
                getUser().getDatas().getLong("spawnLocation.pitch", 0L));

        callLoadedEvent(Main.getInstance());
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void saveBedLocation(Location bedlocation) {
        this.bedLocation = bedlocation;
        this.spawnLocation = p.getLocation();
        getDatas().set("spawnLocation.x", p.getLocation().getX());
        getDatas().set("spawnLocation.y", p.getLocation().getY());
        getDatas().set("spawnLocation.z", p.getLocation().getZ());
        getDatas().set("spawnLocation.yaw", p.getLocation().getYaw());
        getDatas().set("spawnLocation.pitch", p.getLocation().getPitch());
        getDatas().set("bedLocation.x", bedlocation.getX());
        getDatas().set("bedLocation.y", bedlocation.getY());
        getDatas().set("bedLocation.z", bedlocation.getZ());
        getDatas().set("bedLocation.pitch", bedlocation.getPitch());
        getUser().saveDatas();
    }

    public boolean isBedThere() {
        return getBedLocation().getBlock().getType() == Material.BED_BLOCK
                && (bedLocation.getX() < 0 ? -bedLocation.getX() : bedLocation.getX()) <= (getBedLocation().getWorld().getWorldBorder().getSize() * 0.5)
                && (bedLocation.getZ() < 0 ? -bedLocation.getZ() : bedLocation.getZ()) <= (getBedLocation().getWorld().getWorldBorder().getSize() * 0.5);
    }

    private FileConfiguration getDatas() {
        return getUser().getDatas();
    }


}
