package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class Ability {
    private String name;

    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    public void ability(String name) {
        this.name = name;
    }
    
    public String get_name() {
        return this.name;
    }

    public void set_name(String name) {
        this.name = name;
    }
    
    public void ability_add(soldier soldier){
        if (this.name.equals("Berserker rage")) {
            //throw new ArithmeticException("Can't add ablility");
            Berserker_rage_add(soldier);
        } else if (this.name.equals("Legionary eagle")) {
            //throw new ArithmeticException("Not Roman");
            Legionary_eagle_add(soldier);
        } else if (this.name.equals("Phalanx")) {
            Phalanx_add(soldier);
        } else if (this.name.equals("Heroic charge")) {

        } else if (this.name.equals("skirmisher anti-armour")) {
            skirmisher_add(soldier);
        }
        
        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.add(this);
        soldier.set_abilities(abilities);
    }
     
    public void ability_remove(soldier soldier){
        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.remove(this);
        soldier.set_abilities(abilities);
    }

    public void skirmisher_add(soldier soldier) {
        soldier.set_defence((soldier.get_defence()) / 2);
    }

    public void Phalanx_add(soldier soldier) {
        soldier.set_defence((soldier.get_defence()) * 2);
        soldier.set_speed((soldier.get_speed()) / 2);
    }

    public void Berserker_rage_add(soldier soldier){      
        soldier.set_morale(POSITIVE_INFINITY);
        soldier.set_attack((soldier.get_attack()) * 2);
        soldier.set_armed(false);  
    }

    public void Legionary_eagle_add(soldier soldier){
        
        soldier.set_morale(soldier.get_morale() + 1);        
    } 

    public void Legionary_eagle_remove(soldier soldier){
        
        if ((soldier.get_morale() - 0.2) > 1) {
            soldier.set_morale(soldier.get_morale() - 0.2);
        } else {
            soldier.set_morale(1.0);
        }
        
    }


}