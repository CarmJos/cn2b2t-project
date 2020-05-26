package org.cn2b2t.core.managers.utils.scoreboard;

import org.cn2b2t.core.modules.users.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreboardAddEvent extends Event {
    
    private static final HandlerList handler = new HandlerList();
    User user;
    CeramicScoreboard sb;
    
    private ScoreboardAddEvent(){}
    
    public ScoreboardAddEvent(User user,CeramicScoreboard sb){
        this.user = user;
        this.sb = sb;
    }
    
    public CeramicScoreboard getKarScoreboard(){
        return this.sb;
    }
    
    public User getUser(){
        return this.user;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handler;
    }
    
    public static HandlerList getHandlerList() {
        return handler;
    }
    
}
