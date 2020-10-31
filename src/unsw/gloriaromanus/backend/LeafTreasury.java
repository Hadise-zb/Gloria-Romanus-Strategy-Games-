package unsw.gloriaromanus.backend;

public class LeafTreasury implements GoalComponent{

    private Systemcontrol f;
    public LeafTreasury(Systemcontrol f){
        this.f = f;
    }
    @Override
    public boolean goalAchieved(){
        return f.goalTreasury();
    }
}
