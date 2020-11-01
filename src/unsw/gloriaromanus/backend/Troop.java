package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class Troop {
    private String faction;
    private String name;
    private ArrayList<Unit> units =  new ArrayList<Unit>();
    private Province current_province;
    

    public Troop(String faction) {
        this.faction = faction;
    }

    public void move(Province destination) {
        
    }

    public Province get_province() {
        return this.current_province;
    }

    public ArrayList<Unit> get_soldiers() {
        return this.units;
    }

    public void decreaseMorale(){
        for (Unit u : units){
            u.decreaseMorale();
        }
    }

    public void addUnit(Unit u){
        units.add(u);
    }
    
}

