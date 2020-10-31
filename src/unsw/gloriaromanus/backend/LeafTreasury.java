package unsw.gloriaromanus.backend;

public class LeafTreasury implements GoalComponent{
    @Override
    public boolean goalAchieved(Faction f){
        return f.goalTreasury();
    }
}
