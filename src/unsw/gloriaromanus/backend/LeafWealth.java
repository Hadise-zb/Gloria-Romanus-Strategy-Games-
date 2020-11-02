package unsw.gloriaromanus.backend;

public class LeafWealth implements GoalComponent{

    private Systemcontrol f;
    public LeafWealth(Systemcontrol f){
        this.f = f;
    }
    @Override
    public boolean goalAchieved(){
        return f.goalWealth();
    }
}
