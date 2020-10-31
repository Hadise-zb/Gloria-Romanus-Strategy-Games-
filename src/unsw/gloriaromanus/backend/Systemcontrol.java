package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Systemcontrol implements TurnSubject{
    private Faction myFaction;
    private ArrayList<Faction> enermyFactions = new ArrayList<Faction>();
    private int turn;
    private ArrayList<TurnObserver> observers = new ArrayList<TurnObserver>();
    
    @Override
    public void attach(TurnObserver o){
        observers.add(o);
    }

    @Override
    public void detach(TurnObserver o){
        observers.remove(o);
    }

    @Override
    public void notifyobservers(){
        for (TurnObserver o : observers){
            o.update();
        }
    }


    /*
    public void update() {
        for (Province province: this.provinces) {
            this.treasure += province.getTreasure();
            faction.update();
        }
        
    }
 */
    public Systemcontrol(Faction f){
        this.turn = 0;
        this.myFaction = f;
    }

    public void endTurn(){
        turn += 1;
        notifyobservers();
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


    public static void main(String[] args){
        Faction my_faction = new Faction();
        Systemcontrol testSystem = new Systemcontrol(my_faction);
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);  

        p.addTown();
        p.addTown();
        assertEquals(my_faction.getWealth(), 0);
        assertEquals(my_faction.getTreasure(), 50);

        testSystem.endTurn();

        assertEquals(my_faction.getWealth(), 18);
        assertEquals(my_faction.getTreasure(), 52);

        testSystem.endTurn();

        assertEquals(my_faction.getWealth(), 34.2);
        assertEquals(my_faction.getTreasure(), 55.8);

        testSystem.endTurn();
        assertEquals(my_faction.getWealth(), 48.78);
        assertEquals(my_faction.getTreasure(), 61.22);
    }


}