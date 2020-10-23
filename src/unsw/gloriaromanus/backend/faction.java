package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class faction {
    private ArrayList<faction> occupations;
    private ArrayList<faction> neighboors;
    private Soldier Cavalry;
    private Soldier Infantry;
    private Soldier Artillery;
    private Road road;
    private boolean turn;
    private String belong; 

    public faction() {

    }

    public void set_soldiers(int a, int b, int c) {
        
    }

    public ArrayList<Soldier> get_soldiers() {
        ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
        soldiers.add(this.Cavalry);
        soldiers.add(this.Infantry);
        soldiers.add(this.Artillery);
        return soldiers;
    }

    public ArrayList<faction> get_occupations() {
        return this.occupations;
    }

    public ArrayList<faction> set_occupations(faction occupation) {
        this.occupations.add(occupation);
    }

    public ArrayList<faction> get_neighboors() {
        return this.neighboors;
    }

    public ArrayList<faction> set_neighboors(faction neighboor) {
        this.occupations.add(neighboor);
    }

    public void attack(faction enermy) {
        
    }
    

}