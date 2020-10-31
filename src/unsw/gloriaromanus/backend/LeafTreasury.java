package unsw.gloriaromanus.backend;

public class LeafTreasury implements GoalComponent{

    private System f;
    public LeafTreasury(System f){
        this.f = f;
    }
    @Override
    public boolean goalAchieved(){
        return f.goalTreasury();
    }
}
