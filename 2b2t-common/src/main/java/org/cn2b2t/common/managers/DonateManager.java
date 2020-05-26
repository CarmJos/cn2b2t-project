package org.cn2b2t.common.managers;

import org.bukkit.entity.Player;


public class DonateManager {

	public static int total;
	public static int monthly;

	public static void init() {
		total = DataManager.getTotalDonate();
		monthly = DataManager.getDonateInAMonth();
	}


	public static double getPlayerMonthlyPay(Player p) {
		return DataManager.getDonateInAMonth(p.getUniqueId());
	}

	public static double getPlayerTotalPay(Player p) {
		return DataManager.getTotalDonate(p.getUniqueId());
	}

	public static int getID(String s) {
		switch (s) {
			case "5":
				return 13262;
			case "10":
				return 13263;
			case "20":
				return 13264;
			case "50":
				return 13265;
			default:
				return 13261;
		}
	}
}
