package unsw.gloriaromanus.backend;

public class LeafWealth implements GoalComponent{
    @Override
    public boolean goalAchieved(Faction f){
        return f.goalWealth();
    }
}
