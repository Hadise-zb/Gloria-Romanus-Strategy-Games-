package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.Random;

public class CompositeGoal implements GoalComponent{
    private ArrayList<GoalComponent> components = new ArrayList<GoalComponent>();
    private String logical;

    // randomly choose logical conjunction(AND)/disjunction(OR)
    public void logical_chosen(){
        Random r = new Random();
        // If the result generated from r.nextBoolean() is true, assign the logical "AND"
        if (r.nextBoolean()) this.logical = "AND";
        else this.logical = "OR";
    }

    @Override
    public boolean goalAchieved(Faction f){
        if (this.logical.equals("AND")){
            boolean result = true;
            for (GoalComponent p : components){
                if (!p.goalAchieved(f)) result = false;
            }
            return result;
        }
        else {
            boolean result = false;
            for (GoalComponent p: components){
                if (!p.goalAchieved(f)) result = true;
            }
            return result;
        }
    }
}
