
package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import unsw.gloriaromanus.backend.*;
import org.json.*;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers,
 * heavy cavalry, elephants, chariots, archers, slingers, horse-archers,
 * onagers, ballista, etc... higher classes include ranged infantry, cavalry,
 * infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent
 * armour and morale)
 */
public class Unit2 {
    private String category;
    private int numSoldiers; // the number of troops in this unit (should reduce based on depletion)
    private int range; // range of the unit
    private double armour; // armour defense
    private int morale; // resistance to fleeing
    private int helmet;
    private double speed; // ability to disengage from disadvantageous battle
    private int attack; // can be either missile or melee attack to simplify. Could improve
                   // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private int movementpoints;
    private String unit_name;
    private int numTroops;


    public Unit2(String type) {
        this.unit_name = type;
        try {
            JSONObject new_unit = new JSONObject(
                    Files.readString(Paths.get("values/Unit_values.json")));
            
            JSONObject chosen_unit = new_unit.getJSONObject(type);
            //this.category = chosen_unit.getString("category");
            this.numTroops = chosen_unit.getInt("numTroops");
            this.armour = 1;
            //this.armour = chosen_unit.getInt("armour");
            this.attack = chosen_unit.getInt("attack");
            this.defenseSkill = chosen_unit.getInt("defenseSkill");
            this.morale = chosen_unit.getInt("morale");
            this.movementpoints = chosen_unit.getInt("movementpoints");
            this.range = chosen_unit.getInt("range");
            this.shieldDefense = chosen_unit.getInt("shieldDefense");
            this.helmet = 0;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void set_helmet(int new_helmet) {
        this.helmet = new_helmet;
    }

    public void set_armour(double new_armour) {
        this.armour = new_armour;
    }

    public void set_speed(int new_speed) {
        this.speed = new_speed;
    }

    public double get_speed() {
        return this.speed;
    }

    public int getNumTroops(){
        return this.numTroops;
    }


    public void set_movementpoints(int num){
        this.movementpoints = num;
    }

    public String get_type(){
        return this.unit_name;
    }

    public static void main(String[] arg){
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");

        Systemcontrol system = new Systemcontrol(owner);
        system.set_enermy(enermy);

        Province my_province = new Province("Britannia", owner, 50, 2.2);
        Province enermy_province = new Province("Belgica", owner, 50, 2.2);
        Province enermy_province2 = new Province("Lugdunensis", owner, 50, 2.2);

        Troop new_troop = new Troop(owner, my_province);
        Troop new_troop2 = new Troop(owner, my_province);
        
        Unit Roman_legionary = new Unit("legionary", "Roman", "Cavalry");
        Unit Roman_pikemen = new Unit("pikemen", "Roman", "Cavalry");

        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);

        my_province.get_my_troops().add(new_troop);

        // move new_troop from my_province to enermy_province, this shoulde be accepted
        boolean accept1 = system.movement(new_troop, my_province, enermy_province);
        assertEquals(accept1, true);
    
        my_province.get_my_troops().add(new_troop2);
        
        // new_troop is already moved to enermy_province, so can't move again.
        boolean accept2 = system.movement(new_troop, my_province, enermy_province2);
        assertEquals(accept2, false);

        // The troop already moved, the troop in enermy_province should be updated.
        boolean judge_troop = false;
        for (Troop troop : enermy_province.get_my_troops()) {
            if (troop.equals(new_troop)) {
                judge_troop = true;
            }
        }
        System.out.println(judge_troop);
    }
}
