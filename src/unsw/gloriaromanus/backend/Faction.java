
package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Faction {
    private ArrayList<Province> provinces_belong = new ArrayList<Province>();
    private ArrayList<Faction> neighboors = new ArrayList<Faction>();
    
    private Unit unit;
    //private Road road;
    private boolean turn;
    private String name; 
    private double trasure;
    private double wealth;

    public Faction() {

    }

    public void setWealth(){
        for (Province p : provinces_belong){
            wealth += p.getWealth();
        }
    }

    public void set_soldiers(int a, int b, int c) {
        
    }

    public String get_name() {
        return this.name;
    }

    public ArrayList<Unit> get_soldiers() {
        ArrayList<Unit> soldiers = new ArrayList<Unit>();
        soldiers.add(this.unit);
        return soldiers;
    }

    public ArrayList<Province> getProvinces() {
        return this.provinces_belong;
    }

    public void addProvince(Province occupation) {
        this.provinces_belong.add(occupation);
    }

    public ArrayList<Faction> get_neighboors() {
        return this.neighboors;
    }

    public void set_neighboors(Faction neighboor) {
        
    }

    public void attack(Province enermy) {
        boolean occupied = false;
        for (Province i : provinces_belong) {
            if (enermy.equals(i)) {
                occupied = true;
            }
        }

        if (occupied = true) {
            return;
        }
    }


    public void solicitTax(){
        for (Province p: provinces_belong){
            trasure += p.tributeTax();
        }
    }

    public double getTreasure(){
        return this.trasure;
    }

    // this function will check if all the province have been conquered
    public boolean goalConquered(){
        if (neighboors.size() == 0) return true;
        else return false;
    }

    // This function will check if the treasury goal have been achieved
    public boolean goalTreasury(){
        if (this.trasure >= 100000 ) return true;
        else return false;
    }
    
    // This function will check if the wealth goal have been achieved
    public boolean goalWealth(){
        if (this.wealth >= 400000) return true;
        else return false;
    }
}
