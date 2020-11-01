package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

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
            o.update(this.turn);
        }
    }

    
    public Systemcontrol(Faction f){
        this.turn = 0;
        this.myFaction = f;
    }

    public void endTurn(){
        turn += 1;
        notifyobservers();
    }

    public void movement(Troop troop, Province start, Province end) {
        String start_province = start.get_name();
        String destination = end.get_name();
        DPQ shortest_path = new DPQ();
        int movement_point_need = shortest_path.movement(start_province, destination, enermyFactions);
        // to do
        // if can arrive, update the province.
        // if can't , raise error.

    }

    public void engage(String destination) {
        String result = "draw";
        for (Faction faction : enermyFactions) {
            for (Province province : faction.getProvinces()) {
                if (province.get_name().equals(destination)) {
                    result = province.battle();
                    break;
                }
            }
        }
        // to do
        // update the information for province
        if (result.equals("win")) {

        } else if (result.equals("lose")) {

        } else {
            // draw
        }
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

    public int getTurn(){
        return this.turn;
    }

    public static void main(String[] args){
        Faction my_faction = new Faction("AUSTRALIA");
        Systemcontrol testSystem = new Systemcontrol(my_faction);
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);

        Unit new_unit = new Unit("legionary", "AUSTRALIA", "Cavalry");
        my_faction.requestTraining(p, new_unit, 5, testSystem.getTurn());
        
        //testSystem.endTurn();
        System.out.println(p.getUnit());
    }


}