
package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Faction implements TurnObserver{
    private ArrayList<Province> provinces_belong = new ArrayList<Province>();
    private ArrayList<Faction> neighboors = new ArrayList<Faction>();
    
    private Unit unit;
    //private Road road;
    private boolean turn;
    private String name; 
    private double treasure;
    private double wealth;

    // Initially every faction have 50 treasure but 0 wealth
    public Faction() {
        this.treasure = 50;
        this.wealth = 0;
    }

    public void setWealth(){
        for (Province p : provinces_belong){
            wealth = p.getWealth();
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

    public void addProvince(Province province) {
        this.provinces_belong.add(province);
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
            treasure += p.tributeTax();
        }
    }

    public double getTreasure(){
        return this.treasure;
    }

    public double getWealth(){
        return this.wealth;
    }


    // this function will check if all the province have been conquered
    

    // This function will check if the treasury goal have been achieved
    
    
    // This function will check if the wealth goal have been achieved
    

    public int getNumProvince(){
        return provinces_belong.size();
    }

    @Override
    public void update(){
        for (Province p : provinces_belong){
            p.solicitTownWealth();
        }
        this.solicitTax();
        this.setWealth();
    }
}
