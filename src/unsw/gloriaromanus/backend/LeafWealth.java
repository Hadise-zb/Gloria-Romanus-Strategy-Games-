package unsw.gloriaromanus.backend;

public class LeafWealth implements GoalComponent{

    private System f;
    public LeafWealth(System f){
        this.f = f;
    }
    @Override
    public boolean goalAchieved(){
        return f.goalWealth();
    }
}
