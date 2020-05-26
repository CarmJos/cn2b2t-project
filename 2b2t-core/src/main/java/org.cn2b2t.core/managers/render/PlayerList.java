package org.cn2b2t.core.managers.render;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerList {

	public static boolean isActive(Player viewer){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective("KSBR_PL");
		return obj != null;
	}
	
	public static void health(Player viewer){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective(DisplaySlot.PLAYER_LIST);
		if(obj == null){
			obj = sb.registerNewObjective("KSBR_PL", "health");
			obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}
	}
	
	public static void set(Player viewer, Player target, int score){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective(DisplaySlot.PLAYER_LIST);
		if(obj == null){
			obj = sb.registerNewObjective("KSBR_PL", "dummy");
			obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}
		obj.getScore(target).setScore(score);
	}
	
	public static void reset(Player viewer){
		Scoreboard sb = viewer.getScoreboard();
		if(sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Objective obj = sb.getObjective("KSBR_PL");
		if(obj != null){
			obj.unregister();
		}
	}
}
