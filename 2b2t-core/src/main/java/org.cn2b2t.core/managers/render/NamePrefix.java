package org.cn2b2t.core.managers.render;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NamePrefix {

	public static void set(Player viewer, Player target, String str){
		set(viewer,target,str, PSType.PREFIX);
	}
	
	public static void set(Player viewer, Player target, String str, PSType type){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
		}
		Team t = sb.getTeam("NP_"+target.getName().substring(0, Math.min(13, target.getName().length())));
		if(t==null){
			t = sb.registerNewTeam("NP_"+target.getName().substring(0, Math.min(13, target.getName().length())));
			t.setNameTagVisibility(NameTagVisibility.ALWAYS);
		}
		if(str.length()>16){
			str = str.substring(0, 16);
		}
		switch (type) {
			case PREFIX:
				t.setPrefix(str);
				break;
			case SUFFIX:
				t.setSuffix(str);
				break;
			default:
				throw new AssertionError(type.name());
		}
		if(!t.hasEntry(target.getName())) t.addEntry(target.getName());
	}
	
	public static void reset(Player viewer, Player target){
		Scoreboard sb = viewer.getScoreboard();
		if(Bukkit.getScoreboardManager().getMainScoreboard() == sb || sb == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			viewer.setScoreboard(sb);
			return;
		}
		Team t = sb.getTeam("NP_"+target.getUniqueId().toString().substring(0, 13));
		if(t!=null){
			t.unregister();
		}
	}
	
	public enum PSType{
		PREFIX,
		SUFFIX;
	}
	
}
