package org.cn2b2t.core.managers.utils.scoreboard;

import org.cn2b2t.core.modules.users.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreboardEnableEvent extends Event {
    
    private static final HandlerList handler = new HandlerList();
    
    User user;
    boolean b;
    
    private ScoreboardEnableEvent(){}
    
    public ScoreboardEnableEvent(User user,boolean b){
        this.user = user;
        this.b = b;
    }
    
    public boolean getWhetherEnable(){
        return this.b;
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
