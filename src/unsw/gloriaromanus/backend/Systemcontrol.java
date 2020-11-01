package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Systemcontrol implements TurnSubject{
    private Faction myFaction;
    //private Faction enermyFaction;
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

    public boolean movement(Troop troop, Province start, Province end) {
        boolean accept = true;
        String start_province = start.get_name();
        String destination = end.get_name();
        DPQ shortest_path = new DPQ();
        int movement_point_need = shortest_path.movement(start_province, destination, enermyFactions);

        int minimum_movement_point = Integer.MAX_VALUE;
        for (Unit unit : troop.get_soldiers()) {
            if (unit.get_movementpoint() < minimum_movement_point) {
                minimum_movement_point = unit.get_movementpoint();
            }
        }
        // if can arrive, update the province.
        // if can't , return false.
        if (minimum_movement_point < (movement_point_need * 4)) {
            accept = false;
            return accept;
        }

        start.get_my_troops().remove(troop);
        
        end.get_my_troops().add(troop);
        
        if (movement_point_need == Integer.MAX_VALUE) {
            accept = false;
            return accept;
        }
        return accept;
    }

    public void engage(String enermy_faction, String destination) {
        for (Faction enermyfaction : enermyFactions) {
            if (enermyfaction.get_name().equals(enermy_faction)) {
                for (Province province : enermyfaction.getProvinces()) {
                    if (province.get_name().equals(destination)) {
                        province.battle(myFaction, enermyfaction);
                        break;
                    }
                }
            }
        }
    }



    public void saveProgress(){
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
}