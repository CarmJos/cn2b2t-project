/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cn2b2t.core.managers.utils.scoreboard;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author LSeng
 */
public class TitleUpdateEvent extends Event {
    
    private static final HandlerList handler = new HandlerList();
    CeramicScoreboard sb;
    String old,New;
    boolean cancelled = false;
    
    private TitleUpdateEvent(){}
    
    public TitleUpdateEvent(CeramicScoreboard sb,String old,String New){
        this.sb = sb;
        this.old = old;
        this.New = New;
    }
    
    public CeramicScoreboard getKarScoreboard(){
        return this.sb;
    }
    
    public String getOld(){
        return this.old;
    }
    
    public String getNew(){
        return this.New;
    }
    
    public void setNew(String New){
        this.New = New;
    }
    
    public void setCancelled(boolean b){
        this.cancelled = b;
    }
    
    public boolean isCancelled(){
        return this.cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handler;
    }
    
    public static HandlerList getHandlerList() {
        return handler;
    }
    
}
