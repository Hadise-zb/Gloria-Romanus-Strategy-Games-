
package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class faction {
    private ArrayList<faction> occupations;
    private ArrayList<faction> neighboors;
    private Unit unit;
    private Road road;
    private boolean turn;
    private String belong; 

    public faction() {

    }

    public void set_soldiers(int a, int b, int c) {
        
    }

    public ArrayList<Unit> get_soldiers() {
        ArrayList<Unit> soldiers = new ArrayList<Unit>();
        soldiers.add(this.unit);
        return soldiers;
    }

    public ArrayList<faction> get_occupations() {
        return this.occupations;
    }

    public void set_occupations(faction occupation) {
        this.occupations.add(occupation);
    }

    public ArrayList<faction> get_neighboors() {
        return this.neighboors;
    }

    public void set_neighboors(faction neighboor) {
        this.occupations.add(neighboor);
    }

    public void attack(faction enermy) {
        
    }
    

}
