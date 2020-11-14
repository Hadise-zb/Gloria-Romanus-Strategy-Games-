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

        if (f.get_name().equals("Rome")) {
            this.enermyFaction = new Faction("Gaul");
        }

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
    }

    public void endTurn(){
        turn += 1;
        notifyobservers();
    }

    public void set_enermy(Faction enermy) {
        this.enermyFaction = enermy;
    }

    public Faction getEnermyFaction(){
        return this.enermyFaction;
    }

    public Faction get_myfaction() {
        return this.myFaction;
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
            my_writetofile(myFaction);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            enermy_writetofile(enermyFaction);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void continueProgress() {
        try {
            this.myFaction = my_readfile();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            this.enermyFaction = enermy_readfile();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void my_writetofile(Faction f) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("values/myFaction.bin"));
        objectOutputStream.writeObject(f);
    }

    public static Faction my_readfile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("values/myFaction.bin"));
        Faction my = (Faction) objectInputStream.readObject();
        return my;
    }

    public static void enermy_writetofile(Faction f) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("values/enermyFaction.bin"));
        objectOutputStream.writeObject(f);
    }

    public static Faction enermy_readfile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("values/enermyFaction.bin"));
        Faction enermy = (Faction) objectInputStream.readObject();
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


}
