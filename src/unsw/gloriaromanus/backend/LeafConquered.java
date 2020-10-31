package unsw.gloriaromanus.backend;

public class LeafConquered implements GoalComponent{
    @Override
    public boolean goalAchieved(Faction f){
        return f.goalConquered();
    }
}
