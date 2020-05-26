package org.cn2b2t.core.managers.render;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class BelowName {

	public static void set(Player viewer, Player target, int score, String displayName){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective(DisplaySlot.BELOW_NAME);
		if(obj == null){
			obj = sb.registerNewObjective("KSBR_BN", "dummy");
			obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		obj.setDisplayName(displayName);
		obj.getScore(target).setScore(score);
	}
	
	public static void reset(Player viewer){
		Scoreboard sb = viewer.getScoreboard();
		if(sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective("KSBR_BN");
		if(obj != null){
			obj.unregister();
		}
	}
	
}
