package org.cn2b2t.core.managers.team;


import org.cn2b2t.core.modules.users.User;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private static final List<Team> teams = new ArrayList<>();

    protected static void regTeam(Team t) {
        teams.add(t);
    }

    protected static void unregTeam(Team t) {
        teams.remove(t);
    }

    /**
     * 解散所有Team
     */
    public static void unregAll() {
        for (Team t : teams) {
            t.unregistered();
        }
    }

    /**
     * 根据Team名获取Team，注意大小写
     *
     * @param name
     * @return 若没有找到Team，返回null
     */
    public static Team getTeam(String name) {
        for (Team t : teams) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据Team名获取Team，忽略大小写
     *
     * @param name
     * @return 若没有找到Team，返回null
     */
    public static Team getTeamIgnoreLetterCase(String name) {
        for (Team t : teams) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 得到User所在的Team
     *
     * @param u
     * @return User所在的Team，若User不在Team里，则返回null
     */
    public static Team getTeam(User u) {
        for (Team t : teams) {
            if (t.containsTeammate(u)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 得到所有Team的Set拷贝
     *
     * @return
     */
    public static List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

}
