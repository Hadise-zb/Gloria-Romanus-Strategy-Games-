package unsw.gloriaromanus.backend;

public class LeafConquered implements GoalComponent{

    private Systemcontrol f;
    public LeafConquered(Systemcontrol f){
        this.f = f;
    }

    @Override
    public boolean goalAchieved(){
        return f.goalConquered();
    }
}
