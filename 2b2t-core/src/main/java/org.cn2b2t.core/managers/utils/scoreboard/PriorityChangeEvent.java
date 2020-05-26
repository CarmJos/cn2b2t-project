package org.cn2b2t.core.managers.utils.scoreboard;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PriorityChangeEvent extends Event {
    
    private static final HandlerList handler = new HandlerList();
    
    CeramicScoreboard sb;
    SBPriority old,New;
    boolean cancelled = false;
    
    private PriorityChangeEvent(){}
    
    public PriorityChangeEvent(CeramicScoreboard sb,SBPriority old,SBPriority New){
        this.sb = sb;
        this.old = old;
        this.New = New;
    }
    
    public CeramicScoreboard getKarScoreboard(){
        return sb;
    }
    
    public SBPriority getOld(){
        return this.old;
    }
    
    public SBPriority getNew(){
        return this.New;
    }
    
    public void setSBPriority(SBPriority p){
        this.New = p;
    }
    
    public boolean isCancelled(){
        return this.cancelled;
    }
    
    public void setCanncelled(Boolean b){
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }
    
    public static HandlerList getHandlerList() {
        return handler;
    }
    
}
