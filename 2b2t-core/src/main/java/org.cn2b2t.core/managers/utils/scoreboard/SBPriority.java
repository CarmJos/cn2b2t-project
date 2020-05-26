package org.cn2b2t.core.managers.utils.scoreboard;

public enum SBPriority {
    
    CANCEL(-1),
    LOWEAST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHER(5),
    ULTRA(6),
    ULTRA_P(7),
    ULTRA_PS(8),
    ULTRA_X(9),
    COSTOM(0,true);
    
    int pri;
    boolean costom = false;
    
    private SBPriority(int pri){
        this.pri = pri;
    }
    
    private SBPriority(int pri,boolean costom){
        this.pri = pri;
        this.costom = costom;
    }
    
    public boolean costom(int pri){
        if(costom){
            this.pri = pri;
            return true;
        }else{
            return false;
        }
    }
    
    public int size(){
        return this.pri;
    }
    
}
