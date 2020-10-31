
package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Faction {
    private ArrayList<Province> occupations = new ArrayList<Province>();
    private ArrayList<Faction> neighboors = new ArrayList<Faction>();
    private Unit unit;
    private Road road;
    private boolean turn;
    private String belong;
    private String name; 
    private double trasure;

    public Faction() {
        this.treasure = 50;
        this.wealth = 50;
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

    public ArrayList<Province> get_occupations() {
        return this.occupations;
    }

    public void addOccupations(Province occupation) {
        this.occupations.add(occupation);
    }

    public ArrayList<Faction> get_neighboors() {
        return this.neighboors;
    }

    public void set_neighboors(Faction neighboor) {
        //this.occupations.add(neighboor);
    }

    public void attack(Province enermy) {
        boolean occupied = false;
        for (Province i : occupations) {
            if (enermy.equals(i)) {
                occupied = true;
            }
        }

        if (occupied = true) {
            return;
        }

        

    }


    public void solicitTax(){
        for (Province p: occupations){
            trasure += p.tributeTax();
        }
    }

    public double getTreasure(){
        return this.trasure;
    }

}
