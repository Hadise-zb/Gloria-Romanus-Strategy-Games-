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

    public void addComponent(GoalComponent o){
        components.add(o);
    }

    public void detachComponent(GoalComponent o){
        components.remove(o);
    }


    @Override
    public boolean goalAchieved(){
        if (this.logical.equals("AND")){
            boolean result = true;
            for (GoalComponent p : components){
                if (!p.goalAchieved()) result = false;
            }
            return result;
        }
        else {
            boolean result = false;
            for (GoalComponent p: components){
                if (!p.goalAchieved()) result = true;
            }
            return result;
        }
    }
}
