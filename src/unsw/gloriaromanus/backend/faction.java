
package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class faction {
    private ArrayList<Province> occupations;
    private ArrayList<faction> neighboors;
    private Unit unit;
    private Road road;
    private boolean turn;
    private String belong;
    private String name; 

    public faction() {

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

    public void set_occupations(Province occupation) {
        this.occupations.add(occupation);
    }

    public ArrayList<faction> get_neighboors() {
        return this.neighboors;
    }

    public void set_neighboors(faction neighboor) {
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

    

}
