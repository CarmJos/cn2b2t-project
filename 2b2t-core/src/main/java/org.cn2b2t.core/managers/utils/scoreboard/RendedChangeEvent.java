package org.cn2b2t.core.managers.utils.scoreboard;

import org.cn2b2t.core.modules.users.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RendedChangeEvent extends Event {
    
    private static final HandlerList handler = new HandlerList();
    
    User user;
    CeramicScoreboard old,New;
    
    private RendedChangeEvent(){}
    
    public RendedChangeEvent(User user,CeramicScoreboard old,CeramicScoreboard New){
        this.user = user;
        this.old = old;
        this.New = New;
    }
    
    public User getUser(){
        return this.user;
    }
    
     public CeramicScoreboard getOld(){
        return this.old;
    }
    
    public CeramicScoreboard getNew(){
        return this.New;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handler;
    }
    
    public static HandlerList getHandlerList() {
        return handler;
    }
    
}
