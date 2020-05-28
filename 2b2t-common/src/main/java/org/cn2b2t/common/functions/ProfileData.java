package org.cn2b2t.common.functions;

import org.bukkit.entity.Player;
import org.cn2b2t.common.listeners.ScoreboardListener;
import org.cn2b2t.common.managers.DataManager;
import org.cn2b2t.common.managers.DonateManager;
import org.cn2b2t.common.managers.ScoreboardManager;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.modules.users.AbstractUserHandler;
import org.cn2b2t.core.modules.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileData extends AbstractUserHandler {

    public static ProfileData get(User u) {
        return u.containsHandler(ProfileData.class) ? u.getHandler(ProfileData.class) : null;
    }

    public static ProfileData get(Player p) {
        return get(UserManager.getUser(p));
    }


    public Player p;

    public double totalDonate;
    public double monthlyDonate;
    public List<UUID> ignoredPlayers;

    public boolean showDeathMessages;
    public boolean showScoreboard;
    public boolean showJoinAndLeaveAlerts;

    @Override
    protected void init() {
        this.p = getUser().getPlayer();

        totalDonate = DataManager.getTotalDonate(p.getUniqueId());
        monthlyDonate = DataManager.getDonateInAMonth(p.getUniqueId());

        ignoredPlayers = new ArrayList<>();
//		UserManager.getUser(p).getDatas().getStringList(p.getUniqueId().toString() + ".ingnoredPlayers").forEach(s -> ignoredPlayers.add(UUID.fromString(s)));

        showDeathMessages = UserManager.getUser(p).getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showDeathMessages", true);
        showScoreboard = UserManager.getUser(p).getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showScoreboard", true);
        showJoinAndLeaveAlerts = UserManager.getUser(p).getDatas().getBoolean(p.getUniqueId().toString() + ".settings.showJoinAndLeaveAlerts", true);
    }

    public double getMonthlyDonate() {
        return DonateManager.getPlayerMonthlyPay(p);
    }

    public double getTotalDonate() {
        return DonateManager.getPlayerTotalPay(p);
    }

    public List<UUID> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    public boolean isIgnored(UUID uuid) {
        return getIgnoredPlayers().contains(uuid);
    }

    public void ignorePlayer(UUID uuid) {
        ignoredPlayers.add(uuid);
//		UserManager.getUser(p).getDatas().set("ingnoredPlayers", ignoredPlayers);
//		try {
//			UserManager.getUser(p).getDatas().save(UserManager.getUser(p).getConfigfile());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }

//	public void unignorePlayer(UUID uuid) {
//		ignoredPlayers.remove(uuid);
//		UserManager.getUser(p).getDatas().set("ingnoredPlayers", ignoredPlayers);
//		try {
//			UserManager.getUser(p).getDatas().save(UserManager.getUser(p).getConfigfile());
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
        UserManager.getUser(p).getDatas().set("settings.showdeathmessages", showDeathMessages);

        UserManager.getUser(p).saveDatas();
    }

    public void setShowJoinAndLeaveAlerts(boolean showJoinAndLeaveAlerts) {
        this.showJoinAndLeaveAlerts = showJoinAndLeaveAlerts;
        UserManager.getUser(p).getDatas().set("settings.showJoinAndLeaveAlerts", showJoinAndLeaveAlerts);

        UserManager.getUser(p).saveDatas();
    }

    public void setShowScoreboard(boolean showScoreboard) {
        if (showScoreboard && !this.showScoreboard) {
            ScoreboardListener.addScoreboard(p);
        } else if (ScoreboardManager.scoreboards.containsKey(p)) {
            ScoreboardManager.scoreboards.get(p).destroy();
            ScoreboardManager.scoreboards.remove(p);
        }
        this.showScoreboard = showScoreboard;
        UserManager.getUser(p).getDatas().set("settings.showScoreboard", showScoreboard);

        UserManager.getUser(p).saveDatas();
    }

    public String getPrefix() {
        return monthlyDonate >= 50 ? "§e§l" : totalDonate > 0 ? "§e" : "§6";
    }


}
