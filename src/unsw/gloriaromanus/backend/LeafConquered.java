package unsw.gloriaromanus.backend;

public class LeafConquered implements GoalComponent{

    private System f;
    public LeafConquered(System f){
        this.f = f;
    }

    @Override
    public boolean goalAchieved(){
        return f.goalConquered();
    }
}
