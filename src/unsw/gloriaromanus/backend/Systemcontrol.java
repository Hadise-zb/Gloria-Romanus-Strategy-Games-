package unsw.gloriaromanus.backend;

import java.util.ArrayList;
//import unsw.gloriaromanus.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.io.FileNotFoundException;

//import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.*;

public class Systemcontrol implements TurnSubject{
    private Faction myFaction;
    private Faction enermyFaction;
    //private ArrayList<Faction> enermyFactions = new ArrayList<Faction>();
    private int turn;
    private ArrayList<TurnObserver> observers = new ArrayList<TurnObserver>();
    
    private Save save_obj;

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

    
    public Systemcontrol(Faction human_faction, Faction enermy_faction){
        this.turn = 0;
        this.myFaction = human_faction;
        this.enermyFaction = enermy_faction;

        //if (f.get_name().equals("Rome")) {
        //    this.enermyFaction = new Faction("Gaul");
        //}
        this.enermyFaction = enermy_faction;
        
        //
        this.attach(myFaction);
        this.attach(enermyFaction);

        //
        try {
            JSONObject matrix = new JSONObject(
                Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json")));
            for (String key : matrix.keySet()) {
                JSONArray pro = matrix.getJSONArray(key);
                

                if (myFaction.get_name().equals(key)) {
                    for (int j = 0; j < pro.length (); j++) {

                        Province human_province = new Province(pro.getString (j), myFaction, 0, 3.0);
                        myFaction.getProvinces().add(human_province);
                        
                    }
                } else {
                    for (int j = 0; j < pro.length (); j++) {

                        Province enermy_province = new Province(pro.getString (j), myFaction, 0, 3.0);
                        enermyFaction.getProvinces().add(enermy_province);
                    }
                    
                }
            }
        } catch (JSONException | IOException e) {
            ((Throwable) e).printStackTrace();
        } 
        //
        this.save_obj = new Save(this);
        //
    }

    public void endTurn(){
        turn += 1;
        notifyobservers();
    }

    public void set_enermy(Faction enermy) {
        this.enermyFaction = enermy;
    }

    public Faction get_myfaction() {
        return this.myFaction;
    }

    public Faction get_enermyfaction() {
        return this.enermyFaction;
    }

    public boolean human_move(String unit, String start, String end) {
        boolean accept = true;
        if (start.equals(end)) {
            accept = false;
            return accept;
        }
        Unit unit_moved = null;
        for (Province p : myFaction.getProvinces()) {
            if (p.get_name().equals(start)) {
                if (!p.get_units().isEmpty()) {
                    for (Unit u : p.get_units()) {
                        if (u.get_name().equals(unit)) {
                            unit_moved = u;
                            break;
                        }
                    }
                    p.get_units().remove(unit_moved);
                }
                break;          
            }
        }

        if (unit_moved == null) {
            return false;
        } else {
            for (Province p : myFaction.getProvinces()) {
                if (p.get_name().equals(end)) { 
                    p.get_units().add(unit_moved);
                    break;         
                }
            }
        }
        return accept;
    }

    public boolean enermy_move(String unit, String start, String end) {
        boolean accept = true;
        if (start.equals(end)) {
            accept = false;
            return accept;
        }
        Unit unit_moved = null;
        for (Province p : enermyFaction.getProvinces()) {
            if (p.get_name().equals(start)) {
                if (!p.get_units().isEmpty()) {
                    for (Unit u : p.get_units()) {
                        if (u.get_name().equals(unit)) {
                            unit_moved = u;
                            break;
                        }
                    }
                    p.get_units().remove(unit_moved);
                }
                break;          
            }
        }

        if (unit_moved == null) {
            return false;
        } else {
            for (Province p : enermyFaction.getProvinces()) {
                if (p.get_name().equals(end)) {   
                    p.get_units().add(unit_moved);
                    break;         
                }
            }
        }
        return accept;
    }


    public boolean movement(Troop troop, Province start, Province end) {
        boolean accept = true;
        String start_province = start.get_name();
        String destination = end.get_name();
        DPQ shortest_path = new DPQ();
        int movement_point_need = 1;
        //int movement_point_need = shortest_path.movement(start_province, destination, enermyFaction);
        //System.out.println(movement_point_need);
        int minimum_movement_point = Integer.MAX_VALUE;
        
        //judge whether the troop is in the start province
        boolean find = false;
        for (Troop t : start.get_my_troops()){
            if (t.equals(troop)) {
                find = true;
            }
        }
        if (find == false) {
            return false;
        }

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

    public String engage(String myprovince, String enermyprovince) {
        Province my = new Province("", myFaction, 1, 1.1);
        Province enermy = new Province("", enermyFaction, 1, 1.1);
        for (Province my_p : myFaction.getProvinces()){
            if (my_p.get_name().equals(myprovince)) {
                my = my_p;
            }
        }
        for (Province enermy_p : enermyFaction.getProvinces()){
            if (enermy_p.get_name().equals(enermyprovince)) {
                enermy = enermy_p;
            }
        }
        
        for (int i = 0; i < my.get_my_troops().size(); i++) {
            movement(my.get_my_troops().get(i), my, enermy);
        }

        String result = enermy.battle(myFaction, enermyFaction);
        return result;
    }




    public void saveProgress(){
        try {
            my_writetofile(this.save_obj);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void continueProgress() {
        try {
            this.save_obj = my_readfile();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void my_writetofile(Save f) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("values/myFaction.bin"));
        objectOutputStream.writeObject(f);
        //objectOutputStream.close();
    }

    public static Save my_readfile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("values/myFaction.bin"));
        Save my = (Save) objectInputStream.readObject();
        //objectInputStream.close();
        return my;
    }

    public static void enermy_writetofile(Faction f) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("values/enermyFaction.bin"));
        objectOutputStream.writeObject(f);
        objectOutputStream.close();
    }

    public static Faction enermy_readfile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("values/enermyFaction.bin"));
        Faction enermy = (Faction) objectInputStream.readObject();
        objectInputStream.close();
        return enermy;
    }
    
    public void campaignVictory(){
        
    }

    // check if any faction been eliminated
    public void checkFaction(){
        /*
        for (Faction f : enermyFactions){
            if (f.getNumProvince() == 0) {
                enermyFactions.remove(f);
            }
        }*/
        
    }

    public boolean goalConquered(){
        if (enermyFaction == null) return true;
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
        /*
        Faction my_faction = new Faction("AUSTRALIA");
        Systemcontrol testSystem = new Systemcontrol(my_faction);
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);

        Unit new_unit = new Unit("legionary", "AUSTRALIA", "Cavalry");
        my_faction.requestTraining(p, new_unit, 5, testSystem.getTurn());
        
        //testSystem.endTurn();
        System.out.println(p.getUnit());
        */
    }
    


}
