package org.cn2b2t.common.functions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.cn2b2t.common.listeners.ScoreboardListener;
import org.cn2b2t.common.managers.DataManager;
import org.cn2b2t.common.managers.DonateManager;
import org.cn2b2t.common.managers.ScoreboardManager;
import org.cn2b2t.common.managers.UserDataManager;

import java.io.IOException;
import java.util.*;

public class ProfileData {

	public static Map<Player, ProfileData> profileDatas = new HashMap<>();

	public Player player;

	public double totalDonate;
	public double monthlyDonate;
	public List<UUID> ignoredPlayers;

	public boolean showDeathMessages;
	public boolean showScoreboard;
	public boolean showJoinAndLeaveAlerts;

	public Location bedLocation;
	public Location spawnLocation;

	public ProfileData(Player p) {
		this.player = p;

		totalDonate = DataManager.getTotalDonate(p.getUniqueId());
		monthlyDonate = DataManager.getDonateInAMonth(p.getUniqueId());

		ignoredPlayers = new ArrayList<>();
//		UserDataManager.getDatas().getStringList(p.getUniqueId().toString() + ".ingnoredPlayers").forEach(s -> ignoredPlayers.add(UUID.fromString(s)));

		showDeathMessages = UserDataManager.getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showDeathMessages", true);
		showScoreboard = UserDataManager.getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showScoreboard", true);
		showJoinAndLeaveAlerts = UserDataManager.getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showJoinAndLeaveAlerts", true);

		bedLocation = new Location(Bukkit.getWorld("world"),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".bedLocation.x", 0D),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".bedLocation.y", 101D),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".bedLocation.z", 0D),
				UserDataManager.getDatas().getLong(p.getUniqueId().toString() + ".bedLocation.yaw", 0L),
				UserDataManager.getDatas().getLong(p.getUniqueId().toString() + ".bedLocation.pitch", 0L));

		spawnLocation = new Location(Bukkit.getWorld("world"),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".spawnLocation.x", 0D),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".spawnLocation.y", 101D),
				UserDataManager.getDatas().getDouble(p.getUniqueId().toString() + ".spawnLocation.z", 0D),
				UserDataManager.getDatas().getLong(p.getUniqueId().toString() + ".spawnLocation.yaw", 0L),
				UserDataManager.getDatas().getLong(p.getUniqueId().toString() + ".spawnLocation.pitch", 0L));

	}

	public double getMonthlyDonate() {
		return DonateManager.getPlayerMonthlyPay(player);
	}

	public double getTotalDonate() {
		return DonateManager.getPlayerTotalPay(player);
	}

	public List<UUID> getIgnoredPlayers() {
		return ignoredPlayers;
	}

	public boolean isIgnored(UUID uuid) {
		return ignoredPlayers.contains(uuid);
	}

	public void ignorePlayer(UUID uuid) {
		ignoredPlayers.add(uuid);
//		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".ingnoredPlayers", ignoredPlayers);
//		try {
//			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

//	public void unignorePlayer(UUID uuid) {
//		ignoredPlayers.remove(uuid);
//		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".ingnoredPlayers", ignoredPlayers);
//		try {
//			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void updateDonates() {
		this.totalDonate = getTotalDonate();
		this.monthlyDonate = getMonthlyDonate();
	}

	public void setShowDeathMessages(boolean showDeathMessages) {
		this.showDeathMessages = showDeathMessages;
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".settings.showdeathmessages", showDeathMessages);
		try {
			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setShowJoinAndLeaveAlerts(boolean showJoinAndLeaveAlerts) {
		this.showJoinAndLeaveAlerts = showJoinAndLeaveAlerts;
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".settings.showJoinAndLeaveAlerts", showJoinAndLeaveAlerts);
		try {
			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setShowScoreboard(boolean showScoreboard) {
		if (showScoreboard && !this.showScoreboard) {
			ScoreboardListener.addScoreboard(player);
		} else if (ScoreboardManager.scoreboards.containsKey(player)) {
			ScoreboardManager.scoreboards.get(player).destroy();
			ScoreboardManager.scoreboards.remove(player);
		}
		this.showScoreboard = showScoreboard;
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".settings.showScoreboard", showScoreboard);
		try {
			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getPrefix() {
		return monthlyDonate >= 50 ? "§e§l" : totalDonate > 0 ? "§e" : "§6";
	}

	public void saveBedLocation(Location bedlocation) {
		this.bedLocation = bedlocation;
		this.spawnLocation = player.getLocation();
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".spawnLocation.x", player.getLocation().getX());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".spawnLocation.y", player.getLocation().getY());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".spawnLocation.z", player.getLocation().getZ());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".spawnLocation.yaw", player.getLocation().getYaw());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".spawnLocation.pitch", player.getLocation().getPitch());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".bedLocation.x", bedlocation.getX());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".bedLocation.y", bedlocation.getY());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".bedLocation.z", bedlocation.getZ());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".bedLocation.yaw", bedlocation.getYaw());
		UserDataManager.getDatas().set(player.getUniqueId().toString() + ".bedLocation.pitch", bedlocation.getPitch());
		try {
			UserDataManager.getDatas().save(UserDataManager.getConfigfile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isBedThere() {
		return bedLocation.getBlock().getType() == Material.BED_BLOCK && bedLocation.getX() <= 300000 && bedLocation.getZ() <= 300000;
	}

	public static ProfileData getProfileData(Player p) {
		return profileDatas.get(p);
	}

	public static void loadProfileData(Player p) {
		profileDatas.put(p, new ProfileData(p));
	}

	public static void unloadProfileData(Player p) {
		profileDatas.remove(p);
	}


}
