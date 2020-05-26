package org.cn2b2t.core.managers.team;

import org.cn2b2t.core.managers.render.NamePrefix;
import org.cn2b2t.core.modules.users.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class Team {

    private final List<User> teammates = new ArrayList<>();
    private String name;
    private Map<String, Object> handlers = new HashMap<>();
    private boolean friendlyHurt;

    private ChatColor color;

    private Team() {
    }

    public Team(String name) {
        this.name = name;
        TeamManager.regTeam(this);
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        switch (color) {
            case BOLD:
            case MAGIC:
            case ITALIC:
            case UNDERLINE:
            case RESET:
            case STRIKETHROUGH:
                throw new IllegalArgumentException(color.name());
        }
        this.color = color;
        updateColor();
    }

    public void updateColor() {
        for (Team team1 : TeamManager.getTeams()) {
            for (Team team2 : TeamManager.getTeams()) {
                if (team1 != team2) {//看到别的队伍
                    for (User u1 : team1.getTeammates()) {
                        for (User u2 : team2.getTeammates()) {
                            if (team2.getColor() == null) {
                                NamePrefix.set(u1.getPlayer(), u2.getPlayer(), "§c");
                            } else {
                                NamePrefix.set(u1.getPlayer(), u2.getPlayer(), team2.getColor().toString());
                            }
                            if (team1.getColor() == null) {
                                NamePrefix.set(u2.getPlayer(), u1.getPlayer(), "§c");
                            } else {
                                NamePrefix.set(u2.getPlayer(), u1.getPlayer(), team1.getColor().toString());
                            }
                        }
                    }
                } else {//看到自己队伍
                    for (User u1 : team1.getTeammates()) {
                        for (User u2 : team1.getTeammates()) {
                            if (team1.getColor() == null) {
                                NamePrefix.set(u1.getPlayer(), u2.getPlayer(), "§a");
                                NamePrefix.set(u2.getPlayer(), u1.getPlayer(), "§a");
                            } else {
                                NamePrefix.set(u1.getPlayer(), u2.getPlayer(), team1.getColor().toString());
                                NamePrefix.set(u2.getPlayer(), u1.getPlayer(), team1.getColor().toString());
                            }
                        }
                    }
                }
            }
        }
//		for(User viewer : this.teammates){
//			for(User target : this.teammates){//自己队友看自己队伍
//				if(color == null)
//					NamePrefix.set(viewer.getPlayer(), target.getPlayer(), "§a");
//				else
//					NamePrefix.set(viewer.getPlayer(), target.getPlayer(), color.toString());
//			}
//			for (Team ot : TeamManager.getTeams()) {
//				if (ot == this) {
//					continue;
//				}
//				for (User target : ot.getTeammates()) {
//					//自己队伍看到别的队伍
//					if(ot.color == null)
//						NamePrefix.set(viewer.getPlayer(), target.getPlayer(), "§c");
//					else
//						NamePrefix.set(viewer.getPlayer(), target.getPlayer(), color.toString());
//					//别的队伍看到自己队伍
//					if(color == null)
//						NamePrefix.set(target.getPlayer(), viewer.getPlayer(), "§c");
//					else
//						NamePrefix.set(target.getPlayer(), viewer.getPlayer(), color.toString());
//				}
//			}
//		}
    }

    public String getName() {
        return this.name;
    }

    public List<User> getTeammates() {
        return new ArrayList<>(this.teammates);
    }

    public boolean isEmpty() {
        return this.teammates.isEmpty();
    }

    public void join(User teammate) {
        if (unreg) {
            throw new NullPointerException("Team " + this.name + " has been unregistered. It can't hold person anymore.");
        }
        if (teammate == null) {
            throw new NullPointerException("Teammate cannot be null.");
        }
        Team tt = TeamManager.getTeam(teammate);
        if (tt != null) {
            if (tt == this) {
                return;
            } else {
                tt.quit(teammate);
                join(teammate);
//				throw new RuntimeException("User " + teammate.getRealName() + "(#" + teammate.getInkID() + ") 加入Team " + this.name + " 前, 他已在Team " + tt.getName() + "中.(已退出后者)");
                Bukkit.getLogger().severe("User " + teammate.getPlayer().getName() + "(#" + teammate.getPlayer().getUniqueId() + ") 加入Team " + this.name + " 前, 他已在Team " + tt.getName() + "中.(已退出后者)");
            }
        }

        this.teammates.add(teammate);

        updateColor();
    }

    public void quit(User teammate) {
        for (Team ot : TeamManager.getTeams()) {
            for (User target : ot.getTeammates()) {
                NamePrefix.reset(teammate.getPlayer(), target.getPlayer());
                NamePrefix.reset(target.getPlayer(), teammate.getPlayer());
            }
        }
        this.teammates.remove(teammate);
    }

    public boolean containsTeammate(User user) {
        return this.teammates.contains(user);
    }

    public void registerHandler(String name, Object handler) {
        if (this.handlers.containsKey(name)) {
            throw new RuntimeException("Handler " + name + " in Team " + this.name + " is exist.");
        }
        this.handlers.put(name, handler);
    }

    public void removehandler(String name) {
        if (!this.handlers.containsKey(name)) {
            throw new RuntimeException("Handler " + name + " in Team " + this.name + " wasn't exist.");
        }
        this.handlers.remove(name);
    }

    public void replaceHandler(String name, Object handler) {
        if (!this.handlers.containsKey(name)) {
            throw new RuntimeException("Handler " + name + " in Team " + this.name + " wasn't exist.");
        }
        this.handlers.replace(name, handler);
    }

    public boolean containsHandler(String name) {
        return this.handlers.containsKey(name);
    }

    public Object getHandler(String name) {
        return handlers.get(name);
    }

    public boolean friendlyHurt() {
        return this.friendlyHurt;
    }

    public void setFriendlyHurt(boolean b) {
        this.friendlyHurt = b;
    }

    private boolean unreg;

    public void unregistered() {
        if (unreg) {
            throw new NullPointerException("Team " + this.name + " has been unregistered.");
        }
        unreg = true;
        for (User tm : new HashSet<>(this.teammates)) {
//			for(Player target : Bukkit.getOnlinePlayers()) {
//				NamePrefix.reset(tm.getPlayer(), target);
//			}
            quit(tm);
        }
        TeamManager.unregTeam(this);
    }

}
