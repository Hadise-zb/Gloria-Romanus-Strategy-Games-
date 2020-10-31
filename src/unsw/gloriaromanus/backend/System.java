package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class System {
    private Faction myFaction;
    private ArrayList<Faction> enermyFactions = new ArrayList<Faction>();
    private int turn;
    

    /*
    public void update() {
        for (Province province: this.provinces) {
            this.treasure += province.getTreasure();
            faction.update();
        }
        
    }
 */
    public System(Faction f){
        this.turn = 0;
        this.myFaction = f;
    }

    public void startTurn(){
        turn +=1;
    }

    public void endTurn(){
        
    }

    public void solveProgress(){
        // TODO
    }
    
    public void continueProgress(){
        // TODO
    }

    public void campaignVictory(){
        
    }

    // check if any faction been eliminated
    public void checkFaction(){
        for (Faction f : enermyFactions){
            if (f.getNumProvince() == 0) {
                enermyFactions.remove(f);
            }
        }
    }

    public boolean goalConquered(){
        if (enermyFactions.size() == 0) return true;
        else return false;
    }

    public boolean goalTreasury(){
        if (myFaction.getTreasure() >= 100000 ) return true;
        else return false;
    }

    public boolean goalWealth(){
        if (myFaction.getWealth() >= 400000) return true;
        else return false;
    }

    public boolean setGoal(){
        LeafConquered conquereGoal = new LeafConquered(this);
        LeafTreasury treasuryGoal = new LeafTreasury(this);
        LeafWealth wealthGoal = new LeafWealth(this);

        CompositeGoal newGoal = new CompositeGoal();
        newGoal.addComponent(conquereGoal);
        newGoal.addComponent(treasuryGoal);
        newGoal.addComponent(wealthGoal);
        return newGoal.goalAchieved();
    }


}